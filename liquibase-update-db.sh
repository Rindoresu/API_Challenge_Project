#!/bin/bash

set -e

echo "Applying Liquibase migrations..."

mvn liquibase:update

echo "Database updated successfully."
