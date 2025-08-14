package com.engeman.notify_server.models.enums;

public enum NotificationStatus {	
	SCHEDULED,	// Agendado, aguardando envio
    SENT,		// Enviado com sucesso
    FAILED,		// Falha definitiva após retentativas
    RETRYING	// Em processo de retentativa
}
