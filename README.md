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
