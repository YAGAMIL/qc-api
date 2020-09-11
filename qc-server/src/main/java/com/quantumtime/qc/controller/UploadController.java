package com.quantumtime.qc.controller;

import com.quantumtime.qc.utils.AliyunOssUtil;
import com.quantumtime.qc.vo.Result;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

/**
 * .Description:上传接口 & Created on 2019/11/12 11:09
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiIgnore
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

    @PostMapping("/image")
    public Result uploadImages(MultipartFile file) throws IOException {
        if (file.isEmpty() || StringUtils.isEmpty(file.getOriginalFilename())) {
            Result.error500("图片不能为空", null);
        }
        String contentType = file.getContentType();
        Objects.requireNonNull(contentType);
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        // 判断图片的格式是否正确
        if ("BMP".equals(suffix.toUpperCase())
                || suffix.toUpperCase().equals("JPG")
                || suffix.toUpperCase().equals("JPEG")
                || suffix.toUpperCase().equals("PNG")
                || suffix.toUpperCase().equals("GIF")) {
            Map<String, String> map = uploadFile(file, 500, 500);
            return Result.success(map);
        }
        return Result.error500("图片格式不正确,只可以上传[ BMP,JPG,JPEG,PNG,GIF ]中的一种", null);
    }

    @DeleteMapping("/delete")
    public Result deleteImages(@RequestBody Map<String, String> map) throws IOException {
        String url = map.get("url");
        log.info("deleteImages url: {}", url);
        if (url != null && !url.trim().equals("")) {
            AliyunOssUtil.deleteFile(url);
        }
        return Result.success();
    }

    @PostMapping("/avatar")
    public Result uploadAvatar(MultipartFile file) throws IOException {
        if (file.isEmpty() || StringUtils.isEmpty(file.getOriginalFilename())) {
            Result.error500("头像不能为空", null);
        }
        String originalFileName = file.getOriginalFilename();
        String suffix = Objects.requireNonNull(originalFileName).substring(originalFileName.lastIndexOf(".") + 1);
        // 判断图片的格式是否正确
        if (suffix.toUpperCase().equals("BMP")
                || suffix.toUpperCase().equals("JPG")
                || suffix.toUpperCase().equals("JPEG")
                || suffix.toUpperCase().equals("PNG")
                || suffix.toUpperCase().equals("GIF")) {
            Map<String, String> map = uploadFile(file, 200, 200);
            return Result.success(map);
        }
        return Result.error500("头像格式不正确,只可以上传[ BMP,JPG,JPEG,PNG,GIF ]中的一种", null);
    }

    public Map<String, String> uploadFile(MultipartFile file, int width, int height) throws IOException {
        // 获取一个空文件
        File nullFile = makeParentFolder(file.getOriginalFilename());
        // 因为是spring boot 打包以后是jar包，所以需要用ClassPathResource来获取classpath下面的水印图片
        InputStream watermark = new ClassPathResource("images/watermark.png").getInputStream();
        // 图片压缩以后读到空文件中
        Thumbnails.of(file.getInputStream())
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(watermark), 0.5f)
                .outputQuality(0.8f)
                .size(width, height)
                .toFile(nullFile);
        // 把上一不压缩好的图片上传到阿里云OSS
        String url = AliyunOssUtil.putObject(
                new FileInputStream(nullFile), file.getContentType(), AliyunOssUtil.createFolder()
                        + nullFile.getName());
        Map<String, String> result = new HashMap<>(2);
        result.put("url", url);
        result.put("fileName", nullFile.getName());
        log.info(
                "uploadFile( {} , {} , {} , {} );",
                file.getOriginalFilename(),
                file.getSize(),
                file.getContentType(),
                nullFile.getName(),
                url);
        // 删除压缩文件
        nullFile.delete();
        return result;
    }

    private File makeParentFolder(String fileName) {
        // 获取文件的原名称，生成一个UUID的文件名称
        String uuidFileName = AliyunOssUtil.createFileName(fileName);
        // 构建文件路径
        StringBuilder sb = new StringBuilder(System.getProperties().getProperty("user.home"));
        sb.append("/imagesCache/").append(uuidFileName);
        log.info("生成缓存文件 地址是:  {}", sb.toString());
        // 生成文件
        File originalFile = new File(sb.toString());
        // 判断文件父目录是否存在
        if (!originalFile.getParentFile().exists()) {
            // 创建文件目录
            originalFile.getParentFile().mkdir();
        }
        return originalFile;
    }
}
