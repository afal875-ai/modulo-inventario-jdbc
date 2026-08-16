# Informe de pruebas

## Identificación

- **Evidencia:** GA7-220501096-AA2-EV01.
- **Módulo:** Gestión de productos e inventario.
- **Fecha de ejecución:** 11 de agosto de 2026.
- **Compilación objetivo:** Java 17.
- **Herramienta:** Maven y JUnit 5.

## Resultado automatizado

Comando ejecutado:

```bash
mvn clean package
```

Resultado obtenido:

```text
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Casos verificados

| Caso | Resultado |
|---|---|
| Registrar y consultar un producto | Aprobado |
| Listar productos ordenados por nombre | Aprobado |
| Actualizar los datos de un producto | Aprobado |
| Eliminar un producto | Aprobado |
| Rechazar un código duplicado | Aprobado |
| Rechazar un precio negativo | Aprobado |
| Rechazar existencias negativas | Aprobado |

## Prueba funcional del ejecutable

Se ejecutó el archivo JAR generado con una base de datos temporal. Desde el menú se realizó la siguiente secuencia:

1. Registro de un producto.
2. Listado del catálogo.
3. Consulta por identificador.
4. Actualización del nombre, precio y existencias.
5. Eliminación confirmada.
6. Consulta final del catálogo vacío.
7. Cierre normal del programa.

Todas las operaciones finalizaron correctamente y los cambios se reflejaron en la base de datos SQLite.

