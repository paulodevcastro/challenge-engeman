package com.engeman.notify_server.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engeman.notify_server.dtos.SignUpRequestDTO;
import com.engeman.notify_server.dtos.requests.ForgotPasswordRequestDTO;
import com.engeman.notify_server.dtos.requests.ResetPasswordRequestDTO;
import com.engeman.notify_server.models.ClientModel;
import com.engeman.notify_server.repositories.ClientRepository;
import com.engeman.notify_server.security.jwt.JwtUtils;
import com.engeman.notify_server.services.ResetTokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private ResetTokenService resetTokenService;

	@Autowired
	private JwtUtils jwtUtils;

    AuthController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
	
	// Post to user who is already registered
	@PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication.getName());

        return ResponseEntity.ok(new JwtResponse(jwt, authentication.getName()));
    }
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignUpRequestDTO signupRequest) {
		if (clientRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().body("Erro: Email já está em uso!");
		}
		
		// New obj clientModel
		ClientModel newUser = new ClientModel();
		newUser.setUsername(signupRequest.getUsername());
		newUser.setEmail(signupRequest.getEmail());
		
		// password encrypt
		newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
		
		clientRepository.save(newUser);
		return ResponseEntity.ok("Usuário cadastrado com sucesso!");
	}
	
	// Endpoint 1: Request password reset
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO request){
		String resetToken = resetTokenService.ResetTokenForClient(request.getEmail());
		if(resetToken != null) {
			return ResponseEntity.ok("Token de redefinição gerado e enviado para o email: " + resetToken);
		}
		return ResponseEntity.badRequest().body("E-mail não encontrado.");
	}
	
	// Endpoint 2: Reset password with token
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO request){
		boolean success = resetTokenService.resetPassword(request.getToken(), request.getNewPassword());
		if (success) {
			return ResponseEntity.ok("Senha redefinida com sucesso.");
		}
        return ResponseEntity.badRequest().body("Token inválido ou expirado.");
	}
}

class LoginRequest {
    private String username;
    private String password;
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
}

class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private Collection<? extends GrantedAuthority> roles;
    
	public JwtResponse(String token, String username) {
		super();
		this.token = token;
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
}
