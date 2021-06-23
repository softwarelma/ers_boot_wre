package com.softwarelma.ers_boot_wre.rest;

import java.io.Serializable;

public class WreRestReqCommand implements Serializable {

	private static final long serialVersionUID = 1L;
	private String rFile;
	private String params;

	public String getrFile() {
		return rFile;
	}

	public void setrFile(String rFile) {
		this.rFile = rFile;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

}
