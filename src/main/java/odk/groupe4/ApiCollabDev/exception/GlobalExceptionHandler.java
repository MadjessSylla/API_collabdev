package odk.groupe4.ApiCollabDev.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les exceptions de validation des arguments de méthode.
     *
     * @param ex l'exception de validation
     * @return une réponse d'erreur avec les détails de validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
       // Créer une map pour stocker les erreurs de validation
        Map<String, String> errors = new HashMap<>();
        // Parcourir les erreurs de validation et les ajouter à la map
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Créer une réponse d'erreur avec les détails de validation
        ErrorResponse errorResponse = new ErrorResponse(
                "Erreur de validation",
                "Les données fournies ne sont pas valides",
                LocalDateTime.now(),
                errors
        );

        // Retourner la réponse d'erreur avec le statut HTTP 400 (Bad Request)
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les exceptions d'argument invalide.
     *
     * @param ex l'exception d'argument invalide
     * @return une réponse d'erreur avec le message d'erreur
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
       // Créer une réponse d'erreur avec le message d'erreur
        ErrorResponse errorResponse = new ErrorResponse(
                "Argument invalide",
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        // Retourner la réponse d'erreur avec le statut HTTP 400 (Bad Request)
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les exceptions d'exécution.
     *
     * @param ex l'exception d'exécution
     * @return une réponse d'erreur avec le message d'erreur
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        // Créer une réponse d'erreur avec le message d'erreur
        ErrorResponse errorResponse = new ErrorResponse(
                "Erreur interne",
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        // Retourner la réponse d'erreur avec le statut HTTP 500 (Internal Server Error)
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gère les exceptions génériques.
     *
     * @param ex l'exception générique
     * @return une réponse d'erreur avec un message d'erreur générique
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // Créer une réponse d'erreur générique
        ErrorResponse errorResponse = new ErrorResponse(
                "Erreur inattendue",
                "Une erreur inattendue s'est produite",
                LocalDateTime.now(),
                null
        );
        // Retourner la réponse d'erreur avec le statut HTTP 500 (Internal Server Error)
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Classe représentant la réponse d'erreur.
     */
    public static class ErrorResponse {
        private String titre;
        private String message;
        private LocalDateTime timestamp;
        private Map<String, String> details;

        public ErrorResponse(String titre, String message, LocalDateTime timestamp, Map<String, String> details) {
            this.titre = titre;
            this.message = message;
            this.timestamp = timestamp;
            this.details = details;
        }

        // Getters et setters
        public String getTitre() { return titre; }
        public void setTitre(String titre) { this.titre = titre; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        public Map<String, String> getDetails() { return details; }
        public void setDetails(Map<String, String> details) { this.details = details; }
    }
}
