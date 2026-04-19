# 🚀 Spring Security OAuth2: Gateway + Resource Server (Google SSO)

Демонстрационный проект, показывающий интеграцию **Google SSO (OpenID Connect)** с использованием:

- API Gateway (OAuth2 Client)
- Resource Server (JWT validation)

Без собственного Authorization Server.

## Architecture

```
┌─────────────────┐         ┌─────────────────────┐
│ service-with-   │───────> │ service-that-need-  |
│ oauth (OAuth2   │  JWT    │ auth (Resource      │
│ Client/Gateway) │         │ Server)             │
└─────────────────┘         └─────────────────────┘
```

- **service-with-oauth**: OAuth2 Client, который выполняет аутентификацию через Google и передает JWT токены
- **service-that-need-auth**: Resource server, который валидирует JWT токены из входящих запросов

## Technology Stack

- Spring Boot 3.5.13
- Spring Cloud Gateway
- Spring Security OAuth2 Client
- Spring Security OAuth2 Resource Server
- Java 17

## Build

```bash
mvn clean package
```

## Запуск

### Настройка OAuth2 credentials

Отредактируйте файл `.env` с вашими Google OAuth2 credentials:
```
client_id=your-client-id
client_secret=your-client-secret
```

### Запуск сервисов

```bash
# Запуск service-that-need-auth (port 8081)
mvn -pl service-that-need-auth spring-boot:run

# Запуск service-with-oauth (port 8080)
mvn -pl service-with-oauth spring-boot:run
```

## Поток выполнения

1. Пользователь посещает `http://localhost:8080`
2. Происходит перенаправление на Google для аутентификации
3. После логина пользователь возвращается с authorization code
4. Сервис обменивает code на access token
5. Последующие запросы к `http://localhost:8081` включают JWT токен
6. Resource server валидирует токен и обрабатывает запрос
