#!/bin/bash

set -e

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
OUTPUT_FILE="src/main/resources/db/changelog/changes/baseline.xml"

echo "Generating Liquibase baseline: $OUTPUT_FILE"

mvn liquibase:generateChangeLog \
  -Dliquibase.outputChangeLogFile="$OUTPUT_FILE"

echo "Baseline generated at: $OUTPUT_FILE"

echo "Compiling project so resources are copied to target/classes"
mvn compile

echo "Populating DB with Liquibase metadata"

mvn liquibase:changelogSync

echo "Liquibase metadata succesfully populated"
