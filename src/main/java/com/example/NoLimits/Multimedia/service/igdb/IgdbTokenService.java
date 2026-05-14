package com.example.NoLimits.Multimedia.service.igdb;

import com.example.NoLimits.Multimedia.dto.igdb.IgdbTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class IgdbTokenService {

    private static final Logger log =
            LoggerFactory.getLogger(IgdbTokenService.class);

    @Value("${igdb.client-id}")
    private String clientId;

    @Value("${igdb.client-secret}")
    private String clientSecret;

    private String accessToken;
    private Instant expiresAt;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken() {

        if (accessToken == null ||
                expiresAt == null ||
                Instant.now().isAfter(expiresAt.minusSeconds(60))) {

            renovarToken();
        }

        return accessToken;
    }

    private void renovarToken() {

        try {
            String url =
                    "https://id.twitch.tv/oauth2/token" +
                            "?client_id=" + clientId +
                            "&client_secret=" + clientSecret +
                            "&grant_type=client_credentials";

            ResponseEntity<IgdbTokenResponse> response =
                    restTemplate.postForEntity(
                            url,
                            null,
                            IgdbTokenResponse.class
                    );

            IgdbTokenResponse body = response.getBody();

            if (body == null ||
                    body.getAccessToken() == null ||
                    body.getExpiresIn() == null) {

                log.error("Twitch OAuth respondió sin access_token o expires_in");
                throw new RuntimeException("No se pudo obtener token IGDB");
            }

            this.accessToken = body.getAccessToken();
            this.expiresAt = Instant.now().plusSeconds(body.getExpiresIn());

            log.info("Nuevo token IGDB generado correctamente. Expira en {} segundos.",
                    body.getExpiresIn());

        } catch (RestClientException e) {
            log.error("Error al solicitar token IGDB/Twitch OAuth: {}", e.getMessage());
            throw new RuntimeException("Error al renovar token IGDB", e);
        }
    }
}