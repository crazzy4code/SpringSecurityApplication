#!/usr/bin/env bash
set -euo pipefail

database_name="${DB_NAME:-spring_security_app}"
host="${PGHOST:-localhost}"
port="${PGPORT:-5432}"
user="${PGUSER:-postgres}"

if [[ ! "$database_name" =~ ^[a-zA-Z][a-zA-Z0-9_]*$ ]]; then
    echo "Invalid database name: use letters, digits, and underscores, starting with a letter." >&2
    exit 1
fi

if ! command -v psql >/dev/null 2>&1 || ! command -v createdb >/dev/null 2>&1; then
    echo "PostgreSQL client tools (psql and createdb) must be installed and on PATH." >&2
    exit 1
fi

if ! database_exists="$(psql \
    --host="$host" \
    --port="$port" \
    --username="$user" \
    --dbname=postgres \
    --no-align \
    --tuples-only \
    --set=database_name="$database_name" \
    --command="SELECT EXISTS (SELECT 1 FROM pg_database WHERE datname = :'database_name')")"; then
    echo "Could not check whether database '$database_name' exists." >&2
    exit 1
fi

if [[ "$database_exists" == "t" ]]; then
    echo "Database '$database_name' already exists; nothing to do."
    exit 0
fi

createdb --host="$host" --port="$port" --username="$user" "$database_name"
echo "Created database '$database_name'."
