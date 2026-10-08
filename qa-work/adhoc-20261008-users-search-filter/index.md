---
work-id: "adhoc-20261008-users-search-filter"
skill: "qa-workflow"
framework-version: "1.0.0"
created: "2026-10-08T20:02:00Z"
inputs:
  - source: "Ticket pasted in chat — 'Search and filter users on the Users list' (no key; jira.md empty)"
    revision: "2026-10-08T19:29:20Z"
  - source: "selenium-java-skeleton @ feature/testing-ai-tool"
    revision: "22f1311"
  - source: "selenium-java-skeleton @ feature/test-application"
    revision: "9765c38"
  - source: "test-application @ master / origin/ai-testing"
    revision: "ab7858a / a756504"
---

# QA Work Index

**Scope:** Story "Search and filter users on the Users list" (FR1–FR14, NFR1). Ad-hoc work ID — no ticket key supplied.
**Branch / revision:** design artefacts and generated tests on `feature/testing-ai-tool` @ 22f1311 (uncommitted, by user choice — no new branch); reference suite `feature/test-application` @ 9765c38; product `test-application` @ ab7858a
**Workflow:** full
**Updated:** 2026-10-08T21:00:00Z

## Workflow step status and staleness

| Workflow | Step | Status | Artefact | Inputs checked / latest revision | Staleness / note |
|---|---|---|---|---|---|
| design | Analyse requirement | Complete | [requirement.md](requirement.md) | ticket 2026-10-08T19:29Z | fresh |
| design | Code context / branch verification | Complete | [context.md](context.md) | app ab7858a (all branches); suite 9765c38 | fresh — feature **Missing** on all app branches |
| design | Requirement coverage gaps | Complete | [coverage.md](coverage.md) | ab7858a / a756504 / 9765c38 | fresh — Insufficient |
| design | Design manual scenarios | Complete (draft, rev. 20:15Z) | [design.md](design.md) | requirement + coverage | fresh |
| design | Regression risk | Complete | [regression.md](regression.md) | requirement + context | fresh — becomes stale when an implementation diff exists |
| design | Automation plan | Complete (draft) | [automation.md](automation.md) | design + regression | fresh |
| design | Design review | Complete | [review.md](review.md) | design artefacts 20:00Z | Needs Improvement → all 13 findings dispositioned; design.md/automation.md revised 20:15Z |
| design | Final test plan | Complete | [outputs/test-plan.md](outputs/test-plan.md) | all design artefacts 20:15Z | fresh; final |
| automate | Generate tests | Complete | see Test files below | design rev. 20:15Z; branch 22f1311 | fresh — `mvn test-compile` passes |
| automate | Code review | Complete | [review.md](review.md#code-review-qa-review-tests-code-mode--2026-10-08) | generated tests | Approve with changes → C1, C2 fixed and recompiled |
| automate | Run tests | Not run (user declined L3) | [execution.md](execution.md) | — | Run when the feature is deployed locally |
| automate | Analyse failures / fix loop | Not applicable | — | — | no run |
| automate | Update index and plan | Complete | index.md, outputs/test-plan.md | — | fresh |
| full | Design checkpoint / approval | Complete | Gate log | design rev. 20:15Z | approved |

## Traceability matrix

| FR/NFR | Existing evidence (test/assertion or gap) | Scenarios | Automation decision | Test files | Last result |
|---|---|---|---|---|---|
| FR1 search text | Gap — none | S01, S07 | Assessed: automate E2E + API | `src/test/resources/features/users-search.feature` @S01; `src/test/resources/features/users-search-api.feature` @S07 | Not run |
| FR2 full name | Gap — none | S02, S07 | Assessed: automate E2E + API | `src/test/resources/features/users-search.feature` @S02; `src/test/resources/features/users-search-api.feature` @S07 | Not run |
| FR3 position filter | Gap — none | S03, S07 | Assessed: automate E2E + API | `src/test/resources/features/users-search.feature` @S03; `src/test/resources/features/users-search-api.feature` @S07 | Not run |
| FR4 position options | Gap — none | S03 | Assessed: automate E2E | `src/test/resources/features/users-search.feature` @S03 | Not run |
| FR5 combined | Gap — none | S04, S06, S07 | Assessed: automate E2E + API | `src/test/resources/features/users-search.feature` @S04 @S06; `src/test/resources/features/users-search-api.feature` @S07 | Not run |
| FR6 no-results message | Gap — none | S05 | Assessed: automate E2E | `src/test/resources/features/users-search.feature` @S05 | Not run |
| FR7 Clear | Gap — none | S04 | Assessed: automate E2E | `src/test/resources/features/users-search.feature` @S04 | Not run |
| FR8 debounce | Gap — none | S01 (manual timing check) | Assessed: E2E for "no Enter"; exact 300 ms deliberately not automated (flaky) — manual in S01 + dev unit | `src/test/resources/features/users-search.feature` @S01 (typing without Enter only) | Not run |
| FR9 actions while filtered | Partial — users.feature "View a user's details" (unfiltered, 9765c38) | S06 | Assessed: automate E2E | `src/test/resources/features/users-search.feature` @S06 | Not run |
| FR10 API params / no-param | Partial — users-resource.test.js no-param 200 (mocked, origin/ai-testing) | S07, S09 | Assessed: automate API (S07); S09 manual regression | `src/test/resources/features/users-search-api.feature` @S07 | Not run |
| FR11 200 [] | Partial — users-resource.test.js 200 [] (mocked) | S05, S07, S08 | Assessed: automate API | `src/test/resources/features/users-search-api.feature` @S05 @S07 @S08 | Not run |
| FR12 /users/positions | Gap — live 404 | S03, S07 | Assessed: automate API | `src/test/resources/features/users-search-api.feature` @S03 @S07 | Not run |
| FR13 UI sends params | Gap — none | S01 | Assessed: automate E2E (rendered rows = API response) | `src/test/resources/features/users-search.feature` @S01 (listed = API result) | Not run |
| FR14 readme | Gap — not updated | — | Deliberately not automated — PR review | — | n/a |
| NFR1 safe input | Gap — none (existing unit asserts interpolated SQL) | S08 | Assessed: automate API (real DB) + 1 UI example; dev unit recommended | `src/test/resources/features/users-search-api.feature` @S08; `src/test/resources/features/users-search.feature` @S08 | Not run |

## QA Summary

| Ticket | Readiness | Max risk | Coverage verdict | Scenarios written/not written | Automated/manual/not needed | Run result | Published |
|---|---|---|---|---|---|---|---|
| adhoc-20261008-users-search-filter | Amber | HIGH | Insufficient | 9 / 10 | 8 automated (S01–S08: 15 UI + 17 API scenario runs), 1 manual (S09), FR8 timing manual, FR14 PR review | Not run | Not published |

## Open questions

- Q1–Q10 in [requirement.md](requirement.md) (owner: Product Owner / developer). Most urgent: Q1 lost escape character, Q9 element IDs.
- Run the `@search` tests once the feature is deployed to the local Docker app — owner: user/developer.
- CI cannot host the app — owner: team.
- Drift: `project.md` still describes Sauce Demo — suggest `qa-configure refresh`.

## Gate log

| Gate | Action / target | Exact payload or command / side effects | Approver and time | Outcome / ID or URL |
|---|---|---|---|---|
| Workflow plan | full workflow; L1 writes under `qa-work/adhoc-20261008-users-search-filter/` on `feature/testing-ai-tool` | Design artefacts only; automate steps separately gated | User — "Approve – proceed with design", 2026-10-08T19:3xZ (+01:00 local) | Approved |
| Design approval | Finalise test plan and continue to automate | outputs/test-plan.md (draft rev. 20:15Z) | User — "Approve design – finalise and continue to automation", 2026-10-08 ~20:25 +01:00 | Approved; plan marked Final |
| Branch choice | Test location | User: "Just do it on this branch please" — tests on `feature/testing-ai-tool`; no branch created (no L2 action) | User, 2026-10-08 ~20:35 +01:00 | Proposed `feature/users-search-filter-tests` not created |
| L3 test run | `mvn -B -ntp test -Dcucumber.filter.tags="@search"` against local Docker app | S06 creates/deletes 2 users per scenario; reports in target/ | User — "Skip the run – finish with 'Not run'", 2026-10-08 ~21:00 +01:00 | Declined — not run |
