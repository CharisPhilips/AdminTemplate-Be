package com.kilcote.common.exceptions;

import java.util.List;
import lombok.Getter;

@Getter
public class RestMessage {
	
	private String message;
	private List<String> messages;

	public RestMessage(List<String> messages) {
		this.messages = messages;
	}

	public RestMessage(String message) {
		this.message = message;
	}
}
