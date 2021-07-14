package com.softwarelma.ers_boot_wre.rest;

import java.io.Serializable;

public class WreRestRespPostValidateCsv implements Serializable {

	private static final long serialVersionUID = 1L;
	private WreRestState state = new WreRestState();

	public WreRestState getState() {
		return state;
	}

	public void setState(WreRestState state) {
		this.state = state;
	}

}
