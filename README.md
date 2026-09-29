# Event-server

## Run locally

Start the server in PowerShell:

```powershell
.\gradlew.bat bootRun
```

When `APP_AUTH_TOKEN_SECRET` is not configured, the server generates a cryptographically random signing key at startup. Tokens last 24 hours but become invalid when the server restarts, so users need to log in again. For deployments that need tokens to survive restarts or run across multiple server instances, configure the same private `APP_AUTH_TOKEN_SECRET` value on each instance (at least 32 bytes). Creating an event requires `Authorization: Bearer <token>`; the server uses the verified account ID as the event owner. `GET /api/events` remains public and lists events from all accounts.
