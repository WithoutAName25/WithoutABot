import { prismaClient } from "../../../scripts/client"
import { validateStatus } from "../../../scripts/prisma"
import type { RequestHandler } from "./$types"

export const PUT: RequestHandler = async ({ params, request }) => {
  const data = await request.json()
  const test = await prismaClient.statuses.findUnique({
    select: { id: true },
    where: {
      id: parseInt(params.id),
    },
  })

  try {
    const status = validateStatus(data)
    try {
      if (test === null) {
        await prismaClient.statuses.create({
          data: status,
        })
      } else {
        await prismaClient.statuses.update({
          where: {
            id: parseInt(params.id),
          },
          data: status,
        })
      }
      return new Response(undefined, { status: 201 })
    } catch (e) {
      return new Response(
        e instanceof Error ? e.message : typeof e === "string" ? e : undefined,
        { status: 500 }
      )
    }
  } catch (e) {
    return new Response(
      e instanceof Error ? e.message : typeof e === "string" ? e : undefined,
      { status: 400 }
    )
  }
}
