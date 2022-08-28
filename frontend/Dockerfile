FROM node:18 AS base
WORKDIR /app
RUN curl -fsSL https://bun.sh/install | bash
ENV PATH "$PATH:/root/.bun/bin"
COPY package.json bun.lockb ./
RUN bun install
COPY prisma prisma
RUN bun run prisma:generate
COPY . .

FROM base as lint
RUN bun run lint

FROM base as check
RUN bun run check

FROM base AS unittest
RUN bun run test:unit:ci

FROM base AS e2etest
RUN bun run playwright:install:withDebs
RUN bun run test:e2e

FROM base AS build
RUN bun run build

FROM node:18-slim AS final
WORKDIR /app
COPY --from=build /app/build build
COPY --from=build /app/node_modules node_modules
COPY --from=build /app/package.json .
CMD node build