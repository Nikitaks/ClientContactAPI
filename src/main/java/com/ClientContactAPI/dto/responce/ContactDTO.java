package com.ClientContactAPI.dto.responce;

import com.ClientContactAPI.entity.ContactType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class ContactDTO {
    
    private String contact;
    private ContactType contactType;
}
