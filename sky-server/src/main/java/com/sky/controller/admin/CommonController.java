package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("admin/common")
@Slf4j
public class CommonController {
    private final AliOssUtil aliOssUtil;

    public CommonController(AliOssUtil aliOssUtil) {
        this.aliOssUtil = aliOssUtil;
    }

    @RequestMapping("upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传");
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();

            //截取原始文件名的后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

            //构造新文件名称
            String objectName = UUID.randomUUID().toString() + suffix;

            //文件请求路径
            String filePath = aliOssUtil.upload(file.getBytes(),objectName);

            return Result.success(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
