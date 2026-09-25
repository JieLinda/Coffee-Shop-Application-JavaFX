# ☕ Coffee Shop Application — JavaFX & PostgreSQL

A desktop application built with **JavaFX** and **PostgreSQL** to manage transactions, memberships, and product menus for a coffee shop.  
This project demonstrates full CRUD operations, user login with role-based access, and relational data management between menus, discounts, and sales records.

---

## 🚀 Features

### 👤 User Management
- Login authentication with two user roles:
  - **MASTER (master)** → Full access to all modules  
  - **CASHIER (cashier)** → Access limited to transaction features

### 🛍️ Menu & Pricing
- Manage coffee products, cream, and add-ons  
- Each product has multiple size and price options  
- Product activation/deactivation via `status` flag

### 🎁 Discounts
- Manage discount periods and target products  
- Discounts linked dynamically to transactions and product details

### 💳 Transactions
- Create, view, and record sales transactions  
- Calculate totals, discounts, and customer change  
- Linked to customer membership points system

### 💎 Membership
- Track members, phone numbers, and reward points  
- Points automatically updated from transactions

---

## 🗃 Database

This project uses **PostgreSQL**.
The file [`export.sql`](./export.sql) contains:
- Schema definitions (tables, sequences, relationships)
- Dummy data for demo
- Default users for login

### 📥 Import Instructions

`export.sql` creates the `CoffeeDB` database itself and then connects to it, so run it
against the default `postgres` database — do **not** create `CoffeeDB` beforehand:

```bash
psql -U postgres -d postgres -f export.sql
```

On Windows `psql` lives in `C:\Program Files\PostgreSQL\<version>\bin`.
You can also import through **pgAdmin**: Query Tool → open `export.sql` → Execute.

### ⚙️ Connection Settings

Connection details live in [`src/main/resources/db.properties`](./src/main/resources/db.properties):

```properties
db.url=jdbc:postgresql://localhost:5432/CoffeeDB
db.user=postgres
db.password=postgres
```

Set `db.password` to your own PostgreSQL password. Each key can also be overridden without
editing the file, which keeps the password out of version control:

| Source | Example |
| --- | --- |
| JVM system property | `-Ddb.password=secret` |
| Environment variable | `COFFEE_DB_PASSWORD=secret` |
| `db.properties` in the working directory | takes precedence over the bundled copy |

Precedence is system property → environment variable → `db.properties`.

---

## ▶️ Running the Application

**Requirements:** JDK 21 or newer. A separate Maven install is not needed — the bundled
wrapper fetches it on first use.

```bash
./mvnw clean javafx:run      # macOS / Linux
mvnw.cmd clean javafx:run    # Windows
```

The application opens on the login screen.

### 🔑 Default Logins

| Username | Password | Role |
| --- | --- | --- |
| `master` | `master` | MASTER — full access |
| `cashier` | `cashier` | CASHIER — transactions only |

### From IntelliJ IDEA

Open the folder, wait for the Maven import to finish, then run the `javafx:run` goal from the
Maven tool window, or run the `LoginApp` class directly.

---

## 🧰 Troubleshooting

**`PKIX path building failed` while Maven downloads dependencies**
You are behind a TLS-inspecting proxy. On Windows, point Maven at the system certificate store:

```bash
set MAVEN_OPTS=-Djavax.net.ssl.trustStoreType=Windows-ROOT
```

Alternatively, import the proxy's root CA into the JDK truststore. Once the dependencies are
cached, `./mvnw -o clean javafx:run` builds offline and avoids the problem entirely.

**`FATAL: password authentication failed for user "postgres"`**
`db.password` does not match your PostgreSQL password — see *Connection Settings*.

**`FATAL: database "CoffeeDB" does not exist`**
Run the import step above.
