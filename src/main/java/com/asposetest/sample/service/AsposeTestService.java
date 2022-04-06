package com.asposetest.sample.service;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

/**
 * AsposeTestService is a Service interface used for handling AsposeLibrary Test
 * 
 * @author HARIHARAN MANI
 * @since 07-01-2022
 */
@Service
public interface AsposeTestService {

	void downloadPdfPreviewFile(HttpServletResponse response, File inputFile);

}