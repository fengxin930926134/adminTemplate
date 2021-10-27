package com.fengx.template.utils.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUtils {

    /**
     * 获取文件后缀 不包括点
     * @param file file
     * @return Suffix
     */
    public static String getFileSuffix(MultipartFile file) {
        String ext = "";
        String fpath = file.getOriginalFilename();
        if (fpath != null && fpath.lastIndexOf(".") > -1) {
            // 获取后缀名
            ext = fpath.substring(fpath.lastIndexOf(".") + 1);
            ext = ext.toLowerCase();
        }
        return ext;
    }

    /**
     * 纯文件名 包括后缀 不包括路径
     * @param file file
     * @return fileName
     */
    public static String getFileName(MultipartFile file) {
        return file.getOriginalFilename();
    }

    /**
     * 保存文件
     * @param file file
     * @param filePath 全路径
     */
    public static void saveFile(MultipartFile file, String filePath) throws IOException {
        InputStream inputStream = file.getInputStream();
        // 保存文件到本地
        FileUtils.saveFile(inputStream, filePath);
    }

    /**
     * 保存文件
     * @param inputStream in
     * @param filePath 文件全路径包括名字
     */
    public static void saveFile(InputStream inputStream, String filePath) throws IOException {
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流保存到本地文件
        File tempFile = new File(filePath);
        if (!tempFile.getParentFile().exists() && !tempFile.getParentFile().mkdirs()) {
            throw new IOException("创建文件目录失败");
        }
        if (tempFile.isFile() && !tempFile.exists() && !tempFile.createNewFile()) {
            throw new IOException("创建文件失败");
        }
        OutputStream os = new FileOutputStream(filePath);
        // 开始读取
        while ((len = inputStream.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        inputStream.close();
    }
}
