package com.softwarelma.ers_boot_wre.rest;

import java.io.Serializable;

public class WreRestRespPostUrl implements Serializable {

	private static final long serialVersionUID = 1L;
	private WreRestState state = new WreRestState();
	private String filename;

	public WreRestState getState() {
		return state;
	}

	public void setState(WreRestState state) {
		this.state = state;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
