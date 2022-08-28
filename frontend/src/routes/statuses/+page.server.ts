import { prismaClient } from "$lib/client"
import type { PageServerLoad } from "./$types"
import type { Statuses } from "$lib/prisma"

export async function getStatuses(): Promise<Statuses[]> {
  return await prismaClient.statuses.findMany()
}

export const load: PageServerLoad = async () => {
  return { statuses: await getStatuses() }
}
