import { prismaClient } from "$lib/client"
import type { PageServerLoad } from "./$types"
import type { Statuses } from "$lib/prisma"
import { validateBotAdmin } from "../../lib/auth"

export async function getStatuses(): Promise<Statuses[]> {
  return await prismaClient.statuses.findMany()
}

export const load: PageServerLoad = async ({ request }) => {
  await validateBotAdmin(request)
  return { statuses: await getStatuses() }
}
