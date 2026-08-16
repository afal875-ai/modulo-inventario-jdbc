# Manual de usuario

## Requisitos

- Java 17 o superior.
- Maven 3.9 o superior, únicamente para compilar el proyecto.

## Inicio

Compile y ejecute el proyecto siguiendo las instrucciones del archivo `README.md`. Al comenzar aparece el menú:

```text
1. Registrar producto
2. Listar productos
3. Buscar producto por ID
4. Actualizar producto
5. Eliminar producto
0. Salir
```

Escriba el número de la opción y presione Enter.

## Registrar

Seleccione la opción 1 y diligencie código, nombre, descripción, precio y existencias. El separador decimal es punto. Por ejemplo: `15990.50`.

## Consultar

- La opción 2 muestra todos los productos.
- La opción 3 solicita el identificador numérico asignado al crear el producto.

## Actualizar

Seleccione la opción 4, indique el identificador y escriba los nuevos datos. Puede presionar Enter para conservar el valor que aparece entre corchetes.

## Eliminar

Seleccione la opción 5, indique el identificador y confirme escribiendo `S`. Cualquier otra respuesta cancela la eliminación.

## Cerrar el programa

Seleccione la opción 0. La información queda almacenada en la base de datos para la siguiente ejecución.

