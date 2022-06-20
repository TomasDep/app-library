package com.dev.springboot.backend.apirest.controllers;

import com.dev.springboot.backend.apirest.models.entities.Client;
import com.dev.springboot.backend.apirest.models.services.IClientService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private IClientService clientService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/clients")
    @ApiOperation(value = "Encontrar el listado de todos los clientes")
    public ResponseEntity<?> index() {
        Map<String, Object> response = new HashMap<>();

        List<Client> clients = this.clientService.findAll();

        response.put("client", clients);
        response.put("message", "Listado de clientes cargado con exito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/clients/{id}")
    @ApiOperation(value = "Encontrar un cliente por id")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Client client = null;

        try{
            client = this.clientService.findById(id);
        } catch (DataAccessException e) {
            response.put("message", "Error al realizar la consulta a la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (client == null) {
           response.put("message", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos")));
           return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        response.put("client", client);
        response.put("message", "Cliente encontrado con exito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/clients")
    @ApiOperation(value = "Ingresar un nuevo cliente")
    public ResponseEntity<?> create(@Valid @RequestBody Client client, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Client newClient = null;

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return "El campo '".concat(error.getField()).concat("' ").concat(error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            newClient = this.clientService.save(client);
        } catch (DataAccessException e) {
            response.put("message", "Error al insertar a la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El cliente ha sido creado con exito");
        response.put("client", newClient);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/clients/{id}")
    @ApiOperation(value = "Actualizar los datos de un cliente por id")
    public ResponseEntity<?> update(@Valid @RequestBody Client client, BindingResult result, @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Client currentClient = this.clientService.findById(id);
        Client updateClient = null;

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return "El campo '".concat(error.getField()).concat("' ").concat(error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (currentClient == null) {
            response.put("message", "Error: No se pudo actualizar, el cliente ID: ".concat(id.toString().concat(" no existe en la base de datos")));
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
            response.put("message", "Error al actualizar el cliente en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El cliente ha sido actualizado con exito");
        response.put("client", updateClient);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/clients/{id}")
    @ApiOperation(value = "Eliminar un cliente por el id")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            this.clientService.delete(id);
        } catch (DataAccessException e) {
            response.put("message", "Error al eliminar el cliente en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El cliente ha sido eliminado con exito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
