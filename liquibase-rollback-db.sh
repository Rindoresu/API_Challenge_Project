#!/bin/bash

set -e

COUNT=${1:-1}

echo "Rolling back $COUNT changeSet(s)..."

mvn liquibase:rollback \
  -Dliquibase.rollbackCount="$COUNT"

echo "Rollback complete."
