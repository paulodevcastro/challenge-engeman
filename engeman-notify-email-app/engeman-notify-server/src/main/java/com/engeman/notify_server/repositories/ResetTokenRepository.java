package com.engeman.notify_server.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.engeman.notify_server.models.ResetTokenModel;

public interface ResetTokenRepository extends JpaRepository<ResetTokenModel, Long>{

	Optional<ResetTokenModel> findByToken(String token);
}
