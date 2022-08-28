export function getErrorResponse(e: unknown, errorStatusCode: number) {
  return new Response(
    e instanceof Error ? e.message : typeof e === "string" ? e : undefined,
    { status: errorStatusCode }
  )
}
