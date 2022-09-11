<script lang="ts">
  import type { PageData } from "./$types"
  import Status from "./Status.svelte"
  import type { Statuses } from "$lib/prisma"

  export let data: PageData
  $: statuses = data.statuses

  async function reload() {
    const response = await fetch("/statuses/api")
    if (response.ok) {
      let res = await response.json()
      data = { ...data, ...res }
    }
  }

  async function add() {
    const status: Statuses = {
      id: statuses[statuses.length - 1]?.id ?? 0,
      type: "Game",
      name: "",
      url: null,
      duration: 30,
    }
    await fetch("/statuses/api", {
      method: "PUT",
      body: JSON.stringify(status),
    })
    await reload()
  }
</script>

{#each statuses as status}
  <Status {status} on:reload={reload} />
{/each}
<p>
  <button on:click={add}>+</button>
</p>
