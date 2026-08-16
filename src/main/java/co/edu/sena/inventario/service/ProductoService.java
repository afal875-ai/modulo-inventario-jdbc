package co.edu.sena.inventario.service;

import co.edu.sena.inventario.dao.ProductoDao;
import co.edu.sena.inventario.model.Producto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Coordina las operaciones del módulo y aplica sus reglas de negocio.
 */
public final class ProductoService {

    private final ProductoDao productoDao;

    public ProductoService(ProductoDao productoDao) {
        this.productoDao = productoDao;
    }

    public Producto crearProducto(String codigo, String nombre, String descripcion,
                                  BigDecimal precio, int stock) {
        String codigoNormalizado = normalizarCodigo(codigo);
        validarDatos(codigoNormalizado, nombre, precio, stock);
        if (productoDao.consultarPorCodigo(codigoNormalizado).isPresent()) {
            throw new IllegalArgumentException("Ya existe un producto con el código indicado.");
        }

        Producto producto = Producto.nuevo(codigoNormalizado, nombre.trim(),
                normalizarDescripcion(descripcion), precio, stock);
        return productoDao.insertar(producto);
    }

    public Optional<Producto> buscarPorId(long id) {
        validarId(id);
        return productoDao.consultarPorId(id);
    }

    public List<Producto> listarProductos() {
        return productoDao.consultarTodos();
    }

    public boolean actualizarProducto(long id, String codigo, String nombre,
                                      String descripcion, BigDecimal precio, int stock) {
        validarId(id);
        String codigoNormalizado = normalizarCodigo(codigo);
        validarDatos(codigoNormalizado, nombre, precio, stock);

        Producto actual = productoDao.consultarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El producto indicado no existe."));
        productoDao.consultarPorCodigo(codigoNormalizado)
                .filter(encontrado -> !encontrado.getId().equals(id))
                .ifPresent(encontrado -> {
                    throw new IllegalArgumentException("Ya existe otro producto con el código indicado.");
                });

        Producto actualizado = actual.actualizar(codigoNormalizado, nombre.trim(),
                normalizarDescripcion(descripcion), precio, stock);
        return productoDao.actualizar(actualizado);
    }

    public boolean eliminarProducto(long id) {
        validarId(id);
        return productoDao.eliminar(id);
    }

    private void validarDatos(String codigo, String nombre, BigDecimal precio, int stock) {
        if (codigo.isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        if (codigo.length() > 30) {
            throw new IllegalArgumentException("El código no puede superar 30 caracteres.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (nombre.trim().length() > 120) {
            throw new IllegalArgumentException("El nombre no puede superar 120 caracteres.");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a cero.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Las existencias deben ser mayores o iguales a cero.");
        }
    }

    private void validarId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser mayor que cero.");
        }
    }

    private String normalizarCodigo(String codigo) {
        return codigo == null ? "" : codigo.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizarDescripcion(String descripcion) {
        return descripcion == null ? "" : descripcion.trim();
    }
}

