#!/usr/bin/env bash

# Run SQL commands inside the running PostgreSQL container
docker exec -i postgresml psql -U postgresml -d postgresml < users.sql