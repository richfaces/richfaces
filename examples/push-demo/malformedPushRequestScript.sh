#!/bin/bash
echo "Starting the 'long poll of death'! - $(date)"
while true
do
	curl http://localhost:8080/push/__richfaces_push?X-Atmosphere-Transport=long-polling &> /dev/null
	sleep 0.1
done
