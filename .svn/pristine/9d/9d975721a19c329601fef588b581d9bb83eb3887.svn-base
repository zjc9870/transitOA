package com.expect.admin.exception;

public class BaseAppException extends RuntimeException{

	private static final long serialVersionUID = 4727024994189306169L;
	
	private String exceptionMessage;

	public BaseAppException(String exceptionMessage) {
		super();
		this.exceptionMessage = exceptionMessage;
	}
	
	public BaseAppException() {
	}
	
	public BaseAppException(String msg, Throwable e) {
		super();
		this.exceptionMessage = msg;
		this.initCause(e);
	}
	
	public void setCause(Throwable e) {
		this.initCause(e);
	}
	
	public String toString() {
		String s = getClass().getName();
		return s + ": " + exceptionMessage;
	}

	public String getMessage() {
		return exceptionMessage;
	}
}
