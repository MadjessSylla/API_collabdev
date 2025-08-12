package odk.groupe4.ApiCollabDev.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import odk.groupe4.ApiCollabDev.config.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final String clientId;

    public AuthController(JwtService jwtService,
                          @Value("${google.client.id}") String clientId) {
        this.jwtService = jwtService;
        this.clientId = clientId;
    }

    @Operation(
            summary = "Connexion via Google OAuth2",
            description = "Valide le token Google envoyé par le frontend Angular, crée ou retrouve l'utilisateur en base, et retourne un JWT interne."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"token\": \"jwt_token\", \"email\": \"user@example.com\", \"name\": \"Nom Utilisateur\" }"))),
            @ApiResponse(responseCode = "400", description = "idToken manquant ou invalide", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token Google invalide", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur serveur", content = @Content)
    })
    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(
            @Parameter(description = "Objet JSON contenant l'idToken Google", required = true,
                    schema = @Schema(example = "{ \"idToken\": \"eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg4Nj...\" }"))
            @RequestBody Map<String, String> body) {

        String idTokenString = body.get("idToken");
        if (idTokenString == null || idTokenString.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "idToken manquant"));
        }

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance()
            ).setAudience(Collections.singletonList(clientId)).build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                // TODO: rechercher ou créer l’utilisateur en BDD ici
                // User user = userService.findOrCreate(email, name);

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