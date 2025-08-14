package com.engeman.notify_server.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class TestController {
	
	@GetMapping("/test")
    public String userAccess() {
        return "Parabéns, você está logado";
    }

}
