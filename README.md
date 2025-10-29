# AAD_25_26-Acceso-a-datos-Castellano_Ramos_Adrian

# Gestor de Logs - Documentación

## Descripción
Aplicación Java desarrollada con Spring Boot para gestionar un sistema de logging simple. Permite añadir eventos a un archivo de logs, filtrarlos por fecha y cambiar la codificación del archivo.

## Requisitos
- **Java**
- **Spring Boot**
- **Maven**
- **Lombok**

## Ejemplos de Uso Práctico

### Ejemplo 1: Registro de Eventos Básico
**Objetivo**: Añadir varios eventos y visualizarlos

**Pasos**:
1. Seleccionar opción 1: "Añadir evento al log"
2. Introducir mensaje: "Sistema iniciado correctamente"
3. Seleccionar opción 1 nuevamente
4. Introducir mensaje: "Usuario conectado: ana_25"
5. Seleccionar opción 4: "Mostrar todos los logs"

### Ejemplo 2: Filtrado por Fecha
**Objetivo**: Buscar eventos de un día específico

**Pasos**:
1. Añadir eventos en diferentes fechas (simulados)
2. Seleccionar opción 2: "Filtrar eventos por fecha"
3. Introducir fecha: "2025-10-28"

### Ejemplo 3: Cambio de Codificación
**Objetivo**: Cambiar la codificación del archivo de logs

**Pasos**:
1. Seleccionar opción 3: "Cambiar codificación"
2. Elegir opción 2: "ISO-8859-1"
3. Ver mensaje de confirmación
4. Añadir un evento para probar