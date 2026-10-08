# Git and PR conventions

Rendered by `qa-configure` on 2026-10-08 after discovery and L5 approval. Statuses per `.github/ai-qa/framework/method/discovery.md`. `★` values are framework defaults adopted by the user on 2026-10-08, not observations.

## Remote host

| Value | Status | Evidence / source |
|---|---|---|
| GitHub — `origin` `https://github.com/declankbarrett/selenium-java-skeleton.git` | ✓ | `git remote -v` (2026-10-08) |

## Base branch

| Value | Status | Evidence / source |
|---|---|---|
| `main` | ✓ | `git symbolic-ref refs/remotes/origin/HEAD` → `origin/main`; CI push trigger `.github/workflows/tests.yml` L4-5; confirmed by user 2026-10-08 |

## Protected branches

| Value | Status | Evidence / source |
|---|---|---|
| `main` treated as protected by AI-QA (no edits on default branch); repository protection rules unknown | ? | `gh` not installed; settings not readable via available tools |

## Branch patterns

| Pattern / examples | Status | Evidence / source |
|---|---|---|
| `feature\|bugfix\|chore/<ticket>-<slug>`; `<ticket>-` optional while no tracker exists, so `feature/<slug>` remains valid | ★ | Framework default adopted 2026-10-08 (user). Observed: `feature/test-application`, `feature/testing-ai-tool` (2/2 `feature/<slug>`, ◐) |

## Ticket syntax

| Pattern / confirmed project key | Status | Evidence / source |
|---|---|---|
| None — no tracker configured | ∅ | No issue keys in branches/commits; `jira.md` empty; user 2026-10-08 |

## Commit style

| Value | Status | Evidence / source |
|---|---|---|
| Conventional Commits | ★ | Framework default adopted 2026-10-08 (user). Observed history free-form (2 commits, ✗) |

## Commit types

| Types / scope rules | Status | Evidence / source |
|---|---|---|
| `feat`, `fix`, `docs`, `test`, `refactor`, `build`, `ci`, `chore`, `revert`; scope optional | ★ | `.github/ai-qa/framework/defaults/git.md` examples, adopted 2026-10-08 |

## PR title pattern

| Pattern | Status | Evidence / source |
|---|---|---|
| `<type>: <summary> (<ticket>)`; omit ` (<ticket>)` when no ticket | ★ | Framework default adopted 2026-10-08; 0 PRs exist (GitHub MCP, 2026-10-08) |

## PR types

| Types | Status | Evidence / source |
|---|---|---|
| Same as commit types | ★ | Adopted 2026-10-08 |

## Prefix-to-type mapping

| Branch prefix | PR type | Status | Evidence / source |
|---|---|---|---|
| `feature/` | `feat` (or `test` when only tests change) | ★ | Adopted 2026-10-08; confirm ambiguous cases |
| `bugfix/` | `fix` | ★ | Adopted 2026-10-08 |
| `chore/` | `chore` | ★ | Adopted 2026-10-08 |

## PR templates

| Template path / platform | Status | Evidence / source |
|---|---|---|
| No template; use body `## Summary` + `## List of Changes` | ★ | Bounded search: `.github/pull_request_template.md`, `.github/PULL_REQUEST_TEMPLATE*`, `CONTRIBUTING.md` absent; default adopted 2026-10-08 |

## Exemplar PR

| PR / why representative | Status | Evidence / source |
|---|---|---|
| None available | ∅ | GitHub MCP `list_pull_requests` state=all → 0 (2026-10-08) |

## Draft and reviewer policy

| Policy | Status | Evidence / source |
|---|---|---|
| No reviewer or draft policy found; AI-QA creates PRs as draft only after L4 approval; push is a separate L4 action | ∅ | No CODEOWNERS/CONTRIBUTING; `method/safety.md` |

Push, PR creation and external writes remain separately gated by `method/safety.md`; a branch never implies a push and the agent never merges.
