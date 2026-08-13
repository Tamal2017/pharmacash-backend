# Pharmacash Backend - Flyway Migrations

This project uses Flyway for database schema management. Migration files are organized by database type to support both H2 (in-memory for development/testing) and PostgreSQL (for production).

## Directory Structure

```
src/main/resources/db/migration/
├── h2/
│   └── V1__Initial_schema.sql      # H2 migrations
└── postgres/
    └── V1__Initial_schema.sql      # PostgreSQL migrations
```

## Running with Different Databases

### H2 (Default - In-Memory)

H2 is the default profile. To run with H2:

```bash
mvn spring-boot:run
# or
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=h2"
```

**Connection Details:**
- URL: `jdbc:h2:mem:pharmacash;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL`
- Driver: `org.h2.Driver`
- Username: `sa`
- Password: (empty)
- H2 Console: http://localhost:8081/h2-console

### PostgreSQL

To run with PostgreSQL:

1. **Start PostgreSQL Server**
   ```bash
   # macOS (if using Homebrew)
   brew services start postgresql
   
   # Windows (if installed as service)
   # PostgreSQL service should be running
   
   # Or start with Docker
   docker run --name pharmacash-postgres \
     -e POSTGRES_PASSWORD=postgres \
     -e POSTGRES_DB=pharmacash \
     -p 5432:5432 \
     -d postgres:15
   ```

2. **Create Database** (if not using Docker)
   ```sql
   psql -U postgres
   CREATE DATABASE pharmacash;
   ```

3. **Run Application with PostgreSQL Profile**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=postgres"
   ```

**Connection Details:**
- URL: `jdbc:postgresql://localhost:5432/pharmacash`
- Driver: `org.postgresql.Driver`
- Username: `postgres`
- Password: `postgres`

## Migration Files

### V1__Initial_schema.sql

Creates the initial database schema with all tables:

- `pharmacies` - Pharmacy branch information
- `suppliers` - Vendor/supplier information
- `medicines` - Drug details with pricing and expiry dates
- `inventory` - Stock levels per medicine per pharmacy
- `employees` - Staff information linked to pharmacies
- `customers` - Patient/customer information with allergies
- `orders` - Purchase orders from suppliers
- `order_medicine` - Line items in purchase orders
- `prescriptions` - Prescriptions issued to customers
- `prescription_medicine` - Line items in prescriptions
- `transactions` - Sales records/transactions

All tables include appropriate:
- Primary keys with auto-increment IDs
- Foreign key constraints
- Indexes on foreign keys and common query columns
- Unique constraints (e.g., pharmacy + medicine combination in inventory)

## Database-Specific Configuration

Configuration is split into profiles:

- **application.yaml** - Default (H2) configuration
- **application-h2.yaml** - Explicit H2 configuration
- **application-postgres.yaml** - PostgreSQL configuration

### Key Differences

| Aspect | H2 | PostgreSQL |
|--------|----|----|
| JDBC URL | `jdbc:h2:mem:pharmacash` | `jdbc:postgresql://localhost:5432/pharmacash` |
| Driver | `org.h2.Driver` | `org.postgresql.Driver` |
| Hibernate Dialect | `H2Dialect` | `PostgreSQLDialect` |
| Auto-Increment | `AUTO_INCREMENT` | `BIGSERIAL` |
| DDL Auto | `validate` | `validate` |

## Flyway Configuration

Flyway is configured to:
- **Look for migrations** in database-specific directories (via Spring profiles)
- **Use baseline-on-migrate: true** - Creates baseline if schema doesn't exist
- **Validate database matches migrations** - `ddl-auto: validate` ensures schema consistency

## Testing

H2 migrations are used for unit and integration tests (in-memory database is faster and isolated).

## Adding New Migrations

1. Create a new SQL file in the appropriate directory:
   - `src/main/resources/db/migration/h2/V[number]__[description].sql` for H2
   - `src/main/resources/db/migration/postgres/V[number]__[description].sql` for PostgreSQL

2. Version number should increment (V2, V3, etc.)
3. Description should be descriptive but concise (underscores instead of spaces)

Example:
```
V2__Add_audit_columns.sql
V3__Create_audit_tables.sql
```

## Troubleshooting

**Issue: "Flyway migration failed"**
- Check that the migration files are in the correct directory
- Verify the SQL syntax is compatible with your database
- Ensure database credentials are correct

**Issue: "Table already exists"**
- If migrating from manual DDL, run: `mvn flyway:baseline` first
- Or manually create the flyway_schema_history table

**Issue: "Permission denied" (PostgreSQL)**
- Ensure the PostgreSQL user has CREATE DATABASE and CREATE TABLE permissions
- Run migrations with a superuser or grant permissions to the user

## References

- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Spring Boot with Flyway](https://spring.io/guides/gs/accessing-data-mysql/)
- [H2 Database](http://www.h2database.com/)
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/)

