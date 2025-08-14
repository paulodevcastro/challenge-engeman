package com.engeman.notify_server.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engeman.notify_server.models.NotificationModel;
import com.engeman.notify_server.models.enums.NotificationStatus;
import com.engeman.notify_server.repositories.NotificationRepository;

@Service
public class AdminService {
	
	@Autowired
    private NotificationRepository notificationRepository;
	
	public void resendFailedNotification(Long notificationId) {
        NotificationModel notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notificação não encontrada."));

        // Se a notificação falhou, reiniciamos o processo de envio
        if (notification.getStatus() == NotificationStatus.FAILED) {
            notification.setStatus(NotificationStatus.SCHEDULED);
            notification.setRetryCount(0); // Zera o contador de tentativas
            notification.setScheduledTime(LocalDateTime.now()); // Agenda para ser enviada imediatamente
            notification.setErrorMessage(null); // Limpa a mensagem de erro
            notificationRepository.save(notification);
        }
    }

}
