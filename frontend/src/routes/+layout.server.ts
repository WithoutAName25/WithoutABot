import type { LayoutServerLoad } from "./$types"
import { appAuth, isSessionAdmin } from "../lib/auth"

export const load: LayoutServerLoad = async (event) => {
  const session = await appAuth.getSession(event)
  return {
    session,
    isAdmin: isSessionAdmin(session),
  }
}
