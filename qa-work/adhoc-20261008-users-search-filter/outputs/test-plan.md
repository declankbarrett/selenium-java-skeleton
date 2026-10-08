---
work-id: "adhoc-20261008-users-search-filter"
skill: "qa-test-plan"
framework-version: "1.0.0"
created: "2026-10-08T20:20:00Z"
inputs:
  - source: "Ticket pasted in chat — 'Search and filter users on the Users list'"
    revision: "2026-10-08T19:29:20Z"
  - source: "qa-work/adhoc-20261008-users-search-filter/{requirement,context,coverage,design,regression,automation,review}.md"
    revision: "2026-10-08T20:15Z"
  - source: "selenium-java-skeleton feature/test-application; test-application master"
    revision: "9765c38; ab7858a"
---

# Test Plan — Search and filter users on the Users list

**Author:** QA (AI-assisted)
**Date:** 2026-10-08
**Project:** Test Application (Users/Projects) — QA suite `declankbarrett/selenium-java-skeleton`
**Scope:** Single story (ad-hoc work ID `adhoc-20261008-users-search-filter`; no ticket key)
**Status:** Final (design approved by user 2026-10-08 ~20:25 +01:00) — run results to be added after automation

## Summary

Adds free-text search (name, surname, email, full name) and a Position filter to the Users list, backed by optional `search`/`position` query parameters on `GET /users` and a new `GET /users/positions`. Test scope: UI journey, API contract, safe input (SQL injection / wildcards) and regression of other `GET /users` consumers. Out of scope: pagination, sorting, saved searches, Projects-page search. **The feature is not yet implemented** (verified on all app branches and the running stack, 2026-10-08).

## QA Summary

| Ticket | Readiness | Max risk | Coverage verdict | Scenarios written/not written | Automated/manual/not needed | Run result | Published |
|---|---|---|---|---|---|---|---|
| adhoc-20261008-users-search-filter | Amber | HIGH | Insufficient | 9 / 10 | 8 automated (S01–S08: 15 UI + 17 API scenario runs), 1 manual (S09); FR8 timing manual; FR14 PR review | Not run (L3 declined) | Not published |

## Risk Assessment

Impact 4 × Criticality 5 = 20, +5 (new code, no tests on app master) → 25, **Must Test**. Key risks: SQL injection on an unauthenticated API (existing queries interpolate values); `/users/:id` swallowing `/users/positions`; backward compatibility for four consumers of `GET /users`; stale async results with debounce; shared DB with leftover data. Highest regression area rating: HIGH.

## Requirements Breakdown


| ID | Requirement (testable outcome) | Source | Level |
|---|---|---|---|
| FR1 | Typing text filters the Users table to users whose name, surname or email contains the text; case-insensitive, partial match | AC1 | UI + API |
| FR2 | Searching "name surname" (e.g. "anna nowak") returns that user | AC2; tech note `name \|\| ' ' \|\| surname` | UI + API |
| FR3 | Selecting a position shows only users with that position | AC3 | UI + API |
| FR4 | Position dropdown lists each existing position once, alphabetically, default "All positions" | AC4 | UI |
| FR5 | Search text and position combine with AND | AC5 | UI + API |
| FR6 | When nothing matches, the table shows "No users match your search" instead of an empty table | AC6 | UI |
| FR7 | "Clear" resets search text and position; all users shown | AC7 | UI |
| FR8 | Results update ~300 ms after typing stops; no Enter required | AC8 | UI |
| FR9 | View, Update and Remove work on filtered rows; after Remove the current search and filter stay applied | AC9 | UI |
| FR10 | GET /users?search=&position= → 200 with matching users; both optional; no params → all users (unchanged) | AC10 | API |
| FR11 | Query with no matches → 200 `[]`, not 404 | AC11 | API |
| FR12 | GET /users/positions returns the distinct positions for the dropdown | Tech note "Positions" | API |
| FR13 | Frontend `getAllUsers()` sends current search/filter as query params (server-side filtering) | Tech note "Frontend" | UI→API |
| FR14 | readme.md Endpoints section documents the new params and endpoint | Tech note "Docs" | Docs |
| NFR1 | Security: search values containing `'`, `%`, the lost escape character (Q1) and SQL such as `' OR 1=1 --` are treated as plain text — no error, no unexpected rows; parameterised queries ($1, $2) with escaped wildcards | AC12; tech notes | Security |

No performance, logging or environment-specific NFRs are specified ("none specified"); see Q10.

**Edge cases:** duplicate full names (2× Jan Kowalski, different positions); email-only match; mixed case; whitespace; empty param; `%`/`_`/`\` wildcards; quotes; stacked-statement injection; unknown position; repeated params; a position emptied by a remove; removing a project owner fails (pre-existing: `owner INT NOT NULL` + `ON DELETE SET NULL`, create-db.sql L25-30) — AC9 tests must use a non-owner.

**Flags:** none (no flag mechanism — bounded search of backend/ and frontend/). **Integration points:** frontend jQuery → Express API → Postgres.

**Out of scope (ticket):** pagination, sorting, saved searches, Projects-page search.

## Traceability Matrix

| FR/NFR | Existing evidence (test/assertion or gap) | Scenarios | Automation decision | Test files | Last result |
|---|---|---|---|---|---|
| FR1 search text | Gap — none | S01, S07 | Assessed: automate E2E + API | `src/test/resources/features/users-search.feature` @S01; `src/test/resources/features/users-search-api.feature` @S07 | Not run |
| FR2 full name | Gap — none | S02, S07 | Assessed: automate E2E + API | `src/test/resources/features/users-search.feature` @S02; `src/test/resources/features/users-search-api.feature` @S07 | Not run |
| FR3 position filter | Gap — none | S03, S07 | Assessed: automate E2E + API | `src/test/resources/features/users-search.feature` @S03; `src/test/resources/features/users-search-api.feature` @S07 | Not run |
| FR4 position options | Gap — none | S03 | Assessed: automate E2E | `src/test/resources/features/users-search.feature` @S03 | Not run |
| FR5 combined | Gap — none | S04, S06, S07 | Assessed: automate E2E + API | `src/test/resources/features/users-search.feature` @S04 @S06; `src/test/resources/features/users-search-api.feature` @S07 | Not run |
| FR6 no-results message | Gap — none | S05 | Assessed: automate E2E | `src/test/resources/features/users-search.feature` @S05 | Not run |
| FR7 Clear | Gap — none | S04 | Assessed: automate E2E | `src/test/resources/features/users-search.feature` @S04 | Not run |
| FR8 debounce | Gap — none | S01 (no Enter) | Assessed: E2E for "no Enter"; exact 300 ms deliberately not automated (flaky) — manual + dev unit | `src/test/resources/features/users-search.feature` @S01 (typing without Enter only) | Not run |
| FR9 actions while filtered | Partial — users.feature "View a user's details" (unfiltered, 9765c38) | S06 | Assessed: automate E2E | `src/test/resources/features/users-search.feature` @S06 | Not run |
| FR10 API params / no-param | Partial — users-resource.test.js no-param 200 (mocked, origin/ai-testing) | S07, S09 | Assessed: automate API; S09 via existing projects/users features | `src/test/resources/features/users-search-api.feature` @S07 | Not run |
| FR11 200 [] | Partial — users-resource.test.js 200 [] (mocked) | S05, S08 | Assessed: automate API | `src/test/resources/features/users-search-api.feature` @S05 @S07 @S08 | Not run |
| FR12 /users/positions | Gap — live 404 | S03, S07 | Assessed: automate API | `src/test/resources/features/users-search-api.feature` @S03 @S07 | Not run |
| FR13 UI sends params | Gap — none | S01 | Assessed: E2E (implicit, server-side filtering) | `src/test/resources/features/users-search.feature` @S01 (listed = API result) | Not run |
| FR14 readme | Gap — not updated | — | Deliberately not automated — PR review | — | n/a |
| NFR1 safe input | Gap — none (existing unit asserts interpolated SQL) | S08 | Assessed: automate API (real DB) + 1 UI example; dev unit recommended | `src/test/resources/features/users-search-api.feature` @S08; `src/test/resources/features/users-search.feature` @S08 | Not run |

## Test Scenarios

Format: `bdd` (project Scenario format).

### Environment, data and cleanup (all scenarios)

- **Environment:** local Docker Test Application — UI `http://localhost:8081`, API `http://localhost:8080`, DB initialised from `create-db.sql`. Feature must be built into the running containers.
- **Seed data relied on:** `manager@test.com` Jan Kowalski (Project Manager), `tester@test.com` Jan Kowalski (Test Engineer), `developer@test.com` Anna Nowak (Senior Developer), `business@test.com` Piotr Wolski (Business Analyst).
- **Shared DB caveat:** other runs leave rows (e.g. "Alex Morgan"); assert presence/absence of named users, never exact totals.
- **Created data naming:** realistic name + unique email `qa.search.<uuid>@test.com` (e.g. "Marta Lis", Junior Developer). Created users are removed in-scenario (S06) or via `DELETE /users/<id>`.
- **Evidence:** screenshot of table/filters, or API response body + status.

---

### Scenario 01 — Search narrows the list by partial, case-insensitive text across name, surname and email
Tags: `@happy-path` `@ui` · **Covers: FR1, FR8, FR13**

```gherkin
GIVEN I am on the Users page with all positions selected and an empty search box
WHEN I type "anna" without pressing Enter
THEN "Anna Nowak" is listed and every listed row contains "anna" (case-insensitive) in name, surname or email
AND "Jan Kowalski" and "Piotr Wolski" are not listed
AND the browser network log shows a single GET /users?search=anna sent roughly 300 ms after the last keystroke (not one per keystroke)
AND the listed emails equal the body of GET /users?search=anna (filtering is server-side)
```
Manual follow-ups in the same session (separate observations, same assertions): "KOWAL" → both Jan Kowalski users listed, every row contains "kowal"; "business@" → Piotr Wolski listed, Anna Nowak not listed.
Evidence: ______ · Result: ______ · Status: PASS / FAIL / BLOCKED · Notes: ______

### Scenario 02 — Full-name search finds a user by "name surname"
Tags: `@happy-path` `@ui` · **Covers: FR2**

```gherkin
GIVEN I am on the Users page
WHEN I search "anna nowak"
THEN "Anna Nowak" is listed
WHEN I search "jan kowalski"
THEN both "Jan Kowalski" users (manager@test.com and tester@test.com) are listed
```
Evidence: ______ · Result: ______ · Status: PASS / FAIL / BLOCKED · Notes: ______

### Scenario 03 — Position filter lists distinct sorted options and filters the table
Tags: `@happy-path` `@ui` `@api` · **Covers: FR3, FR4, FR12**

```gherkin
GIVEN I am on the Users page
THEN the Position filter shows "All positions" selected
AND its remaining options equal GET /users/positions, each once, in alphabetical order
AND they include "Business Analyst", "Project Manager", "Senior Developer", "Test Engineer"
WHEN I select "Test Engineer"
THEN only users with position "Test Engineer" are listed
AND "tester@test.com" is listed and "manager@test.com" is not
```
Evidence: ______ · Result: ______ · Status: PASS / FAIL / BLOCKED · Notes: ______

### Scenario 04 — Search and filter combine, then Clear restores the full list
Tags: `@happy-path` `@ui` · **Covers: FR5, FR7**

```gherkin
GIVEN I am on the Users page
WHEN I search "kowalski" and select "Test Engineer"
THEN tester@test.com (Jan Kowalski, Test Engineer) is listed
AND manager@test.com (Jan Kowalski, Project Manager) is not listed
AND every listed row contains "kowalski" and has position "Test Engineer"
WHEN I click "Clear"
THEN the search box is empty
AND the Position filter shows "All positions"
AND all four seed users are listed again
```
Evidence: ______ · Result: ______ · Status: PASS / FAIL / BLOCKED · Notes: ______

### Scenario 05 — No matches shows a message in the UI and an empty array from the API
Tags: `@negative` `@ui` `@api` · **Covers: FR6, FR11**

```gherkin
GIVEN I am on the Users page
WHEN I search "zz-no-such-user"
THEN the table shows "No users match your search"
AND no user rows are shown
AND GET /users?search=zz-no-such-user returns 200 with body []
```
Evidence: ______ · Result: ______ · Status: PASS / FAIL / BLOCKED · Notes: ______

### Scenario 06 — View, Update and Remove work on filtered results and the filter persists after Remove
Tags: `@happy-path` `@ui` `@data-write` · **Covers: FR9, FR5**

```gherkin
GIVEN two users created via POST /users sharing a unique token in their emails: "Marta Lis" qa.search.<token>.a@test.com and "Ola Lis" qa.search.<token>.b@test.com, both "Junior Developer"
AND I am on the Users page with search "qa.search.<token>" and position "Junior Developer" applied
THEN exactly those two users are listed (the unique token makes the count deterministic)
WHEN I click "View details" on the row with qa.search.<token>.a@test.com
THEN the details page shows qa.search.<token>.a@test.com
WHEN I return, re-apply the search and filter and click "Update user" on the row with qa.search.<token>.a@test.com
THEN the update form opens pre-filled with that user's email
WHEN I return, re-apply the search and filter and click "Remove user" on the row with qa.search.<token>.a@test.com
THEN that row disappears and "Ola Lis" (…b@test.com) is still listed
AND the search box still contains "qa.search.<token>" and the filter still shows "Junior Developer"
```
Rows are identified by unique email, not name. The second user keeps the position non-empty, so the result does not depend on Q5.
Cleanup: delete any remaining qa.search.<token> users via `GET /users` (no params, independent of the feature) → `DELETE /users/<id>`; runs even if the scenario fails.
Evidence: ______ · Result: ______ · Status: PASS / FAIL / BLOCKED · Notes: ______

### Scenario 07 — API query parameters: each alone, both, neither
Tags: `@api` `@contract` · **Covers: FR10, FR11, FR1, FR2, FR3, FR5, FR12**

```gherkin
GIVEN the API is reachable at http://localhost:8080
WHEN I call GET /users with no parameters
THEN status is 200 and all four seed users are present (unchanged behaviour)
WHEN I call GET /users?search=ANNA
THEN status is 200 and every result contains "anna" in name, surname, email or full name
WHEN I call GET /users?position=Test%20Engineer
THEN status is 200 and every result has position "Test Engineer"
WHEN I call GET /users?search=jan%20kowalski
THEN status is 200 and both manager@test.com and tester@test.com are present
WHEN I call GET /users?search=kowalski&position=Project%20Manager
THEN status is 200, manager@test.com is present, tester@test.com is absent and every result matches both criteria
WHEN I call GET /users?search=zz-no-such-user
THEN status is 200 and the body is []
WHEN I call GET /users/positions
THEN status is 200 and the body is a list of distinct position strings including the four seed positions
AND GET /users/1 still returns a single user object with id 1
AND GET /users/projects/1 still returns 200 with a list
```
Evidence: ______ · Result: ______ · Status: PASS / FAIL / BLOCKED · Notes: ______

### Scenario 08 — Special characters and SQL injection strings are treated as plain text
Tags: `@security` `@negative` `@api` `@ui` · **Covers: NFR1, FR11**

```gherkin
GIVEN the API is reachable and the seed users exist
WHEN I call GET /users?search=<value> for each of: '   %   ' OR 1=1 --   ' OR '1'='1   %' OR email LIKE '%
AND (provisional on Q1) for each of: _   \
THEN every response is 200 with a JSON array (no 4xx/5xx error)
AND every returned user literally contains the value in name, surname, email or full name (for seed data: [])
AND GET /users still returns all four seed users afterwards
WHEN I type "%" into the UI search box
THEN the table shows "No users match your search" rather than every user
```
Evidence: ______ · Result: ______ · Status: PASS / FAIL / BLOCKED · Notes: ______

### Scenario 09 — Regression: other pages that load users are unaffected
Tags: `@regression` `@ui` · **Covers: FR10 (backward compatibility)**

```gherkin
GIVEN the seed users exist
WHEN I open the Add project page
THEN the owner dropdown contains all four seed users
WHEN I open "Add users to project" for "Example Project"
THEN all four seed users are offered
WHEN I open "Update project" for "Example Project"
THEN the owner dropdown contains all four seed users with Jan Kowalski (manager@test.com) selected
AND the Users page with no search shows all four seed users
```
Evidence: ______ · Result: ______ · Status: PASS / FAIL / BLOCKED · Notes: ______

---

### Scenarios Not Written

| Requirement ID / category | Why omitted | Existing passing evidence |
|---|---|---|
| FR8 precise 300 ms timing (automated) | Not automated: exact timing in a browser is flaky. Checked **manually** in S01 (network log: one request ~300 ms after last keystroke); developer unit test with fake timers recommended | none (no tests) — provisional |
| Q7 search text echoed in no-results message (XSS) | Behaviour undefined; add an escaping scenario if the message echoes input | n/a |
| Destructive SQL (stacked `DROP`/`DELETE`) probes | Forbidden on the shared DB (safety.md). Only on a disposable DB with separate approval; S08 uses non-destructive payloads | n/a |
| FR14 readme update | Documentation; verified by PR review, not a test scenario | n/a |
| Escaping/parameterisation internals | Pure query-building logic belongs to developer unit tests (assert `$1/$2` + values). The externally observable outcome is covered by S08 | none — provisional, gap recorded in coverage.md |
| Permission scenarios | App has no authentication/authorisation (server.js — no auth middleware) | n/a |
| Feature flag ON/OFF | No flag introduced (∅) | n/a |
| Environment-specific behaviour | Single local environment; `int`/`qa` properties point at the same host | n/a |
| Out-of-scope items | Pagination, sorting, saved searches, Projects-page search — excluded by ticket | n/a |
| Q-dependent edges (whitespace trim, repeated params, unknown position, stale responses) | Expected behaviour undefined (Q2, Q3, Q6, Q8); scenarios to be added once answered | n/a |

**Count:** 9 written; 10 categories/IDs not written.

Revision 2026-10-08T20:15Z after design review (review.md): non-destructive S08 payloads; per-row assertions instead of "only/exactly"; S06 unique-email data via API with feature-independent cleanup; S07 adds no-match, full name and `/users/projects/1`; S09 adds Update project and member list; FR8 manual timing explicit. Manual scenarios keep multiple When/Then observations; automated Gherkin is one behaviour per scenario.

## Automation Recommendation

### Nine factors

| Factor | Answer | Reason |
|---|---|---|
| Cross-service impact | Yes | Browser UI → Express API → Postgres |
| API changes | Yes | New params on GET /users; new GET /users/positions |
| Backend logic | Yes | New filtered query and escaping |
| Database side effects | Yes (reads) | New read query; injection risk could cause writes |
| Feature flags | No | None (∅) |
| Caching behaviour | Yes (async) | Debounced async requests; stale-response race |
| Authentication / authorisation | No | No auth in app |
| Logging validation | No | No logging requirement (Q10) |
| Environment-based configuration | Yes | Local Docker only; new `apiUrl` property needed; CI has no app |

### Decisions

| Scenario / requirement | Decision | Level | Location | Mocking | Environment / data | CI impact | Justification / evidence |
|---|---|---|---|---|---|---|---|
| S01 search partial/case/email (FR1, FR8, FR13) | Automate | E2E UI | `src/test/resources/features/users-search.feature` (Scenario Outline); `stepdefinitions/UserSearchSteps.java`; extend `pages/UsersListPage.java` | None (real stack) | local; seed users; per-row assertions (every row contains the text) + named presence/absence; never totals | +~10 s/example; **condition-based waits**: wait until every rendered row satisfies the expectation (or the no-results message shows) — avoids passing on the stale pre-debounce table; no sleeps | Core journey; no coverage. FR13 automated by comparing rendered emails with `GET /users?search=` from the API helper |
| S02 full name (FR2) | Automate | E2E UI | Same Outline as S01 (examples "anna nowak", "jan kowalski") | None | seed | negligible | Merges with S01 |
| S03 position options + filter (FR3, FR4, FR12) | Automate | E2E UI | `users-search.feature` | None | seed; options compared with `GET /users/positions` via API helper | ~10 s | Sorting/distinct/default only observable in UI |
| S04 combined + Clear (FR5, FR7) | Automate | E2E UI | `users-search.feature` | None | seed 2× Jan Kowalski | ~10 s | Duplicate names give a deterministic AND check |
| S05 no results (FR6, FR11) | Automate | E2E UI + API | UI in `users-search.feature`; API in `users-search-api.feature` | None | seed; unique non-matching text | negligible | Boundary UI↔API |
| S06 actions while filtered (FR9) | Automate | E2E UI | `users-search.feature` | None | two users with a unique email token created via `POST /users` (API helper); rows located by email; cleanup in a tag-scoped `@After` in `UserSearchSteps` that finds `qa.search.<token>` users via unfiltered `GET /users` and deletes them (independent of the feature under test) | ~20 s; writes to shared DB (self-cleaning) | Data-write path; highest UI regression value |
| S07 API params (FR10, FR11, FR12) | Automate | Integration (API) | `src/test/resources/features/users-search-api.feature` (tags `@search @api`); `stepdefinitions/UsersApiSteps.java`; new `src/main/java/api/UsersApiClient.java` (`java.net.http` + bundled `org.openqa.selenium.json.Json` — **no new dependency**) | None — real API + DB | `apiUrl` property added to `environments/*.properties` (`http://localhost:8080`) | seconds per call, but the global `@Before` hook (Hooks.java) still opens a browser per scenario (~2–3 s); accepted to avoid editing shared hooks | One request/response → integration; includes `/users/1`, `/users/projects/1` route checks |
| S08 safe input (NFR1) | Automate | Integration (API) + one E2E example (`%`) | API Scenario Outline in `users-search-api.feature`; UI example in `users-search.feature` | None | seed; **non-destructive** values `'`, `%`, `' OR 1=1 --`, `' OR '1'='1`, `%' OR email LIKE '%`; `_` and `\` rows provisional on Q1; afterwards assert seed users still present. No stacked DROP/DELETE payloads (safety.md) | seconds | Security; must hit real Postgres |
| S09 other GET /users consumers | Manual | E2E (manual) | — | — | seed; Example Project | none | Existing `projects.feature` only partly covers it (owner dropdown asserted via "Anna Nowak"; member list and Update project page not asserted) — review finding. Candidate for later automation |
| FR8 exact 300 ms | Manual + recommend dev unit | — | Developer: frontend has no test harness (∅) → manual stopwatch/network-log check | — | — | — | Precise timing in E2E is flaky |
| NFR1 parameterisation internals | Recommend dev unit (not QA-written) | Unit | test-application `backend/users/users-queries.test.js` (origin/ai-testing pattern) — assert `pool.query(text, [values])` with `$1/$2` and escaped wildcards | `pg` mocked | — | app repo | Role boundary: developer-owned |
| FR12 route order | Recommend dev unit + QA API (S07) | Unit/Integration | app `users-resource.test.js` | — | — | — | Route registered before `/users/:id` |
| FR14 readme | Not needed | — | PR review | — | — | — | Documentation |

### Proposed shape (example, `bdd`)

```gherkin
@search
Feature: Search and filter users
  @functional
  Scenario Outline: Search narrows the users list without pressing Enter
    Given I open the users page
    When I search users for "<text>"
    Then the users list should show "<shown>"
    And the users list should not show "<hidden>"
    Examples:
      | text        | shown        | hidden       |
      | anna        | Anna Nowak   | Jan Kowalski |
      | KOWAL       | Jan Kowalski | Anna Nowak   |
      | business@   | Piotr Wolski | Anna Nowak   |
      | anna nowak  | Anna Nowak   | Piotr Wolski |
```

### Locator contract (blocker — Q9)

The new controls do not exist yet. Proposed IDs for the developer to adopt (kept as constants in `UsersListPage` so they are a one-line change): `#users-search`, `#position-filter`, `#clear-filters`, `#no-users-message`.

### Impact

**High** — new API helper/package and config key (framework work), two new feature files, two step classes, page-object extension.

### Blockers / residual risk

- Feature not implemented → generated tests will **fail** until delivered (expected; not a test defect).
- Locator IDs unconfirmed (Q9).
- CI: `tests.yml` has no app container, so these (and all existing Users-app tests) cannot pass in GitHub Actions; options: run locally only, exclude `@search` in CI, or add the app to CI (team decision; not done here).
- Base branch: page objects exist only on `feature/test-application`.

## Automated Tests and Execution

Generated on `feature/testing-ai-tool` (uncommitted): `users-search.feature` (`@search`, 15 runs), `users-search-api.feature` (`@search @api`, 17 runs), page objects `UsersListPage`/`ViewUserPage`/`UpdateUserPage`, `api.UsersApiClient`, steps `UserSearchSteps`/`UsersApiSteps`, `support.UserSearchData`; new env keys `appBaseUrl`, `apiUrl`. Compile passes; code review Approve with changes (C1, C2 fixed). **Not run** — user declined the L3 run; see [../execution.md](../execution.md). Command when the feature is deployed: `mvn -B -ntp test -Dcucumber.filter.tags="@search"`.

## Regression Impact

API Behaviour, Existing Endpoints, Backend Logic and Database Layer are HIGH; targeted checks S07, S08, S09 plus existing `users.feature`/`projects.feature` as a regression run. Developer-owned Jest tests on `origin/ai-testing` asserting the old SQL string will need updating.

## Environment Impact

Local Docker stack only (UI `http://localhost:8081`, API `http://localhost:8080`; QA property keys `baseUrl`, new `apiUrl`). No credentials needed (no auth). Tests write to the shared local DB only in S06 (self-cleaning). GitHub Actions has no app container — Users-app tests cannot pass in CI as configured. Destructive SQL probes are excluded.

## Open Questions

Questions for Refinement:
  - Q1 Which characters must be escaped/treated literally? The pasted text lost one (`_` or `\`). QA assumption: `%`, `_` and `\` all literal.
  - Q2 Is the position filter an exact, case-sensitive match? What does an unknown position return (assume 200 [])?
  - Q3 Are leading/trailing spaces trimmed, and is an empty or whitespace-only `search=` treated as "no filter"?
  - Q4 Full-name match: only "name surname" with one space, or also "surname name" / multiple spaces?
  - Q5 Does the Position dropdown list positions of ALL users (loaded once per page) and is it refreshed after a remove empties a position?
  - Q6 AC8 timing tolerance for "about 300ms"? Should out-of-order (stale) responses be discarded?
  - Q7 Does the no-results message echo the search text? If so it must be HTML-escaped (rows are built by string concatenation).
  - Q8 Expected UI/API behaviour on backend error, repeated params (?search=a&search=b) or very long input?
  - Q9 Element IDs for the new controls (search input, position select, Clear, no-results message) — needed for stable automation.
  - Q10 Confirm no logging/metrics/audit requirement.
  - Branch strategy for tests and CI handling — owner: user/team.

## Regression Risk Matrix


| Area | Risk Level | Why? | Regression Needed? | Automation Update Needed? |
|---|---|---|---|---|
| API Behaviour | HIGH | GET /users contract gains two optional params and new semantics (200 [] on no match); four pages depend on the no-param response | Yes | Yes |
| Existing Endpoints | HIGH | `/users/:id` (users-resource.js L13) currently captures `/users/positions` (live 404). Wrong route order breaks FR12; a careless fix could break `/users/:id` or `/users/projects/:id` | Yes | Yes |
| Feature Flags | LOW | No flag introduced; no flag mechanism exists (bounded search backend/, frontend/) | No | No |
| Caching | MEDIUM | No server cache, but async debounced requests: out-of-order responses can render stale results; browser GET caching after Remove (escalated: async behaviour) | Yes | Yes (E2E waits on content, not time) |
| Authentication / Authorisation | LOW | App has no auth; unchanged. Note: API is unauthenticated, raising NFR1 impact | No | No |
| API Gateway | LOW | No gateway; open `cors()` unchanged (server.js L9) | No | No |
| Backend Logic | HIGH | New dynamic query (ILIKE across 4 expressions, AND with position, wildcard escaping) | Yes | Yes |
| Database Layer | HIGH | Persistence read path modified; existing file uses string-interpolated SQL; pg simple-query protocol allows stacked statements → injection could destroy data (escalated: persistence logic) | Yes | Yes |
| Data Integrity | MEDIUM | Read-only feature, but Remove from a filtered view must delete the right id; QA data writes into a shared DB | Yes | Yes |
| Logging / Monitoring | LOW | No logging exists or is required (Q10 open); errors collapse to 404 — reduces diagnosability, not behaviour | No | No |
| Environment Configuration | MEDIUM | Frontend hardcodes `API_URL` localhost:8080; QA suite needs a new API base URL property; all envs point at local Docker | Yes | Yes |
| CI/CD Pipeline | MEDIUM | `tests.yml` runs every PR against `-Denvironment=qa` (localhost:8081) with no app container → Users-app tests cannot pass in CI (pre-existing on feature/test-application); new tests extend that exposure | Unknown | Unknown (needs decision) |
| Backward Compatibility | MEDIUM | No-param GET /users must stay identical; developer Jest test asserting exact SQL (origin/ai-testing) will need update | Yes | Yes |
| *Extra:* Cross-browser | LOW | Input/keyup debounce across Chrome/Edge/Firefox; CI Chrome-only | No | No |
| *Extra:* Environment configuration (local/int/qa, headless) | MEDIUM | Same as Environment Configuration; headless timing of debounce | Yes | No |

**Highest overall risk: HIGH** (API Behaviour, Existing Endpoints, Backend Logic, Database Layer).

### HIGH-risk handling

| Area | Targeted regression scenario | Automation reinforcement | Potential production impact | Rollout validation |
|---|---|---|---|---|
| API Behaviour | S07 (no-param unchanged, each param, both); S09 other pages | QA API feature `users-search-api.feature` | Project pages lose owner/member lists; search returns wrong users | After deploy, GET /users (no params) count equals pre-deploy count |
| Existing Endpoints | S07 `/users/positions` 200 + `/users/1` still 200 | API step asserting both routes | Positions dropdown empty; user details broken | Smoke `/users/positions`, `/users/1`, `/users/projects/1` |
| Backend Logic | S01–S05, S07 | E2E + API; dev Jest service/query tests | Incorrect filtering | Spot-check seed searches |
| Database Layer | S08 injection & wildcard strings, table intact afterwards | API injection Scenario Outline; dev unit asserting `$1/$2` | Data exfiltration or loss via unauthenticated API | Run S08 against each environment before release; code review of query |

## Drift

`project.md` still describes Sauce Demo as the SUT (Users-app suite lives on `feature/test-application`); `testing.md` lacks the new API scope; scenario tags differ (`@functional` vs `@regression`). Suggest `qa-configure refresh`.
