package com.softwarelma.ers_boot_wre.rest;

public class WreRestState {

	private int code = 1;// the ID, 0 means not set, 1 means OK
	private int type = 1;// 0 means not set, 1=info|2=warn|3=error
	private String descr;

	public WreRestState() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WreRestState other = (WreRestState) obj;
		if (code != other.code)
			return false;
		return true;
	}

	public void setState(WreRestState state) {
		this.code = state.code;
		this.type = state.type;
		this.descr = state.descr;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

}
