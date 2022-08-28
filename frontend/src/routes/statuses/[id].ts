import type { RequestEvent, RequestHandlerOutput } from "@sveltejs/kit"
import { prismaClient } from "../../scripts/client"
import { validateStatus } from "../../scripts/prisma"

export async function PUT({
  params,
  request,
}: RequestEvent<{ id: string }>): Promise<RequestHandlerOutput> {
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
      return {
        status: 201,
      }
    } catch (e) {
      return {
        status: 500,
        body:
          e instanceof Error
            ? e.message
            : typeof e === "string"
            ? e
            : undefined,
      }
    }
  } catch (e) {
    return {
      status: 400,
      body:
        e instanceof Error ? e.message : typeof e === "string" ? e : undefined,
    }
  }
}
