package co.edu.sena.inventario.dao;

import co.edu.sena.inventario.model.Producto;

import java.util.List;
import java.util.Optional;

/**
 * Define las operaciones de persistencia para los productos.
 */
public interface ProductoDao {

    Producto insertar(Producto producto);

    Optional<Producto> consultarPorId(long id);

    Optional<Producto> consultarPorCodigo(String codigo);

    List<Producto> consultarTodos();

    boolean actualizar(Producto producto);

    boolean eliminar(long id);
}

