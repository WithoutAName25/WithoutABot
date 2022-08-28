import { prismaClient } from "../../scripts/client"
import type { PageServerLoad } from "./$types"

export const load: PageServerLoad = async () => {
  const statuses = await prismaClient.statuses.findMany()
  return {
    statuses,
  }
}
