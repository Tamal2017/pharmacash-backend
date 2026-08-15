# Pharmacash Backend

This repository contains the backend for the Pharmacash service (Spring Boot).

Checklist
- [x] Add `README.md` to the repository root
- [x] Add an "IAM" section with Keycloak realm, clients and roles details (based on `iam/keycloak/pharma-cash-keycloak.json`)

Summary

The application uses Keycloak for identity and access management. A Keycloak realm export is provided in `iam/keycloak/pharma-cash-keycloak.json` and the project includes a docker-compose file to run a local Keycloak instance for development.

Important paths
- Realm export: `iam/keycloak/pharma-cash-keycloak.json`
- Local docker-compose for Keycloak: `iam/docker-compose.yaml`

Running Keycloak locally (development)

1. Provide an admin password env var for the docker compose file:

   In PowerShell (example):

   $env:KEYCLOAK_ADMIN_PASSWORD = "your-admin-password";
   docker compose -f iam/docker-compose.yaml up

2. The docker-compose service uses Keycloak start-dev with `--import-realm` and mounts `iam/keycloak/` into `/opt/keycloak/data/import/` so the realm will be imported automatically on startup.

IAM (Keycloak) — overview (derived from `pharma-cash-keycloak.json`)

Realm
- Name: `pharma-cash-keycloak`
- Realm defaults include default roles `default-roles-pharma-cash-keycloak` (composite roles include `offline_access` and `uma_authorization`).

Top-level realm roles
- `ADMIN` — Administrator of the dedicated app or a manager
- Built-in roles such as `offline_access`, `uma_authorization` and the default composite role `default-roles-pharma-cash-keycloak` are present.

Clients of interest (high level)
- `pharma-cash-admin-client`
  - Client id: `pharma-cash-admin-client`
  - Name: `admin-rest-api-client`
  - Purpose: server-to-server admin operations (Keycloak Admin REST API)
  - `serviceAccountsEnabled`: true (this client has a service account user: `service-account-pharma-cash-admin-client`)
  - Use this client for programmatic Keycloak administration (the application uses it via `KeycloakProperties`)

- `pharma-cash-keycloak-client-service` (a.k.a. pharma-cash-service)
  - Client id: `pharma-cash-keycloak-client-service`
  - Name: `pharma-cash-service`
  - Purpose: back-end service / resource server client
  - Typically used in `spring.security.oauth2.client.registration.keycloak.client-id`

- `pharma-cash-service` / other built-in clients
  - There are several other Keycloak built-in clients (account, account-console, admin-cli, realm-management, etc.) — see the JSON for a full list.

Service account user
- `service-account-pharma-cash-admin-client` — associated with `pharma-cash-admin-client` (this is created when service accounts are enabled for that client).

Secrets
- The realm JSON in the repository masks secrets with `**********` in several places. Real client secrets will be visible after importing the realm into Keycloak or by viewing the client in the Keycloak admin console.

Application configuration

The Spring Boot application expects Keycloak admin API properties under `keycloak.admin-api` (see `KeycloakProperties`):

Notes and recommendations
- The included `iam/docker-compose.yaml` will import the provided realm automatically. Set the environment variable `KEYCLOAK_ADMIN_PASSWORD` before starting the container.
- If you re-import the realm into an existing Keycloak with different client secrets, update the `application.yaml` values with the new client secrets.
- Secrets are sensitive — do not commit real secrets to source control. Use environment variables or a secrets manager in CI/CD.
- To inspect or edit clients/roles after importing, log in to the Keycloak admin console (default credentials set by `KEYCLOAK_ADMIN` and `KEYCLOAK_ADMIN_PASSWORD` environment variables in the docker-compose file).

Where to look in the codebase
- Keycloak admin bean: `src/main/java/com/jo/dev/pharmacash/api/config/KeycloakConfig.java`
- Keycloak admin properties binding: `src/main/java/com/jo/dev/pharmacash/api/config/KeycloakAdminProperties.java`
- Application YAML example: `src/main/resources/application.yaml`
- Realm JSON to import: `iam/keycloak/pharma-cash-keycloak.json`
- Local Keycloak docker compose: `iam/docker-compose.yaml`
