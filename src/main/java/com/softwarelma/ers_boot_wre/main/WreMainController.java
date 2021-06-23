package com.softwarelma.ers_boot_wre.main;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softwarelma.ers_boot_wre.engine.WreEngine;
import com.softwarelma.ers_boot_wre.rest.WreRestReqCommand;
import com.softwarelma.ers_boot_wre.rest.WreRestReqPostFile;
import com.softwarelma.ers_boot_wre.rest.WreRestRespCommand;
import com.softwarelma.ers_boot_wre.rest.WreRestRespGetFile;
import com.softwarelma.ers_boot_wre.rest.WreRestRespPostFile;

/**
 * see https://spring.io/guides/gs/rest-service-cors/
 */
@RestController
public class WreMainController {

	private static final Logger logger = Logger.getLogger(WreMainController.class.getName());
	private final WreEngine engine = new WreEngine();

	@CrossOrigin(origins = WreMainConstant.CORS_ORIGIN)
	@PostMapping(path = "/rest/postFile", consumes = "application/json", produces = "application/json")
	public WreRestRespPostFile postFile(HttpServletResponse httpServletResponse, @RequestBody WreRestReqPostFile req) {
		logger.info("postFile - begin");
		WreRestRespPostFile resp = this.engine.postFile(req);
		logger.info("postFile - end");
		return resp;
	}

	@CrossOrigin(origins = WreMainConstant.CORS_ORIGIN)
	@PostMapping(path = "/rest/postExecuteCommand", consumes = "application/json", produces = "application/json")
	public WreRestRespCommand postExecuteCommand(HttpServletResponse httpServletResponse, @RequestBody WreRestReqCommand req) {
		logger.info("postExecuteCommand - begin");
		logger.info(" - rFile = " + req.getrFile());
		logger.info(" - params = " + req.getParams());
		WreRestRespCommand resp = this.engine.postExecuteCommand(req);
		logger.info("postExecuteCommand - end");
		return resp;
	}

	@CrossOrigin(origins = WreMainConstant.CORS_ORIGIN)
	@GetMapping("/rest/getFile/{suffix}")
	public WreRestRespGetFile getFile(HttpServletResponse httpServletResponse, @PathVariable String suffix) {
		logger.info("getFile - begin");
		logger.info(" - suffix = " + suffix);
		WreRestRespGetFile resp = this.engine.getFile(suffix);
		logger.info("getFile - end");
		return resp;
	}

}