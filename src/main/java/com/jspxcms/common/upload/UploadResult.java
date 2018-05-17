package com.jspxcms.common.upload;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class UploadResult {
	/**
	 * 错误状态码
	 */
	public static final int ERROR_STATUS = 500;

	private int status = 0;
	private String message;
	private String fileUrl;
	private String fileName;
	private long fileLength;
	private String fileExtension;
	private String pdfUrl;
	private String swfUrl;

	private MessageSource messageSource;
	private Locale locale;

	public UploadResult() {
	}

	public UploadResult(String fileUrl, String fileName, String fileExtension,
			long fileLength) {
		set(fileUrl, fileName, fileExtension, fileLength);
	}

	public void setMessageSource(MessageSource messageSource, Locale locale) {
		this.messageSource = messageSource;
		this.locale = locale;
	}

	public void set(String fileUrl, String fileName, String fileExtension,
			long fileLength) {
		this.fileUrl = fileUrl;
		this.fileName = fileName;
		this.fileExtension = fileExtension;
		this.fileLength = fileLength;
	}

	public void set(String fileUrl, String fileName, String fileExtension,
			long fileLength, String pdfUrl, String swfUrl) {
		this.fileUrl = fileUrl;
		this.fileName = fileName;
		this.fileExtension = fileExtension;
		this.fileLength = fileLength;
		this.pdfUrl = pdfUrl;
		this.swfUrl = swfUrl;
	}

	public void setError(String message) {
		this.status = ERROR_STATUS;
		this.message = message;
	}

	public void setErrorCode(String code) {
		setErrorCode(code, null);
	}

	public void setErrorCode(String code, String[] args) {
		this.status = ERROR_STATUS;
		setCode(code, args);
	}

	public void setCode(String code) {
		setCode(code, null);
	}

	public void setCode(String code, String[] args) {
		if (messageSource != null && locale != null) {
			setMessage(messageSource.getMessage(code, args, code, locale));
		} else {
			this.message = code;
		}
	}

	public boolean isError() {
		return status >= ERROR_STATUS;
	}

	public boolean isSuccess() {
		return !isError();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public String getSwfUrl() {
		return swfUrl;
	}

	public void setSwfUrl(String swfUrl) {
		this.swfUrl = swfUrl;
	}

}