package com.daniel.scafford.infrastructure.config.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleAuthService {
    @Value("${google.client-id}")
    private String clientId;

    public GoogleIdToken.Payload verify(String idTokenString) {
        try {
            var verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();
            var token = verifier.verify(idTokenString);
            if (token != null && (boolean) token.getPayload().get("email_verified")) {
                return token.getPayload();
            }
            throw new BadCredentialsException("Google Token inválido ou e-mail não verificado");
        } catch (Exception e) {
            throw new BadCredentialsException("Erro na comunicação com Google");
        }
    }
}