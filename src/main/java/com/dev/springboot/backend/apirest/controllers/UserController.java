package com.dev.springboot.backend.apirest.controllers;

import com.dev.springboot.backend.apirest.dto.UserDto;
import com.dev.springboot.backend.apirest.enums.RoleEnum;
import com.dev.springboot.backend.apirest.models.entities.Role;
import com.dev.springboot.backend.apirest.models.entities.User;
import com.dev.springboot.backend.apirest.models.services.IRoleService;
import com.dev.springboot.backend.apirest.models.services.IUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final String USERS = "users";
    private static final String MESSAGE = "message";
    private static final String ERROR = "error";
    private static final String ERRORS = "errors";
    private static final String ROLE = "admin";
    private static final String TOTAL = "total";

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private MessageSource messageSource;

    @ApiOperation(value = "${UserController.index.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> index(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        Page<User> pageUser = this.userService.findAll(pageNumber, pageSize);
        List<User> users = pageUser.getContent();

        response.put(MESSAGE, this.messageSource.getMessage("users.message.successIndex", null, locale));
        response.put(USERS, users);
        response.put(TOTAL, pageUser.getTotalElements());

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "${UserController.show.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity<?> show(@PathVariable Long id, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        String messageDataAccess = this.messageSource.getMessage("users.message.errorDataAccess", null, locale);
        String messageUserNull = this.messageSource.getMessage("users.message.userNull", null, locale);
        User user = new User();

        try {
            user = this.userService.findById(id);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("users.message.internalServerError", null, locale));
            response.put(ERROR, String.format(messageDataAccess, e.getMessage(), e.getMostSpecificCause()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (user == null) {
            response.put(MESSAGE, String.format(messageUserNull, id.toString()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        response.put(USERS, user);
        response.put(MESSAGE, this.messageSource.getMessage("users.message.successShow", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "${UserController.create.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<?> create(
            @Valid @RequestBody UserDto user,
            BindingResult result,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        String messageErrors = this.messageSource.getMessage("users.message.errors", null, locale);
        String errorMessage = this.messageSource.getMessage("users.message.errorExist", null, locale);
        String username = this.messageSource.getMessage("user.username", null, locale);
        String email = this.messageSource.getMessage("user.email", null, locale);
        User newUser = new User();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
               return String.format(messageErrors, error.getField(), error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put(ERRORS, errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        User tempUser = new User(
                user.getName(),
                user.getLastname(),
                user.getUsername(),
                user.getEmail(),
                this.passwordEncoder.encode(user.getPassword())
        );

        Set<Role> roles = new HashSet<>();
        roles.add(this.roleService.getByRoleName(RoleEnum.ROLE_USER).get());

        if (user.getRoles().contains(ROLE)) {
            roles.add(this.roleService.getByRoleName(RoleEnum.ROLE_ADMIN).get());
        }

        tempUser.setRoles(roles);

        if (this.userService.existsByUsername(user.getUsername())) {
            response.put(MESSAGE, String.format(errorMessage, username));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (this.userService.existsByEmail(user.getEmail())) {
            response.put(MESSAGE, String.format(errorMessage, email));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            newUser = this.userService.save(tempUser);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("users.message.internalServerError", null, locale));
            response.put(USERS, newUser);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(MESSAGE, this.messageSource.getMessage("users.message.successCreate", null, locale));
        response.put(USERS, newUser);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "${UserController.update.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}")
    public ResponseEntity<?> update(
            @Valid @RequestBody UserDto user,
            BindingResult result,
            @PathVariable Long id,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        String messageErrors = this.messageSource.getMessage("users.message.errors", null, locale);
        String messageUserNull = this.messageSource.getMessage("users.message.userNull", null, locale);
        String messageDataAccess = this.messageSource.getMessage("users.message.errorDataAccess", null, locale);
        User currentUser = this.userService.findById(id);
        User updateUser = new User();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return String.format(messageErrors, error.getField(), error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put(ERRORS, errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (currentUser == null) {
            response.put(MESSAGE, String.format(messageUserNull, id.toString()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(this.roleService.getByRoleName(RoleEnum.ROLE_USER).get());

        if (user.getRoles().contains(ROLE)) {
            roles.add(this.roleService.getByRoleName(RoleEnum.ROLE_ADMIN).get());
        }

        try {
            currentUser.setName(user.getName());
            currentUser.setLastname(user.getLastname());
            currentUser.setLastname(user.getLastname());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());
            currentUser.setRoles(roles);

            updateUser = this.userService.save(currentUser);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("users.message.internalServerError", null, locale));
            response.put(ERROR, String.format(messageDataAccess, e.getMessage(), e.getMostSpecificCause()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(MESSAGE, this.messageSource.getMessage("users.message.successUpdate", null, locale));
        response.put(USERS, updateUser);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "${UserController.delete.value}")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        String messageDataAccess = this.messageSource.getMessage("users.message.errorDataAccess", null, locale);

        try {
            this.userService.delete(id);
        } catch (DataAccessException e) {
            response.put(MESSAGE, this.messageSource.getMessage("users.message.internalServerError", null, locale));
            response.put(ERROR, String.format(messageDataAccess, e.getMessage(), e.getMostSpecificCause()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put(MESSAGE, this.messageSource.getMessage("users.message.successDelete", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}