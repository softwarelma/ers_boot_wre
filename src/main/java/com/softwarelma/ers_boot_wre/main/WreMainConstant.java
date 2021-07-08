package com.softwarelma.ers_boot_wre.main;

public class WreMainConstant {

	public static final String CORS_ORIGIN_DEVELOP = "*";
	public static final String CORS_ORIGIN_PRODUCTION = "https://www.phytovre.lifewatchitaly.eu";
	public static final String CORS_ORIGIN = CORS_ORIGIN_PRODUCTION; // FIXME unica costante da settare

	public static final String FOLDER_UPLOAD_DEVELOP = "C:/develop/workspaces/wre";
	public static final String FOLDER_UPLOAD_PRODUCTION = "/media/rserver/PhytoFiles";
	public static final String FOLDER_UPLOAD = isProd() ? FOLDER_UPLOAD_PRODUCTION : FOLDER_UPLOAD_DEVELOP;

	public static final String FOLDER_DOWNLOAD_DEVELOP = "C:/develop/workspaces/wre";
	public static final String FOLDER_DOWNLOAD_PRODUCTION = "/media/rserver/PhytoFiles";
	public static final String FOLDER_DOWNLOAD = isProd() ? FOLDER_DOWNLOAD_PRODUCTION : FOLDER_DOWNLOAD_DEVELOP;

	public static final String COMMAND_DEVELOP = "echo file=%s params=%s\necho suffix=%s\nexit";
	public static final String COMMAND_PRODUCTION = "Rscript /home/rserver/PHYTO/PhytoScripts/%s %s %s\nexit";
	public static final String COMMAND = isProd() ? COMMAND_PRODUCTION : COMMAND_DEVELOP;

	public static final int STATE_CODE_OK = 1;
	public static final int STATE_CODE_ERROR_GUESSING_ENCODING = 2;
	public static final int STATE_CODE_ERROR_WRITING_FILE = 3;
	public static final int STATE_CODE_ERROR_LISTING_FILES = 4;
	public static final int STATE_CODE_ERROR_READING_FILE = 5;
	public static final int STATE_CODE_ERROR_EXECUTING_SHELL_SCRIPT = 6;
	public static final int STATE_CODE_ERROR_EXECUTION_ENDED_WITH_ERROR = 7;
	public static final int STATE_CODE_ERROR_EXECUTION_HALTED = 8;

	public static final int STATE_TYPE_INFO = 1;
	public static final int STATE_TYPE_WARN = 2;
	public static final int STATE_TYPE_ERROR = 3;

	public static final String STATE_DESCR_ERROR_GUESSING_ENCODING = "Error guessing the encoding for file \"%s\".";
	public static final String STATE_DESCR_ERROR_WRITING_FILE = "Error writing file \"%s\".";
	public static final String STATE_DESCR_ERROR_LISTING_FILES = "Error listing files with suffix \"%s\".";
	public static final String STATE_DESCR_ERROR_READING_FILE = "Error reading file \"%s\".";
	public static final String STATE_DESCR_ERROR_EXECUTING_SHELL_SCRIPT = "Error executing shell script.";
	public static final String STATE_DESCR_ERROR_EXECUTION_ENDED_WITH_ERROR = "Execution ended with error.";
	public static final String STATE_DESCR_ERROR_EXECUTION_HALTED = "Execution halted.";

	private static boolean isProd() {
		return CORS_ORIGIN == CORS_ORIGIN_PRODUCTION;
	}

}
