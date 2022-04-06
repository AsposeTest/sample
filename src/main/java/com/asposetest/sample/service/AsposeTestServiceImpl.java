package com.asposetest.sample.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import com.aspose.cells.CellsException;
import com.aspose.cells.GridlineType;
import com.aspose.cells.LoadFormat;
import com.aspose.cells.LoadOptions;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.slides.InvalidPasswordException;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.IncorrectPasswordException;
import com.aspose.words.PdfCompliance;
import com.aspose.words.PdfSaveOptions;
import com.aspose.words.SaveFormat;
import com.asposetest.sample.support.AspsoeTestException;
import com.asposetest.sample.support.SettingCons;
import com.asposetest.sample.support.SupportUtils;

/**
 * This is an implementation class used for handling AsposeLibrary Test
 * 
 * @author HARIHARAN MANI
 * @since 07-01-2022
 */
@Service
public class AsposeTestServiceImpl implements AsposeTestService {

	private static final String CONTENT_HEADER = "Content-Disposition";
	private static final String[] PDF_DOCUMENT_GROUP = { "txt", "doc", "docx", "rtf", "odt" };
	private static final String[] PDF_WORKSHEET_GROUP = { "ods", "xls", "xlsx" }; // Need to add CSV in phase-3
	private static final String[] PDF_PRESENTAION_GROUP = { "ppt", "pptx" };

	private static final List<String> PDF_SUPPORTED_EXTENSIONS = getPdfSupportedExtensions();

	private static List<String> getPdfSupportedExtensions() {
		List<String> supportedExtensionList = new ArrayList<>();
		supportedExtensionList.addAll(Arrays.asList(PDF_DOCUMENT_GROUP));
		supportedExtensionList.addAll(Arrays.asList(PDF_WORKSHEET_GROUP));
		supportedExtensionList.addAll(Arrays.asList(PDF_PRESENTAION_GROUP));
		return supportedExtensionList;
	}
	@Override
	public void downloadPdfPreviewFile(HttpServletResponse response, File inputFile) {

		String originalFileName = inputFile.getName();

		String tempFileKey = String.valueOf(UUID.randomUUID());
		String tempSaveFileName = tempFileKey.concat(".pdf");

		SupportUtils.printSysout(
				String.format("Generating PDF Preview for [%s] in Temp >> %s", originalFileName, tempFileKey));

		File outPutFile = new File(SettingCons.TEMP_PREVIEW_DIR, tempSaveFileName);

		convertToPdf(inputFile, outPutFile);

		downloadConvertedPdf(response, outPutFile);
	}

	private void downloadConvertedPdf(HttpServletResponse response, File outPutFile) {
		String fileName = outPutFile.getName();
		SupportUtils.printSysout("Downloading " + fileName);

		try (FileInputStream inputStream = new FileInputStream(outPutFile);
				ServletOutputStream outputStream = response.getOutputStream();) {

			String encodedFileName = URLEncoder.encode(fileName, SettingCons.DEF_ENCODING).replace("+", "%20");
			response.setCharacterEncoding(SettingCons.DEF_ENCODING);
			response.setHeader("Content-Length", String.valueOf(outPutFile.length())); // To show total size
			response.setHeader(CONTENT_HEADER, "attachment; filename*=UTF-8''" + encodedFileName);
			response.setContentType("text/html");

			IOUtils.copy(inputStream, outputStream);

			outputStream.flush();
			SupportUtils.printSysout(String.format("[%s] PDF download completed...!", fileName));

			SupportUtils.printSysout(String.format("TEMP file delete status => [%s]", outPutFile.delete()));
		} catch (Exception e) {
			throw SupportUtils.customException("Download Failed : " + e.getMessage());
		}
	}

	private void convertToPdf(File inputFile, File outPutFile) {

		String fileName = inputFile.getName();
		String fileType = fileName.split("\\.")[1];
		if (!PDF_SUPPORTED_EXTENSIONS.contains(fileType)) {
			throw SupportUtils.customException("Preview not supported");
		}
		try (FileInputStream inputStream = new FileInputStream(inputFile);
				FileOutputStream outputStream = new FileOutputStream(outPutFile)) {
			if (Arrays.asList(PDF_DOCUMENT_GROUP).contains(fileType)) {
				saveDocumentGroup(inputStream, outputStream, fileName);
			} else if (Arrays.asList(PDF_WORKSHEET_GROUP).contains(fileType)) {
				saveWorksheetGroup(inputStream, outputStream, fileName);
			} else if (Arrays.asList(PDF_PRESENTAION_GROUP).contains(fileType)) {
				savePresentationGroup(inputStream, outputStream, fileName);

			}

			outputStream.flush();
			SupportUtils.printSysout("deCrypt, deCompress process for PDF FilePreview completed...!");
		} catch (AspsoeTestException restExcep) {
			throw restExcep;
		} catch (Exception e) {
			throw SupportUtils.customException("generateTempPdf ERROR - " + e.getMessage());
		}
	}

	private void savePresentationGroup(FileInputStream inputStream, FileOutputStream outputStream, String fileType) {

		try {
			com.aspose.slides.LoadOptions loadOptions = new com.aspose.slides.LoadOptions();
			Presentation pres = new Presentation(inputStream, loadOptions);
			SupportUtils.printSysout(String.format("[%s] loaded with [aspose.slides] class", fileType));

			pres.save(outputStream, com.aspose.slides.SaveFormat.Pdf); // this line throws error only in server

			SupportUtils.printSysout(fileType + " saved as PDF");
		} catch (InvalidPasswordException e) {
			throw SupportUtils.customException("Password Protected File");
		} catch (Exception e) {
			throw SupportUtils.customException("savePresentationGroup ERROR - " + e.getMessage());
		}
	}

	private void saveDocumentGroup(FileInputStream inputStream, FileOutputStream outputStream, String fileType) {
		try {
			Document document = null;
			if (fileType.equals(".txt")) {
				document = new Document(inputStream, getTextLoadOptions()); // ".TXT"
			} else {
				document = new Document(inputStream); // ".DOC", ".DOCX", ".RTF", ".ODT"
			}

			if (fileType.equals(".odt")) {
				Document odtDocument = document.deepClone();
				odtDocument.save(outputStream, getOdtSaveOptions()); // ".ODT"
			} else {
				document.save(outputStream, SaveFormat.PDF); // ".TXT", ".DOC", ".DOCX", ".RTF"
			}
			SupportUtils.printSysout(fileType + " saved as PDF");
		} catch (IncorrectPasswordException e) {
			throw SupportUtils.customException("Password Protected File");
		} catch (Exception e) {
			throw SupportUtils.customException("saveDocumentGroup ERROR - " + e.getMessage());

		}
	}

	private PdfSaveOptions getOdtSaveOptions() {
		PdfSaveOptions odtPdfSaveOptions = new PdfSaveOptions();
		odtPdfSaveOptions.setCompliance(PdfCompliance.PDF_A_1_B);
		return odtPdfSaveOptions;
	}

	private com.aspose.words.LoadOptions getTextLoadOptions() {
		com.aspose.words.LoadOptions wordLoadOptions = new com.aspose.words.LoadOptions();
		wordLoadOptions.setLoadFormat(com.aspose.words.LoadFormat.TEXT);
		return wordLoadOptions;
	}

	private void saveWorksheetGroup(FileInputStream inputStream, FileOutputStream outputStream, String fileType) {
		try {
			com.aspose.cells.PdfSaveOptions cellsPdfSaveOptions = new com.aspose.cells.PdfSaveOptions();

			if (fileType.equals(".csv")) {
				LoadOptions opts = new LoadOptions(LoadFormat.CSV);
				Workbook csvworkbook = new Workbook(inputStream, opts); // ".CSV"

				setCsvBorder(csvworkbook);
				cellsPdfSaveOptions.setGridlineType(GridlineType.HAIR);
				csvworkbook.save(outputStream, cellsPdfSaveOptions);
			} else {
				Workbook workBook = new Workbook(inputStream); // ".ODS", ".XLS", ".XLSX"

				workBook.save(outputStream, cellsPdfSaveOptions);
			}
		} catch (CellsException e) {
			throw SupportUtils.customException("Password Protected File");
		} catch (Exception e) {
			throw SupportUtils.customException("saveWorksheetGroup ERROR - " + e.getMessage());
		}
	}

	private void setCsvBorder(Workbook csvWorkBook) {
		int sheetCount = csvWorkBook.getWorksheets().getCount();
		IntStream.range(0, sheetCount).forEach(action -> {
			Worksheet csvSheet = csvWorkBook.getWorksheets().get(action);
			csvSheet.getPageSetup().setPrintGridlines(true);
			try {
				csvSheet.autoFitColumns();
			} catch (Exception e) {
				SupportUtils.printSysout("Set CSV border error");
			}
		});
	}

}