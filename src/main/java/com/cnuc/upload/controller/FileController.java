package com.cnuc.upload.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cnuc.upload.base.BaseOpt;

@RestController
@RequestMapping
@CrossOrigin(origins = "*", maxAge = 3600, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,RequestMethod.DELETE })
public class FileController {
	@Value("${file.path}")
	private String path;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public Map<String,String> uoloadFile(HttpServletRequest request) throws Exception {
		Map<String, String> results = new HashMap<String, String>();
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iter = multiRequest.getFileNames();
		String folder = request.getParameter("folder");
		if(StringUtils.isEmpty(folder)){
			folder = "test";
		}
		int i = 0;
		while (iter.hasNext()) {
		// 一次遍历所有文件
		MultipartFile file = multiRequest.getFile(iter.next().toString());
			if (file != null) {
				//文件处理start
				String url = BaseOpt.fileCopyToPath(path, file, request, folder,UUID.randomUUID().toString().replaceAll("-", ""));
				results.put("url_"+i, "/s/"+url);
				results.put("original_"+i,file.getOriginalFilename());
				//文件处理end
				}
			i++;
		}
		return results;
	}


	/**
	 * 显示文件
	 */
	@RequestMapping(value = "/s/{folder}/{fileName:.+}", method = RequestMethod.GET)
	public void show(@PathVariable String folder, @PathVariable String fileName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		if (StringUtils.isEmpty(folder)) {
			folder = "";
		}
		String pathRoot = path + System.getProperty("file.separator") + folder + System.getProperty("file.separator")
		+ fileName;
		File file = new File(pathRoot);
		
		byte[] bytes = BaseOpt.getBytes(pathRoot);
		
		FileInputStream inputStream = new FileInputStream(file);
		inputStream.close();
		
		String name = file.getName();
		String extension = BaseOpt.getExtension(name).toUpperCase();
		if("JPEG".equals(extension)||"JPG".equals(extension)||"PNG".equals(extension)||"GIF".equals(extension)||"BMP".equals(extension)) {
			response.setContentType("image/"+extension.toLowerCase()+"; charset=utf-8");
		}else if("FLV".equals(extension)||"AVI".equals(extension)||"MP4".equals(extension)||"MOV".equals(extension)) {
			response.setContentType("video/"+extension.toLowerCase()+"; charset=utf-8");	
		}
		
		OutputStream stream = response.getOutputStream();
		stream.write(bytes);
		stream.flush();
		stream.close();
	}


}