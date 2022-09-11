import { appAuth } from "$lib/auth"
import type { RequestHandler } from "./$types"

export const GET: RequestHandler = async (e) => {
  const res = await (appAuth.get(e) as Promise<Response>)
  return new Response(res.body ?? null, {
    status: res.status ?? 500,
    headers: res.headers ?? res.headers,
  })
}
