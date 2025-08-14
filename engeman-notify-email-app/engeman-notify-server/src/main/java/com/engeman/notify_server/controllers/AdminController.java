package com.engeman.notify_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engeman.notify_server.dtos.requests.ResendNotificationRequest;
import com.engeman.notify_server.services.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
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
}