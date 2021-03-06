package com.dev.springboot.backend.apirest.controllers;

import com.dev.springboot.backend.apirest.dto.AuthorDto;
import com.dev.springboot.backend.apirest.models.entities.Author;
import com.dev.springboot.backend.apirest.models.services.IAuthorService;
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
public class AuthorController {
    private static final String AUTHOR = "author";
    private static final String MESSAGE = "message";
    private static final String ERROR = "error";
    private static final String ERRORS = "errors";
    private static final String TOTAL = "total";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private IAuthorService authorService;

    @ApiOperation(value = "${AuthorController.index.value}")
    @GetMapping("/authors")
    public ResponseEntity<?> index(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        Page<Author> authorPage = this.authorService.findAll(pageNumber, pageSize);
        List<Author> authors = authorPage.getContent();

        response.put(MESSAGE, this.messageSource.getMessage("author.message.successIndex", null, locale));
        response.put(AUTHOR, authors);
        response.put(TOTAL, authorPage.getTotalElements());

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "${AuthorController.show.value}")
    @GetMapping("/author/{id}")
    public ResponseEntity<?> show(@PathVariable Long id, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        Author author = new Author();
        String authorNull = this.messageSource.getMessage("author.message.authorNull", null, locale);

        try {
            author = this.authorService.findById(id);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("author.message.internalServerError", null, locale));
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (author == null) {
            response.put(MESSAGE, String.format(authorNull, id.toString()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        response.put(AUTHOR, author);
        response.put(MESSAGE, this.messageSource.getMessage("author.message.successShow", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "${AuthorController.create.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/author")
    public ResponseEntity<?> create(
            @Valid @RequestBody AuthorDto author,
            BindingResult result,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        Author newAuthor = new Author();
        String errorsMessage = this.messageSource.getMessage("author.message.errors", null, locale);
        String errorDataAccessMessage = this.messageSource.getMessage("author.message.errorDataAccess", null, locale);

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return String.format(errorsMessage, error.getField(), error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put(ERRORS, errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            newAuthor = this.authorService.save(
                    new Author(author.getName(), author.getLastname(), author.getCountry())
            );
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("author.message.internalServerError", null, locale));
            response.put(ERROR, String.format(errorDataAccessMessage, e.getMessage(), e.getMostSpecificCause()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(AUTHOR, newAuthor);
        response.put(MESSAGE, this.messageSource.getMessage("author.message.successCreate", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "${AuthorController.update.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/author/{id}")
    public ResponseEntity<?> update(
            @Valid @RequestBody AuthorDto author,
            BindingResult result,
            @PathVariable Long id,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        Author currentAuthor = this.authorService.findById(id);
        Author updateAuthor = new Author();
        String errorsMessage = this.messageSource.getMessage("author.message.errors", null, locale);
        String authorNull = this.messageSource.getMessage("author.message.authorNull", null, locale);

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return String.format(errorsMessage, error.getField(), error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put(ERRORS, errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (currentAuthor == null) {
            response.put(MESSAGE, String.format(authorNull, id.toString()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            currentAuthor.setName(author.getName());
            currentAuthor.setLastname(author.getLastname());
            currentAuthor.setCountry(author.getCountry());

            updateAuthor = this.authorService.save(currentAuthor);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("author.message.internalServerError", null, locale));
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(AUTHOR, updateAuthor);
        response.put(MESSAGE, this.messageSource.getMessage("author.message.successUpdate", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "${AuthorController.delete.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/author/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        String errorMessage = this.messageSource.getMessage("author.message.errorDataAccess", null, locale);

        try {
            this.authorService.delete(id);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("author.message.internalServerError", null, locale));
            response.put(ERROR, String.format(errorMessage, e.getMessage(), e.getMostSpecificCause()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(MESSAGE, this.messageSource.getMessage("author.message.successDelete", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
