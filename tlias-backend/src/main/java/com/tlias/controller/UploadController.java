package com.tlias.controller;

import com.tlias.entity.Result;
import com.tlias.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传：保存到本地磁盘（tlias.upload.dir），并返回可访问的 URL
 */
@Slf4j
@RestController
public class UploadController {

    private static final List<String> ALLOWED = List.of("jpg", "jpeg", "png", "gif", "webp");

    @Value("${tlias.upload.dir:./uploads}")
    private String uploadDir;

    @PostMapping("/upload")
    public Result upload(@RequestParam(value = "file", required = false) MultipartFile file,
                         @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        MultipartFile target = (file != null && !file.isEmpty()) ? file : image;
        if (target == null || target.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        String original = target.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.') + 1).toLowerCase();
        }
        if (!ALLOWED.contains(ext)) {
            throw new BusinessException("只允许上传 " + String.join("/", ALLOWED) + " 格式的图片");
        }
        String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;
        File dir = new File(uploadDir).getAbsoluteFile();
        if (!dir.exists() && !dir.mkdirs()) {
            throw new BusinessException("创建上传目录失败：" + dir);
        }
        target.transferTo(new File(dir, filename));
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/").path(filename).toUriString();
        log.info("文件上传成功：{} -> {}", original, url);
        return Result.success(url);
    }
}
