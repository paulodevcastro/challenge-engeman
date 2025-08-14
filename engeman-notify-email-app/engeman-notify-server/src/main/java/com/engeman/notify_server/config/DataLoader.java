package com.engeman.notify_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.engeman.notify_server.models.ClientModel;
import com.engeman.notify_server.models.enums.Role;
import com.engeman.notify_server.repositories.ClientRepository;

@Component
public class DataLoader implements CommandLineRunner {

	@Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe um usuário ADMIN
        if (clientRepository.findByEmail("admin@example.com").isEmpty()) {
            ClientModel admin = new ClientModel();
            admin.setUsername("Admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("engeman"));
            admin.setRole(Role.ADMIN);

            clientRepository.save(admin);
            System.out.println("Usuário ADMIN criado com sucesso!");
        }
    }

}
