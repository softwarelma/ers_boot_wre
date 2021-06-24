package com.softwarelma.ers_boot_wre.main;

public class WreMainConstant {

	public static final String CORS_ORIGIN = "*";// "https://www.phytovre.lifewatchitaly.eu";
	public static final String FOLDER_UPLOAD = "C:/develop/workspaces/wre";
	public static final String FOLDER_DOWNLOAD = "C:/develop/workspaces/wre";
	public static final String COMMAND = "echo file=%s params=%s\necho suffix=%s\nexit";

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

}
