package co.edu.sena.inventario.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa un producto administrado por el módulo de inventario.
 */
public final class Producto {

    private final Long id;
    private final String codigo;
    private final String nombre;
    private final String descripcion;
    private final BigDecimal precio;
    private final int stock;
    private final LocalDateTime fechaCreacion;
    private final LocalDateTime fechaActualizacion;

    public Producto(Long id, String codigo, String nombre, String descripcion,
                    BigDecimal precio, int stock, LocalDateTime fechaCreacion,
                    LocalDateTime fechaActualizacion) {
        this.id = id;
        this.codigo = Objects.requireNonNull(codigo);
        this.nombre = Objects.requireNonNull(nombre);
        this.descripcion = descripcion == null ? "" : descripcion;
        this.precio = Objects.requireNonNull(precio);
        this.stock = stock;
        this.fechaCreacion = Objects.requireNonNull(fechaCreacion);
        this.fechaActualizacion = Objects.requireNonNull(fechaActualizacion);
    }

    public static Producto nuevo(String codigo, String nombre, String descripcion,
                                 BigDecimal precio, int stock) {
        LocalDateTime ahora = LocalDateTime.now();
        return new Producto(null, codigo, nombre, descripcion, precio, stock, ahora, ahora);
    }

    public Producto conId(long nuevoId) {
        return new Producto(nuevoId, codigo, nombre, descripcion, precio, stock,
                fechaCreacion, fechaActualizacion);
    }

    public Producto actualizar(String nuevoCodigo, String nuevoNombre,
                               String nuevaDescripcion, BigDecimal nuevoPrecio,
                               int nuevoStock) {
        return new Producto(id, nuevoCodigo, nuevoNombre, nuevaDescripcion,
                nuevoPrecio, nuevoStock, fechaCreacion, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
}

