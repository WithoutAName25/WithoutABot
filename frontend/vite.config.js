import { sveltekit } from "@sveltejs/kit/vite"
import autoprefixer from "autoprefixer"
import OpenProps from "open-props"
import postcssJitProps from "postcss-jit-props"
import postcssPresetEnv from "postcss-preset-env"
import Icons from "unplugin-icons/vite"
import { defineConfig } from "vitest/config"

export default defineConfig({
  plugins: [
    sveltekit(),
    Icons({
      autoInstall: true,
      compiler: "svelte",
      defaultStyle: "vertical-align: -0.125em;",
      defaultClass: "icon",
      scale: 1,
    }),
  ],
  ssr: {
    noExternal: ["open-props"],
  },
  css: {
    postcss: {
      plugins: [
        autoprefixer(),
        postcssPresetEnv({
          stage: 1,
        }),
        postcssJitProps(OpenProps),
      ],
    },
  },
  test: {
    coverage: {
      all: true,
      src: ["src"],
      reporter: ["json", "text", "text-summary"],
    },
    include: ["tests/unit/**/*.ts"],
  },
})
