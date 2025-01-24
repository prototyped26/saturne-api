package com.zeritec.saturne.services;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private final JwtEncoder encoder;
	
	public JwtService(JwtEncoder jwtEncoder) {
		this.encoder = jwtEncoder;
	}
	
	public String generateToken(Authentication authentication) {
		Instant now = Instant.now();
		String scope = authentication.getAuthorities().stream()
							.map(GrantedAuthority::getAuthority)
							.collect(Collectors.joining(" "));
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				.expiresAt(now.plusMillis(TimeUnit.MINUTES.toMillis(10080)))
				.subject(authentication.getName())
				.claim("scope", scope)
				.build();
		
		return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
}
