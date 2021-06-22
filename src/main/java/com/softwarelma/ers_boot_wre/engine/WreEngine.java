package com.softwarelma.ers_boot_wre.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import com.softwarelma.ers_boot_wre.main.WreMainConstant;
import com.softwarelma.ers_boot_wre.model.WreModelFile;
import com.softwarelma.ers_boot_wre.rest.WreRestReq;
import com.softwarelma.ers_boot_wre.rest.WreRestResp;

public class WreEngine {

	private static final Logger logger = Logger.getLogger(WreEngine.class.getName());

	public WreRestResp getFile(String suffix) {
		WreRestResp resp = new WreRestResp();
		String base64;
		try {
			List<String> listFiles = EpeDiskFinalList_files.listFiles(WreMainConstant.FOLDER_DOWNLOAD, null, null, "suffix=" + suffix);
			List<String> listFullFileName = new ArrayList<>(listFiles.size());
			for (String file : listFiles) {
				listFullFileName.add(WreMainConstant.FOLDER_DOWNLOAD + "/" + file);
			}
			String zipName = WreMainConstant.FOLDER_DOWNLOAD + "/" + suffix + ".zip";
			EpeDiskFinalFdzip.createZip(false, listFullFileName, zipName);
			File fileZip = new File(zipName);
			try {
				FileInputStream fis = new FileInputStream(fileZip);
				byte[] arrayByte = IOUtils.toByteArray(fis);
				fis.close();
				// FileUtils.writeByteArrayToFile(new File(zipName2), arrayByte);
				base64 = Base64.encodeBase64String(arrayByte);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return resp;
			}
		} catch (EpeAppException e) {
			String msg = String.format(WreMainConstant.STATE_DESCR_ERROR_LISTING_FILES, suffix);
			logger.log(Level.SEVERE, msg, e);
			resp.getState().setCode(WreMainConstant.STATE_CODE_ERROR_LISTING_FILES);
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

	public WreRestResp postFile(WreRestReq req) {
		WreRestResp resp = new WreRestResp();
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

}
