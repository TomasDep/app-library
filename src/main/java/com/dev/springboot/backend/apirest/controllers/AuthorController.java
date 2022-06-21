package com.dev.springboot.backend.apirest.controllers;

import com.dev.springboot.backend.apirest.models.entities.Author;
import com.dev.springboot.backend.apirest.models.services.IAuthorService;
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
public class AuthorController {
    @Autowired
    private IAuthorService authorService;

    @GetMapping("/authors")
    public ResponseEntity<?> index() {
        Map<String, Object> response = new HashMap<>();

        List<Author> authors = this.authorService.findAll();

        response.put("author", authors);
        response.put("message", "Lista de autores cargada correctamente");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/author/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Author author = null;

        try {
            author = this.authorService.findById(id);
        } catch (DataAccessException e) {
            response.put("message", "Error al realizar la consulta a la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (author == null) {
            response.put("message", "El autor Id: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        response.put("author", author);
        response.put("message", "Autor cargado correctamente");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/author")
    public ResponseEntity<?> create(@Valid @RequestBody Author author, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Author newAuthor = null;

        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return "El campo '".concat(error.getField()).concat("' ").concat(error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            newAuthor = this.authorService.save(author);
        } catch (DataAccessException e) {
            response.put("message", "Error al realizar la consulta a la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("author", newAuthor);
        response.put("message", "Autor creado correctamente");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/author/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Author author, BindingResult result, @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Author currentAuthor = this.authorService.findById(id);
        Author updateAuthor = null;

        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return "El campo '".concat(error.getField()).concat("' ").concat(error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (currentAuthor == null) {
            response.put("message", "Error: No se pudo actualizar, el autor ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            currentAuthor.setName(author.getName());
            currentAuthor.setLastname(author.getLastname());
            currentAuthor.setCountry(author.getCountry());
            currentAuthor.setBook(author.getBook());

            updateAuthor = this.authorService.save(currentAuthor);
        } catch (DataAccessException e) {
            response.put("message", "Error al actualizar el autor en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("author", updateAuthor);
        response.put("message", "El autor ha sido actualizado correctamente");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/author/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            this.authorService.delete(id);
        } catch (DataAccessException e) {
            response.put("message", "Error al elimiar al autor en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El autor ha sido eliminado con exito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
