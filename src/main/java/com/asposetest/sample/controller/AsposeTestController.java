package com.asposetest.sample.controller;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.asposetest.sample.service.AsposeTestService;
import com.asposetest.sample.support.SettingCons;
import com.asposetest.sample.support.SupportUtils;

/**
 * This controller class used for handling user requests.
 * 
 * @author HARIHARAN MANI
 * @since 07-01-2022
 */
@Controller
public class AsposeTestController {

	@Autowired
	private AsposeTestService asposeTestService;

	@GetMapping(value = "preview")
	public void filePreview(HttpServletResponse response, @RequestParam(name = "file_name") String fileName) {

		if (Objects.toString(fileName, "").isEmpty()) {
			throw SupportUtils.customException("'file_name' is required");
		}

		File inputFile = new File(getResourcePath() + File.separator + fileName);
		if (!inputFile.exists()) {
			throw SupportUtils.customException(String.format("[%s] - File not available in the resource", fileName));
		}

		asposeTestService.downloadPdfPreviewFile(response, inputFile);
	}

	@GetMapping(value = "health")
	public ResponseEntity<String> showServerHealth() {
		StringBuilder sb = new StringBuilder();
		sb.append("<h1> Aspose Library Test Application </h1>");

		sb.append("Build.No : ").append(SettingCons.TOOL_BUILD_NO);
		sb.append("<br>");
		sb.append("Server Time : ").append(SupportUtils.getDbDateFormat(new Date()));

		sb.append("<br> <br>");
		sb.append("JAVA Version : ").append(System.getProperty("java.version"));
		sb.append("<br>");
		sb.append("JMX Version : ").append(ManagementFactory.getRuntimeMXBean().getVmVersion());

		sb.append("<br> <br> <br>");

		File resourceDirectory = new File(getResourcePath());
		File[] resourceFileList = resourceDirectory.listFiles();

		if (resourceFileList.length == 0) {
			sb.append("No files available in the resource (").append(resourceDirectory.getAbsolutePath()).append(")");
		} else {
			sb.append("Files available in the resources are : <br> ");
			for (File resourceFile : resourceFileList) {
				sb.append(resourceFile.getName()).append("<br>");
			}

			sb.append("<br> <br>");
			sb.append("/asposetest/preview?file_name=").append(resourceFileList[0].getName());
		}

		HttpStatus httpStatus = HttpStatus.OK;
		return ResponseEntity.status(httpStatus.value()).body(sb.toString());
	}

	private String getResourcePath() {
		return getClass().getClassLoader().getResource("sample-files").getPath();
	}
}