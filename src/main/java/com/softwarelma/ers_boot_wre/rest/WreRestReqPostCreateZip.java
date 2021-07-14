package com.softwarelma.ers_boot_wre.rest;

import java.io.Serializable;

public class WreRestReqPostCreateZip implements Serializable {

	private static final long serialVersionUID = 1L;
	private String suffix;

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
