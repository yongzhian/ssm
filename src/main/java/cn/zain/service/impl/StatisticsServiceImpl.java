package cn.zain.service.impl;

import cn.zain.service.StatisticsService;
import cn.zain.util.StringTools;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Zain
 */
@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Resource(name = "mongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public String exportMongoDbData2ExcelByFile(String collectionName, int pageNum, int pageSize, String dir) {
        logger.debug("{}...", "exportMongoDbData2ExcelByFile");
        String fileName =  StringTools.genRandomStr(10) + ".xlsx";
        try(SXSSFWorkbook workbook = exportMongoDbData2Excel(collectionName, pageNum,  pageSize);
            FileOutputStream fileOutputStream = new FileOutputStream(dir + File.separator + fileName);) {
            workbook.write(fileOutputStream);
        }catch (Exception e){
            logger.error("exportMongoDbData2ExcelByFile异常,导出文件失败.", e);
            return null;
        }
        return fileName;
    }

    @Override
    public void exportMongoDbData2ExcelByStream(String collectionName, int pageNum, int pageSize, ServletOutputStream outputStream) {
        logger.debug("{}...", "exportMongoDbData2ExcelByStream");
        try(SXSSFWorkbook workbook = exportMongoDbData2Excel(collectionName, pageNum,  pageSize)) {
            workbook.write(outputStream);
        }catch (Exception e){
            logger.error("exportMongoDbData2ExcelByStream异常,导出文件失败.", e);
        }
    }

    private SXSSFWorkbook exportMongoDbData2Excel(String collectionName, int pageNum, int pageSize) {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.ASC, "_id"));
        query.skip((pageNum - 1) * pageSize);
        query.limit(pageSize);
        List<Map> list = mongoTemplate.find(query, Map.class, collectionName);
        if (null == list || list.isEmpty()) {
            logger.warn("查询记录为空,list:{}", list);
        }
        String[] header = (String[]) list.get(0).keySet().toArray(new String[]{});
        String fileName = "/" + StringTools.genRandomStr(10) + ".xlsx";
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet("recode" + list.size());
        //添加头部
        SXSSFRow headerRow = sheet.createRow(0);
        for (int j = 0; j < header.length; j++) {
            headerRow.createCell(j).setCellValue(header[j]);
        }
        for (int i = 0; i < list.size(); i++) {
            SXSSFRow bodyRow = sheet.createRow(i + 1);
            for (int j = 0; j < header.length; j++) {
                Object obj = list.get(i).get(header[j]);
                bodyRow.createCell(j).setCellValue(obj == null ? "" : String.valueOf(obj));
            }
        }
        logger.debug("文件写入成功...");
        return workbook;
    }

}
