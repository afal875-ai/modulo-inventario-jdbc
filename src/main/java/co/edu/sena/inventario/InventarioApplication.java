package co.edu.sena.inventario;

import co.edu.sena.inventario.config.DatabaseConfig;
import co.edu.sena.inventario.dao.ProductoDao;
import co.edu.sena.inventario.dao.jdbc.ProductoDaoJdbc;
import co.edu.sena.inventario.exception.DatabaseException;
import co.edu.sena.inventario.service.ProductoService;
import co.edu.sena.inventario.ui.ConsolaInventario;

import java.util.Scanner;

/**
 * Punto de entrada del módulo de gestión de inventario.
 */
public final class InventarioApplication {

    private InventarioApplication() {
    }

    public static void main(String[] args) {
        try {
            DatabaseConfig databaseConfig = new DatabaseConfig();
            databaseConfig.initializeDatabase();

            ProductoDao productoDao = new ProductoDaoJdbc(databaseConfig);
            ProductoService productoService = new ProductoService(productoDao);

            try (Scanner scanner = new Scanner(System.in)) {
                new ConsolaInventario(productoService, scanner).iniciar();
            }
        } catch (DatabaseException exception) {
            System.err.println("No fue posible iniciar el módulo: " + exception.getMessage());
        }
    }
}

