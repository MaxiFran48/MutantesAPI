# API de Detección de Mutantes

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)
![Gradle](https://img.shields.io/badge/Gradle-8.7-blue.svg)
![Docker](https://img.shields.io/badge/Docker-blue.svg)
[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com)

API RESTful desarrollada en Spring Boot para el desafío de Mercado Libre. El propósito de la API es detectar si un humano es mutante basándose en su secuencia de ADN.

## 📜 Descripción del Proyecto

Magneto quiere reclutar la mayor cantidad de mutantes posible y te ha contratado para desarrollar un proyecto que detecte si un humano es mutante basándose en su secuencia de ADN.

La lógica de negocio principal consiste en:
- Un humano es considerado mutante si se encuentra **más de una secuencia de cuatro letras iguales** de forma oblicua, horizontal o vertical en su matriz de ADN.
- La API expone endpoints para verificar secuencias de ADN y consultar estadísticas de las verificaciones.

## 🚀 Tecnologías Utilizadas

- **Lenguaje**: Java 21
- **Framework**: Spring Boot 3.2.5
- **Gestor de Dependencias**: Gradle 8.7
- **Base de Datos**: H2 (en memoria para desarrollo y pruebas)
- **Documentación**: SpringDoc OpenAPI (Swagger UI)
- **Contenerización**: Docker
- **Despliegue**: Render

## Endpoints de la API

La API expone los siguientes endpoints para su consumo:

---

### 1. Verificar ADN

Verifica una secuencia de ADN para determinar si corresponde a un mutante o a un humano.

- **Endpoint**: `POST /mutant`
- **Descripción**: Recibe una matriz de ADN. Devuelve `200 OK` si es mutante y `403 Forbidden` si es humano.
- **Request Body**:
  ```json
  {
    "dna": [
      "ATGCGA",
      "CAGTGC",
      "TTATGT",
      "AGAAGG",
      "CCCCTA",
      "TCACTG"
    ]
  }
  ```
- **Ejemplo con cURL**:
  ```bash
  curl -X POST https://mutantesapi.onrender.com/mutant \
  -H "Content-Type: application/json" \
  -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}'
  ```

---

### 2. Obtener Estadísticas

Devuelve las estadísticas de las verificaciones de ADN realizadas.

- **Endpoint**: `GET /stats`
- **Descripción**: Retorna un objeto JSON con la cantidad de ADN mutante, ADN humano y el ratio entre ambos. Se pueden usar los parámetros `startDate` y `endDate` (formato `YYYY-MM-DD`) para filtrar por fecha.
- **Response Body**:
  ```json
  {
    "count_mutant_dna": 40,
    "count_human_dna": 100,
    "ratio": 0.4
  }
  ```
- **Ejemplo con cURL**:
  ```bash
  curl -X GET https://mutantesapi.onrender.com/stats
  ```

---

### 3. Verificar Estado de la API

Endpoint de salud que confirma si la aplicación está en funcionamiento.

- **Endpoint**: `GET /health`
- **Descripción**: Devuelve el estado actual del servicio y la fecha/hora del sistema.
- **Response Body**:
  ```json
  {
    "status": "UP",
    "timestamp": "2025-11-26T18:00:00.12345"
  }
  ```
- **Ejemplo con cURL**:
  ```bash
  curl -X GET https://mutantesapi.onrender.com/health
  ```

---

## ⚙️ Cómo Ejecutar en Local

### Prerrequisitos

- Java JDK 21
- Git

### Pasos

1.  **Clonar el repositorio:**
    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd MutantesAPI
    ```

2.  **Ejecutar la aplicación con Gradle:**
    El proyecto incluye un Gradle Wrapper que facilita la ejecución.
    ```bash
    # En Mac/Linux
    ./gradlew bootRun

    # En Windows
    ./gradlew.bat bootRun
    ```
    La aplicación se iniciará en `http://localhost:8080`.

3.  **Ejecutar los tests:**
    Para correr la suite de tests unitarios y de integración, ejecuta:
    ```bash
    ./gradlew test
    ```
  
## 🔗 Enlace del repositorio

A continuación, el enlaces de la API subida en GitHub.

- **URL de GitHub**:
  `https://github.com/MaxiFran48/MutantesAPI`
    
    
## 🔗 Enlaces del Despliegue

A continuación, los enlaces relevantes de la API desplegada en Render.

- **URL Base de la API**:
  `https://mutantesapi.onrender.com`

- **Documentación Interactiva (Swagger UI)**:
  `https://mutantesapi.onrender.com/swagger-ui.html`

- **Endpoint de Estadísticas**:
  `https://mutantesapi.onrender.com/stats`
