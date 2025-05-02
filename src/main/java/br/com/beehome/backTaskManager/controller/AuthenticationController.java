package br.com.beehome.backTaskManager.controller;

import br.com.beehome.backTaskManager.dto.LoginDTO;
import br.com.beehome.backTaskManager.dto.UserDTO;
import br.com.beehome.backTaskManager.dto.UserLoginDTO;
import br.com.beehome.backTaskManager.dto.UserRegisterDTO;
import br.com.beehome.backTaskManager.exception.CustomizeException;
import br.com.beehome.backTaskManager.infra.security.SecurityConfig;
import br.com.beehome.backTaskManager.infra.security.TokenService;
import br.com.beehome.backTaskManager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@SecurityRequirement(name = SecurityConfig.SECURITY)
@Tag(name = "Rota de criação e login de usuários")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Login Usuário", description = "Login de usuário")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    @ApiResponse(responseCode = "400", description = "Erro de envio de campos obrigatórios")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<UserLoginDTO> login(@RequestBody LoginDTO login){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(login.email(),
                        login.password());

        Authentication authenticate = this.authenticationManager.
                authenticate(usernamePasswordAuthenticationToken);

        var usuario = (User) authenticate.getPrincipal();
        br.com.beehome.backTaskManager.model.User user = userService.getByEmail(usuario.getUsername());

        return ResponseEntity.ok(
                new UserLoginDTO(user,
                        tokenService.generateToken(usuario)));

    }

    @Transactional(rollbackOn = {Exception.class, RuntimeException.class, CustomizeException.class})
    @PostMapping("/register")
    @Operation(summary = "Registro de Usuário", description = "Registro de usuário")
    @ApiResponse(responseCode = "200", description = "Usuário cadastrado")
    @ApiResponse(responseCode = "400", description = "Erro de envio de campos obrigatórios")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<UserDTO> register(@RequestBody UserRegisterDTO userRegisterDTO){
        return ResponseEntity.ok(userService.register(userRegisterDTO));
    }
}
