package com.engeman.notify_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.engeman.notify_server.services.ClientDetailsServices;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final ClientDetailsServices clientDetailsServices;
	
	public SecurityConfig(ClientDetailsServices clientDetailsServices) {
		this.clientDetailsServices = clientDetailsServices;
	}

	// request data's authorized by HttpSecurity
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/h2-console/**").permitAll().anyRequest().authenticated()
						)
				.headers(header -> header
						.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
				.formLogin(formLogin -> formLogin.permitAll())
				.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
				
				.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
