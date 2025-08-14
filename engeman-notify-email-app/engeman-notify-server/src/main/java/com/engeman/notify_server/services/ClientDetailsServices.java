package com.engeman.notify_server.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.engeman.notify_server.models.ClientModel;
import com.engeman.notify_server.repositories.ClientRepository;

@Service
public class ClientDetailsServices implements UserDetailsService{

	@Autowired
	private ClientRepository clientRepository;
	
	public ClientDetailsServices(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ClientModel client = clientRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
		
		String roleName = "ROLE_" + client.getRole().name();

		return User.builder()
				.username(client.getEmail())
				.password(client.getPassword())
				.authorities(Collections.singletonList(new SimpleGrantedAuthority(roleName)))
				.build();
	}

}
