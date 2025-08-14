package com.engeman.notify_server.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pass_reset_token")
public class ResetTokenModel {
	
	private static final int EXPIRATION = 20; // token reset in 20 minutes
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String token;
	
	@OneToOne(targetEntity = ClientModel.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "client_id")
	private ClientModel client;
	
	@Column(nullable = false)
	private LocalDateTime expiryDate;
	
	public ResetTokenModel() {}
	
	public ResetTokenModel(String token, ClientModel client) {
        this.token = token;
        this.client = client;
        this.expiryDate = calculateExpiryDate();
    }
	
	private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusMinutes(EXPIRATION);
    }
	
	public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ClientModel getClient() {
		return client;
	}

	public void setClient(ClientModel client) {
		this.client = client;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

}
