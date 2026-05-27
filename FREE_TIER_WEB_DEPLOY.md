# Free tier deployment (GitHub web + Render web only)

No paid Render plans required. Use this guide if you are **not** using the terminal.

---

## What you need

1. A **GitHub** account (free)
2. A **Render** account (free) — sign up with GitHub at [render.com](https://render.com)

**Do not share passwords or personal access tokens in chat.**  
Share only your **public repo URL** after Step 1 (e.g. `https://github.com/yourname/sales-order-app`).

---

## Step 1 — Put code on GitHub (web)

1. Open [github.com/new](https://github.com/new)
2. Repository name: e.g. `sales-order-app`
3. Public or Private — both work with Render free tier
4. **Do not** check “Add a README”
5. Click **Create repository**

6. Click **Add file → Upload files**
7. Upload everything from your `React` folder **except**:
   - `sales-order-app/node_modules/`
   - `item-backend/portal/target/`
   - any `.env` file
8. Commit: `Initial commit — Spring Boot + React for Render`

Your repo should contain at least:

```
item-backend/portal/
sales-order-app/
render.yaml
DEPLOY_RENDER.md
.gitignore
```

---

## Step 2 — Connect Render to GitHub (web)

1. [dashboard.render.com](https://dashboard.render.com) → log in
2. **Account Settings → GitHub** → Connect
3. Allow Render access to your new repository

---

## Step 3 — Backend on Render FREE tier (web)

1. **New + → Web Service**
2. Select your repo
3. Settings:

| Field | Value |
|-------|--------|
| Name | `portal-api` |
| Region | Singapore or closest |
| Branch | `main` |
| Root Directory | `item-backend/portal` |
| Runtime | **Docker** |
| Instance Type | **Free** |
| Build Command | *(empty)* |
| Start Command | *(empty)* |

4. **Create Web Service** — first build may take 5–10 minutes
5. When **Live**, copy URL: `https://portal-api.onrender.com` (name may vary)

**Free tier note:** Backend sleeps after ~15 minutes idle. First request after sleep can take 30–60 seconds.

---

## Step 4 — Frontend on Render FREE static site (web)

1. **New + → Static Site**
2. Same repo
3. Settings:

| Field | Value |
|-------|--------|
| Name | `sales-order-app` |
| Branch | `main` |
| Root Directory | `sales-order-app` |
| Build Command | `npm ci && npm run build` |
| Publish Directory | `dist` |

4. **Environment** → Add variable:

| Key | Value |
|-----|--------|
| `VITE_API_BASE_URL` | `https://portal-api.onrender.com` |

Use **your actual backend URL** from Step 3. No trailing slash.

5. **Create Static Site**

If you created the site before setting the env var: **Manual Deploy → Clear build cache & deploy**.

**Static sites on Render free tier stay always on** (no sleep).

---

## Step 5 — Test (web)

1. Open static site URL in browser
2. Submit a test order
3. If it fails, open DevTools (F12) → Network → check POST URL

---

## Optional — Blueprint (one form, free tier)

If `render.yaml` is in the repo root:

1. **New + → Blueprint**
2. Select repo → Render reads `render.yaml` (`plan: free` for backend)
3. After backend deploys, set `VITE_API_BASE_URL` on the static site and redeploy frontend

---

## Free tier limits (good to know)

| Service | Free behavior |
|---------|----------------|
| Backend Web Service | 750 hrs/month, sleeps when idle, cold start delay |
| Frontend Static Site | Free, no sleep, good for production demos |
| Custom domain | Supported on free static sites |

For a demo or portfolio app, this setup is enough.

---

## After you create the GitHub repo

Send your repo URL in chat. I can:

- Verify the folder layout matches what Render expects
- Give you exact Render field values for your repo name
- Help troubleshoot build logs if deploy fails

I **cannot** log into github.com or render.com on your behalf — you click the buttons; I guide and debug.
