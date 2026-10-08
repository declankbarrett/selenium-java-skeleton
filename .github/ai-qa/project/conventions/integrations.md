# Integrations conventions

Rendered by `qa-configure` on 2026-10-08 after discovery and L5 approval. Skills request provider operations and remain deployment-neutral. Environment-variable names only; never secret values.

## Work items

| Field | Value | Status | Evidence/source |
|---|---|---|---|
| Provider | None | ∅ | `jira.md` empty; no tracker URL/key in repository; user chose Manual 2026-10-08 |
| Deployment | n/a | ∅ | — |
| Base URL | n/a | ∅ | — |
| Identifiers | n/a | ∅ | — |
| Transports (preferred → fallback) | Manual (user pastes title, description, acceptance criteria) | ✓ | User confirmation 2026-10-08 |
| Authentication method | n/a | ∅ | — |
| Environment variable names | n/a | ∅ | — |
| Verified | n/a | — | — |

## Docs

| Field | Value | Status | Evidence/source |
|---|---|---|---|
| Provider | None (local `README.md` only) | ∅ | No Confluence/Azure Wiki links found |
| Deployment | n/a | ∅ | — |
| Base URL | n/a | ∅ | — |
| Identifiers | n/a | ∅ | — |
| Transports (preferred → fallback) | Manual; outputs to `qa-work/<work-id>/outputs/` | ✓ | User confirmation 2026-10-08 |
| Authentication method | n/a | ∅ | — |
| Environment variable names | n/a | ∅ | — |
| Verified | n/a | — | — |

## Repository and PRs

| Field | Value | Status | Evidence/source |
|---|---|---|---|
| Provider | GitHub | ✓ | `git remote -v` |
| Deployment | github.com | ✓ | Remote URL host |
| Base URL | `https://github.com/declankbarrett/selenium-java-skeleton` | ✓ | `git remote -v`; `README.md` L40 |
| Identifiers | `declankbarrett/selenium-java-skeleton` | ✓ | `git remote -v` |
| Transports (preferred → fallback) | MCP (host-provided GitHub MCP) → Manual; `gh` CLI not installed | ◐ | MCP `list_pull_requests` read succeeded 2026-10-08; `which gh` → not found |
| Authentication method | Host session's GitHub MCP authentication | ◐ | Read succeeded; method not inspected |
| Environment variable names | None | ∅ | — |
| Verified | Yes — read only (`repo.pr.list`, 0 PRs) | ✓ | GitHub MCP, 2026-10-08 |

## CI

| Field | Value | Status | Evidence/source |
|---|---|---|---|
| Provider | GitHub Actions | ✓ | `.github/workflows/tests.yml` |
| Deployment | github.com | ✓ | Remote URL host |
| Base URL | `https://github.com/declankbarrett/selenium-java-skeleton/actions` | ◐ | Derived from repository URL |
| Identifiers | Workflow `UI Tests` (`.github/workflows/tests.yml`), artefact `test-results` | ✓ | `.github/workflows/tests.yml` L1, L48 |
| Transports (preferred → fallback) | MCP (host-provided GitHub MCP) → Manual | ◐ | GitHub MCP available; `ci.*` reads not yet probed |
| Authentication method | Host session's GitHub MCP authentication | ◐ | — |
| Environment variable names | None | ∅ | Workflow uses no secrets |
| Verified | Unverified for CI operations | ◐ | Not probed |

If a preferred transport fails, disclose the failure and fall back. Manual operations must work: the user supplies input and generated output is written under `qa-work/<work-id>/outputs/`. Every external write is L4; `.vscode/mcp.json` is L5 and is not configured.
