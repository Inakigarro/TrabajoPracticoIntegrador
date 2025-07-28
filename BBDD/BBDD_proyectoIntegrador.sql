-- Tabla Warehouse
CREATE TABLE warehouse (
    id INT AUTO_INCREMENT PRIMARY KEY,
    razon_social VARCHAR(100) NOT NULL
);

-- Tabla Nave
CREATE TABLE nave (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Tabla Zona
CREATE TABLE zona (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('ENTRADA', 'SALIDA', 'TRANSFORMACION', 'ALMACENAMIENTO') NOT NULL,
    id_nave INT,
    FOREIGN KEY (id_nave) REFERENCES nave(id)
);

-- Tabla Ubicacion
CREATE TABLE ubicacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_ubicacion VARCHAR(100) NOT NULL,
    nivel INT,
    capacidad_maxima DOUBLE,
    id_zona INT,
    activa BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_zona) REFERENCES zona(id)
);

-- Tabla Producto
CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255),
    unidad_medida VARCHAR(50),
    contenido DOUBLE,
    stock DOUBLE DEFAULT 0,
    stock_disponible DOUBLE DEFAULT 0
);

-- Tabla ProductoUbicacion
CREATE TABLE producto_ubicacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT,
    id_ubicacion INT,
    stock DOUBLE,
    FOREIGN KEY (id_producto) REFERENCES producto(id),
    FOREIGN KEY (id_ubicacion) REFERENCES ubicacion(id)
);

-- Tabla OrdenTransformacion
CREATE TABLE orden_transformacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    estado ENUM('REARMADO', 'FRACCIONAMIENTO')
);

-- Tabla DetalleTransformacion
CREATE TABLE detalle_transformacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT,
    cantidad DOUBLE,
    id_orden_transformacion INT,
    FOREIGN KEY (id_producto) REFERENCES producto(id),
    FOREIGN KEY (id_orden_transformacion) REFERENCES orden_transformacion(id)
);

-- Tabla OrdenMovimiento
CREATE TABLE orden_movimiento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    tipo ENUM('INGRESO', 'EGRESO', 'INTERNO'),
    estado ENUM('PENDIENTE', 'ANULADA', 'EN_CURSO', 'COMPLETADA', 'CANCELADA')
);

-- Tabla DetalleMovimiento
CREATE TABLE detalle_movimiento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT,
    cantidad DOUBLE,
    id_orden_movimiento INT,
    id_ubicacion_origen INT,
    id_ubicacion_destino INT,
    FOREIGN KEY (id_producto) REFERENCES producto(id),
    FOREIGN KEY (id_orden_movimiento) REFERENCES orden_movimiento(id),
    FOREIGN KEY (id_ubicacion_origen) REFERENCES ubicacion(id),
    FOREIGN KEY (id_ubicacion_destino) REFERENCES ubicacion(id)
);
