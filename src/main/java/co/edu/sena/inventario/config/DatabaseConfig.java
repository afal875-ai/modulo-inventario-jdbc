package co.edu.sena.inventario.config;

import co.edu.sena.inventario.exception.DatabaseException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Configura las conexiones JDBC y crea la estructura inicial de la base de datos.
 */
public final class DatabaseConfig {

    public static final String DATABASE_PATH_PROPERTY = "inventario.db.path";
    private static final String DEFAULT_DATABASE_PATH = "data/inventario.db";
    private static final String SCHEMA_RESOURCE = "/database/schema.sql";

    private final String databasePath;
    private final String jdbcUrl;

    public DatabaseConfig() {
        this(System.getProperty(DATABASE_PATH_PROPERTY, DEFAULT_DATABASE_PATH));
    }

    public DatabaseConfig(String databasePath) {
        if (databasePath == null || databasePath.isBlank()) {
            throw new IllegalArgumentException("La ruta de la base de datos es obligatoria.");
        }
        this.databasePath = databasePath.trim();
        this.jdbcUrl = "jdbc:sqlite:" + this.databasePath;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }

    public void initializeDatabase() {
        createParentDirectory();
        String schema = readSchema();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
            statement.executeUpdate(schema);
        } catch (SQLException exception) {
            throw new DatabaseException("No fue posible inicializar la base de datos.", exception);
        }
    }

    private void createParentDirectory() {
        Path absoluteDatabasePath = Path.of(databasePath).toAbsolutePath();
        Path parent = absoluteDatabasePath.getParent();
        if (parent == null) {
            return;
        }

        try {
            Files.createDirectories(parent);
        } catch (IOException exception) {
            throw new DatabaseException("No fue posible crear la carpeta de datos.", exception);
        }
    }

    private String readSchema() {
        try (InputStream inputStream = DatabaseConfig.class.getResourceAsStream(SCHEMA_RESOURCE)) {
            if (inputStream == null) {
                throw new DatabaseException("No se encontró el esquema de la base de datos.");
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new DatabaseException("No fue posible leer el esquema de la base de datos.", exception);
        }
    }
}

