package com.ClientContactAPI.controller;

import com.ClientContactAPI.dto.Message;
import com.ClientContactAPI.dto.request.ClientDTO;
import com.ClientContactAPI.dto.responce.ClientContactsDTO;
import com.ClientContactAPI.dto.responce.ContactDTO;
import com.ClientContactAPI.entity.Client;
import com.ClientContactAPI.exception.InvalidDataException;
import com.ClientContactAPI.mapper.EntityDTOmapper;
import com.ClientContactAPI.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class ClientController {
    @Autowired
    private ContactController contactController;
    @Autowired
    private ClientService clientService;
    @Autowired
    private EntityDTOmapper mapper;

    @Operation(summary = "Получение списка клиентов")
    @GetMapping("/clients")
    public List<ClientDTO> getListClientsDTO() {
        return mapper.clientListToClientDTOList(clientService.findAllClients());
    }

    @Operation(summary = "Добавление нового клиента")
    @PostMapping(path = "/clients")
    public ResponseEntity<Message> addClient(@RequestBody ClientDTO clientDTO, HttpServletRequest request) {
        if ((clientDTO.getName() == null) || ("".equals(clientDTO.getName()))) {
            return ResponseEntity.badRequest().body(new Message("Name can't be null or empty"));
        }
        Client client = clientService.saveClient(mapper.clientDTOtoEntity(clientDTO));
        URI uri = URI.create(request.getServletPath() + "/" + String.valueOf(client.getId()));
        return ResponseEntity.created(uri).body(new Message("Created successfully"));
    }

    @Operation(summary = "Получение информации по заданному клиенту (по id)")
    @GetMapping("/clients/{clientId}")
    public ResponseEntity<ClientContactsDTO> getInfoClientId(@PathVariable Long clientId) throws InvalidDataException {
        if (clientId == null) {
            throw (new InvalidDataException(new Message("ClientId can't be null")));
        }
        Optional<Client> optionalClients = clientService.findClientById(clientId);
        if (optionalClients.isPresent()) {
            Client client = optionalClients.get();
            List<ContactDTO> contacts = contactController.getListContactType(clientId, null).getBody();
            ClientContactsDTO dto = mapper.clientContactsToClientContactsDTO(client, contacts);
            return ResponseEntity.ok().body(dto);
        } else {
            throw (new InvalidDataException(new Message("Client not exists")));
        }
    }

}
