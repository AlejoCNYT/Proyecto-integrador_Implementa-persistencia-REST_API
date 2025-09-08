# ECONEXION – Spring Data MongoDB Persistence Lab
**EN / ES – International README**

---

## 🌱 Overview / Descripción

**EN.** This lab adds a MongoDB persistence layer to the existing Spring Boot API. You will:
1) Configure a MongoDB Atlas cluster and connect from Spring Boot.  
2) Create a `@Document` model, a Spring Data `Repository`, and a `Service` with CRUD.  
3) Use an existing controller to expose endpoints.  
4) Verify with Postman that data is stored in MongoDB.  
5) Commit code and the Postman collection.

**ES.** Este laboratorio agrega una capa de persistencia MongoDB a la API Spring Boot existente. Vas a:
1) Configurar un clúster de MongoDB Atlas y conectarte desde Spring Boot.  
2) Crear un modelo `@Document`, un `Repository` de Spring Data y un `Service` con CRUD.  
3) Usar el controlador existente para exponer endpoints.  
4) Verificar con Postman que la información se almacena en MongoDB.  
5) Hacer commit del código y de la colección de Postman.

---

## ✅ Requirements / Requisitos

- **Java 17+** (tested with 21)  
- **Maven 3.9+**  
- **MongoDB Atlas** account (free tier OK)  
- **Postman** (or curl)

---

## 🧩 Tech Stack

- Spring Boot 3.3.x  
- Spring Data MongoDB  
- Spring Web  
- (Optional) Spring Security, Actuator

---

## 📦 Project Structure / Estructura

```
src/main/java/io/econexion/
  lab/users/
    model/
      LabUser.java                    # @Document
    repository/
      LabUserRepository.java          # MongoRepository<LabUser, String>
    service/
      InMemoryLabUserService.java     # @Profile("lab")
      MongoLabUserService.java        # @Profile("mongo")
    LabUserController.java            # uses LabUserService (injected)

src/main/resources/
  application.properties              # base
  application-lab.yml                 # profile for in-memory
  application-mongo.properties        # profile for mongo (URI)

postman/
  Econexion-LabUsers.postman_collection.json
```

<img width="1880" height="758" alt="Captura de pantalla 2025-09-07 195011" src="https://github.com/user-attachments/assets/c1e74e36-9c40-4100-b093-0a566ee04f89" />
<img width="1900" height="923" alt="Captura de pantalla 2025-09-07 195003" src="https://github.com/user-attachments/assets/caca3e40-c7e4-4a7c-a864-e85f1f3f2026" />
<img width="1890" height="1018" alt="Captura de pantalla 2025-09-07 194954" src="https://github.com/user-attachments/assets/45cbc261-6eb5-4eba-bf67-2bce20c1d396" />

---

## ⚙️ Configuration / Configuración

### 1) MongoDB Atlas

**EN.** In Atlas: Create cluster → Create DB user (username/password) → Network access: allow your IP (or 0.0.0.0/0 for lab) → Copy connection string.

**ES.** En Atlas: Crea el clúster → Crea usuario de BD (usuario/contraseña) → Acceso de red: habilita tu IP (o 0.0.0.0/0 para el lab) → Copia la cadena de conexión.

Example / Ejemplo:
```
mongodb+srv://USER:PASS@CLUSTER/DBNAME?retryWrites=true&w=majority&appName=Econexion
```

### 2) Environment variables / Variables de entorno

Create a `.env` (optional) or set variables in your shell.

**Windows PowerShell**
```powershell
$env:MONGODB_URI="mongodb+srv://USER:PASS@CLUSTER/DBNAME?retryWrites=true&w=majority&appName=Econexion"
$env:MONGODB_DB="labdb"
$env:SPRING_PROFILES_ACTIVE="mongo"
```

**Linux/macOS (bash/zsh)**
```bash
export MONGODB_URI="mongodb+srv://USER:PASS@CLUSTER/DBNAME?retryWrites=true&w=majority&appName=Econexion"
export MONGODB_DB="labdb"
export SPRING_PROFILES_ACTIVE="mongo"
```

### 3) `application-mongo.properties`
```properties
spring.data.mongodb.uri=${MONGODB_URI}
spring.data.mongodb.database=${MONGODB_DB:labdb}
spring.data.mongodb.auto-index-creation=true
server.port=${PORT:8080}
```

> **Security tip / Tip de seguridad:** Si usas Spring Security, permite `/lab/**` para pruebas:
```java
http.csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/lab/**").permitAll()
        .anyRequest().authenticated());
```

---

## 🧱 Domain Model / Modelo

`LabUser` (simplified):
```java
@Document(collection = "lab_users")
public class LabUser {
  @Id private String id;        // stored as String (UUID)
  private String name;
  @Indexed(unique = true) private String email;
  private String role;          // optional for this lab
  private Instant createdAt = Instant.now();
}
```

---

## 🗄️ Repository

```java
public interface LabUserRepository extends MongoRepository<LabUser, String> {
  Optional<LabUser> findByEmail(String email);
}
```

---

## 🔧 Services (Profiles)

- `InMemoryLabUserService` → `@Profile("lab")` (sin persistencia).  
- `MongoLabUserService` → `@Profile("mongo")` (usa `LabUserRepository` + mapeo DTOs).  
  - Genera `UUID` como `String` para `_id`.  
  - CRUD + wrappers `String ↔ UUID` si la interfaz usa ambos tipos.

---

## ▶️ Run / Ejecutar

<img width="715" height="183" alt="Captura de pantalla 2025-09-07 194705" src="https://github.com/user-attachments/assets/45e7fcae-bd0f-422a-942e-472589945ef3" />

**Option A – Maven**
```bash
# With env vars set / Con variables definidas
mvn spring-boot:run
```

**Option B – Fat JAR**
```bash
mvn -U -DskipTests package
java -Dspring.profiles.active=mongo -jar target/econexion-1.0-SNAPSHOT.jar
```

**Switch to memory profile / Cambiar a memoria**
```bash
# PowerShell
$env:SPRING_PROFILES_ACTIVE="lab"; mvn spring-boot:run

# Linux/macOS
SPRING_PROFILES_ACTIVE=lab mvn spring-boot:run
```

---

## 🔗 Endpoints & Samples / Endpoints y ejemplos

**Base URL:** `http://localhost:8080`

- `GET    /lab/users`
- `GET    /lab/users/{id}`
- `POST   /lab/users`  
  ```json
  { "name": "Ada Lovelace", "email": "ada@example.com" }
  ```
- `PUT    /lab/users/{id}`  
  ```json
  { "name": "Ada L.", "email": "ada@example.com" }
  ```
- `DELETE /lab/users/{id}`

<img width="1387" height="807" alt="Captura de pantalla 2025-09-07 194725" src="https://github.com/user-attachments/assets/8cca0391-a625-45c0-a63e-0d9597098d4f" />
<img width="1383" height="754" alt="Captura de pantalla 2025-09-07 194735" src="https://github.com/user-attachments/assets/354f546b-646f-486e-8fc9-f6de63374831" />
<img width="1368" height="505" alt="Captura de pantalla 2025-09-07 194744" src="https://github.com/user-attachments/assets/28ec7cad-e776-41c5-a255-4e746637d34d" />
<img width="1381" height="805" alt="Captura de pantalla 2025-09-07 194756" src="https://github.com/user-attachments/assets/58c2ead5-7bd0-423a-967b-0fa02f67b1ba" />
<img width="1368" height="809" alt="Captura de pantalla 2025-09-07 194804" src="https://github.com/user-attachments/assets/d1fb0597-aaee-4b56-a124-4eebc8708580" />

**curl (Linux/macOS)**
```bash
curl -X POST http://localhost:8080/lab/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Ada Lovelace","email":"ada@example.com"}'
```

**PowerShell**
```powershell
irm http://localhost:8080/lab/users -Method POST -ContentType "application/json" -Body '{"name":"Ada Lovelace","email":"ada@example.com"}'
```

---

## 📨 Postman Collection

Path / Ruta: `postman/Econexion-LabUsers.postman_collection.json`  
It chains requests 1→5 and stores the created `{{userId}}` automatically.  
También puedes descargar la colección desde aquí (si estás viendo este README fuera del repo): `Econexion-LabUsers.postman_collection.json`.

---

## 🧪 Verify in Atlas / Verificación en Atlas

1) Atlas → **Data Explorer** → DB `${MONGODB_DB}` → Collection `lab_users`.  
2) Create user in Postman → refresh Data Explorer → document appears.  
3) Update/Delete reflect immediately.

> **Unique index**: with `spring.data.mongodb.auto-index-creation=true`, a unique index on `email` is created. Creating two users with the same `email` throws a duplicate key (optionally map to **409 Conflict**).

---

## 🧾 Rubric Checklist / Lista de verificación

- [x] **Part 1.** Spring Boot connects to Atlas using env vars (`MONGODB_URI`, `MONGODB_DB`).  
- [x] **Part 1.** `spring-boot-starter-data-mongodb` dependency added.  
- [x] **Part 2.1.** `@Document` model created (`LabUser`).  
- [x] **Part 2.2.** `Repository` interface created (`LabUserRepository`).  
- [x] **Part 2.3.** `Service` with CRUD implemented (`MongoLabUserService`).  
- [x] **Part 2.4.** Controller uses the service (profile-based).  
- [x] **Part 2.5.** Postman tests run successfully (Create/List/Get/Update/Delete).  
- [x] **Part 2.6.** Commits done; Postman collection included.

---

## 🛠️ Troubleshooting / Solución de problemas

- **404 on `/lab/users`** → Asegura que el controller **no** tenga un `@Profile` que lo excluya. Debe llevar `@RequestMapping("/lab/users")`.  
- **401/403** → Permite `/lab/**` en `SecurityConfig`.  
- **Unknown lifecycle phase '.run.profiles=...'** (PowerShell) → usa env var:  
  `"$env:SPRING_PROFILES_ACTIVE='mongo'; mvn spring-boot:run"`.  
- **Java 21 not supported** → usa JDK 21 o cambia `<java.version>` a 17 y Project SDK=17.  
- **Imports de Mongo** deben ser:
  - `org.springframework.data.mongodb.core.mapping.Document`  
  - `org.springframework.data.annotation.Id`  
  - `org.springframework.data.mongodb.core.index.Indexed`

---

## 📄 License

Educational purpose / Uso educativo.
