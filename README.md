# AAD_25_26-Acceso-a-datos-Castellano_Ramos_Adrian

# 1. Qué es un conector y su papel en la aplicación.
    El conector sirve de puente para conectar la base de datos con la aplicacion

# 2. Cómo has levantado el servicio PostgreSQL. 
    En Intellij, he abierto desde la carpeta resources el terminal y con el comando docker compose up -d he levantado el contenedor

# 3. Qué variables has utilizado y por qué.
    version: '3.8'
    services:
    postgres:
    image: postgres:15
    container_name: postgres-db
    environment:
    POSTGRES_USER: admin
    POSTGRES_PASSWORD: admin
    POSTGRES_DB: prueba
    ports:
    - "5432:5432"
    volumes:
      - ./data:/var/lib/postgresql/data

# 4. Cómo probar la conexión.
    Creamos una conexion en DBeaver de Postgres con los siguientes parámetros:
    user: admin
    password: admin
    base de datos: prueba
    puerto: 5432
    Host: localhost

# 5. Explicación de JDBC y su papel en la aplicación
    JDBC es una API que permite a las aplicaciones Java conectarse y ejecutar consultas en bases de datos. En la aplicación, JDBC se utiliza para gestionar la comunicación entre el código Java y la base de datos PostgreSQL, permitiendo realizar operaciones como consultas, inserciones, actualizaciones y eliminaciones de datos.

# 6. Breve descripción del application.yml y del código de prueba.
    El archivo application.yml contiene la configuración necesaria para que la aplicación Java pueda conectarse a la base de datos PostgreSQL. Incluye detalles como la URL de conexión, el nombre de usuario y la contraseña. El código de prueba utiliza esta configuración para establecer una conexión con la base de datos y realizar operaciones básicas, verificando que la conexión se ha establecido correctamente y que las consultas funcionan según lo esperado.

# 7. Pasos para ejecutar el contenedor y verificar la conexión desde IntelliJ.
    1. Abre IntelliJ y navega hasta la carpeta resources donde se encuentra el archivo docker-compose.yml.
    2. Abre una terminal dentro de IntelliJ.
    3. Ejecuta el comando `docker compose up -d` para levantar el contenedor de PostgreSQL.
    4. Espera unos momentos para que el contenedor se inicie completamente.
    5. Configura la conexión a la base de datos en tu aplicación Java utilizando los parámetros definidos en el archivo application.yml.
    6. Ejecuta el código de prueba para verificar que la conexión a la base de datos se ha establecido correctamente y que las operaciones funcionan como se espera.
    7. Si todo funciona correctamente, deberías ver resultados positivos en la consola o en los logs de la aplicación.