package com.engeman.notify_server.dtos.requests;

public class ResendNotificationRequest {
	private Long notificationId;

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}
}