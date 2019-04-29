#!/bin/bash
set -e


# Negotiate the Vault permanent token
. /vault-scripts/set-perm-token


java -cp /usr/local/share/workflow/workflow-client.jar  com.imanage.workflow.client.ClientMain
