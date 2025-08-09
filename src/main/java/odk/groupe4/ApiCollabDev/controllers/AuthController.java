package odk.groupe4.ApiCollabDev.controllers;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import odk.groupe4.ApiCollabDev.config.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/login/oauth2/code")
public class AuthController {

    private final JwtService jwtService;
    private final String CLIENT_ID = "425444552086-3pd70ibsfafbg9gg4rc1s0iqngtadndf.apps.googleusercontent.com";

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> body) {
        try {
            String idTokenString = body.get("idToken");

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance()
            ).setAudience(Collections.singletonList(CLIENT_ID)).build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");

                // Ici, tu peux chercher ou créer l'utilisateur en base avec email...

                // Génère ton JWT (implémenté dans JwtService)
                String jwt = jwtService.generateToken(email);

                return ResponseEntity.ok(Map.of(
                        "token", jwt,
                        "email", email,
                        "name", name
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Google invalide");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur");
        }
    }
}