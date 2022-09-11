import { prismaClient } from "$lib/client"
import type { Statuses } from "$lib/prisma"
import { validateStatus } from "$lib/prisma"
import type { RequestHandler } from "./$types"
import { getErrorResponse } from "$lib/error"
import { validateBotAdmin } from "$lib/auth"

export const PATCH: RequestHandler = async ({ params, request }) => {
  await validateBotAdmin(request)
  const data = await request.json()
  const id = parseInt(params.id)
  const current = await prismaClient.statuses.findUnique({ where: { id } })

  if (current !== null) {
    let status: Statuses
    try {
      status = validateStatus({ ...current, ...data })
    } catch (e) {
      return getErrorResponse(e, 400)
    }
    const aggregations = await prismaClient.statuses.aggregate({
      _max: { id: true },
    })
    const maxId = aggregations._max.id
    if (maxId === null) return new Response("Illegal state", { status: 500 })
    if (status.id < 0) status.id = 0
    else if (status.id > maxId) status.id = maxId
    if (current.id === status.id) {
      await prismaClient.statuses.update({
        where: { id },
        data: status,
      })
    } else {
      await prismaClient.statuses.delete({ where: { id: current.id } })
      await prismaClient.statuses.updateMany({
        where: {
          id: {
            gte: Math.min(current.id, status.id),
            lte: Math.max(current.id, status.id),
          },
        },
        data: { id: { increment: current.id > status.id ? 1 : -1 } },
      })
      await prismaClient.statuses.create({ data: status })
    }
    return new Response()
  }
  return new Response(
    `Status with id ${id} does not exist yet, use "PUT /statuses" instead`,
    { status: 400 }
  )
}

export const DELETE: RequestHandler = async ({ params, request }) => {
  await validateBotAdmin(request)
  const id = parseInt(params.id)
  await prismaClient.statuses.delete({ where: { id } })
  await prismaClient.statuses.updateMany({
    where: { id: { gt: id } },
    data: { id: { decrement: 1 } },
  })
  return new Response()
}
