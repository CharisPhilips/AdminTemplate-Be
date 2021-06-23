package com.kilcote.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestException extends RuntimeException {

	private static final long serialVersionUID = -7228712698992476652L;
	
	private String message;
    private Object[] args;
}
