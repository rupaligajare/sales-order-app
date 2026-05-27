# Render deployment guide

This monorepo contains:

| Path | Role |
|------|------|
| `item-backend/portal/` | Spring Boot API (Java 21, Maven) |
| `sales-order-app/` | React + Vite static frontend |

Deploy **two Render services** from one GitHub repository (backend Web Service + frontend Static Site).

---

## 1. GitHub repository structure (recommended)

Keep the current layout; use **Root Directory** in Render for each service:

```
your-repo/                    # Git root (push entire React folder)
├── item-backend/
│   └── portal/               # Backend rootDir for Render
│       ├── Dockerfile
│       ├── build.sh
│       ├── pom.xml
│       └── src/
├── sales-order-app/          # Frontend rootDir for Render
│       ├── build.sh
│       ├── package.json
│       └── src/
├── render.yaml               # Optional Blueprint
└── DEPLOY_RENDER.md
```

**Do not** commit `node_modules/`, `target/`, or `.env` (secrets). Each subproject already has a `.gitignore`.

**Easiest Render integration:** one repo, two services, different `rootDir` values—no need to split into separate repos.

---

## 2. Backend (Spring Boot)

### Option A — Docker (recommended)

| Setting | Value |
|---------|--------|
| **Service type** | Web Service |
| **Root Directory** | `item-backend/portal` |
| **Environment** | Docker |
| **Dockerfile Path** | `./Dockerfile` (default) |
| **Build Command** | *(leave empty; Docker builds the image)* |
| **Start Command** | *(leave empty; uses Dockerfile `ENTRYPOINT`)* |

### Option B — Native build (no Docker)

| Setting | Value |
|---------|--------|
| **Service type** | Web Service |
| **Root Directory** | `item-backend/portal` |
| **Environment** | Java (or Shell) |
| **Build Command** | `./mvnw clean package -DskipTests` |
| **Start Command** | `java -jar target/portal-0.0.1-SNAPSHOT.jar` |

### Local build script

```bash
cd item-backend/portal
chmod +x build.sh
./build.sh
```

Artifact: `target/portal-0.0.1-SNAPSHOT.jar`

### Runtime

- Render sets `PORT`; `application.properties` uses `server.port=${PORT:8080}`.
- After deploy, note the public URL, e.g. `https://portal-api.onrender.com`.

---

## 3. Frontend (React + Vite)

### Render Static Site (recommended)

| Setting | Value |
|---------|--------|
| **Service type** | Static Site |
| **Root Directory** | `sales-order-app` |
| **Build Command** | `npm ci && npm run build` |
| **Publish Directory** | `dist` |
| **Start Command** | *(not used for static sites)* |

### Environment variable (required for production)

| Key | Example | When |
|-----|---------|------|
| `VITE_API_BASE_URL` | `https://portal-api.onrender.com` | Set in Render **before** the static site build runs |

Vite inlines `import.meta.env.VITE_API_BASE_URL` at **build time**. After changing it, trigger a **manual redeploy** of the static site.

Local development (optional):

```bash
cp .env.example .env
# edit VITE_API_BASE_URL if needed
npm run dev
```

### Local build script

```bash
cd sales-order-app
chmod +x build.sh
export VITE_API_BASE_URL=https://your-backend.onrender.com   # optional for prod-like build
./build.sh
```

---

## 4. Readiness checklist

### Repository

- [ ] Code pushed to GitHub (full monorepo root).
- [ ] `node_modules/` and `item-backend/portal/target/` are **not** tracked.
- [ ] Backend builds locally: `cd item-backend/portal && ./mvnw clean package -DskipTests`
- [ ] Frontend builds locally: `cd sales-order-app && npm ci && npm run build`

### Backend service (Render)

- [ ] Web Service created with **Root Directory** = `item-backend/portal`
- [ ] Docker **or** native build/start commands from section 2
- [ ] Service shows **Live** and responds on `/api/orders/get-pending` (GET) or your health path
- [ ] Public URL copied for frontend env var

### Frontend static site (Render)

- [ ] Static Site with **Root Directory** = `sales-order-app`
- [ ] **Build Command:** `npm ci && npm run build`
- [ ] **Publish Directory:** `dist`
- [ ] **Environment:** `VITE_API_BASE_URL` = `https://<your-backend-host>` (no trailing slash)
- [ ] Redeployed static site **after** backend URL is known
- [ ] Submit form in browser; Network tab shows POST to `https://<backend>/api/orders/create`

### CORS / API

- [ ] Backend `@CrossOrigin` allows your static site origin (current code uses `origins = "*"` for development)
- [ ] HTTPS only in production (Render provides TLS)

### Optional hardening (later)

- [ ] Restrict CORS to your static site URL instead of `*`
- [ ] Add Spring Actuator health endpoint if using Blueprint `healthCheckPath`
- [ ] Persist data (current queue is in-memory; restarts clear orders)

---

## 5. Quick reference — Render dashboard commands

### Backend (native, non-Docker)

| Field | Command |
|-------|---------|
| Root Directory | `item-backend/portal` |
| Build Command | `./mvnw clean package -DskipTests` |
| Start Command | `java -jar target/portal-0.0.1-SNAPSHOT.jar` |

### Backend (Docker)

| Field | Command |
|-------|---------|
| Root Directory | `item-backend/portal` |
| Build Command | *(empty)* |
| Start Command | *(empty)* |

### Frontend (Static Site)

| Field | Command |
|-------|---------|
| Root Directory | `sales-order-app` |
| Build Command | `npm ci && npm run build` |
| Publish Directory | `dist` |
| Env | `VITE_API_BASE_URL=https://<backend-service>.onrender.com` |

---

## 6. Deploy order

1. Deploy **backend** first; wait until Live.
2. Set `VITE_API_BASE_URL` on the **frontend** service to the backend URL.
3. Deploy **frontend** (or redeploy if env was added late).
4. Smoke-test: create an order from the static site UI.
