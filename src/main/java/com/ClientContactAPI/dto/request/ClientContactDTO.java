package com.ClientContactAPI.dto.request;

import com.ClientContactAPI.entity.Contact;
import com.ClientContactAPI.entity.ContactType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class ClientContactDTO {
    
	private Long clientId;
    private ContactType contactType;
    private String contact;

	public ClientContactDTO(Long clientId, ContactType contactType, String contact) {
		super();
		this.clientId = clientId;
		this.contactType = contactType;
		this.contact = contact;
	}
}
