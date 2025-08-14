package com.engeman.notify_server.services.schedule;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.engeman.notify_server.models.NotificationModel;
import com.engeman.notify_server.models.enums.NotificationStatus;
import com.engeman.notify_server.repositories.NotificationRepository;
import com.engeman.notify_server.services.EmailService;

@Component
public class NotificationScheduler {

	private static final int MAX_RETRIES = 3; // Limite de tentativas
    private static final int RETRY_DELAY_SECONDS = 5; // Atraso de 5 segundos
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Scheduled(fixedRate = 60000)
    public void sendScheduledNotifications() {
    	List<NotificationModel> notificationsToSend = 
    			notificationRepository.findByStatusAndScheduledTimeBefore(NotificationStatus.SCHEDULED, LocalDateTime.now());
    	if (!notificationsToSend.isEmpty()) {
    		System.out.println("Encontradas " + notificationsToSend.size() + " notificações para enviar.");
    	}
    	
    	for (NotificationModel notification : notificationsToSend) {
            try {
                emailService.sendNotificationEmail(notification.getClient().getEmail(), notification.getTitle(), notification.getContent());
                
                // Success
                notification.setStatus(NotificationStatus.SENT);;
                notificationRepository.save(notification);
            } catch (Exception e) {
                // Failed
                notification.setRetryCount(notification.getRetryCount() + 1);
                notification.setErrorMessage(e.getMessage());
                if (notification.getRetryCount() >= MAX_RETRIES) {
                    // definitive failed
                    notification.setStatus(NotificationStatus.FAILED);
                } else {
                    // Schedule a new notification
                	notification.setStatus(NotificationStatus.RETRYING);
                    notification.setScheduledTime(LocalDateTime.now().plusSeconds(RETRY_DELAY_SECONDS));
                }
                
                notificationRepository.save(notification);
            }
        }
    }
    
	
}
