package com.dev.springboot.backend.apirest.controllers;

import com.dev.springboot.backend.apirest.models.entities.Genre;
import com.dev.springboot.backend.apirest.models.services.IGenreService;
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
public class GenreController {
    @Autowired
    private IGenreService genreService;

    @ApiOperation(value = "Listado de Generos")
    @GetMapping("/genres")
    public ResponseEntity<?> index() {
        Map<String, Object> response = new HashMap<>();

        List<Genre> genres = this.genreService.findAll();

        response.put("genres", genres);
        response.put("message", "Listado de generos cargado con exito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Mostrar genero por id")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("genre/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Genre genre = null;

        try {
            genre = this.genreService.findById(id);
        } catch (DataAccessException e) {
            response.put("message", "Error al realizar la consulta a la base de datos");
            response.put("error", response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (genre == null) {
            response.put("message", "El genero ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        response.put("genre", genre);
        response.put("message", "Genero encontrado con exito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Crear un genero")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/genre")
    public ResponseEntity<?> create(@Valid @RequestBody Genre genre, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Genre newGenre = null;

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return "El campo '".concat(error.getField()).concat("' ").concat(error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            newGenre = this.genreService.save(genre);
        } catch (DataAccessException e) {
            response.put("message", "Error al insertar a la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("genre", newGenre);
        response.put("message", "Genero creado con exito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Actualizar un genero")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/genre/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Genre genre, BindingResult result, @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Genre currentGenres = this.genreService.findById(id);
        Genre updateGenres = null;

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return "El campo '".concat(error.getField()).concat("' ").concat(error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (currentGenres == null) {
            response.put("message", "Error: No se pudo actualizar, el cliente ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            currentGenres.setName(genre.getName());

            updateGenres = this.genreService.save(currentGenres);
        } catch (DataAccessException e) {
            response.put("message", "Error al actualizar el cliente en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El genero ha sido actualizado con exito");
        response.put("client", updateGenres);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Eliminar un genero")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/genre/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            this.genreService.delete(id);
        } catch (DataAccessException e) {
            response.put("message", "Error al eliminar el cliente en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El genero ha sido eliminado con exito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}