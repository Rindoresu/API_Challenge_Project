#!/bin/bash

set -e

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
OUTPUT_FILE="src/main/resources/db/changelog/changes/${TIMESTAMP}_diff.xml"

echo "Generating Liquibase diff-based migration: $OUTPUT_FILE"

echo "Remember this action requires liquibase secure, paid version"

mvn liquibase:diff-changelog \
  -Dliquibase.changelog-file="$OUTPUT_FILE"

echo "Migration generated at: $OUTPUT_FILE"
