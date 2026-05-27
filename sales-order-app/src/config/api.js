/**
 * API base URL for deployment. Set VITE_API_BASE_URL in Render (or .env) at build time.
 * Defaults to local Spring Boot for development.
 */
const raw = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';
export const API_BASE_URL = raw.replace(/\/$/, '');
