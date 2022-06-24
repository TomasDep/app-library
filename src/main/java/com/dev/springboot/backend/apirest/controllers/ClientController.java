package com.dev.springboot.backend.apirest.controllers;

import com.dev.springboot.backend.apirest.models.entities.Client;
import com.dev.springboot.backend.apirest.models.services.IClientService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
    private static final String CLIENT = "client";
    private static final String MESSAGE = "message";
    private static final String ERROR = "error";
    private static final String ERRORS = "errors";

    @Autowired
    private IClientService clientService;

    @Autowired
    private MessageSource messageSource;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/clients")
    @ApiOperation(value = "Encontrar el listado de todos los clientes")
    public ResponseEntity<?> index(Locale locale) {
        Map<String, Object> response = new HashMap<>();

        List<Client> clients = this.clientService.findAll();

        response.put(CLIENT, clients);
        response.put(MESSAGE, this.messageSource.getMessage("client.message.successIndex", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/clients/{id}")
    @ApiOperation(value = "Encontrar un cliente por id")
    public ResponseEntity<?> show(@PathVariable Long id, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        String messageNotFound = this.messageSource.getMessage("client.message.clientNull", null, locale);
        Client client = null;

        try{
            client = this.clientService.findById(id);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("client.message.internalServerError", null, locale));
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (client == null) {
           response.put(MESSAGE, String.format(messageNotFound, id.toString()));
           return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        response.put(CLIENT, client);
        response.put(MESSAGE, this.messageSource.getMessage("cliente.message.successShow", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/clients")
    @ApiOperation(value = "Ingresar un nuevo cliente")
    public ResponseEntity<?> create(@Valid @RequestBody Client client, BindingResult result, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        Client newClient = null;

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return "El campo '".concat(error.getField()).concat("' ").concat(error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put(ERRORS, errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            newClient = this.clientService.save(client);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("client.message.successShow", null, locale));
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(MESSAGE, this.messageSource.getMessage("client.message.successCreate", null, locale));
        response.put(CLIENT, newClient);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/clients/{id}")
    @ApiOperation(value = "Actualizar los datos de un cliente por id")
    public ResponseEntity<?> update(
            @Valid @RequestBody Client client,
            BindingResult result,
            @PathVariable Long id,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        String messageNotFound = this.messageSource.getMessage("client.message.clientNull", null, locale);
        Client currentClient = this.clientService.findById(id);
        Client updateClient = null;

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return "El campo '".concat(error.getField()).concat("' ").concat(error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put(ERRORS, errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (currentClient == null) {
            response.put(MESSAGE, String.format(messageNotFound, id.toString()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            currentClient.setName(client.getName());
            currentClient.setLastname(client.getLastname());
            currentClient.setEmail(client.getEmail());
            currentClient.setAddress(client.getAddress());
            currentClient.setPhone(client.getPhone());

            updateClient = this.clientService.save(currentClient);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("client.message.interalServerError", null, locale));
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(MESSAGE, this.messageSource.getMessage("client.message.successUpdate", null, locale));
        response.put(CLIENT, updateClient);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/clients/{id}")
    @ApiOperation(value = "Eliminar un cliente por el id")
    public ResponseEntity<?> delete(@PathVariable Long id, Locale locale) {
        Map<String, Object> response = new HashMap<>();

        try {
            this.clientService.delete(id);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("client.message.interalServerError", null, locale));
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(MESSAGE, this.messageSource.getMessage("client.message.successDelete", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
