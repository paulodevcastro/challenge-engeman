package com.engeman.notify_server.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.engeman.notify_server.models.ClientModel;
import com.engeman.notify_server.models.ResetTokenModel;
import com.engeman.notify_server.repositories.ClientRepository;
import com.engeman.notify_server.repositories.ResetTokenRepository;

@Service
public class ResetTokenService {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ResetTokenRepository resetTokenRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	
	public String ResetTokenForClient(String email) {
		Optional<ClientModel> clientOptional = clientRepository.findByEmail(email);
		if(clientOptional.isPresent()) {
			ClientModel client = clientOptional.get();
			
			// Generate random and unique token
			String token = UUID.randomUUID().toString();
			
			// Save token in database user associate
			ResetTokenModel resetToken = new ResetTokenModel(token, client);
			resetTokenRepository.save(resetToken);
			
			// Service e-mail
			emailService.sendPasswordResetEmail(client.getEmail(), token);
			
			return "Um link de redefinição de senha foi enviado para o seu e-mail.";
		}
		return null;
		
	}
	
	// Method redefine password
	public boolean resetPassword(String token, String newPassword) {
		Optional<ResetTokenModel> tokenOptional = resetTokenRepository.findByToken(token);
		if(tokenOptional.isPresent()) {
			ResetTokenModel resetToken = tokenOptional.get();
			if (resetToken.isExpired()) {
				// Expired token, remove
				resetTokenRepository.delete(resetToken);
				return false;
			}
			ClientModel client = resetToken.getClient();
			// Encode the new password and save it
			client.setPassword(passwordEncoder.encode(newPassword));
			clientRepository.save(client);
			
			// Delete token after use
			resetTokenRepository.delete(resetToken);
			return true;
		}
		return false;
	}

}
