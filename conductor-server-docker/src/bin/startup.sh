#!/bin/sh
# startup.sh - startup script for the server docker image

echo "Starting Conductor server"

# Start the server
cd /app/libs
echo "Property file: $CONFIG_PROP"
echo $CONFIG_PROP
export config_file=

if [ -z "$CONFIG_PROP" ];
  then
    echo "Using an in-memory instance of conductor";
    export config_file=/app/config/config-local.properties
  else
    echo "Using '$CONFIG_PROP'";
    export config_file=/app/config/$CONFIG_PROP
fi

if [ -z "LOG4J_CONFIG" ];
   then
        echo "Using an using default log4j configuration";
        export log4jconfig=
   else
       echo "Using log4j config file : '$LOG4J_CONFIG'"
       export log4jconfig=/app/config/$LOG4J_CONFIG
fi

java -jar conductor-server-*-all.jar $config_file $log4jconfig
