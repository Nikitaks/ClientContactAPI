package com.ClientContactAPI.dto.responce;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class ClientContactsDTO {

	private String name;
	private List<ContactDTO> contactsList;
}
