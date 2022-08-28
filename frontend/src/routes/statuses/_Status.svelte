<script lang="ts">
  import type { Statuses } from "../../scripts/prisma"
  import { activityTypes } from "../../scripts/prisma"
  import { createEventDispatcher } from "svelte"

  export let status: Statuses

  const dispatcher = createEventDispatcher()
  let error: string

  async function save() {
    const response = await fetch("/statuses/" + status.id, {
      method: "PUT",
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
  <button on:click={save}>Save</button>
  {#if error}
    <span class="error">{error}</span>
  {/if}
</p>
