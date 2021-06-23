package com.softwarelma.ers_boot_wre.rest;

import java.io.Serializable;

public class WreRestRespCommand implements Serializable {

	private static final long serialVersionUID = 1L;
	private WreRestState state = new WreRestState();
	private String suffix;

	public WreRestState getState() {
		return state;
	}

	public void setState(WreRestState state) {
		this.state = state;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
