/* Webstorm can't handle "export * from '.prisma/client'" in "@prisma/client"
 so we have to import it manually.*/

import { int, nullable, object, oneOf, string } from "checkeasy"

export { PrismaClient, Prisma } from "@prisma/client"

export const activityTypes = [
  "Game",
  "Streaming",
  "Listening",
  "Watching",
  "Competing",
] as const

const statusValidator = object({
  id: int({ min: 0 }),
  type: oneOf(activityTypes),
  name: string(),
  url: nullable(string()),
  duration: int({ min: 10, max: 300 }),
})

export function validateStatus(data: unknown): Statuses {
  const status: Statuses = statusValidator(data, "status")
  if (status.type === "Streaming") {
    if (status.url === null) {
      throw new Error("Streaming status must have a url")
    }
  } else {
    if (status.url !== null) {
      throw new Error("Non-streaming status must not have a url")
    }
  }
  return status
}

// Fix for Webstorm (copied from .prisma/client)
/**
 * Model InfoCommandActions
 *
 */
export type InfoCommandActions = {
  command: number
  row: number
  position: number
  url: string
  label: string
}

/**
 * Model InfoCommands
 *
 */
export type InfoCommands = {
  id: number
  server: bigint | null
  command: string
  description: string
  info: string
  ephemeral: boolean
  embed: boolean
  color: number | null
  enabled: boolean
}

/**
 * Model Statuses
 *
 */
export type Statuses = {
  id: number
  type: string
  name: string
  url: string | null
  duration: number
}
