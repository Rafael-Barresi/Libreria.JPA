Proyecto Biblioteca
Este proyecto es una implementación de una biblioteca utilizando Java y Java Persistence API (JPA).
Fue desarrollado como parte de la práctica de aprendizaje, con el objetivo de aplicar conceptos avanzados de Java en un contexto práctico y funcional.

Funcionalidades
Gestión de Libros:

Crear, modificar y eliminar libros.
Consultar libros disponibles en la biblioteca.
Gestión de Préstamos:

Registrar nuevos préstamos.
Realizar la devolución de libros prestados.
Consultar préstamos por cliente.
Modificar el estado de los préstamos.
Estructura del Proyecto
Entidades: Representan las tablas de la base de datos y están mapeadas usando JPA.
Servicios: Contienen la lógica de negocio, validaciones y gestión de datos.
Controladores: Manejan la interacción con el usuario, proporcionando menús y opciones para realizar operaciones.
Persistencia (DAO): Encapsulan el acceso a la base de datos, permitiendo realizar operaciones CRUD sobre las entidades.
Requisitos
Java Development Kit (JDK) 17
JPA (Java Persistence API)
Base de Datos: Configurada para utilizar JPA con una base de datos en memoria (H2) para pruebas y desarrollo.
Se adjunta Script con ejemplos para prueba.
