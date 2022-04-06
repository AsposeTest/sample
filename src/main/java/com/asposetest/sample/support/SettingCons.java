package com.asposetest.sample.support;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * This class contains Static Constants
 * 
 * @author HARIHARAN MANI
 * @since 07-01-2022
 */
public class SettingCons {

	private SettingCons() {
		throw new IllegalStateException();
	}

	public static final String TOOL_BUILD_NO = "B001";

	public static final String JSON_UTF8_ENCODING = "application/json; charset=UTF-8";

	public static final String TEMP_DIR = System.getProperty("java.io.tmpdir"); // TEMP DIRECTORY

	public static final String TEMP_PREVIEW_DIR = TEMP_DIR + File.separator + "Aspose_Temp_Backup";

	public static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DEF_ENCODING = "UTF-8";
	public static final Charset DEF_CHARSET = StandardCharsets.UTF_8;
}