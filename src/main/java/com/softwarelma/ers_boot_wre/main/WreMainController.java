package com.softwarelma.ers_boot_wre.main;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softwarelma.ers_boot_wre.engine.WreEngine;
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

/**
 * see https://spring.io/guides/gs/rest-service-cors/
 */
@RestController
public class WreMainController {

	private static final Logger logger = Logger.getLogger(WreMainController.class.getName());
	private final WreEngine engine = new WreEngine();

	@CrossOrigin(origins = WreMainConstant.CORS_ORIGIN, allowedHeaders = "*")
	@PostMapping(path = "/rest/postUrl", consumes = "application/json", produces = "application/json")
	public WreRestRespPostUrl postUrl(HttpServletResponse httpServletResponse, @RequestBody WreRestReqPostUrl req) {
		logger.info("postUrl - begin");
		logger.info(" - url = " + req.getUrl());
		WreRestRespPostUrl resp = this.engine.postUrl(req);
		logger.info("postUrl - end");
		return resp;
	}

	@CrossOrigin(origins = WreMainConstant.CORS_ORIGIN, allowedHeaders = "*")
	@PostMapping(path = "/rest/postFile", consumes = "application/json", produces = "application/json")
	public WreRestRespPostFile postFile(HttpServletResponse httpServletResponse, @RequestBody WreRestReqPostFile req) {
		logger.info("postFile - begin");
		WreRestRespPostFile resp = this.engine.postFile(req);
		logger.info("postFile - end");
		return resp;
	}

	@CrossOrigin(origins = WreMainConstant.CORS_ORIGIN, allowedHeaders = "*")
	@PostMapping(path = "/rest/postValidateCsv", consumes = "application/json", produces = "application/json")
	public WreRestRespPostValidateCsv postValidateCsv(HttpServletResponse httpServletResponse,
			@RequestBody WreRestReqPostValidateCsv req) {
		logger.info("postValidateCsv - begin");
		logger.info(" - filename = " + req.getFilename());
		logger.info(" - listHeader = " + req.getListHeader());
		WreRestRespPostValidateCsv resp = this.engine.postValidateCsv(req);
		logger.info("postValidateCsv - end");
		return resp;
	}

	@CrossOrigin(origins = WreMainConstant.CORS_ORIGIN, allowedHeaders = "*")
	@PostMapping(path = "/rest/postExecuteCommand", consumes = "application/json", produces = "application/json")
	public WreRestRespCommand postExecuteCommand(HttpServletResponse httpServletResponse,
			@RequestBody WreRestReqCommand req) {
		logger.info("postExecuteCommand - begin");
		logger.info(" - rFile = " + req.getrFile());
		logger.info(" - params = " + req.getParams());
		WreRestRespCommand resp = this.engine.postExecuteCommand(req);
		logger.info("postExecuteCommand - end");
		return resp;
	}

	@CrossOrigin(origins = WreMainConstant.CORS_ORIGIN, allowedHeaders = "*")
	@PostMapping(path = "/rest/postCreateZip", consumes = "application/json", produces = "application/json")
	public WreRestRespPostCreateZip postCreateZip(HttpServletResponse httpServletResponse,
			@RequestBody WreRestReqPostCreateZip req) {
		logger.info("postCreateZip - begin");
		logger.info(" - suffix = " + req.getSuffix());
		WreRestRespPostCreateZip resp = this.engine.postCreateZip(req);
		logger.info("postCreateZip - end");
		return resp;
	}

}