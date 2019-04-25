#!/usr/bin/env bash
set -eu

max_attempts=${MAX_ATTEMPTS:-48}
sleep_seconds=${SLEEP_SECONDS:-10}

services="${@}"
for service in ${services}; do
    echo "Waiting for service ${service} to exit successfully..."

    container_id=''
    attempt=1
    while true; do
        sleep ${sleep_seconds}

        if [[ -z "${container_id}" ]]; then
            container_id=$(docker ps --all --quiet --filter "name=${service}")
        fi

        if [[ -n "${container_id}" && "$(docker inspect --format='{{.State.Running}}{{.State.ExitCode}}' ${container_id})" == 'false0' ]]; then
            echo "Service ${service} exited successfully."
            break
        fi

        if (( ${attempt} >= ${max_attempts} )); then
            echo "Service ${service} (container ID: ${container_id}) did not exit successfully after $(( max_attempts * sleep_seconds )) seconds."
            printf "\n[$(date)]: DOCKER CONTAINER(S) FOR ${service} FAILED TO START. DOCKER LOGS FOLLOWING:\n\n";
            for i in `ls docker-compose*.yml`; do docker-compose -f ${i} logs; done;
            exit 1
        fi

        (( attempt++ ))
    done
done
