# CarritoGT — Migración de PHP a Java EE (Spring Boot)

Migración del proyecto [`carrito-compra-basico`](https://github.com/mvelasquezo/carrito-compra-basico) (PHP, arquitectura de 3 capas) a **Java con Spring Boot**, reestructurado a **n-capas** y expuesto como **API REST/microservicio**, más un front-end en **JavaScript Vanilla**.

## Arquitectura (n-capas)

```
Cliente (frontend/ HTML+JS)
        │  HTTP/JSON
        ▼
controller/   → Capa de presentación (endpoints REST)
        │
service/      → Capa de negocio (interfaces + impl)
        │
repository/   → Capa de acceso a datos (Spring Data JPA)
        │
model/        → Entidades (mapeadas a la BD MySQL "cc")
```

Equivalencias con el proyecto PHP original:

| PHP original                          | Java (Spring Boot)                                  |
|----------------------------------------|------------------------------------------------------|
| `public/index.php` + `Core/Router.php` | Spring Boot embebido (Tomcat) + `@RestController`     |
| `App/Controllers/Home.php`             | `controller/ProductoController.java`                 |
| `App/Controllers/ShoppingCart.php`     | `controller/CarritoController.java`                  |
| `App/Models/ProductoModel.php`         | `model/Producto.java` + `service/ProductoService*`   |
| `App/Models/ShoppingCartModel.php`     | `model/ItemCarrito.java` + `service/CarritoService*` |
| `App/Config.php` (credenciales BD)     | `src/main/resources/application.properties`          |
| `App/Auth.php` / `App/Token.php`       | `codUsuario` generado en el navegador (`js/api.js`)  |
| Vistas Twig (`App/Views/*.html`)       | `frontend/*.html` + `fetch()` consumiendo la API      |

## 1. Requisitos previos (Linux)

- **JDK 17** o superior
- **Maven** (o usar el wrapper que trae IntelliJ)
- **MySQL Server** corriendo en `localhost:3306`
- **IntelliJ IDEA** (Community o Ultimate)

Instalar JDK y MySQL en Ubuntu/Debian, por ejemplo:

```bash
sudo apt update
sudo apt install openjdk-17-jdk mysql-server -y
sudo systemctl start mysql
```

## 2. Base de datos

Si ya tienes la base `cc` del proyecto PHP importada, **no necesitas hacer nada**: Spring Boot/Hibernate reconocerá las tablas `producto` y `shopping_cart` tal cual están (mismo nombre de tablas y columnas).

Si no la tienes, impórtala con el script incluido:

```bash
mysql -u root -p < src/main/resources/db/cc.sql
```

Ajusta usuario/contraseña en `src/main/resources/application.properties` si son distintos a `root` / `USAC123`.

## 3. Abrir y ejecutar el backend en IntelliJ IDEA

1. Abre IntelliJ IDEA → **Open** → selecciona la carpeta `carrito-java` (donde está el `pom.xml`).
2. IntelliJ detectará que es un proyecto Maven y descargará las dependencias automáticamente (ícono de Maven en la barra lateral derecha, si no lo hace clic en el ícono de refrescar).
3. Ve a `src/main/java/com/carritogt/CarritoApplication.java`.
4. Haz clic en el ▶ verde junto a `public static void main` (o botón derecho → **Run 'CarritoApplication'**).
5. Cuando veas en la consola `Tomcat started on port 8080`, el backend está listo.
6. Prueba en el navegador: `http://localhost:8080/api/productos` — deberías ver el JSON con los 4 productos.

## 4. Ejecutar el front-end

El front-end son archivos estáticos (`frontend/index.html`, `carrito.html`, `css/`, `js/`). Puedes abrirlos de varias formas:

**Opción A — Plugin de IntelliJ:** clic derecho sobre `frontend/index.html` → **Open in Browser**.

**Opción B — Servidor simple con Python (ya viene en la mayoría de Linux):**
```bash
cd frontend
python3 -m http.server 5500
```
Luego abre `http://localhost:5500` en tu navegador.

> El front-end apunta a `http://localhost:8080/api` (ver `frontend/js/api.js`). Si cambias el puerto del backend, actualiza esa constante.

## 5. Endpoints REST disponibles

| Método | Endpoint                          | Descripción                                  |
|--------|------------------------------------|-----------------------------------------------|
| GET    | `/api/productos`                   | Lista el catálogo de productos                |
| GET    | `/api/carrito/{codUsuario}`        | Obtiene el carrito de un usuario              |
| POST   | `/api/carrito/{codUsuario}/items`  | Agrega un producto (`{idProducto, cantidad}`) |
| DELETE | `/api/carrito/items/{id}`          | Elimina un item específico del carrito        |
| DELETE | `/api/carrito/{codUsuario}`        | Vacía el carrito completo                     |

## 6. Siguientes pasos sugeridos (para el entregable)

- Separar este backend como su propio microservicio (ya lo está: es un servicio independiente con su propio `pom.xml` y puede desplegarse por separado).
- Si el requerimiento pide *varios* microservicios, puedes dividir `Producto` y `Carrito` en dos proyectos Spring Boot independientes, cada uno con su propia base de datos o esquema, comunicándose vía REST (usa `RestTemplate` o `WebClient` para las llamadas entre servicios).
- Agregar pruebas unitarias con JUnit 5 + Mockito para la capa `service/` (similar a `tests/ProductoModelTest.php` del original).
- Documentar la API con Swagger/OpenAPI (`springdoc-openapi-starter-webmvc-ui`).

## Solución de problemas comunes

- **`Communications link failure` / no conecta a MySQL:** verifica que MySQL esté corriendo (`sudo systemctl status mysql`) y que usuario/contraseña en `application.properties` sean correctos.
- **CORS bloqueado en el navegador:** ya está habilitado en `config/CorsConfig.java` para `/api/**`; si sigues viendo error, revisa que el backend esté corriendo en el puerto 8080.
- **Puerto 8080 ocupado:** cambia `server.port` en `application.properties` y actualiza `API_BASE` en `frontend/js/api.js`.
