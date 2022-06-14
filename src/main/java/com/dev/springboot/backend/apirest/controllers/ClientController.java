package com.dev.springboot.backend.apirest.controllers;

import com.dev.springboot.backend.apirest.models.entities.Client;
import com.dev.springboot.backend.apirest.models.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private IClientService clientService;

    @GetMapping("/clients")
    public List<Client> index() {
        return this.clientService.findAll();
    }

    @GetMapping("/client/{id}")
    public Client show(@PathVariable Long id) {
        return this.clientService.findById(id);
    }

    @PostMapping("/client")
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@RequestBody Client client) {
        return this.clientService.save(client);
    }

    @PutMapping("/client/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Client update(@RequestBody Client client, @PathVariable Long id) {
        Client updateClient = this.clientService.findById(id);

        updateClient.setName(client.getName());
        updateClient.setLastname(client.getLastname());
        updateClient.setEmail(client.getEmail());
        updateClient.setAddress(client.getAddress());
        updateClient.setPhone(client.getPhone());

        return this.clientService.save(updateClient);
    }

    @DeleteMapping("/client/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.clientService.delete(id);
    }
}
