package com.zeritec.saturne.configuration;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	 @Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		return jwt.getClaimAsStringList("scope").stream()
	            .map(SimpleGrantedAuthority::new)
	            .collect(Collectors.toList());
	}
}
