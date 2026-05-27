#!/usr/bin/env bash
# Builds the Vite production bundle (dist/).
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "==> Installing dependencies..."
npm ci

echo "==> Building frontend (npm run build)..."
npm run build

if [[ ! -d "dist" ]]; then
  echo "ERROR: dist/ directory was not created." >&2
  exit 1
fi

echo "==> Build complete: dist/"
