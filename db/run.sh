# Run PostgreSQL service instead of psql
docker run \
    -d \
    --name postgresml \
    -v postgresml_data:/var/lib/postgresql \
    -p 5433:5432 \
    -p 8000:8000 \
    ghcr.io/postgresml/postgresml:2.9.3 \
    postgres