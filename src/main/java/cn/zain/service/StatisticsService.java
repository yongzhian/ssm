package cn.zain.service;

import javax.servlet.ServletOutputStream; /**
 * 数据统计Service
 * @author Zain
 */
public interface StatisticsService {
    /**
     * 导出文件
     * @param collectionName
     * @param dir
     * @return
     */
    String exportMongoDbData2ExcelByFile(String collectionName, int pageNum, int pageSize, String dir);

    /**
     * 导出Excel到流中
     * @param collectionName
     * @param pageNum
     * @param pageSize
     * @param outputStream
     */
    void exportMongoDbData2ExcelByStream(String collectionName, int pageNum, int pageSize, ServletOutputStream outputStream);
}
