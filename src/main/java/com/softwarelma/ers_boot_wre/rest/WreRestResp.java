package com.softwarelma.ers_boot_wre.rest;

import java.io.Serializable;

import com.softwarelma.ers_boot_wre.model.WreModelFile;

public class WreRestResp implements Serializable {

	private static final long serialVersionUID = 1L;
	private WreRestState state = new WreRestState();
	private WreModelFile file;

	public WreRestState getState() {
		return state;
	}

	public void setState(WreRestState state) {
		this.state = state;
	}

	public WreModelFile getFile() {
		return file;
	}

	public void setFile(WreModelFile file) {
		this.file = file;
	}

}
