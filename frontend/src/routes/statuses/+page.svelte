<script lang="ts">
  import type { PageData } from "./$types"
  import Status from "./_Status.svelte"

  export let data: PageData

  async function reload() {
    const response = await fetch("/statuses/__data.json")
    if (response.ok) {
      data = await response.json()
    }
  }
</script>

{#each data.statuses as status}
  <Status {status} on:reload={reload} />
{/each}
