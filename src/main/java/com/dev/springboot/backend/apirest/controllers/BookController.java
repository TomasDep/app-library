package com.dev.springboot.backend.apirest.controllers;

import com.dev.springboot.backend.apirest.dto.BookDto;
import com.dev.springboot.backend.apirest.models.entities.Book;
import com.dev.springboot.backend.apirest.models.services.IBookService;
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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BookController {
    private static final String BOOK = "book";
    private static final String MESSAGE = "message";
    private static final String ERROR = "error";
    private static final String ERRORS = "errors";
    private static final String TOTAL = "total";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private IBookService bookService;

    @ApiOperation(value = "${BookController.index.value}")
    @GetMapping("/books")
    public ResponseEntity<?> index(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        Page<Book> bookPage = this.bookService.findAll(pageNumber, pageSize);
        List<Book> books = bookPage.getContent();

        response.put(MESSAGE, this.messageSource.getMessage("book.message.successIndex", null, locale));
        response.put(BOOK, books);
        response.put(TOTAL, bookPage.getTotalElements());

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "${BookController.show.value}")
    @GetMapping("/book/{id}")
    public ResponseEntity<?> show(@PathVariable Long id, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        Book book = new Book();
        String bookNull = this.messageSource.getMessage("book.message.bookNull", null, locale);
        String messageDataAccess = this.messageSource.getMessage("book.message.errorDataAccess", null, locale);

        try {
            book = this.bookService.findById(id);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("book.message.internalServerError", null, locale));
            response.put(ERROR, String.format(messageDataAccess, e.getMessage(), e.getMostSpecificCause()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (book == null) {
            response.put(MESSAGE, String.format(bookNull, id.toString()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        response.put(BOOK, book);
        response.put(MESSAGE, this.messageSource.getMessage("book.message.successShow", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "${BookController.create.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/book")
    public ResponseEntity<?> create(
            @Valid @RequestBody BookDto book,
            BindingResult result,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        Book newBook = new Book();
        String errorsMessage = this.messageSource.getMessage("book.message.errors", null, locale);
        String errorDataAccessMessage = this.messageSource.getMessage("book.message.errorDataAccess", null, locale);

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return String.format(errorsMessage, error.getField(), error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put(ERRORS, errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            newBook = this.bookService.save(
                    new Book(
                            book.getName(),
                            book.getDescription(),
                            book.getStatus(),
                            book.getObservation(),
                            book.getStock(),
                            book.getPrice(),
                            book.getGenre()
                    )
            );
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("book.message.internalServerError", null, locale));
            response.put(ERROR, String.format(errorDataAccessMessage, e.getMessage(), e.getMostSpecificCause()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(BOOK, newBook);
        response.put(MESSAGE, this.messageSource.getMessage("book.message.successCreate", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "${BookController.update.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/book/{id}")
    public ResponseEntity<?> update(
            @Valid @RequestBody BookDto book,
            BindingResult result,
            @PathVariable Long id,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        Book currentBook = this.bookService.findById(id);
        Book updateBook = new Book();
        String errorsMessage = this.messageSource.getMessage("book.message.errors", null, locale);
        String errorDataAccessMessage = this.messageSource.getMessage("book.message.errorDataAccess", null, locale);
        String bookNull = this.messageSource.getMessage("book.message.bookNull", null, locale);

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return String.format(errorsMessage, error.getField(), error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put(ERRORS, errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (currentBook == null) {
            response.put(MESSAGE, String.format(bookNull, id.toString()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            currentBook.setName(book.getName());
            currentBook.setDescription(book.getDescription());
            currentBook.setStatus(book.getStatus());
            currentBook.setObservation(book.getObservation());
            currentBook.setStock(book.getStock());
            currentBook.setPrice(book.getPrice());
            currentBook.setGenre(book.getGenre());

            updateBook = this.bookService.save(currentBook);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("book.message.internalServerError", null, locale));
            response.put(ERROR, String.format(errorDataAccessMessage, e.getMessage(), e.getMostSpecificCause()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(BOOK, updateBook);
        response.put(MESSAGE, this.messageSource.getMessage("book.message.successUpdate", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "${BookController.delete.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        String errorMessage = this.messageSource.getMessage("book.message.errorDataAccess", null, locale);

        try {
            this.bookService.delete(id);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("book.message.internalServerError", null, locale));
            response.put(ERROR, String.format(errorMessage, e.getMessage(), e.getMostSpecificCause()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(MESSAGE, this.messageSource.getMessage("book.message.successDelete", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
