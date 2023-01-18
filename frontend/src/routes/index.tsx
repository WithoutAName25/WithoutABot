import { component$, Resource } from "@builder.io/qwik"
import type { RequestHandler } from "@builder.io/qwik-city"
import { useEndpoint } from "@builder.io/qwik-city"
import { prisma } from "~/lib/prisma"

interface MyData {
  name: string
}

export const onGet: RequestHandler<MyData> = async () => {
  const { name } = await prisma.statuses.findUniqueOrThrow({ where: { id: 1 } })
  return {
    name,
  }
}

export default component$(() => {
  const data = useEndpoint<MyData>()
  return (
    <div>
      <Resource
        value={data}
        onPending={() => <div>Loading...</div>}
        onRejected={(error) => <div>Error: {error.message}</div>}
        onResolved={(data) => <div>Hello {data.name}</div>}
      />
    </div>
  )
})
