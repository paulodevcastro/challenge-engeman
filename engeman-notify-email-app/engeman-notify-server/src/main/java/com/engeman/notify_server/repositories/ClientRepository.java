package com.engeman.notify_server.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.engeman.notify_server.models.ClientModel;

public interface ClientRepository extends JpaRepository<ClientModel, Long>{
	
	// Method to find a user through your username (applied to clientDetails -> service layer)
	Optional<ClientModel> findByUsername(String username);
	Optional<ClientModel> findByEmail(String email);
}
