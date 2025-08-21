package odk.groupe4.ApiCollabDev.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global des exceptions de l'application.
 * Toutes les exceptions qui se produisent dans les contrôleurs
 * peuvent être interceptées ici pour générer une réponse JSON claire et standardisée.
 */
@RestControllerAdvice // Intercepte les exceptions dans tous les contrôleurs REST
public class GlobalExceptionHandler {

    /**
     * Gère les erreurs de validation des champs (@Valid / @NotNull / etc.).
     * Retourne une réponse contenant les détails des champs invalides.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Parcours toutes les erreurs et stocke le nom du champ + message
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Crée un objet de réponse avec détails des erreurs
        ErrorResponse errorResponse = new ErrorResponse(
                "Erreur de validation",
                "Les données fournies ne sont pas valides",
                LocalDateTime.now(),
                errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les IllegalArgumentException (ex: argument invalide dans un service).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Argument invalide",
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère toutes les RuntimeException (erreurs imprévues non vérifiées).
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Erreur interne",
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gère toutes les autres exceptions génériques.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Erreur inattendue",
                "Une erreur inattendue s'est produite",
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gère le cas où la taille du fichier uploadé dépasse la limite autorisée.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Taille maximale dépassée",
                "Taille du fichier dépasse la limite maximale autorisée",
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les erreurs d'entrée/sortie (lecture/écriture de fichiers).
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException exc) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Erreur de traitement du fichier",
                exc.getMessage(),
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Classe interne représentant la structure de la réponse d'erreur envoyée au client.
     */
    public static class ErrorResponse {
        private String titre; // titre de l'erreur
        private String message; // message explicatif
        private LocalDateTime timestamp; // date et heure de l'erreur
        private Map<String, String> details; // détails supplémentaires (ex: erreurs de champs)

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
