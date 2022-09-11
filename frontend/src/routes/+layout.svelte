<script lang="ts">
  import type { PageData } from "./$types"
  import { browser } from "$app/environment"
  import { afterNavigate, beforeNavigate } from "$app/navigation"
  import NProgress from "nprogress"
  import { page } from "$app/stores"

  export let data: PageData

  NProgress.configure({
    speed: 250,
  })
  if (browser) {
    beforeNavigate(() => {
      NProgress.start()
    })
    afterNavigate(() => {
      NProgress.done()
    })
  }
</script>

<nav>
  <a href="/">Home</a>
  {#if data.isAdmin}
    <a href="/statuses">Statuses</a>
  {/if}
  {#if data.session.user === undefined}
    <a href="/api/auth/signin/discord?redirect={$page.url.pathname}">Signin</a>
  {/if}
</nav>
<slot />
