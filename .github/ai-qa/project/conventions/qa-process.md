# QA process conventions

Rendered by `qa-configure` on 2026-10-08 after discovery and L5 approval. These conventions cannot weaken `method/safety.md`. `★` values are framework defaults adopted by the user on 2026-10-08.

## Readiness

| Threshold / decision owner | Value | Status | Evidence/source |
|---|---|---|---|
| Six scores, RAG, ownership and recommendation | Framework rubric `method/readiness.md`; no project extension | ★ | No project readiness rules found; adopted 2026-10-08 |

## Definition of done and QA evidence

| Evidence / approval requirement | Value | Status | Evidence/source |
|---|---|---|---|
| Required FR/NFR, test, run, review and release evidence | Stable FR/NFR IDs, evidence-linked coverage, scenario decisions incl. Scenarios Not Written, 13-area regression assessment, automation rationale, actual run evidence (CI `test-results` artefact or local `target/cucumber-reports/`) or explicit Not run | ★ | `defaults/qa-process.md`; CI artefact `.github/workflows/tests.yml` L44-54; adopted 2026-10-08 |

## Test plan destination and timing

| Destination / when plan is published | Value | Status | Evidence/source |
|---|---|---|---|
| Local only | `qa-work/<work-id>/outputs/test-plan.md`; no remote destination configured | ★ | No docs provider (`integrations.md`); adopted 2026-10-08 |

## Comment templates

| Provider / template path | Value | Status | Evidence/source |
|---|---|---|---|
| None found | No work-item provider; none required | ∅ | Bounded search of repository |

## Extra regression areas

| Area | Why included | Status | Evidence/source |
|---|---|---|---|
| Cross-browser (Chrome / Edge / Firefox) | Framework supports three browsers; CI runs Chrome only | ◐ | `BrowserType.java` L6-9; `README.md` L259 |
| Environment configuration (`local` / `int` / `qa`, headless vs headed) | Behaviour differs by environment file and `-D` overrides | ◐ | `ConfigReader.java` L23-51; `environments/*.properties` |

## Fix loop

| Max iterations / scope / owner | Value | Status | Evidence/source |
|---|---|---|---|
| Framework rule | Max 3 iterations, stop early on repeat; fix test defects only in files created/modified for the work item; never touch product code, delete/skip/disable tests or loosen assertions; log to `execution.md` | ★ | `defaults/qa-process.md`; adopted 2026-10-08 |

## Commands safe to run (L3 exemptions)

| Command and selector | Environment / duration / data effects / cleanup | Status | Evidence/source |
|---|---|---|---|
| None — every test run requires L3 approval | Tests launch a real browser against an external public site | ✓ | User confirmation 2026-10-08 |

## Work-id rule

| Rule | Value | Status | Evidence/source |
|---|---|---|---|
| explicit → branch pattern → ad-hoc fallback | No ticket syntax configured, so branches do not yield a ticket; use explicit argument, otherwise `adhoc-<yyyymmdd>-<slug>` | ★ | `method/work-id-and-git.md`; `conventions/git.md` Ticket syntax ∅ |

## qa-work policy

| Commit / ignore / retention policy | Value | Status | Evidence/source |
|---|---|---|---|
| Commit `index.md` and `outputs/`; ignore everything else | Default kept, no override | ✓ | `.gitignore` L8-14 (`# ai-qa` block) |

## Scenario format

| Format | Value | Status | Evidence/source |
|---|---|---|---|
| Given / When / Then | `bdd` | ★ | No manual test cases or ticket ACs found (∅); framework default confirmed by user 2026-10-08 |

## Locale

| Locale | Value | Status | Evidence/source |
|---|---|---|---|
| English (UK) | `en-GB` | ★ | Framework candidate adopted 2026-10-08 |

## Team options

| Option | Enabled? | Status | Evidence/source |
|---|---|---|---|
| Keep Confluence page empty until testing | no | ∅ | No Confluence configured |
| Use `.sql` test data | no | ∅ | No database; test data in `.properties`/`Examples` |
| Branch description length 10–45 characters | no | ∅ | Not confirmed |

Options are not defaults; enable only with project evidence or user confirmation. Default fix loop is maximum 3 iterations, stopping early on repeat; fix test defects only and append attempts to `execution.md`.
