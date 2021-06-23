package com.kilcote.common.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultResponse {
	
	private int code = 0; //errorCode
	private String msg; //
	private Object data;
	private Long count;
}
