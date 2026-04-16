# TMS Authentication & Authorization (Нэвтрэлт ба Зөвшөөрлийн систем)

## 1. Ерөнхий тойм

TMS системд хэрэглэгч нэвтрэх (Authentication) болон эрх шалгах (Authorization) механизмыг
**api_gateway** + **user_service** хоёрын хамтын ажиллагаагаар хэрэгжүүлсэн.

### Үндсэн зарчим

```
Client (Postman / Frontend)
    │
    ▼
┌──────────────────────────────────────────┐
│           API Gateway (:8080)            │
│  1. JWT token шалгах (decode + verify)   │
│  2. Role-д суурилсан эрх шалгах          │
│  3. Зөв service рүү дамжуулах            │
└──────────────────────────────────────────┘
    │                          │
    ▼                          ▼
┌──────────────┐      ┌────────────────┐
│ user_service │      │ thesis_service │  ... бусад service-үүд
│    (:8081)   │      │    (:8083)     │
└──────────────┘      └────────────────┘
```

- **Client** зөвхөн gateway (:8080) руу хандана
- **Gateway** JWT token шалгаад, зөв service рүү route хийнэ
- **user_service** нэвтрэлтийн логик (login, JWT үүсгэх) хариуцна
- Бусад service-үүд нь gateway-н ард аюулгүй байна

---

## 2. Нэвтрэлт (Authentication) — Хэрхэн ажилладаг вэ?

### 2.1. Нэвтрэх (Login) урсгал

```
Алхам 1: Client → POST /api/auth/login   (email + password)
Алхам 2: Gateway → user_service руу дамжуулна (энэ endpoint нээлттэй)
Алхам 3: user_service → DB-с email-аар хэрэглэгч хайна
Алхам 4: BCrypt-ээр нууц үг шалгана
Алхам 5: Зөв бол → JWT token үүсгэж буцаана
Алхам 6: Client → JWT token хадгалж, дараагийн хүсэлтүүдэд ашиглана
```

### 2.2. Оролцогч файлууд

| Файл | Үүрэг |
|------|-------|
| `user_service/.../AuthController.java` | `/api/auth/login` endpoint. Client-н email/password хүлээн авна |
| `user_service/.../LoginRequest.java` | Login хүсэлтийн формат (email + password, validation-тай) |
| `user_service/.../AuthenticateUserUseCase.java` | Нэвтрэлтийн use case интерфэйс (hexagonal architecture port) |
| `user_service/.../AuthenticateUserService.java` | Бизнес логик: email хайх → BCrypt шалгах → JWT үүсгэх |
| `user_service/.../JwtTokenService.java` | JWT token үүсгэгч (HS256, claims тохируулах) |
| `user_service/.../PasswordConfig.java` | BCryptPasswordEncoder bean тохиргоо |
| `user_service/.../LoginCommand.java` | Controller → Service руу дамжуулах DTO |
| `user_service/.../LoginResponse.java` | Амжилттай нэвтэрсэн үед буцаах хариу |
| `user_service/.../AuthenticationFailedException.java` | 401 алдаа (email/password буруу) |

### 2.3. Нууц үг хэрхэн хадгалагддаг вэ?

- Хэрэглэгч үүсгэхэд нууц үгийг **BCrypt** алгоритмаар hash хийж `password_hash` баганад хадгална
- Login хийхэд client-с ирсэн нууц үгийг DB дахь hash-тай `BCrypt.matches()` ашиглан харьцуулна
- Hash нь нэг талын (one-way) — тайлж анхны нууц үгийг олох боломжгүй
- `User.java` дээр `@JsonIgnore` тавьсан тул API response-д `passwordHash` хэзээ ч гарахгүй

### 2.4. JWT Token-ий бүтэц

Token нь 3 хэсгээс бүрдэнэ: `Header.Payload.Signature`

**Header:**
```json
{
  "alg": "HS256"
}
```

**Payload (Claims):**
```json
{
  "iss": "tms-user-service",
  "sub": "a0000000-0000-0000-0000-000000000001",
  "email": "admin@tms.num.edu.mn",
  "role": "ADMIN",
  "departmentId": null,
  "iat": 1713280800,
  "exp": 1713288000
}
```

| Claim | Тайлбар |
|-------|---------|
| `iss` | Token үүсгэгч (issuer) — `tms-user-service` |
| `sub` | Хэрэглэгчийн ID (subject) |
| `email` | Хэрэглэгчийн email |
| `role` | Системийн үүрэг: ADMIN, STUDENT, TEACHER, DEPARTMENT, GUEST |
| `departmentId` | Тэнхимийн ID (хэрэв хамаарах бол) |
| `iat` | Token үүсгэсэн цаг |
| `exp` | Token дуусах цаг (120 минутын дараа) |

**Signature:** HS256 алгоритмаар нууц түлхүүрээр гарын тоо хийсэн. Gateway энэ гарын тоог шалгаж token хуурамч биш гэдгийг баталгаажуулна.

### 2.5. Нууц түлхүүр (Secret Key)

- user_service болон api_gateway **ижил нууц түлхүүр** ашиглана
- `TMS_JWT_SECRET` орчны хувьсагчаар тохируулна
- Default: `tms-local-development-secret-key-please-change`
- user_service тохиргоо: `application.properties` → `app.security.jwt.secret`
- api_gateway тохиргоо: `application.yml` → `app.security.jwt.secret`

> **Чухал:** Production-д заавал урт, санамсаргүй нууц түлхүүр ашиглах ёстой.

### 2.6. Login Response жишээ

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJpc3Mi...",
  "tokenType": "Bearer",
  "expiresIn": 7200,
  "expiresAt": "2026-04-16T17:27:53Z",
  "userId": "1b9a3209-f63a-464e-906a-842f7a691abc",
  "email": "22B1NUM0000@stud.num.edu.mn",
  "role": "STUDENT",
  "departmentId": "dep-01"
}
```

---

## 3. Зөвшөөрөл (Authorization) — Эрх шалгах

### 3.1. Gateway дээр хэрхэн ажилладаг вэ?

```
Client хүсэлт ирнэ
    │
    ▼
Gateway: Authorization header байна уу?
    │
    ├─ Үгүй → 401 Unauthorized
    │
    ├─ Байна → JWT decode хийнэ (HS256, нууц түлхүүрээр шалгана)
    │    │
    │    ├─ Token хугацаа дууссан / хүчингүй → 401 Unauthorized
    │    │
    │    └─ Token зөв → role claim уншина
    │         │
    │         ▼
    │    Endpoint-н шаардлагатай role тохирч байна уу?
    │         │
    │         ├─ Тохирохгүй → 403 Forbidden
    │         │
    │         └─ Тохирно → Хүсэлтийг downstream service рүү дамжуулна ✅
    │
    └─ Нээлттэй endpoint (/api/auth/**) → шууд дамжуулна ✅
```

### 3.2. Хандалтын эрхийн дүрэм

Эдгээр дүрмийг `SecurityConfig.java` файлд тодорхойлсон:

| Endpoint | HTTP Method | Шаардлагатай эрх | Тайлбар |
|----------|-------------|-------------------|---------|
| `/api/auth/**` | Бүгд | **Нээлттэй** | Нэвтрэх, token авах |
| `/actuator/health` | GET | **Нээлттэй** | Серверийн төлөв шалгах |
| `/api/users/students` | POST | **ADMIN** | Оюутан үүсгэх |
| `/api/users/teachers` | POST | **ADMIN** | Багш үүсгэх |
| `/api/users/departments` | POST | **ADMIN** | Тэнхим үүсгэх |
| `/api/users/all` | GET | **DEPARTMENT** | Бүх хэрэглэгч жагсаах |
| `/api/workflows/**` | Бүгд | **DEPARTMENT** | Ажлын урсгал удирдах |
| `/api/evaluations/**` | Бүгд | **TEACHER** эсвэл **DEPARTMENT** | Үнэлгээ хийх |
| `/api/grades/**` | Бүгд | **DEPARTMENT** | Дүн харах |
| `/api/resolutions/**` | Бүгд | **DEPARTMENT** | Тогтоол харах |
| `/api/reports/**` | Бүгд | **TEACHER** эсвэл **DEPARTMENT** | Тайлан харах |
| Бусад бүх endpoint | Бүгд | **Authenticated** | Нэвтэрсэн байхад хангалттай |

### 3.3. Role-ийн тайлбар

| Role | Тайлбар | Гол эрхүүд |
|------|---------|------------|
| `ADMIN` | Системийн админ | Хэрэглэгч (student, teacher, department) үүсгэх |
| `STUDENT` | Оюутан | Өөрийн дипломын ажилтай холбоотой endpoint-ууд |
| `TEACHER` | Багш | Үнэлгээ хийх, тайлан харах |
| `DEPARTMENT` | Тэнхим | Ажлын урсгал, дүн, тогтоол, бүх хэрэглэгч, тайлан |
| `GUEST` | Зочин | Зөвхөн authenticated шаардлагатай endpoint-ууд |

### 3.4. JWT → Spring Security хөрвүүлэлт

Gateway дотор JWT token-г Spring Security-н authentication объект руу хөрвүүлэх логик:

1. `ReactiveJwtDecoder` — token decode + гарын тоо шалгана
2. `jwtAuthenticationConverter()` — JWT → `AbstractAuthenticationToken` хөрвүүлнэ
3. `extractAuthorities()` — `role` claim уншиж `ROLE_STUDENT` гэх мэт GrantedAuthority үүсгэнэ
4. Spring Security `hasRole("STUDENT")` нь дотооддоо `ROLE_STUDENT` authority байгаа эсэхийг шалгана

---

## 4. ADMIN Seed хэрэглэгч

Систем анх ажиллахад хэрэглэгч үүсгэх endpoint ADMIN эрх шаарддаг. Тиймээс анхны ADMIN хэрэглэгчийг DB-д шууд оруулсан (seed data).

**Файл:** `docker/postgres/init/01-init-tms.sql`

| Талбар | Утга |
|--------|------|
| ID | `a0000000-0000-0000-0000-000000000001` |
| Нэр | System Admin |
| Email | `admin@tms.num.edu.mn` |
| Нууц үг | `Admin@TMS2026!` |
| Role | `ADMIN` |

### Seed хэрэглэгчээр нэвтрэх

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@tms.num.edu.mn","password":"Admin@TMS2026!"}'
```

Буцаах хариунаас `accessToken` авч, дараагийн хүсэлтүүдэд `Authorization: Bearer <token>` header нэмнэ.

### ADMIN token-р хэрэглэгч үүсгэх жишээ

```bash
# Оюутан үүсгэх (ADMIN token шаардлагатай)
curl -X POST http://localhost:8080/api/users/students \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -d '{
    "firstName": "Бат",
    "lastName": "Дорж",
    "email": "22B1NUM0001@stud.num.edu.mn",
    "password": "StrongPassword123!",
    "studentId": "22B1NUM0001",
    "departmentId": "dep-01",
    "major": "Мэдээллийн технологи"
  }'
```

---

## 5. Ашигласан технологи

| Технологи | Хэрэглээ |
|-----------|----------|
| **Spring Cloud Gateway** | API routing, CORS, request forwarding |
| **Spring Security (WebFlux)** | JWT validation, role-based access control |
| **Spring OAuth2 Resource Server** | JWT decode (Nimbus), HS256 verification |
| **BCrypt** | Нууц үг нэг талын hash хийх |
| **Nimbus JOSE+JWT** | JWT token үүсгэх (user_service) болон шалгах (gateway) |
| **R2DBC (Reactive)** | Хэрэглэгчийн мэдээлэл PostgreSQL-с реактив байдлаар унших |

---

## 6. Файлын бүтэц

```
api_gateway/
├── src/main/java/.../
│   ├── ApiGatewayApplication.java     # Gateway-н main application
│   └── config/
│       └── SecurityConfig.java        # JWT decode + role-based дүрмүүд
└── src/main/resources/
    └── application.yml                # Route, CORS, JWT secret тохиргоо

user_service/
├── src/main/java/.../
│   ├── adapter/in/web/
│   │   ├── controller/
│   │   │   ├── AuthController.java    # POST /api/auth/login endpoint
│   │   │   └── UserController.java    # CRUD user endpoints
│   │   └── request/
│   │       └── LoginRequest.java      # Login хүсэлтийн формат
│   ├── application/
│   │   ├── dto/
│   │   │   ├── LoginCommand.java      # Login command DTO
│   │   │   └── LoginResponse.java     # Login хариу DTO
│   │   ├── port/in/
│   │   │   └── AuthenticateUserUseCase.java  # Нэвтрэлтийн интерфэйс
│   │   └── service/
│   │       ├── AuthenticateUserService.java  # Нэвтрэлтийн бизнес логик
│   │       └── JwtTokenService.java          # JWT token үүсгэгч
│   ├── config/
│   │   └── PasswordConfig.java        # BCrypt encoder bean
│   ├── domain/model/
│   │   ├── User.java                  # Хэрэглэгчийн домэйн модел
│   │   └── SystemRole.java            # ADMIN, STUDENT, TEACHER, DEPARTMENT, GUEST
│   └── exception/
│       ├── AuthenticationFailedException.java  # 401 алдаа
│       └── GlobalExceptionHandler.java         # Төвлөрсөн алдаа боловсруулагч
└── src/main/resources/
    └── application.properties         # JWT secret, DB, Kafka тохиргоо

docker/postgres/init/
└── 01-init-tms.sql                    # DB schema + ADMIN seed data
```

---

## 7. Аюулгүй байдлын анхааруулга

1. **Нууц түлхүүр** — Production-д `TMS_JWT_SECRET` орчны хувьсагчаар урт, санамсаргүй утга тохируулах
2. **ADMIN нууц үг** — Анхны нэвтрэлтийн дараа нууц үг солих (одоогоор солих endpoint байхгүй, дараа нэмэгдэнэ)
3. **HTTPS** — Production-д заавал HTTPS ашиглах (JWT token дамжуулалт аюулгүй байхын тулд)
4. **Token хугацаа** — 120 минут. Дараа нь refresh token нэмэх шаардлагатай
5. **Seed data** — `01-init-tms.sql` дотор нууц үг hash-тай хадгалагдсан, гэхдээ repo-д нууц үг plaintext-ээр comment-д бичсэн. Production-д устгах
