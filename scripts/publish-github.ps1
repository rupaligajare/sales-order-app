# Creates GitHub repo and pushes code. Run after: gh auth login
$ErrorActionPreference = "Stop"
$gh = "C:\Program Files\GitHub CLI\gh.exe"
$repoRoot = Split-Path $PSScriptRoot -Parent

Set-Location $repoRoot

& $gh auth status
if ($LASTEXITCODE -ne 0) {
    Write-Host "Not logged in. Run: gh auth login" -ForegroundColor Yellow
    exit 1
}

$repoName = "sales-order-app"
Write-Host "Creating public repo: $repoName ..."
& $gh repo create $repoName --public --source=. --remote=origin --push

if ($LASTEXITCODE -eq 0) {
    $url = & $gh repo view --json url -q .url
    Write-Host "Done: $url" -ForegroundColor Green
}
