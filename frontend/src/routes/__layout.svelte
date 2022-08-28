<script lang="ts">
  import { browser } from "$app/env"
  import { afterNavigate, beforeNavigate } from "$app/navigation"
  import NProgress from "nprogress"

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
</nav>
<slot />
