package com.softwarelma.ers_boot_wre.rest;

import java.io.Serializable;

import com.softwarelma.ers_boot_wre.model.WreModelFile;

public class WreRestReq implements Serializable {

	private static final long serialVersionUID = 1L;
	private WreModelFile file;

	public WreModelFile getFile() {
		return file;
	}

	public void setFile(WreModelFile file) {
		this.file = file;
	}

}
