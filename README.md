# Library API — REST Assured Automation Framework

Framework de automatización de pruebas API para el **Desafío #3** sobre el servicio:
`https://rahulshettyacademy.com/Library`

---

## Tecnologías

| Herramienta    | Versión | Propósito                        |
|----------------|---------|----------------------------------|
| Java           | 17      | Runtime                          |
| Maven          | 3.9+    | Build y gestión de dependencias  |
| Rest Assured   | 5.4.0   | Cliente HTTP + aserciones fluent |
| TestNG         | 7.10.2  | Test runner y ordenamiento       |
| Allure         | 2.27.0  | Reportes HTML interactivos       |
| Jackson        | 2.17.2  | Serialización POJO ↔ JSON        |
| AssertJ        | 3.26.3  | Aserciones fluent                |

---

## Estructura del Proyecto

```
RestAssured_Framenwork/
├── src/
│   ├── main/java/com/library/
│   │   ├── config/
│   │   │   └── ApiConfig.java           # BASE_URL y constantes
│   │   ├── models/
│   │   │   ├── AddBookRequest.java      # POJO request — Add Book
│   │   │   ├── AddBookResponse.java     # POJO response — Add Book
│   │   │   ├── GetBookResponse.java     # POJO response — Get Book
│   │   │   └── DeleteBookRequest.java   # POJO request — Delete Book
│   │   └── utils/
│   │       └── ApiHelper.java           # Métodos HTTP reutilizables
│   └── test/java/com/library/
│       ├── base/
│       │   └── BaseTest.java            # Configuración global de Rest Assured
│       ├── data/
│       │   └── TestData.java            # Datos de prueba centralizados
│       └── tests/
│           ├── AddBookTest.java         # 5 pruebas funcionales — POST /Addbook.php
│           ├── GetBookTest.java         # 4 pruebas funcionales — GET /GetBook.php
│           ├── DeleteBookTest.java      # 4 pruebas funcionales — POST /DeleteBook.php
│           └── LibraryIntegrationTest.java  # 4 pruebas integrales — CRUD completo
└── src/test/resources/
    ├── testng.xml       # Suite de pruebas
    └── allure.properties
```

---

## Requisitos Previos

- **Java 17+** — `java -version`
- **Maven 3.9+** — `mvn -version`

---

## Ejecución

```bash
# Ejecutar todas las pruebas
mvn test

# Generar reporte Allure y abrirlo en el navegador
mvn allure:serve

# Solo generar el reporte (sin abrir)
mvn allure:report
# Resultado en: target/allure-report/index.html
```

---

## Cobertura de Pruebas — 17 casos en total

### 1. POST /Addbook.php — 5 pruebas funcionales

| Test | Descripción |
|------|-------------|
| a    | Código de respuesta exitoso: **200 OK** |
| b    | Estructura JSON: claves `Msg` (String) y `ID` (String) |
| c    | Tiempo de respuesta < umbral definido |
| d    | `ID` = concatenación de `isbn` + `aisle` |
| e    | Código de error **500** al enviar `aisle` no numérico |

### 2. GET /GetBook.php — 4 pruebas funcionales

| Test | Descripción |
|------|-------------|
| a    | Código de respuesta exitoso: **200 OK** |
| b    | Estructura JSON: claves `book_name`, `isbn`, `aisle`, `author` (todos String) |
| c    | Tiempo de respuesta < umbral definido |
| d    | ID inexistente retorna **HTTP 404** |

### 3. POST /DeleteBook.php — 4 pruebas funcionales

| Test | Descripción |
|------|-------------|
| a    | Código de respuesta exitoso: **200 OK** |
| b    | Estructura JSON: clave `msg` (String) |
| c    | Tiempo de respuesta < umbral definido |
| d    | ID inexistente retorna **HTTP 404** |

### 4. Integración — Full CRUD Flow — 4 pruebas

| Test | Descripción |
|------|-------------|
| i    | Crea 2 libros — valida a, b, c, d de Add Book |
| ii   | Obtiene los 2 libros — valida a, b, c de Get Book |
| iii  | Elimina los 2 libros — valida a, b, c de Delete Book |
| iv   | Consulta los libros eliminados — valida d (404) de Get Book |

---

## Reporte Allure

Cada prueba incluye:
- Captura completa del **request** (URL, headers, body)
- Captura completa del **response** (status, headers, body)
- Épicas, Features y Stories jerárquicas
- Niveles de severidad (BLOCKER, CRITICAL, NORMAL)

```bash
mvn allure:serve
```

---

## Nota sobre Tiempo de Respuesta

La especificación del desafío indica validar que el tiempo de respuesta sea **< 500 ms**.
El servicio `rahulshettyacademy.com` es un entorno público compartido cuya latencia real
oscila entre 800 ms y 2500 ms según carga y ubicación geográfica.

El umbral actual está configurado en **3000 ms** como límite práctico.
Para ajustarlo, modificar `ApiConfig.MAX_RESPONSE_TIME_MS`.
