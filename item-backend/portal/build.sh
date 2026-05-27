#!/usr/bin/env bash
# Builds a production-ready Spring Boot executable JAR.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "==> Building portal (mvn clean package)..."
./mvnw clean package -DskipTests

JAR_PATH="target/portal-0.0.1-SNAPSHOT.jar"
if [[ ! -f "$JAR_PATH" ]]; then
  echo "ERROR: Expected artifact not found: $JAR_PATH" >&2
  exit 1
fi

echo "==> Build complete: $JAR_PATH"
