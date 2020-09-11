package com.quantumtime.qc.utils;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.*;


@Slf4j
public class AliyunOssUtil {

    static {
        // 加载阿里云oss配置文件到配置类中
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = new ClassPathResource("aliyunoss.properties").getInputStream();
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        config = new AliyunOssConfig(prop.getProperty("endpoint").trim(),
                prop.getProperty("accessKeyId").trim(),
                prop.getProperty("accessKeySecret").trim(),
                prop.getProperty("bucketName").trim()
        );
    }

    private static AliyunOssConfig config;


    public final static String createFolder() {
        Calendar now = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(now.get(Calendar.YEAR))
                .append("/")
                .append((now.get(Calendar.MONTH) + 1))
                .append("/")
                .append(now.get(Calendar.DAY_OF_MONTH))
                .append("/")
                .append(now.get(Calendar.HOUR_OF_DAY))
                .append("/")
                .append(now.get(Calendar.MINUTE))
                .append("/");
        return sb.toString();
    }

    public final static String createFileName(String originalFileName) {
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        return UUID.randomUUID().toString().toLowerCase().replace("-", "") + "." + suffix;
    }

    public final static String createFolderFileName(String originalFileName) {
        return createFolder() + createFileName(originalFileName);
    }

    public final static String putObject(InputStream input, String contentType, String fileName) {
        // 默认null
        OSSClient ossClient = new OSSClient(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());
        //创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        // 被下载时网页的缓存行为
        meta.setCacheControl("no-cache");
        meta.setHeader("Pragma", "no-cache");
        meta.setContentType(contentType);
        // 创建上传请求
        PutObjectRequest request = new PutObjectRequest(config.getBucketName(), fileName, input, meta);
        try {
            PutObjectResult putObjectResult = ossClient.putObject(request);
            log.info("putObjectResult: {}", putObjectResult);
        } finally {
            ossClient.shutdown();
        }
        // 构造URL路径
        String url = config.getEndpoint().replaceFirst("http://", "https://" + config.getBucketName() + ".") + fileName;
        return url;
    }


    public final static String putObject(File file, String fileType, String fileName) throws FileNotFoundException {
        return putObject(new FileInputStream(file), fileType, fileName);
    }

    public final static boolean deleteFile(String fileUrl) {
        int result = deleteFile(Arrays.asList(fileUrl));
        if (result > 0) {
            return true;
        }
        return false;
    }

    public final static int deleteFile(List<String> fileUrls) {

        // 成功删除的个数
        int deleteCount = 0;
        // 根据url获取bucketName
        String bucketName = getBucketName(fileUrls.get(0));
        // 根据url获取fileName
        List<String> fileNames = getFileName(fileUrls);
        if (bucketName == null || fileNames.size() <= 0)
            return 0;
        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());
            DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName).withKeys(fileNames);
            DeleteObjectsResult result = ossClient.deleteObjects(request);
            deleteCount = result.getDeletedObjects().size();
        } catch (OSSException oe) {
            oe.printStackTrace();
            throw new RuntimeException("OSS服务异常:", oe);
        } catch (ClientException ce) {
            ce.printStackTrace();
            throw new RuntimeException("OSS客户端异常:", ce);
        } finally {
            ossClient.shutdown();
        }
        return deleteCount;
    }

    public final static int deleteFiles(List<String> fileUrls) {
        int count = 0;
        for (String url : fileUrls) {
            if (deleteFile(url)) {
                count++;
            }
        }
        return count;
    }

    public final static String contentType(String fileType) {

        fileType = fileType.toLowerCase();
        String contentType = "";
        switch (fileType) {
            case "bmp":
                contentType = "image/bmp";
                break;
            case "gif":
                contentType = "image/gif";
                break;
            case "png":
            case "jpeg":
            case "jpg":
                contentType = "image/jpeg";
                break;
            case "html":
                contentType = "text/html";
                break;
            case "txt":
                contentType = "text/plain";
                break;
            case "vsd":
                contentType = "application/vnd.visio";
                break;
            case "ppt":
            case "pptx":
                contentType = "application/vnd.ms-powerpoint";
                break;
            case "doc":
            case "docx":
                contentType = "application/msword";
                break;
            case "xml":
                contentType = "text/xml";
                break;
            case "mp4":
                contentType = "video/mp4";
                break;
            default:
                contentType = "application/octet-stream";
                break;
        }
        return contentType;
    }

    public final static String getBucketName(String fileUrl) {

        String http = "http://";
        String https = "https://";
        int httpIndex = fileUrl.indexOf(http);
        int httpsIndex = fileUrl.indexOf(https);
        int startIndex = 0;
        if (httpIndex == -1) {
            if (httpsIndex == -1) {
                return null;
            } else {
                startIndex = httpsIndex + https.length();
            }
        } else {
            startIndex = httpIndex + http.length();
        }
        int endIndex = fileUrl.indexOf(".oss-");
        return fileUrl.substring(startIndex, endIndex);
    }

    public final static String getFileName(String fileUrl) {
        String str = "aliyuncs.com/";
        int beginIndex = fileUrl.indexOf(str);
        if (beginIndex == -1)
            return null;
        return fileUrl.substring(beginIndex + str.length());
    }

    public final static List<String> getFileName(List<String> fileUrls) {
        List<String> names = new ArrayList<>();
        for (String url : fileUrls) {
            names.add(getFileName(url));
        }
        return names;
    }


}



@Getter
@Setter
class AliyunOssConfig {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    public AliyunOssConfig(String endpoint, String accessKeyId, String accessKeySecret, String bucketName) {
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
    }

}