package com.cnuc.upload.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

/**
 * FileUtils
 */
public class BaseOpt {

    /**
     * 复制文件到指定目录
     * 
     * @param config   配置文件读取到的文件库地址
     * @param file     文件
     * @param request  HttpServletRequest
     * @param folder   文件夹名称
     * @param fileName 需要保存的文件名
     * @return
     */
    public static String fileCopyToPath(String config, MultipartFile file, HttpServletRequest request, String folder,
            String fileName) {
        String extension = getExtension(file.getOriginalFilename());
        // 获得物理地址
        String pathRoot = config + folder + System.getProperty("file.separator") + fileName + "." + extension;
        // 生成相对的URL地址
        String url = folder + "/" + fileName + "." + extension;
        // 创建文件实例
        File tempFile = new File(pathRoot);

        // 判断父级目录是否存在，不存在则创建
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }
        try {
            // 将接收的文件保存到指定文件中
            file.transferTo(tempFile);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 删除文件处理
     * 
     * @param request
     * @param url
     * @return
     */
    public static String delFileUtils(String config, HttpServletRequest request, String url) {
        // 创建文件实例
        File tempFile = new File(config + url);
        try {
            // 删除
            tempFile.delete();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 获取文件后缀名
     * @param filename
     * @return
     */
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }

        int extensionPos = filename.lastIndexOf(".");
        int lastUnixPos = filename.lastIndexOf("/");
        int lastWindowsPos = filename.lastIndexOf("\\");
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);
        //  斜杠比.的位置靠右，说明文件名右问题
        int index =  lastSeparator > extensionPos ? -1 : extensionPos;


        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }


    /**
     * 获取字节数组
     */
    public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
}