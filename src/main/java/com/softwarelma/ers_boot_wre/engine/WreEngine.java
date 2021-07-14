package com.softwarelma.ers_boot_wre.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.encodings.EpeEncodings;
import com.softwarelma.epe.p2.encodings.EpeEncodingsResponse;
import com.softwarelma.epe.p3.disk.EpeDiskFinalFdzip;
import com.softwarelma.epe.p3.disk.EpeDiskFinalFread_encoding;
import com.softwarelma.epe.p3.disk.EpeDiskFinalFwrite;
import com.softwarelma.epe.p3.disk.EpeDiskFinalList_files;
import com.softwarelma.epe.p3.generic.EpeGenericFinalExec_shell;
import com.softwarelma.epe.p3.xml.EpeXmlFinalXml_read_site;
import com.softwarelma.ers_boot_wre.main.WreMainConstant;
import com.softwarelma.ers_boot_wre.model.WreModelFile;
import com.softwarelma.ers_boot_wre.rest.WreRestReqCommand;
import com.softwarelma.ers_boot_wre.rest.WreRestReqPostCreateZip;
import com.softwarelma.ers_boot_wre.rest.WreRestReqPostFile;
import com.softwarelma.ers_boot_wre.rest.WreRestReqPostUrl;
import com.softwarelma.ers_boot_wre.rest.WreRestReqPostValidateCsv;
import com.softwarelma.ers_boot_wre.rest.WreRestRespCommand;
import com.softwarelma.ers_boot_wre.rest.WreRestRespPostCreateZip;
import com.softwarelma.ers_boot_wre.rest.WreRestRespPostFile;
import com.softwarelma.ers_boot_wre.rest.WreRestRespPostUrl;
import com.softwarelma.ers_boot_wre.rest.WreRestRespPostValidateCsv;

public class WreEngine {

	private static final Logger logger = Logger.getLogger(WreEngine.class.getName());

	public WreRestRespPostUrl postUrl(WreRestReqPostUrl req) {
		WreRestRespPostUrl resp = new WreRestRespPostUrl();
		try {
			resp.setFilename(EpeXmlFinalXml_read_site.readSiteHeaderAttributeCustomWre(false, req.getUrl()));
		} catch (EpeAppException e) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_READING_SITE_FILENAME, req.getUrl());
			logger.log(Level.SEVERE, msg, e);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_READING_SITE_FILENAME);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		String fileContent;
		try {
			fileContent = EpeXmlFinalXml_read_site.readSite(req.getUrl());
		} catch (EpeAppException e) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_READING_SITE, req.getUrl());
			logger.log(Level.SEVERE, msg, e);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_READING_SITE);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		EpeEncodings enc = new EpeEncodings();
		EpeEncodingsResponse endResp;
		try {
			endResp = enc.readGuessingByContent(fileContent);
		} catch (EpeAppException e) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_GUESSING_ENCODING, resp.getFilename());
			logger.log(Level.SEVERE, msg, e);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_GUESSING_ENCODING);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		String filePath = this.getFilePath(resp.getFilename());
		try {
			EpeDiskFinalFwrite.fWrite(false, filePath, endResp.getFileContent(), endResp.getEncoding(), false);
		} catch (EpeAppException e) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_WRITING_FILE, resp.getFilename());
			logger.log(Level.SEVERE, msg + " Path: " + filePath, e);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_WRITING_FILE);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		return resp;
	}

	public WreRestRespPostFile postFile(WreRestReqPostFile req) {
		WreRestRespPostFile resp = new WreRestRespPostFile();
		WreModelFile file = req.getFile();
		EpeEncodings enc = new EpeEncodings();
		String fileContent = new String(Base64.decodeBase64(file.getBase64()));
		// String fileContent = file.getBase64();
		EpeEncodingsResponse endResp;
		try {
			endResp = enc.readGuessingByContent(fileContent);
		} catch (EpeAppException e) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_GUESSING_ENCODING, file.getName());
			logger.log(Level.SEVERE, msg, e);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_GUESSING_ENCODING);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		String filePath = this.getFilePath(file.getName());
		try {
			EpeDiskFinalFwrite.fWrite(false, filePath, endResp.getFileContent(), endResp.getEncoding(), false);
		} catch (EpeAppException e) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_WRITING_FILE, file.getName());
			logger.log(Level.SEVERE, msg + " Path: " + filePath, e);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_WRITING_FILE);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		return resp;
	}

	private String getFilePath(String filename) {
		return WreMainConstant.FOLDER_UPLOAD + "/" + filename;
	}

	public WreRestRespPostValidateCsv postValidateCsv(WreRestReqPostValidateCsv req) {
		WreRestRespPostValidateCsv resp = new WreRestRespPostValidateCsv();
		if (req.getListHeader() == null) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_LIST_HEADER_NOT_FOUND);
			logger.log(Level.SEVERE, msg);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_LIST_HEADER_NOT_FOUND);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		if (req.getFilename() == null) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_FILENAME_NOT_FOUND);
			logger.log(Level.SEVERE, msg);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_FILENAME_NOT_FOUND);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		String filePath = this.getFilePath(req.getFilename());
		String fileContent;
		try {
			fileContent = EpeDiskFinalFread_encoding.fRead(false, filePath, null).getFileContent();
		} catch (EpeAppException e) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_READING_FILE, req.getFilename());
			logger.log(Level.SEVERE, msg + " Path: " + filePath, e);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_READING_FILE);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		String[] arrayLine = fileContent.split("\n");
		String line1 = null;
		for (String line : arrayLine) {
			if (!line.trim().equals("")) {
				line1 = line;
				break;
			}
		}
		if (line1 == null) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_HEADER_LINE_NOT_FOUND);
			logger.log(Level.SEVERE, msg);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_HEADER_LINE_NOT_FOUND);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		line1 = ";" + line1.trim().toLowerCase() + ";";
		String[] arrayRequiredHeader = req.getListHeader().split(";");
		List<String> listMissingHeader = new LinkedList<>();
		for (String requiredHeader : arrayRequiredHeader) {
			requiredHeader = requiredHeader.trim();
			if (requiredHeader.equals(""))
				continue;
			if (!line1.contains(";" + requiredHeader.toLowerCase() + ";"))
				listMissingHeader.add(requiredHeader);
		}
		if (!listMissingHeader.isEmpty()) {
			String missingHeaders = listMissingHeader.toString();
			missingHeaders = missingHeaders.substring(1, missingHeaders.length() - 1);
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_HEADERS_NOT_FOUND, missingHeaders);
			logger.log(Level.SEVERE, msg);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_HEADERS_NOT_FOUND);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		return resp;
	}

	public WreRestRespCommand postExecuteCommand(WreRestReqCommand req) {
		WreRestRespCommand resp = new WreRestRespCommand();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
		String suffix = format.format(new Date());
		String command = String.format(WreMainConstant.COMMAND, req.getrFile(), req.getParams(), suffix);
		logger.info(" - command = " + command);
		try {
			List<String> listFinalOutput = new ArrayList<>();
			String output = EpeGenericFinalExec_shell.execShellScript(command, new ArrayList<>(), listFinalOutput);
			output += output.equals("0") ? " (OK)" : " (KO)";
			logger.info(" - output = " + output);
			if (output.contains("KO")) {
				resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_EXECUTION_ENDED_WITH_ERROR);
				resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
				resp.getState().setDescr(WreMainConstant.STATE_DESCR_ERROR_EXECUTION_ENDED_WITH_ERROR);
				resp.setSuffix(suffix);
				return resp;
			}
			for (String finalOutput : listFinalOutput) {
				if (finalOutput == null || finalOutput.trim().isEmpty())
					continue;
				if (finalOutput.toLowerCase().contains("error in")
						|| finalOutput.toLowerCase().contains("execution halted")) {
					resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_EXECUTION_HALTED);
					resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
					resp.getState().setDescr(WreMainConstant.STATE_DESCR_ERROR_EXECUTION_HALTED);
					return resp;
				}
			}
		} catch (EpeAppException e) {
			String msg = WreMainConstant.STATE_DESCR_ERROR_EXECUTING_SHELL_SCRIPT;
			logger.log(Level.SEVERE, msg, e);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_EXECUTING_SHELL_SCRIPT);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		resp.setSuffix(suffix);
		return resp;
	}

	public WreRestRespPostCreateZip postCreateZip(WreRestReqPostCreateZip req) {
		WreRestRespPostCreateZip resp = new WreRestRespPostCreateZip();
		String base64;
		File fileZip;
		String zipName = WreMainConstant.FOLDER_DOWNLOAD + "/" + req.getSuffix() + ".zip";
		try {
			List<String> listFiles = EpeDiskFinalList_files.listFiles(WreMainConstant.FOLDER_DOWNLOAD, null, null, null,
					"suffixNoExtension=" + req.getSuffix());
			List<String> listFullFileName = new ArrayList<>(listFiles.size());
			for (String file : listFiles) {
				listFullFileName.add(WreMainConstant.FOLDER_DOWNLOAD + "/" + file);
			}
			EpeDiskFinalFdzip.createZip(false, listFullFileName, zipName);
			fileZip = new File(zipName);
		} catch (EpeAppException e) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_LISTING_FILES, req.getSuffix());
			logger.log(Level.SEVERE, msg, e);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_LISTING_FILES);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		try {
			FileInputStream fis = new FileInputStream(fileZip);
			byte[] arrayByte = IOUtils.toByteArray(fis);
			fis.close();
			// FileUtils.writeByteArrayToFile(new File(zipName2), arrayByte);
			base64 = Base64.encodeBase64String(arrayByte);
		} catch (IOException e) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_READING_FILE, zipName);
			logger.log(Level.SEVERE, msg, e);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_READING_FILE);
			resp.getState().setType(WreMainConstant.STATE_TYPE_ERROR);
			resp.getState().setDescr(msg);
			return resp;
		}
		WreModelFile file = new WreModelFile();
		resp.setFile(file);
		file.setBase64(base64);
		file.setName(req.getSuffix() + ".zip");
		return resp;
	}

}
