package co.edu.sena.inventario.dao.jdbc;

import co.edu.sena.inventario.config.DatabaseConfig;
import co.edu.sena.inventario.dao.ProductoDao;
import co.edu.sena.inventario.exception.DatabaseException;
import co.edu.sena.inventario.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de ProductoDao mediante JDBC y sentencias preparadas.
 */
public final class ProductoDaoJdbc implements ProductoDao {

    private static final String INSERT_SQL = """
            INSERT INTO productos
                (codigo, nombre, descripcion, precio, stock, fecha_creacion, fecha_actualizacion)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM productos WHERE id = ?";
    private static final String SELECT_BY_CODE_SQL = "SELECT * FROM productos WHERE codigo = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM productos ORDER BY nombre, id";
    private static final String UPDATE_SQL = """
            UPDATE productos
               SET codigo = ?, nombre = ?, descripcion = ?, precio = ?, stock = ?,
                   fecha_actualizacion = ?
             WHERE id = ?
            """;
    private static final String DELETE_SQL = "DELETE FROM productos WHERE id = ?";

    private final DatabaseConfig databaseConfig;

    public ProductoDaoJdbc(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Producto insertar(Producto producto) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, producto.getCodigo());
            statement.setString(2, producto.getNombre());
            statement.setString(3, producto.getDescripcion());
            statement.setBigDecimal(4, producto.getPrecio());
            statement.setInt(5, producto.getStock());
            statement.setString(6, producto.getFechaCreacion().toString());
            statement.setString(7, producto.getFechaActualizacion().toString());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return producto.conId(generatedKeys.getLong(1));
                }
            }
            throw new DatabaseException("La base de datos no retornó el identificador del producto.");
        } catch (SQLException exception) {
            throw new DatabaseException("No fue posible registrar el producto.", exception);
        }
    }

    @Override
    public Optional<Producto> consultarPorId(long id) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapProducto(resultSet)) : Optional.empty();
            }
        } catch (SQLException exception) {
            throw new DatabaseException("No fue posible consultar el producto.", exception);
        }
    }

    @Override
    public Optional<Producto> consultarPorCodigo(String codigo) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_CODE_SQL)) {
            statement.setString(1, codigo);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapProducto(resultSet)) : Optional.empty();
            }
        } catch (SQLException exception) {
            throw new DatabaseException("No fue posible consultar el código del producto.", exception);
        }
    }

    @Override
    public List<Producto> consultarTodos() {
        List<Producto> productos = new ArrayList<>();
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                productos.add(mapProducto(resultSet));
            }
            return productos;
        } catch (SQLException exception) {
            throw new DatabaseException("No fue posible listar los productos.", exception);
        }
    }

    @Override
    public boolean actualizar(Producto producto) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, producto.getCodigo());
            statement.setString(2, producto.getNombre());
            statement.setString(3, producto.getDescripcion());
            statement.setBigDecimal(4, producto.getPrecio());
            statement.setInt(5, producto.getStock());
            statement.setString(6, producto.getFechaActualizacion().toString());
            statement.setLong(7, producto.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException exception) {
            throw new DatabaseException("No fue posible actualizar el producto.", exception);
        }
    }

    @Override
    public boolean eliminar(long id) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException exception) {
            throw new DatabaseException("No fue posible eliminar el producto.", exception);
        }
    }

    private Producto mapProducto(ResultSet resultSet) throws SQLException {
        return new Producto(
                resultSet.getLong("id"),
                resultSet.getString("codigo"),
                resultSet.getString("nombre"),
                resultSet.getString("descripcion"),
                resultSet.getBigDecimal("precio"),
                resultSet.getInt("stock"),
                LocalDateTime.parse(resultSet.getString("fecha_creacion")),
                LocalDateTime.parse(resultSet.getString("fecha_actualizacion"))
        );
    }
}

