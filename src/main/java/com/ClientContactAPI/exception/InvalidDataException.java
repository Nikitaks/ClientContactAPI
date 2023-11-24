package com.ClientContactAPI.exception;

import com.ClientContactAPI.dto.Message;

public class InvalidDataException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private Message messageDTO;

	public InvalidDataException(Message messageDTO) {
		super();
		this.messageDTO = messageDTO;
	}

	public Message getMessageDTO() {
		return messageDTO;
	}
}
