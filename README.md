# AAD_25_26-Acceso-a-datos-Castellano_Ramos_Adrian

# 1. Descripción general
Esta práctica implementa un sistema de gestión de matrículas académicas utilizando Spring Boot con acceso a datos mediante JDBC puro (sin JPA ni Spring Data). El objetivo principal es demostrar el manejo directo de conexiones a base de datos, ejecución de consultas SQL nativas, gestión manual de transacciones y ejecución de funciones almacenadas.

Tecnologías utilizadas:

Java 17 con Spring Boot 3.5.6

PostgreSQL 15 en contenedor Docker

JDBC puro con Connection, PreparedStatement y ResultSet

Gestión manual de transacciones (commit/rollback)

Funciones almacenadas en PostgreSQL (PL/pgSQL)

# 2. Instrucciones de ejecución
Prerrequisitos
- Java 17 o superior

- Maven 3.6+

- Docker y Docker Compose

# Paso 1: Levantar PostgreSQL con Docker
# Navegar al directorio del proyecto
cd AAD_25_26-Acceso-a-datos-Castellano_Ramos_Adrian

# Levantar el contenedor de PostgreSQL
docker-compose up -d

Esto creará un contenedor PostgreSQL con:

Base de datos: prueba

Usuario: admin

Contraseña: admin

Puerto: 5432

# Paso 2: Compilar y ejecutar el proyecto con Maven
# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn spring-boot:run

# Paso 3: Validación del sistema
- El sistema ejecutará automáticamente el método run() de CommandLineRunner que incluye:
Student miriam = new Student(null, "66280457T", "Miriam", "miriam@g.educaand.es", "DAM", List.of());
Module programacion = new Module(null, "0485", "Programacion", 250);

miriam = studentManagementService.createStudent(miriam);
programacion = studentManagementService.createModule(programacion);

studentManagementService.enrollStudentInModule(miriam.getId(), programacion.getId());

studentRepository.delete(miriam.getId());

# 3. Evidencias de ejecución

# 3.1 Inicialización de la base de datos
2025-11-17T12:27:51.923+01:00  INFO - Database initialized from SQL scripts

# 3.2 Creación de Estudiantes y Módulos
2025-11-17T12:27:51.947+01:00  INFO - Student inserted: Student(id=1, nif=66280457T, name=Miriam, email=miriam@g.educaand.es, curse=DAM, modules=[])
2025-11-17T12:27:51.961+01:00  INFO - Module inserted: Module(id=1, code=0485, name=Programacion, hours=250)

# 3.3 Inserción de Matrículas en Transacción
2025-11-17T12:27:52.040+01:00  INFO - Enrollment created: Enrollment(id=null, studentId=1, moduleId=1, date=2025-11-17)
2025-11-17T12:27:52.041+01:00  INFO - Student 1 successfully enrolled in module 1

# 3.4 Invocación de Función Almacenada
2025-11-17T12:27:52.064+01:00  INFO - Total enrollments for student 1: 1

# 3.5 Persistencia en PostgreSQL
Los datos se crean temporalmente y se eliminan al final según el código de prueba. Para verificar la persistencia temporal, se pueden consultar las tablas durante la ejecución:
-- Consultar datos durante la ejecución
SELECT * FROM alumno;
SELECT * FROM modulo;
SELECT * FROM matricula;
SELECT count_enrollments(1);

# 4. Conclusion personal
