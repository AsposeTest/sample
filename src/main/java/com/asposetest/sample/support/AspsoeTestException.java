package com.asposetest.sample.support;

/**
 * AspsoeTestException class is used as an user-defined Exception Class
 * 
 * @author HARIHARAN MANI
 * @since 07-01-2022
 */
public class AspsoeTestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String asposeTestErrorMsg;

	// USE THIS CONSTRUCTOR ALWAYS
	public AspsoeTestException(String localeDefMsg) {
		super(localeDefMsg);
		this.asposeTestErrorMsg = localeDefMsg;
	}

	public String getAsposeTestErrorMsg() {
		return this.asposeTestErrorMsg;
	}
}