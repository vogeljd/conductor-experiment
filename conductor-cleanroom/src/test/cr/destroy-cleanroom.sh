#!/usr/bin/env bash

echo "Destroying cleanroom...";
for f in `ls docker-compose*.yml`; do
    docker-compose -f ${f} down --volumes;
done
echo "done destroying cleanroom."
