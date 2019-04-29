#!/bin/sh
# startup.sh - startup script for the UI docker image

echo "Starting Conductor UI"

# Start the UI
cd /app/ui/dist
if [ -z "$WF_SERVER" ];
  then
    export WF_SERVER=http://conductor-server.service.imanagecloud.com:8080/api/
  else
    echo "using Conductor API server from '$WF_SERVER'"
fi

node server.js
