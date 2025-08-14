package com.engeman.notify_server.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtils {
	
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	public String generateJwtToken(String username) {
		try {
			Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
			return JWT.create()
					.withSubject(username)
					.withIssuedAt(new Date())
					.withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
					.sign(algorithm);
		}
		catch (JWTCreationException exception) {
			// Invalid Signing configuration / Couldn't convert Claims.
			throw new RuntimeException("Erro ao gerar token JWT", exception);
		}
	}
	
	public String getSubjectFromToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
			DecodedJWT decodedJWT = JWT.require(algorithm)
					.build()
					.verify(token);
			return decodedJWT.getSubject();
		}
		catch (JWTVerificationException exception) {
			return null;
		}
	}
	
	public boolean validateJwtToken(String token) {
        return getSubjectFromToken(token) != null;
    }

}
