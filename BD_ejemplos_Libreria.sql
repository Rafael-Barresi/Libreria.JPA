-- Eliminamos la base de datos si existe
DROP DATABASE IF EXISTS libreriajpa;

-- Creamos la base de datos libreriajpa
CREATE DATABASE libreriajpa;

-- Conexión a la base de datos libreriajpa
USE libreriajpa;

-- Creación de la tabla Cliente
CREATE TABLE Cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    documento VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100),
    telefono VARCHAR(20),
    alta BOOLEAN
);

-- Creación de la tabla Autor
CREATE TABLE Autor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    alta BOOLEAN
);

-- Creación de la tabla Editorial
CREATE TABLE Editorial (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    alta BOOLEAN
);

-- Creación de la tabla Libro
CREATE TABLE Libro (
    isbn INT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    anio INT,
    ejemplares INT,
    ejemplaresPrestados INT,
    ejemplaresRestantes INT, -- Nueva columna añadida
    alta BOOLEAN,
    autor_id INT,
    editorial_id INT,
    FOREIGN KEY (autor_id) REFERENCES Autor(id),
    FOREIGN KEY (editorial_id) REFERENCES Editorial(id)
);

-- Creación de la tabla Prestamo
CREATE TABLE Prestamo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fechaPrestamo DATE,
    fechaDevolucion DATE,
    libro_isbn INT,
    cliente_id INT,
    alta BOOLEAN,
    FOREIGN KEY (libro_isbn) REFERENCES Libro(isbn),
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);

-- Inserción de datos en Autor
INSERT INTO Autor (nombre, alta) VALUES
    ('Gabriel García Márquez', true),
    ('Isabel Allende', true),
    ('J.K. Rowling', true),
    ('Stephen King', true),
    ('Haruki Murakami', true);

-- Inserción de datos en Editorial
INSERT INTO Editorial (nombre, alta) VALUES
    ('Editorial Sudamericana', true),
    ('Editorial Planeta', true),
    ('Bloomsbury', true),
    ('Viking Press', true),
    ('Kodansha', true);

-- Inserción de datos en Libro
INSERT INTO Libro (isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, alta, autor_id, editorial_id) VALUES
    (12345, 'Cien Años de Soledad', 1967, 100, 10, 90, true, 1, 1),
    (54321, 'La Casa de los Espíritus', 1982, 150, 20, 130, true, 2, 2),
    (67890, 'Harry Potter y la Piedra Filosofal', 1997, 80, 5, 75, true, 3, 3),
    (98765, 'El Resplandor', 1977, 120, 15, 105, true, 4, 4),
    (11223, 'Kafka en la Orilla', 2002, 200, 30, 170, true, 5, 5);

-- Inserción de datos en Cliente
INSERT INTO Cliente (documento, nombre, apellido, telefono, alta) VALUES
    ('12345678A', 'Juan', 'Pérez', '123456789', true),
    ('87654321B', 'María', 'Gómez', '987654321', true),
    ('56789012C', 'Carlos', 'López', '567890123', true),
    ('90123456D', 'Ana', 'Martínez', '901234567', true),
    ('34567890E', 'Luis', 'Rodríguez', '345678901', true);

-- Inserción de datos en Prestamo
INSERT INTO Prestamo (fechaPrestamo, fechaDevolucion, libro_isbn, cliente_id, alta) VALUES
    ('2024-01-01', '2024-01-15', 12345, 1, true),
    ('2024-02-01', '2024-02-15', 54321, 2, true),
    ('2024-03-01', '2024-03-15', 67890, 3, true),
    ('2024-04-01', '2024-04-15', 98765, 4, true),
    ('2024-05-01', '2024-05-15', 11223, 5, true);

-- Verificación de datos insertados
SELECT * FROM Autor;
SELECT * FROM Editorial;
SELECT * FROM Libro;
SELECT * FROM Cliente;
SELECT * FROM Prestamo;
