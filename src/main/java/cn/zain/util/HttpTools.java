package cn.zain.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HTTP工具
 *
 * @author Zain
 */
public class HttpTools {

    private static final Logger logger = LogManager.getLogger(HttpTools.class);

    private static final String CHARSET = "UTF-8";


    /**
     * 功能说明 ：基于apache的httpclient进行post 请求
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param contentType   请求类型，目前支持json和form
     * @param authorization 认证信息
     * @return String
     */
    public static String postByHttpclient(String url, Map<String, String> params, ContentType contentType, String authorization) {
        if (StringUtils.isBlank(url) || null == params) {
            logger.info("postByHttpclient请求参数为空，url：" + url + "    params:" + params);
            return null;
        }
        //构建httpPost
        HttpPost httpPost = new HttpPost(url);
        if (StringUtils.isNotBlank(authorization)) {
            httpPost.setHeader("Authorization", "Basic " + StringTools.byte2BASE64(authorization.getBytes()));
        }

        StringEntity entity;
        if (ContentType.APPLICATION_JSON.equals(contentType)) {
            Set<String> keys = params.keySet();
            List<NameValuePair> list = new ArrayList<>();
            for (String key : keys) {
                list.add(new BasicNameValuePair(key, params.get(key)));
            }
            try {
                entity = new UrlEncodedFormEntity(list);
            } catch (UnsupportedEncodingException e) {
                logger.error("编码不支持，url：" + url + " params:" + params, e);
                return null;
            }
        } else if (ContentType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
            String str = JsonSerializeUtils.serialize(params);
            entity = new StringEntity(str, "utf-8");
        } else {
            logger.error("无法识别或暂不支持的ContentType：" + contentType);
            return null;
        }
        entity.setContentEncoding(CHARSET);
        entity.setContentType(contentType.getMimeType());
        httpPost.setEntity(entity);

        try (CloseableHttpClient httpclient = HttpClients.createDefault();
             CloseableHttpResponse response = httpclient.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.error("请求失败，url：" + url + " params:" + params + "  response:" + response);
                return null;
            }
            HttpEntity respEntity = response.getEntity();
            if (respEntity != null) {
                return EntityUtils.toString(respEntity, CHARSET);
            }
        } catch (ClientProtocolException e) {
            logger.error("请求协议异常，url：" + url + "  params:" + params, e);
        } catch (IOException e) {
            logger.error("IO异常，url：" + url + "  params:" + params, e);
        }
        return null;
    }

    /**
     * 功能说明 ：基于apache的httpclient进行post 请求
     * form表单格式
     *
     * @param url        请求地址
     * @param formParams 请求参数
     * @return String
     */
    public static String postByHttpclient(String url, List<NameValuePair> formParams) {
        if (StringUtils.isBlank(url) || null == formParams) {
            logger.info("请求参数为空，url：" + url + "   formParams:" + formParams);
            return null;
        }
        //构建httpPost
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity;
        try {
            entity = new UrlEncodedFormEntity(formParams);
        } catch (UnsupportedEncodingException e) {
            logger.error("编码不支持，url：" + url + " formParams:" + formParams, e);
            return null;
        }
        entity.setContentEncoding(CHARSET);
        entity.setContentType(ContentType.MULTIPART_FORM_DATA.getMimeType());
        httpPost.setEntity(entity);

        try (CloseableHttpClient httpclient = HttpClients.createDefault();
             CloseableHttpResponse response = httpclient.execute(httpPost);) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.error("请求失败，url：" + url + " formParams:" + formParams + "  response:" + response);
                return null;
            }
            HttpEntity respEntity = response.getEntity();
            if (respEntity != null) {
                return EntityUtils.toString(respEntity, CHARSET);
            }
        } catch (ClientProtocolException e) {
            logger.error("请求协议异常，url：" + url + "  formParams:" + formParams, e);
        } catch (IOException e) {
            logger.error("IO异常，url：" + url + "  formParams:" + formParams, e);
        }
        return null;
    }

    /**
     * 功能说明:发送get请求
     *
     * @param url 请求地址
     * @return String
     */
    public static String getByHttpclient(String url) {
        if (StringUtils.isBlank(url)) {
            logger.info("请求参数为空，url：" + url);
            return null;
        }

        HttpGet httpGet = new HttpGet(url);
        Builder builder = RequestConfig.custom();
        // 连接超时时间
        builder.setConnectTimeout(6000);
        // 获取数据超时时间，设置时间内未返回数据，直接放弃本次请求
        builder.setSocketTimeout(4000);
        RequestConfig requestConfig = builder.build();
        httpGet.setConfig(requestConfig);

        try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
             CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet)) {
            if (closeableHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(closeableHttpResponse.getEntity(), CHARSET);
            } else {
                logger.error("请求失败，地址错误或已经失效.url: " + url);
                return null;
            }
        } catch (IOException e) {
            logger.error("IO请求异常: " + url, e);
        } catch (Exception e) {
            logger.error("系统异常: " + url, e);
        }
        return null;
    }

    /**
     * 功能说明：构建一个运行值输出加密的键值对用于http请求
     */
    public static class BasicNameEncryptValuePair extends BasicNameValuePair { //可加密值

        public BasicNameEncryptValuePair(String name, String value) {
            super(name, value);
        }

        @Override
        public String toString() {
            if (this.getValue() == null) {
                return this.getName() + "=null";
            }
            return this.getName() + "=******";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + 1;
            return result;
        }
    }
}
