package com.fengx.template.service.sys.impl;

import com.fengx.template.dao.sys.SourceDAO;
import com.fengx.template.pojo.entity.sys.Source;
import com.fengx.template.service.sys.SourceService;
import com.fengx.template.utils.common.FileUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {

    @Value("${source.file.path}")
    private String filePath;
    @Value("${source.file.mapping}")
    private String fileMapping;
    private final @NonNull SourceDAO sourceDAO;

    @Override
    public void upload(MultipartFile file, String baseDir) throws IOException {
        String fileSuffix = FileUtils.getFileSuffix(file);
        String fileName = UUID.randomUUID().toString() + "." + fileSuffix;
        String filePathReal = filePath + baseDir + fileName;
        String filePathMapping = fileMapping + baseDir + fileName;
        // 保存文件到本地
        FileUtils.saveFile(file, filePathReal);
        // 保存文件信息到数据库
        Source source = new Source();
        source.setFileName(FileUtils.getFileName(file));
        source.setFilePath(filePathMapping);
        source.setFileType(fileSuffix);
        source.setFileSize(file.getSize());
        sourceDAO.save(source);
    }
}
