package com.softwarelma.ers_boot_wre.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.encodings.EpeEncodings;
import com.softwarelma.epe.p2.encodings.EpeEncodingsResponse;
import com.softwarelma.epe.p3.disk.EpeDiskFinalFdzip;
import com.softwarelma.epe.p3.disk.EpeDiskFinalFwrite;
import com.softwarelma.epe.p3.disk.EpeDiskFinalList_files;
import com.softwarelma.epe.p3.generic.EpeGenericFinalExec_shell;
import com.softwarelma.ers_boot_wre.main.WreMainConstant;
import com.softwarelma.ers_boot_wre.model.WreModelFile;
import com.softwarelma.ers_boot_wre.rest.WreRestReqCommand;
import com.softwarelma.ers_boot_wre.rest.WreRestReqPostFile;
import com.softwarelma.ers_boot_wre.rest.WreRestRespCommand;
import com.softwarelma.ers_boot_wre.rest.WreRestRespGetFile;
import com.softwarelma.ers_boot_wre.rest.WreRestRespPostFile;

public class WreEngine {

	private static final Logger logger = Logger.getLogger(WreEngine.class.getName());

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
		String filePath = WreMainConstant.FOLDER_UPLOAD + "/" + file.getName();
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
				return resp;
			}
			for (String finalOutput : listFinalOutput) {
				if (finalOutput == null || finalOutput.trim().isEmpty())
					continue;
				if (finalOutput.toLowerCase().contains("error in") || finalOutput.toLowerCase().contains("execution halted")) {
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

	public WreRestRespGetFile getFile(String suffix) {
		WreRestRespGetFile resp = new WreRestRespGetFile();
		String base64;
		File fileZip;
		String zipName = WreMainConstant.FOLDER_DOWNLOAD + "/" + suffix + ".zip";
		try {
			List<String> listFiles = EpeDiskFinalList_files.listFiles(WreMainConstant.FOLDER_DOWNLOAD, null, null, null, "suffixNoExtension=" + suffix);
			List<String> listFullFileName = new ArrayList<>(listFiles.size());
			for (String file : listFiles) {
				listFullFileName.add(WreMainConstant.FOLDER_DOWNLOAD + "/" + file);
			}
			EpeDiskFinalFdzip.createZip(false, listFullFileName, zipName);
			fileZip = new File(zipName);
		} catch (EpeAppException e) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_LISTING_FILES, suffix);
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
		file.setName(suffix + ".zip");
		return resp;
	}

}
