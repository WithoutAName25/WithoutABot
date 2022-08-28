<script lang="ts">
  import type { Statuses } from "$lib/prisma"
  import { activityTypes } from "$lib/prisma"
  import { createEventDispatcher } from "svelte"

  export let status: Statuses

  const dispatcher = createEventDispatcher()
  let error: string | undefined

  async function del() {
    await fetch(`/statuses/api/${status.id}`, { method: "DELETE" })
  }

  async function save() {
    await patch()
  }

  async function up() {
    await patch(status.id - 1)
  }

  async function down() {
    await patch(status.id + 1)
    dispatcher("reload")
  }

  async function patch(newId?: number) {
    let id = status.id
    status.id = newId ?? status.id
    const response = await fetch(`/statuses/api/${id}`, {
      method: "PATCH",
      body: JSON.stringify(status),
    })
    if (response.ok) {
      error = undefined
      dispatcher("reload")
    } else {
      error = await response.text()
    }
  }
</script>

<p>
  <select bind:value={status.type}>
    {#each activityTypes as type}
      <option value={type}>{type}</option>
    {/each}
  </select>
  <input type="text" bind:value={status.name} />
  {#if status.type === "Streaming"}
    <input type="text" bind:value={status.url} />
  {/if}
  <input type="number" min="10" max="300" bind:value={status.duration} />
  <button on:click={up}>Up</button>
  <button on:click={down}>Down</button>
  <button on:click={save}>Save</button>
  <button on:click={del}>Delete</button>
  {#if error}
    <span class="error">{error}</span>
  {/if}
</p>
