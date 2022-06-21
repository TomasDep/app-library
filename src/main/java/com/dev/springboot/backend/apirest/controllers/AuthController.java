package com.dev.springboot.backend.apirest.controllers;

import com.dev.springboot.backend.apirest.dto.JwtDto;
import com.dev.springboot.backend.apirest.dto.LoginDto;
import com.dev.springboot.backend.apirest.dto.NewUserDto;
import com.dev.springboot.backend.apirest.enums.RoleEnum;
import com.dev.springboot.backend.apirest.jwt.JwtProvider;
import com.dev.springboot.backend.apirest.models.entities.Role;
import com.dev.springboot.backend.apirest.models.entities.User;
import com.dev.springboot.backend.apirest.models.services.IRoleService;
import com.dev.springboot.backend.apirest.models.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {
    private static final String MESSAGE = "message";
    private static final String USER = "user";
    private static final String ERRORS = "errors";
    private static final String ROLE = "admin";
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("auth/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody NewUserDto newUserDto,
            BindingResult result,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();
        String errorMessage = this.messageSource.getMessage("auth.message.errorExist", null, locale);
        String username = this.messageSource.getMessage("user.username", null, locale);
        String email = this.messageSource.getMessage("user.email", null, locale);
        String errorsMessages = this.messageSource.getMessage("auth.message.errors", null, locale);

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(error -> {
                return String.format(errorsMessages, error.getField()).concat(" ").concat(error.getDefaultMessage());
            }).collect(Collectors.toList());

            response.put(MESSAGE, this.messageSource.getMessage("auth.message.invalid", null, locale));
            response.put(ERRORS, errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (this.userService.existsByUsername(newUserDto.getUsername())) {
            response.put(MESSAGE, String.format(errorMessage, username));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (this.userService.existsByEmail(newUserDto.getEmail())) {
            response.put(MESSAGE, String.format(errorMessage, email));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        User user = new User(
                newUserDto.getName(),
                newUserDto.getLastname(),
                newUserDto.getUsername(),
                newUserDto.getEmail(),
                this.passwordEncoder.encode(newUserDto.getPassword())
        );

        Set<Role> roles = new HashSet<>();
        roles.add(this.roleService.getByRoleName(RoleEnum.ROLE_USER).get());

        if (newUserDto.getRoles().contains(ROLE)) {
            roles.add(this.roleService.getByRoleName(RoleEnum.ROLE_ADMIN).get());
        }

        user.setRoles(roles);

        this.userService.save(user);

        response.put(USER, user);
        response.put(MESSAGE, this.messageSource.getMessage("auth.message.register", null, locale));

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PostMapping("auth/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginDto loginUser,
            BindingResult result,
            Locale locale
    ) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            response.put(MESSAGE, this.messageSource.getMessage("auth.message.errorCredentials", null, locale));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Authentication authentication =
                this.authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword())
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());

        return new ResponseEntity<JwtDto>(jwtDto, HttpStatus.OK);
    }
}
