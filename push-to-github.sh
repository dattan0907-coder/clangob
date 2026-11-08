#!/usr/bin/env bash
# Usage: ./push-to-github.sh <github-user> <repo-name>
# Needs: git, and a EMPTY repo already created on github.com (no README/license).
set -e
USER="${1:?github user}"; REPO="${2:-goblin-clan-neoforge-1.21.1}"
[ -d .git ] || git init -b main
git add -A
git commit -m "Goblin Clan + Shrek (NeoForge 1.21.1, GeckoLib)" || true
git remote remove origin 2>/dev/null || true
git remote add origin "https://github.com/$USER/$REPO.git"
git push -u origin main
echo "Done. Open https://github.com/$USER/$REPO/actions to watch the build."
