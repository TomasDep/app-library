package com.dev.springboot.backend.apirest.controllers;

import com.dev.springboot.backend.apirest.dto.GenreDto;
import com.dev.springboot.backend.apirest.models.entities.Genre;
import com.dev.springboot.backend.apirest.models.services.IGenreService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
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
public class GenreController {
    private static final String GENRES = "genres";
    private static final String MESSAGE = "message";
    private static final String ERROR = "error";
    private static final String ERRORS = "errors";
    private static final String TOTAL = "total";

    @Autowired
    private IGenreService genreService;

    @Autowired
    private MessageSource messageSource;

    @ApiOperation(value = "${GenreController.index.value}")
    @GetMapping("/genres")
    public ResponseEntity<?> index(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        Page<Genre> genrePage = this.genreService.findAll(pageNumber, pageSize);
        List<Genre> genres = genrePage.getContent();

        response.put(MESSAGE, this.messageSource.getMessage("genres.message.successIndex", null, locale));
        response.put(GENRES, genres);
        response.put(TOTAL, genrePage.getTotalElements());

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "${GenreController.show.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("genre/{id}")
    public ResponseEntity<?> show(@PathVariable Long id, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        String messageDataAccess = this.messageSource.getMessage("genres.message.errorDataAccess", null, locale);
        String messageNotFound = this.messageSource.getMessage("genres.message.clientNull", null, locale);
        Genre genre = new Genre();

        try {
            genre = this.genreService.findById(id);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("genres.message.internalServerError", null, locale));
            response.put(ERROR, String.format(messageDataAccess, e.getMessage(), e.getMostSpecificCause()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (genre == null) {
            response.put(MESSAGE, String.format(messageNotFound, id.toString()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        response.put(GENRES, genre);
        response.put(MESSAGE, this.messageSource.getMessage("genres.message.successShow", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "${GenreController.create.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/genre")
    public ResponseEntity<?> create(
            @Valid @RequestBody GenreDto genre,
            BindingResult result,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        String messageDataAccess = this.messageSource.getMessage("genres.message.errorDataAccess", null, locale);
        String messageErrors = this.messageSource.getMessage("genres.message.errors", null, locale);
        Genre newGenre = new Genre();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return String.format(messageErrors, error.getField(), error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put(ERRORS, errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            newGenre = this.genreService.save(new Genre(genre.getName()));
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("genres.message.internalServerError", null, locale));
            response.put(ERROR, String.format(messageDataAccess, e.getMessage(), e.getMostSpecificCause()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(GENRES, newGenre);
        response.put(MESSAGE, this.messageSource.getMessage("genres.message.successCreate", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "${GenreController.update.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/genre/{id}")
    public ResponseEntity<?> update(
            @Valid @RequestBody GenreDto genre,
            BindingResult result,
            @PathVariable Long id,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        Genre currentGenres = this.genreService.findById(id);
        String messageDataAccess = this.messageSource.getMessage("genres.message.errorDataAccess", null, locale);
        String messageErrors = this.messageSource.getMessage("genres.message.errors", null, locale);
        String messageNotFound = this.messageSource.getMessage("genres.message.clientNull", null, locale);
        Genre updateGenres = new Genre();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return String.format(messageErrors, error.getField(), error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put(ERRORS, errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (currentGenres == null) {
            response.put(MESSAGE, String.format(messageNotFound, id.toString()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            currentGenres.setName(genre.getName());

            updateGenres = this.genreService.save(currentGenres);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("genres.message.internalServerError", null, locale));
            response.put(ERROR, String.format(messageDataAccess, e.getMessage(), e.getMostSpecificCause()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(MESSAGE, this.messageSource.getMessage("genres.message.successUpdate", null, locale));
        response.put(GENRES, updateGenres);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "${GenreController.delete.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/genre/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        String messageDataAccess = this.messageSource.getMessage("genres.message.errorDataAccess", null, locale);

        try {
            this.genreService.delete(id);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("genres.message.internalServerError", null, locale));
            response.put(ERROR, String.format(messageDataAccess, e.getMessage(), e.getMostSpecificCause()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(MESSAGE, this.messageSource.getMessage("genres.message.successDelete", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}