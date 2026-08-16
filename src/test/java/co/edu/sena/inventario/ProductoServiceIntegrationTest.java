package co.edu.sena.inventario;

import co.edu.sena.inventario.config.DatabaseConfig;
import co.edu.sena.inventario.dao.ProductoDao;
import co.edu.sena.inventario.dao.jdbc.ProductoDaoJdbc;
import co.edu.sena.inventario.model.Producto;
import co.edu.sena.inventario.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductoServiceIntegrationTest {

    @TempDir
    Path tempDirectory;

    private ProductoService productoService;

    @BeforeEach
    void configurarBaseDeDatos() {
        DatabaseConfig databaseConfig = new DatabaseConfig(
                tempDirectory.resolve("inventario-pruebas.db").toString());
        databaseConfig.initializeDatabase();
        ProductoDao productoDao = new ProductoDaoJdbc(databaseConfig);
        productoService = new ProductoService(productoDao);
    }

    @Test
    void debeRegistrarYConsultarUnProducto() {
        Producto creado = crearProductoBase();

        Optional<Producto> consultado = productoService.buscarPorId(creado.getId());

        assertTrue(consultado.isPresent());
        assertNotNull(creado.getId());
        assertEquals("PROD-001", consultado.orElseThrow().getCodigo());
        assertEquals(0, new BigDecimal("15990.50")
                .compareTo(consultado.orElseThrow().getPrecio()));
    }

    @Test
    void debeListarLosProductosOrdenadosPorNombre() {
        productoService.crearProducto("B-01", "Teclado", "", new BigDecimal("90000"), 4);
        productoService.crearProducto("A-01", "Mouse", "", new BigDecimal("50000"), 6);

        List<Producto> productos = productoService.listarProductos();

        assertEquals(2, productos.size());
        assertEquals("Mouse", productos.get(0).getNombre());
        assertEquals("Teclado", productos.get(1).getNombre());
    }

    @Test
    void debeActualizarUnProducto() {
        Producto creado = crearProductoBase();

        boolean actualizado = productoService.actualizarProducto(
                creado.getId(), "prod-002", "Cuaderno grande", "200 hojas",
                new BigDecimal("22000"), 15);

        Producto producto = productoService.buscarPorId(creado.getId()).orElseThrow();
        assertTrue(actualizado);
        assertEquals("PROD-002", producto.getCodigo());
        assertEquals("Cuaderno grande", producto.getNombre());
        assertEquals(15, producto.getStock());
    }

    @Test
    void debeEliminarUnProducto() {
        Producto creado = crearProductoBase();

        boolean eliminado = productoService.eliminarProducto(creado.getId());

        assertTrue(eliminado);
        assertFalse(productoService.buscarPorId(creado.getId()).isPresent());
    }

    @Test
    void debeRechazarCodigosDuplicados() {
        crearProductoBase();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productoService.crearProducto(
                        "prod-001", "Otro", "", BigDecimal.TEN, 1));

        assertEquals("Ya existe un producto con el código indicado.", exception.getMessage());
    }

    @Test
    void debeRechazarPrecioNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> productoService.crearProducto(
                        "PROD-003", "Producto", "", new BigDecimal("-1"), 0));
    }

    @Test
    void debeRechazarStockNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> productoService.crearProducto(
                        "PROD-004", "Producto", "", BigDecimal.ONE, -1));
    }

    private Producto crearProductoBase() {
        return productoService.crearProducto(
                "prod-001", "Cuaderno", "100 hojas",
                new BigDecimal("15990.50"), 20);
    }
}
