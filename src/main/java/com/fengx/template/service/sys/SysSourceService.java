package com.fengx.template.service.sys;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface SysSourceService {

    /**
     * 上传文件
     * @param file file
     * @param baseDir 需要存放的目录 /为当前
     */
    void upload(MultipartFile file, String baseDir) throws IOException;
}
