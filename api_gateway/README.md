# TMS API Gateway

`api_gateway` нь TMS-ийн бүх REST API request-ийг нэг entry point-оор авч зөв microservice рүү дамжуулдаг Spring Cloud Gateway service.

## Ямар port дээр ажиллах вэ

```text
http://localhost:8080
```

## Route mapping

| Gateway path | Target service |
|---|---|
| `/api/users/**` | `http://localhost:8081` |
| `/api/auth/**` | `http://localhost:8081` |
| `/api/theses/**` | `http://localhost:8083` |
| `/api/workflows/**` | `http://localhost:8084` |
| `/api/evaluations/**` | `http://localhost:8085` |
| `/api/grades/**`, `/api/grading/**`, `/api/resolutions/**` | `http://localhost:8086` |
| `/api/notifications/**` | `http://localhost:8087` |
| `/api/reports/**` | `http://localhost:8088` |

## Ажиллуулах

```bash
mvn -f api_gateway/pom.xml spring-boot:run
```

Жишээ request:

```bash
curl http://localhost:8080/api/users/all
```

Энэ request gateway дээр орж `user_service`-ийн `http://localhost:8081/api/users/all` руу дамжина.

## Security

Gateway нь `user_service`-ээс үүссэн JWT token-ийг шалгана.

Token авах:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"student@test.mn","password":"StrongPassword123!"}'
```

Token-той request хийх:

```bash
curl http://localhost:8080/api/theses \
  -H "Authorization: Bearer <accessToken>"
```

Одоогоор `/api/auth/**` болон bootstrap user creation endpoint-ууд нээлттэй. `ADMIN` эсвэл seed user flow нэмэгдээгүй тул анхны хэрэглэгч үүсгэх боломжийг түр хадгалсан.

## Дараагийн security алхам

Service бүр дээр JWT resource server validation, ownership check, мөн admin/seed user flow нэмнэ.
