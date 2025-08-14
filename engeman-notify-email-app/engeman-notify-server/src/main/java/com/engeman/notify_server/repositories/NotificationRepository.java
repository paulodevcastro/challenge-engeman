package com.engeman.notify_server.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engeman.notify_server.models.ClientModel;
import com.engeman.notify_server.models.NotificationModel;
import com.engeman.notify_server.models.enums.NotificationStatus;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel, Long>{
	
	// filters by notifications that did not fail
	List<NotificationModel> findByStatusAndScheduledTimeBefore(NotificationStatus status, LocalDateTime currentTime);
	
	List<NotificationModel> findByClient(ClientModel client);
}
