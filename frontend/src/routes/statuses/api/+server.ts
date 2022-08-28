import type { RequestHandler } from "./$types"
import { getStatuses } from "../+page.server"
import type { Statuses } from "$lib/prisma"
import { validateStatus } from "$lib/prisma"
import { prismaClient } from "$lib/client"
import { getErrorResponse } from "$lib/error"

export const GET: RequestHandler = async () => {
  return new Response(JSON.stringify({ statuses: await getStatuses() }), {
    headers: { "Content-Type": "application/json" },
  })
}

export const PUT: RequestHandler = async ({ request }) => {
  const aggregations = await prismaClient.statuses.aggregate({
    _max: { id: true },
  })
  const data = await request.json()
  data.id = (aggregations._max.id ?? -1) + 1
  let status: Statuses
  try {
    status = validateStatus(data)
  } catch (e) {
    return getErrorResponse(e, 400)
  }
  await prismaClient.statuses.create({ data: status })
  return new Response()
}
