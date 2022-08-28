<script lang="ts">
  import Status from "./_Status.svelte"
  import type { Statuses } from "../../scripts/prisma"

  export let statuses: Statuses[]

  async function reload() {
    const response = await fetch("/statuses/__data.json")
    if (response.ok) {
      const data = await response.json()
      statuses = data.statuses
    }
  }
</script>

{#each statuses as status}
  <Status {status} on:reload={reload} />
{/each}
