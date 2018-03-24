package ua.vadim.security.service;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;


import javax.servlet.http.HttpServletRequest;

@Service
public class TokenManager {

    @Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public AccessToken getAccessToken() {
        HttpServletRequest request = new RequestManager().getServletRequest();
        return ((KeycloakPrincipal) request.getUserPrincipal())
                .getKeycloakSecurityContext().getToken();
    }

    public String getUsername(){
        AccessToken token = getAccessToken();
        return token.getPreferredUsername();
    }

    public boolean isAdmin(){
       return getAccessToken().getRealmAccess().getRoles().contains("admin");
    }
}
