package odk.groupe4.ApiCollabDev.config;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * Service qui gère la génération de tokens JWT.
 * JWT (JSON Web Token) est un format de token utilisé pour authentifier un utilisateur
 * dans les applications web.
 */
@Service // Indique que cette classe est un service Spring (instancié et géré par le conteneur)
public class JwtService {

    /**
     * Clé secrète utilisée pour signer le JWT.
     * ⚠En production, cette clé devrait être stockée dans un fichier de configuration
     * ou dans une variable d'environnement, jamais en dur dans le code.
     * Elle doit aussi être suffisamment longue et aléatoire pour garantir la sécurité.
     */
    private final String secretKey = "MaCleSuperSecretePourJWTDoitEtreLongue";

    /**
     * Génère un token JWT à partir de l'email de l'utilisateur.
     * @param email L'email qui sera mis comme "subject" du token
     * @return Un token JWT signé
     */
    public String generateToken(String email) {
        return Jwts.builder()
                // Définit le "subject" du token (généralement l'identité de l'utilisateur)
                .setSubject(email)

                // Définit la date de création du token
                .setIssuedAt(new Date())

                // Définit la date d'expiration (ici +24h = 86 400 000 ms)
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 jour

                // Signe le token avec l'algorithme HS256 et la clé secrète
                .signWith(SignatureAlgorithm.HS256, secretKey)

                // Construit et retourne le token sous forme de String
                .compact();
    }
}
