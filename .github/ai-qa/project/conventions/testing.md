# Testing conventions

Rendered by `qa-configure` on 2026-10-08 after discovery and L5 approval. All test files were read (4/4 features, 5/5 test classes, 2/2 page objects). Configuration keys only; never secret values.

## Scopes

| Test path | Level | Pack | Location | Naming | Fixtures/builders | Tags | Base classes | Assertions | Status | Evidence/source |
|---|---|---|---|---|---|---|---|---|---|---|
| `src/test/resources/features/**/*.feature` | E2E (browser UI) | `cucumber-java` | `src/test/resources/features/` | `<area>.feature`, one `Feature:` per file with user-story header; declarative steps; `Scenario Outline` + `Examples` for data variants | Reuse existing steps, e.g. `Given I am logged into the application` | Feature tag `@<area>`; scenario tag `@smoke` or `@regression` | — | Expected outcomes in `Then` steps | ✓ | `login.feature` L1-22; `logout.feature` L1-11; `navigation.feature` L1-18; `README.md` L163-178 |
| `src/test/java/stepdefinitions/**/*.java` | E2E glue | `cucumber-java` | package `stepdefinitions` | `<Area>Steps`; method names camelCase of step text | Page objects as instance fields or `new`; no DI container; config via `ConfigReader.get(...)` | — | None | JUnit 5 `assertTrue`/`assertEquals` with failure message, in steps only | ✓ | `LoginSteps.java` L12-48; `NavigationSteps.java` L12-40; `CommonSteps.java` L11-26 |
| `src/test/java/hooks/**/*.java`, `src/test/java/runners/**/*.java` | Lifecycle / runner | `cucumber-java`, `selenium-java` | packages `hooks`, `runners` | `Hooks`; `*Runner` (Surefire include) | Cucumber `@Before` → `DriverFactory.initDriver()`; `@After` → failure screenshot + `quitDriver()` | — | — | — | ✓ | `Hooks.java` L17-38; `TestRunner.java` L16-20; `pom.xml` L120-122 |
| `src/main/java/{pages,driver,utils,config}/**/*.java` | Test framework (Page Objects) | `selenium-java` | `src/main/java/` | `<Name>Page extends BasePage`; private `By` locators (`id`, then `[data-test=...]` CSS; no XPath) | `BasePage` helpers (`click`, `enterText`, `getText`, `isDisplayed`, waits); `WaitUtils`; `ThreadLocal` `DriverFactory` | — | `BasePage` | None in page objects | ✓ | `BasePage.java` L17-67; `LoginPage.java` L7-35; `HomePage.java` L7-46; `README.md` L180-203 |

Project rules override pack guidance: driver lifecycle is in Cucumber hooks (not JUnit `@BeforeEach`); there is no Maven wrapper, so use `mvn` (not `./mvnw`); no `Thread.sleep()`; implicit wait stays zero.

## Commands

| Command kind | Command / selector | Status | Evidence/source |
|---|---|---|---|
| All | `mvn test` (optionally `-Denvironment=local\|int\|qa -Dheadless=true`) | ✓ | `README.md` L49-56; `.github/workflows/tests.yml` L37-42 |
| Path | No feature-path selector configured; select by tag instead | ∅ | `TestRunner.java` L18 `@SelectPackages("features")`; `README.md` L47-68 |
| Tag | `mvn test -Dcucumber.filter.tags="<tag expression>"` | ✓ | `README.md` L58-60, L233-235; `.github/workflows/tests.yml` L42 |
| Lint/compile | `mvn -B -ntp test-compile` | ✓ | `.github/workflows/tests.yml` L34-35; `README.md` L42 |
| List/help | `mvn -v` (toolchain check only; no feature listing command exists) | ✓ | Read-only probe 2026-10-08: Maven 3.9.16 |

Every test run is L3 (approval required); none is exempt (see `qa-process.md`).

## Reports

| Format | Path | Coverage source | Status | Evidence/source |
|---|---|---|---|---|
| Cucumber HTML / JSON / JUnit XML | `target/cucumber-reports/cucumber.{html,json,xml}` | None (no coverage tooling) | ✓ | `src/test/resources/junit-platform.properties` L2-5; `README.md` L88-98 |
| Surefire reports | `target/surefire-reports/` | None | ✓ | `.github/workflows/tests.yml` L51 |
| Failure screenshots / log | `target/screenshots/`, `target/logs/test-execution.log` | — | ✓ | `ScreenshotUtils.java` L19; `README.md` L97-98 |

## Environments and base URLs

| Environment | Purpose / allowed use | Base URL variable name | Other variable names | Status | Evidence/source |
|---|---|---|---|---|---|
| `local` (default) | Local runs, headed browser | property `baseUrl` (`-DbaseUrl`) | properties `browser`, `timeout`, `headless`, `username`, `password` | ✓ | `local.properties` L4-11; `ConfigReader.java` L18, L28-51 |
| `int` | Integrated environment, headless | property `baseUrl` | as above | ✓ | `int.properties` L4-11 |
| `qa` | QA environment, headless; CI default | property `baseUrl` | as above | ✓ | `qa.properties` L4-11; `.github/workflows/tests.yml` L13, L40 |

No OS environment variables are used; configuration is `.properties` keys overridable with `-D`. All environments currently target the public Sauce Demo site.

## Test data rules

| Rule | Value | Status | Evidence/source |
|---|---|---|---|
| Ownership, source and permitted data | Users and URLs from environment `.properties` or `-D`; inline `Examples` tables for negative data. Only public demo credentials may be committed; real credentials via `-Dusername/-Dpassword` from a secrets store | ✓ | `README.md` L72-86; `login.feature` L19-22 |
| Fixture naming, isolation and cleanup | Fresh browser per scenario; driver quit in `@After`; no persistent data created | ✓ | `Hooks.java` L17-38 |
| Retention/privacy constraints | Never commit secrets; screenshots/logs stay in `target/` (git-ignored) | ✓ | `README.md` L85-86; `.gitignore` L1, L5-6 |

## Manual testing ownership

| Behaviour / level | Owner | Evidence/status | Source |
|---|---|---|---|
| Manual scenarios for behaviour not covered by the automated features | QA | ★ | `.github/ai-qa/framework/defaults/testing.md`, adopted 2026-10-08 |

Unit tests are developer-owned unless project evidence says otherwise. QA assesses whether they exist and cover requirements; distinguish test existence, assertion relevance, run result and measured coverage. Apply `method/dedup-rule.md` to avoid duplicate manual scenarios.
