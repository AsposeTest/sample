package com.asposetest.sample.support;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * This is used as a SupportUtils
 * 
 * @author HARIHARAN MANI
 * @since 07-01-2022
 */
public class SupportUtils {

	private SupportUtils() {
		throw new IllegalStateException();
	}

	public static void printSysout(String printMsg) {
		System.out.println(printMsg);
	}

	public static AspsoeTestException customException(String errorMsg) {
		return new AspsoeTestException(errorMsg);
	}

	public static String getTimeDuration(long milliSeconds) {
		if (milliSeconds < 1000) {
			return milliSeconds + " milliSeconds";
		} else if (milliSeconds < 60000) {
			return String.format("%d sec", TimeUnit.MILLISECONDS.toSeconds(milliSeconds));
		} else {
			return String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliSeconds),
					TimeUnit.MILLISECONDS.toSeconds(milliSeconds)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
		}
	}

	public static void serverStartedStatus() {
		Date startTime = new Date();
		printSysout("__________________________________________________________");
		printSysout("@ 01 - Server Build No       :  " + SettingCons.TOOL_BUILD_NO);
		printSysout("@ 02 - Server Started Time :  " + startTime);
	}

	public static String getDbDateFormat(Date dateInput) {
		return new SimpleDateFormat(SettingCons.DB_DATE_FORMAT).format(dateInput);
	}

	public static String reCreateTempFolder() {
		return reCreateFolder(SettingCons.TEMP_PREVIEW_DIR);
	}

	private static String reCreateFolder(String folderDirectory) {
		String reCreateStatus = "";
		File tempDirectory = new File(folderDirectory);
		if (tempDirectory.exists()) {
			String tempDirectoryPath = tempDirectory.getPath();
			String[] tempFileList = tempDirectory.list();
			int errorCount = 0;
			for (String tempFile : tempFileList) {
				File currentFile = new File(tempDirectoryPath, tempFile);
				if (!deleteFile(currentFile.getAbsolutePath())) {
					errorCount++;
				}
			}
			if ((errorCount == 0) && (deleteFile(folderDirectory))) {
				reCreateStatus = "Existing Temp Folder Deleted >> " + tempDirectoryPath;
			} else {
				reCreateStatus = errorCount + " Temp Files Deletion Error... Kindly, Delete manually >> "
						+ tempDirectoryPath;
			}
		}
		tempDirectory.mkdir();
		return reCreateStatus;
	}

	public static boolean deleteFile(String filePath) {
		try {
			return Files.deleteIfExists(Paths.get(filePath));
		} catch (IOException e) {
			printSysout("File Delete Error > " + e.getMessage());
			return false;
		}
	}

}
