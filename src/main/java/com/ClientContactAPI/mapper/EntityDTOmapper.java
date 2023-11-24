package com.ClientContactAPI.mapper;

import com.ClientContactAPI.dto.request.ClientContactDTO;
import com.ClientContactAPI.dto.request.ClientDTO;
import com.ClientContactAPI.dto.responce.ClientContactsDTO;
import com.ClientContactAPI.dto.responce.ContactDTO;
import com.ClientContactAPI.entity.Client;
import com.ClientContactAPI.entity.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityDTOmapper {

    @Mapping(target = "contactsList", expression = "java(contacts)")
    ClientContactsDTO clientContactsToClientContactsDTO(Client client, List<ContactDTO> contacts);

    @Mapping(target = "id", constant = "0L")
    Client clientDTOtoEntity(ClientDTO clientDTO);

    @Mapping(target = "id", constant = "0L")
    Contact clientContactDTOtoEntity(ClientContactDTO clientContactDTO);

    List<ClientDTO> clientListToClientDTOList(List<Client> clients);

    List<ContactDTO> contactListToContactDTOList(List<Contact> contacts);
}
