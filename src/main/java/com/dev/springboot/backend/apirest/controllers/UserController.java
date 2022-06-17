package com.dev.springboot.backend.apirest.controllers;

import com.dev.springboot.backend.apirest.models.entities.User;
import com.dev.springboot.backend.apirest.models.services.IUserService;
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
public class UserController {
    @Autowired
    IUserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    @ApiOperation(value = "Encontrar listado de todos los usuarios")
    public ResponseEntity<?> index() {
        Map<String, Object> response = new HashMap<>();

        List<User> users = this.userService.findAll();

        response.put("user", users);
        response.put("message", "Listado de usuarios cargado con exito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}")
    @ApiOperation(value = "Encontrar un usuario por id")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        User user = null;

        try {
            user = this.userService.findById(id);
        } catch (DataAccessException e) {
            response.put("message", "Error al realizar la consulta a la base de datos");
            response.put("error", e.getMessage().concat(": "). concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (user == null) {
            response.put("message", "El usuario Id: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        response.put("user", user);
        response.put("message", "Usuario encontrado con exito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    @ApiOperation(value = "Registrar a un usuario")
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        User newUser = null;

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
               return "El campo '".concat(error.getField()).concat("' ").concat(error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            newUser = this.userService.save(user);
        } catch (DataAccessException e) {
            response.put("message", "Usuario creado con exito");
            response.put("user", newUser);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El usuario ha sido creado con exito");
        response.put("user", newUser);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}")
    @ApiOperation(value = "Actualizar los datos de un usuario por id")
    public ResponseEntity<?> update(@Valid @RequestBody User user, BindingResult result ,@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        User currentUser = this.userService.findById(id);
        User updateUser = null;

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return "El campo '".concat(error.getField()).concat("' ").concat(error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (currentUser == null) {
            response.put("message", "Error: No se pudo actualizar, el usuario ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            currentUser.setName(user.getName());
            currentUser.setLastname(user.getLastname());
            currentUser.setLastname(user.getLastname());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());

            updateUser = this.userService.save(currentUser);
        } catch (DataAccessException e) {
            response.put("message", "Error al actualizar al usuario en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El usuario ha sido actualizado con exito");
        response.put("client", updateUser);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    @ApiOperation(value = "Eliminar un usuario por el id")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            this.userService.delete(id);
        } catch (DataAccessException e) {
            response.put("message", "Error al eliminar el usuario en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El usuario ha sido eliminado con exito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}