package com.softwarelma.ers_boot_wre.rest;

import java.io.Serializable;

public class WreRestReqPostValidateCsv implements Serializable {

	private static final long serialVersionUID = 1L;
	private String filename;
	private String listHeader;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getListHeader() {
		return listHeader;
	}

	public void setListHeader(String listHeader) {
		this.listHeader = listHeader;
	}

}
