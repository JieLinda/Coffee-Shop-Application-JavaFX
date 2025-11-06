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
You can restore the database using **pgAdmin** or the `psql` command:

```bash
createdb CoffeeDB
psql -U postgres -d CoffeeDB -f export.sql
