import { SvelteKitAuth } from "sk-auth"
import { OAuth2Provider } from "sk-auth/providers"
import { env } from "$env/dynamic/private"
import type { APIGuild } from "./discord"
import { discordApi } from "./discord"
import { error } from "@sveltejs/kit"

const manageGuild = 0x20
const administartor = 0x8

const botAdmins = new Set<string>(env.ADMINS.split(","))

export const appAuth = new SvelteKitAuth({
  providers: [
    new OAuth2Provider({
      id: "discord",
      clientId: env.DISCORD_CLIENT_ID,
      clientSecret: env.DISCORD_CLIENT_SECRET,
      accessTokenUrl: `${discordApi}/oauth2/token`,
      authorizationUrl: `${discordApi}/oauth2/authorize`,
      scope: ["identify", "guilds"],
      grantType: "authorization_code",
      contentType: "application/x-www-form-urlencoded",
      profileUrl: `${discordApi}/users/@me`,
      profile: async (profile, tokens) => {
        const response = await fetch(`${discordApi}/users/@me/guilds`, {
          headers: {
            Authorization: `${tokens.token_type} ${tokens.access_token}`,
          },
        })
        const guilds: APIGuild[] = await response.json()
        return {
          ...profile,
          guilds: guilds.filter((guild) => {
            const permissions = parseInt(guild.permissions ?? "0")
            return (
              guild.owner ||
              (permissions & administartor) === administartor ||
              (permissions & manageGuild) === manageGuild
            )
          }),
        }
      },
    }),
  ],
  basePath: "/api/auth",
  protocol: "http",
  host: "localhost:5173",
  jwtSecret: env.JWT_SECRET,
})

export function isSessionAdmin(session: any): boolean {
  const id = session?.user?.id
  return id !== undefined && botAdmins.has(id)
}

export async function isAdmin(request: Request) {
  const session = await appAuth.getSession({ request })
  return isSessionAdmin(session)
}

export async function validateBotAdmin(request: Request) {
  if (!(await isAdmin(request)))
    throw error(403, "You don't have admin permissions")
}
