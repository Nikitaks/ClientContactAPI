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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список клиентов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ClientDTO.class)))
                    })
    })
    @GetMapping("/clients")
    public List<ClientDTO> getListClientsDTO() {
        return mapper.clientListToClientDTOList(clientService.findAllClients());
    }

    @Operation(summary = "Добавление нового клиента")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Имя клиента")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Сообщение об успешном добавлении",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Message.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка проверки входных данных",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Message.class))
                    })
    })
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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Объект со всеми данными по клиенту",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClientContactsDTO.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Сообщение с причиной невозможности получения информации",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Message.class))
                    })
    })
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
