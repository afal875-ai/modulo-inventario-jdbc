package co.edu.sena.inventario.ui;

import co.edu.sena.inventario.exception.DatabaseException;
import co.edu.sena.inventario.model.Producto;
import co.edu.sena.inventario.service.ProductoService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de texto para operar el módulo de inventario.
 */
public final class ConsolaInventario {

    private final ProductoService productoService;
    private final Scanner scanner;

    public ConsolaInventario(ProductoService productoService, Scanner scanner) {
        this.productoService = productoService;
        this.scanner = scanner;
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");
            ejecutarOpcion(opcion);
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println("\n=== MÓDULO DE INVENTARIO ===");
        System.out.println("1. Registrar producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Buscar producto por ID");
        System.out.println("4. Actualizar producto");
        System.out.println("5. Eliminar producto");
        System.out.println("0. Salir");
    }

    private void ejecutarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1 -> registrarProducto();
                case 2 -> listarProductos();
                case 3 -> buscarProducto();
                case 4 -> actualizarProducto();
                case 5 -> eliminarProducto();
                case 0 -> System.out.println("Sesión finalizada.");
                default -> System.out.println("La opción seleccionada no es válida.");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Validación: " + exception.getMessage());
        } catch (DatabaseException exception) {
            System.out.println("No fue posible completar la operación. " + exception.getMessage());
        }
    }

    private void registrarProducto() {
        System.out.println("\n--- Registrar producto ---");
        String codigo = leerTexto("Código: ");
        String nombre = leerTexto("Nombre: ");
        String descripcion = leerTexto("Descripción: ");
        BigDecimal precio = leerDecimal("Precio: ");
        int stock = leerEntero("Existencias: ");

        Producto producto = productoService.crearProducto(
                codigo, nombre, descripcion, precio, stock);
        System.out.println("Producto registrado con ID " + producto.getId() + ".");
    }

    private void listarProductos() {
        System.out.println("\n--- Productos registrados ---");
        List<Producto> productos = productoService.listarProductos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        productos.forEach(this::mostrarProducto);
    }

    private void buscarProducto() {
        long id = leerEntero("ID del producto: ");
        productoService.buscarPorId(id)
                .ifPresentOrElse(this::mostrarProducto,
                        () -> System.out.println("No se encontró el producto."));
    }

    private void actualizarProducto() {
        long id = leerEntero("ID del producto a actualizar: ");
        Producto actual = productoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El producto indicado no existe."));

        System.out.println("Presione Enter para conservar el valor actual.");
        String codigo = leerTextoOpcional("Código [" + actual.getCodigo() + "]: ", actual.getCodigo());
        String nombre = leerTextoOpcional("Nombre [" + actual.getNombre() + "]: ", actual.getNombre());
        String descripcion = leerTextoOpcional(
                "Descripción [" + actual.getDescripcion() + "]: ", actual.getDescripcion());
        BigDecimal precio = leerDecimalOpcional(
                "Precio [" + actual.getPrecio().toPlainString() + "]: ", actual.getPrecio());
        int stock = leerEnteroOpcional("Existencias [" + actual.getStock() + "]: ", actual.getStock());

        productoService.actualizarProducto(id, codigo, nombre, descripcion, precio, stock);
        System.out.println("Producto actualizado correctamente.");
    }

    private void eliminarProducto() {
        long id = leerEntero("ID del producto a eliminar: ");
        Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El producto indicado no existe."));
        mostrarProducto(producto);

        String confirmacion = leerTexto("¿Confirma la eliminación? (S/N): ");
        if (!confirmacion.equalsIgnoreCase("S")) {
            System.out.println("Eliminación cancelada.");
            return;
        }

        boolean eliminado = productoService.eliminarProducto(id);
        System.out.println(eliminado ? "Producto eliminado correctamente." : "No se eliminó el producto.");
    }

    private void mostrarProducto(Producto producto) {
        System.out.printf(
                "ID: %d | Código: %s | Nombre: %s | Precio: $%s | Existencias: %d | Descripción: %s%n",
                producto.getId(), producto.getCodigo(), producto.getNombre(),
                producto.getPrecio().toPlainString(), producto.getStock(),
                producto.getDescripcion().isBlank() ? "Sin descripción" : producto.getDescripcion());
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private String leerTextoOpcional(String mensaje, String valorActual) {
        String valor = leerTexto(mensaje);
        return valor.isBlank() ? valorActual : valor;
    }

    private int leerEntero(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje);
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException exception) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }

    private int leerEnteroOpcional(String mensaje, int valorActual) {
        while (true) {
            String valor = leerTexto(mensaje);
            if (valor.isBlank()) {
                return valorActual;
            }
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException exception) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }

    private BigDecimal leerDecimal(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje);
            try {
                return new BigDecimal(valor);
            } catch (NumberFormatException exception) {
                System.out.println("Ingrese un número decimal válido usando punto.");
            }
        }
    }

    private BigDecimal leerDecimalOpcional(String mensaje, BigDecimal valorActual) {
        while (true) {
            String valor = leerTexto(mensaje);
            if (valor.isBlank()) {
                return valorActual;
            }
            try {
                return new BigDecimal(valor);
            } catch (NumberFormatException exception) {
                System.out.println("Ingrese un número decimal válido usando punto.");
            }
        }
    }
}

