# Cookie Auth (Spring Boot, **без** Spring Security)

Учебный проект с cookie-сессиями на чистом Spring MVC/Boot:

- `GET /my.app/api/login?username=...&password=...` — аутентификация  
  **200** → создаётся HTTP-сессия и контейнер выставляет `Set-Cookie: JSESSIONID=...`  
  **403** → неверные учётные данные
- `GET /my.app/api/auth` — авторизация  
  **200** → JSON-массив полномочий пользователя, например `["p1","p2","p3"]`  
  **403** → если нет валидной сессии

Логика вынесена в отдельный **Spring Boot Starter** (`auth-starter`), который даёт бины репозитория пользователей, хешера паролей и сервисов.


## 🔐 Пользователи по умолчанию (in-memory)

user1 / pass1
Роли: KM
Права: p1, p2, p3

user2 / pass2
Роли: KM, GKM
Права: p1, p2, p3, p4, p5

Пароли хранятся в виде, зависящем от активного PasswordHasher (по умолчанию — NoOp, т.е. «как есть»).

## 🧪 Проверка через curl / Postman / браузер
### curl (рекомендуется)
1) логин — сохраняем куки в файл

curl -i -c cookies.txt "http://localhost:8080/my.app/api/login?username=user1&password=pass1"


`ожидаем: HTTP/1.1 200 и заголовок Set-Cookie: JSESSIONID=...`
2) авторизация — отправляем те же куки
curl -i -b cookies.txt "http://localhost:8080/my.app/api/auth"
`ожидаем: HTTP/1.1 200 и тело ["p1","p2","p3"]`

#### негативные проверки
curl -i "http://localhost:8080/my.app/api/auth"                                         # без куки → 403

curl -i "http://localhost:8080/my.app/api/login?username=user1&password=WRONG"         # 403

### Postman

* GET {{baseUrl}}/my.app/api/login с Params: username=user1, password=pass1 → Send

* GET {{baseUrl}}/my.app/api/auth → Send (Postman сам подставит JSESSIONID из Cookie Jar)

### Браузер

Открой http://localhost:8080/my.app/api/login?username=user1&password=pass1, затем http://localhost:8080/my.app/api/auth.
DevTools → Application/Storage → Cookies → localhost — увидишь JSESSIONID.

## 🛠️ API
`GET /my.app/api/login`

Параметры:
* username — имя пользователя
* password — пароль

Коды
* `200 OK` — аутентификация пройдена; создаётся сессия, выставляется JSESSIONID
* `403` Forbidden — ошибка аутентификации

Пример:

GET /my.app/api/login?username=user1&password=pass1
→ 200 + Set-Cookie: JSESSIONID=...

GET /my.app/api/auth

Требуется: cookie JSESSIONID с валидной сессией

Коды

* `200 OK`— тело: ["p1","p2","p3"] (или больше для user2)

* `403` Forbidden — нет сессии/пользователь не найден