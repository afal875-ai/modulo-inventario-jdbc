# Evidencia GA7-220501096-AA2-EV01

## Codificación de módulos del software

**Aprendiz:** Andrés Felipe Avendaño López  
**Ficha:** 3235904  
**Programa:** Análisis y Desarrollo de Software  
**Módulo implementado:** Gestión de productos e inventario

Este proyecto implementa un módulo de consola en Java para administrar productos. Utiliza JDBC con una base de datos SQLite y ofrece las cuatro operaciones requeridas: inserción, consulta, actualización y eliminación (CRUD).

## Funcionalidades

- Registrar productos con código único, nombre, descripción, precio y existencias.
- Consultar todos los productos o buscar uno por su identificador.
- Actualizar la información de un producto existente.
- Eliminar un producto después de solicitar confirmación.
- Validar datos antes de almacenarlos.
- Crear automáticamente la base de datos y su tabla al iniciar.
- Conservar fechas de creación y actualización.
- Gestionar errores de persistencia sin mostrar detalles internos al usuario.

## Tecnologías seleccionadas

- Java 17.
- Maven 3.9 o superior.
- JDBC.
- SQLite.
- JUnit 5.
- Git.

## Estructura del proyecto

```text
src/main/java/co/edu/sena/inventario/
├── config/       Configuración y conexión JDBC
├── dao/          Contrato de persistencia
├── dao/jdbc/     Implementación del CRUD con JDBC
├── exception/    Excepciones controladas del módulo
├── model/        Entidad Producto
├── service/      Reglas de negocio y validaciones
└── ui/           Interfaz de consola
```

La base de datos de ejecución se guarda en `data/inventario.db`. El archivo se genera automáticamente y no se incluye en el control de versiones.

## Compilar y ejecutar

Desde la carpeta del proyecto:

```bash
mvn clean package
java -jar target/modulo-inventario-jdbc-1.0.0-ejecutable.jar
```

También puede ejecutarse desde un IDE abriendo el proyecto Maven y ejecutando la clase `InventarioApplication`.

## Ejecutar las pruebas

```bash
mvn test
```

Las pruebas usan una base de datos temporal independiente y verifican las operaciones CRUD y las validaciones principales.

## Configuración opcional

La ubicación de la base de datos puede cambiarse con la propiedad de Java `inventario.db.path`:

```bash
java -Dinventario.db.path=/ruta/datos.db \
  -jar target/modulo-inventario-jdbc-1.0.0-ejecutable.jar
```

## Cumplimiento de la evidencia

| Requisito | Evidencia en el proyecto |
|---|---|
| Conexión con base de datos mediante JDBC | `DatabaseConfig` y `ProductoDaoJdbc` |
| Inserción, consulta, actualización y eliminación | Métodos CRUD de `ProductoDaoJdbc` y menú de consola |
| Estándares de codificación | Paquetes en minúscula, clases en PascalCase y métodos/variables en camelCase |
| Artefactos del ciclo de software | Carpeta `docs` |
| Herramienta de versionamiento | Repositorio Git incluido con historial de confirmaciones |
| Pruebas | Casos automatizados en `src/test/java` |

La documentación complementaria se encuentra en [docs/ARTEFACTOS_PROYECTO.md](docs/ARTEFACTOS_PROYECTO.md), [docs/MANUAL_USUARIO.md](docs/MANUAL_USUARIO.md) y [docs/INFORME_PRUEBAS.md](docs/INFORME_PRUEBAS.md).

## Publicación del repositorio

El proyecto ya está inicializado y versionado localmente con Git. Para completar el enlace público o privado solicitado por la plataforma, publique este repositorio en el servicio autorizado por el aprendiz y reemplace el campo pendiente en `ENLACE_REPOSITORIO.txt`.
