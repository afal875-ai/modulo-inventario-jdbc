package co.edu.sena.inventario.exception;

/**
 * Encapsula los errores técnicos producidos durante el acceso a datos.
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

