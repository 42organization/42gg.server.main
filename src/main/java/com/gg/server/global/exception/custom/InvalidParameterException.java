package com.gg.server.global.exception.custom;

import com.gg.server.global.exception.ErrorCode;

public class InvalidParameterException extends CustomRuntimeException {
	public InvalidParameterException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}
}
