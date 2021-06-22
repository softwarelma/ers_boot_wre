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
import com.softwarelma.ers_boot_wre.rest.WreRestReq;
import com.softwarelma.ers_boot_wre.rest.WreRestResp;

/**
 * see https://spring.io/guides/gs/rest-service-cors/
 */
@RestController
public class WreMainController {

	private static final Logger logger = Logger.getLogger(WreMainController.class.getName());
	private final WreEngine engine = new WreEngine();

	@CrossOrigin(origins = WreMainConstant.CORS_ORIGIN)
	@GetMapping("/rest/getFile/{suffix}")
	public WreRestResp getFile(HttpServletResponse httpServletResponse, @PathVariable String suffix) {
		logger.info("getFile - begin - suffix: " + suffix);
		WreRestResp resp = this.engine.getFile(suffix);
		logger.info("getFile - end");
		return resp;
	}

	@CrossOrigin(origins = WreMainConstant.CORS_ORIGIN)
	@PostMapping(path = "/rest/postFile", consumes = "application/json", produces = "application/json")
	public WreRestResp postFile(HttpServletResponse httpServletResponse, @RequestBody WreRestReq req) {
		logger.info("postFile - begin");
		WreRestResp resp = this.engine.postFile(req);
		logger.info("postFile - end");
		return resp;
	}

}