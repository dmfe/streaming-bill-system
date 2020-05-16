#!/bin/bash

for f in $(find /docker-entrypoint-initdb.d/ -type f -name "*.sql" | sort); do
    echo "($0) running $f"; psql --username "${POSTGRES_USER}" --dbname "${POSTGRES_DB}" < "$f" && echo
done