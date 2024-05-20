package com.bookshop.productservice.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakAccessConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        List<String> realmRoles = jwt.getClaim("roles");
        if (realmRoles != null){
            for (String realmRole : realmRoles) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + realmRole));
            }
        }
        log.info(realmRoles.toString());
        return grantedAuthorities;
    }
}
