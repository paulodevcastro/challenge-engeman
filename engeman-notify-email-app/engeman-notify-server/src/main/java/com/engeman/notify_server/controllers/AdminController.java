package com.engeman.notify_server.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engeman.notify_server.dtos.ClientDTO;
import com.engeman.notify_server.dtos.NotificationAdminDTO;
import com.engeman.notify_server.dtos.requests.ResendNotificationRequest;
import com.engeman.notify_server.models.ClientModel;
import com.engeman.notify_server.models.NotificationModel;
import com.engeman.notify_server.repositories.ClientRepository;
import com.engeman.notify_server.repositories.NotificationRepository;
import com.engeman.notify_server.services.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
    private ClientRepository clientRepository;
	
	@Autowired
    private NotificationRepository notificationRepository;
	
	@PostMapping("/resend-notification")
    public ResponseEntity<String> resendNotification(@RequestBody ResendNotificationRequest request) {
        adminService.resendFailedNotification(request.getNotificationId());
        return ResponseEntity.ok("Notificação agendada para reenvio.");
    }

    // Endpoint resend e-mail
    @PostMapping("/resend-email")
    @PreAuthorize("hasRole('ADMIN')")
    public String resendFailedEmail(@RequestBody ResendNotificationRequest request) {
        // Implementar a lógica para reenviar o email
        return "Email reenviado com sucesso para " + request.getNotificationId();
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<ClientDTO>> getAllUsers() {
        List<ClientModel> clients = clientRepository.findAll();
        
        // Mapeia a lista de ClientModel para uma lista de UserDTOs
        List<ClientDTO> userDTOs = clients.stream().map(client -> {
        	ClientDTO dto = new ClientDTO();
            dto.setId(client.getId());
            dto.setUsername(client.getUsername());
            dto.setEmail(client.getEmail());
            return dto;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(userDTOs);
    }
    
    @GetMapping("/users/{userId}/notifications")
    public ResponseEntity<List<NotificationAdminDTO>> getNotificationsForUser(@PathVariable Long userId) {
        ClientModel client = clientRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        List<NotificationModel> notifications = notificationRepository.findByClient(client);

        List<NotificationAdminDTO> dtos = notifications.stream().map(notification -> {
            NotificationAdminDTO dto = new NotificationAdminDTO();
            dto.setId(notification.getId());
            dto.setTitle(notification.getTitle());
            dto.setContent(notification.getContent());
            dto.setStatus(notification.getStatus());
            dto.setRetryCount(notification.getRetryCount());
            dto.setErrorMessage(notification.getErrorMessage());
            dto.setScheduledTime(notification.getScheduledTime());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}