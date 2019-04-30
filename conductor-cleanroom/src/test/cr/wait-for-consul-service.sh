#!/usr/bin/env bash

trace() {
  echo "[$(date)]: $1"
}

if [ $# -ne 2 ]; then
  trace "Expected 2 arguments.  1) The service. 2) The timeout"
  exit 1
fi

SERVICE=$1
TIMEOUT=$2
status=""

COUNTER=$TIMEOUT
while [ $COUNTER -gt 0 ]; do
  let mod=COUNTER%5
  if [ $mod -eq 0 ]; then
    trace "Waiting for ${SERVICE} to become available in Consul.  Current status=${status}.  Timeout in ${COUNTER} seconds"
  fi

  status=$(curl -s http://consul.service.imanagecloud.com:8500/v1/health/checks/${SERVICE} | jq -r .[0].Status)
#  trace "status='${status}'"
  if [ "z${status}" = "zpassing" ]; then
    trace "${SERVICE} is available"
    exit 0
  fi

  sleep 1

  let COUNTER=COUNTER-1
done

trace "${SERVICE} failed to become available in Consul after ${TIMEOUT} seconds."
exit 1
