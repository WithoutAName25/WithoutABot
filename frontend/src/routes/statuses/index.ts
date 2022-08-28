import type { RequestHandlerOutput } from "@sveltejs/kit"
import { prismaClient } from "../../scripts/client"

export async function GET(): Promise<RequestHandlerOutput> {
  const statuses = await prismaClient.statuses.findMany()
  return {
    body: {
      statuses,
    },
  }
}
