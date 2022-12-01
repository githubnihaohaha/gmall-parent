package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author: liu-wēi
 * @date: 2022/12/1,13:33
 */
@RestController
@RequestMapping("/admin/product")
public class FileUploadController {
    
    /**
     * 从配置中心获取minio配置
     */
    @Value("${minio.endpointUrl}")
    public String endpointUrl;
    @Value("${minio.accessKey}")
    public String accessKey;
    @Value("${minio.secreKey}")
    public String secreKey;
    @Value("${minio.bucketName}")
    public String bucketName;
    
    
    /**
     * 文件上传接口
     *
     * @param file
     * @return
     */
    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file) {
        String fileUrl = "";
        try {
            MinioClient minioClient = MinioClient
                    .builder()
                    .endpoint(endpointUrl)
                    .credentials(accessKey, secreKey)
                    .build();
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (exists) {
                System.out.println("Bucket already exists.");
            } else {
                // 如果不存在,则创建桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            
            // 文件名称
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + file.getOriginalFilename();
            
            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            
            // 文件的完整路径名称
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok(fileUrl);
    }
}
