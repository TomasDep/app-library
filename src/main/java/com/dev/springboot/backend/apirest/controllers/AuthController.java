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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IUserService userService;

    @Autowired
    IRoleService roleService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody NewUserDto newUserDto, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            response.put("message", "Datos invalidos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (this.userService.existsByUsername(newUserDto.getUsername())) {
            response.put("message", "El nombre de usuario ya existe");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (this.userService.existsByEmail(newUserDto.getEmail())) {
            response.put("message", "El email ya existe");
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

        if (newUserDto.getRoles().contains("admin")) {
            roles.add(this.roleService.getByRoleName(RoleEnum.ROLE_ADMIN).get());
        }

        user.setRoles(roles);

        this.userService.save(user);

        response.put("user", user);
        response.put("message", "Usuario registrado correctamente");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PostMapping("auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginUser, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            response.put("message", "Credenciales invalidas");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        Authentication authentication =
                this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());

        return new ResponseEntity<JwtDto>(jwtDto, HttpStatus.OK);
    }
}
