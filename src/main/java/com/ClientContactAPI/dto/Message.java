package com.ClientContactAPI.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message {
	private String message;

	public Message(String message) {
		super();
		this.message = message;
	}
}
