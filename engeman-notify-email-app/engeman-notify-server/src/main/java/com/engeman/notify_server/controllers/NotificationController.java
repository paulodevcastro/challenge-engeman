package com.engeman.notify_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engeman.notify_server.dtos.requests.NotificationRequestDTO;
import com.engeman.notify_server.models.ClientModel;
import com.engeman.notify_server.models.NotificationModel;
import com.engeman.notify_server.repositories.ClientRepository;
import com.engeman.notify_server.repositories.NotificationRepository;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
	
	@Autowired
    private NotificationRepository notificationRepository;
	
	@Autowired
    private ClientRepository clientRepository;
	
	@PostMapping("/schedule")
	public ResponseEntity<?> scheduleNotification(@RequestBody NotificationRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
		// find the authenticated client
		ClientModel client = clientRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        NotificationModel notification = new NotificationModel();
        notification.setTitle(request.getTitle());
        notification.setContent(request.getContent());
        notification.setScheduledTime(request.getScheduledTime());
        notification.setClient(client);

        notificationRepository.save(notification);

        return ResponseEntity.ok("Notificação agendada com sucesso!");
	}

}
