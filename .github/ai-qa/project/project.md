# Project Context

Project Context for `declankbarrett/selenium-java-skeleton`, written by `qa-configure` after L5 approval on 2026-10-08. This file describes what the project is; `conventions/*.md` describe how AI-QA operates on it. Never include credentials or secret values.

<!-- ai-qa:managed:summary -->
## Summary

A minimal UI test-automation framework (Selenium 4 + Cucumber BDD + JUnit 5, Java 21, Maven), intended as a starting point for new automation projects and a training/onboarding reference. The bundled tests run against the public demo application Sauce Demo (`https://www.saucedemo.com`), covering availability, login (success and error), logout and navigation journeys. The repository *is* the test framework; there is no application under test in this repository. Owner: GitHub user `declankbarrett` (repository owner); no CODEOWNERS file.

**Confidence:** High
**Sources:** `README.md` L1-15, L152-159; `pom.xml` L7-10; `src/main/resources/environments/local.properties` L1-4; `git remote -v` (origin `https://github.com/declankbarrett/selenium-java-skeleton.git`); revision `abdb42c`
<!-- /ai-qa:managed:summary -->

<!-- ai-qa:user -->
<!-- Project-authored context. Configure preserves this section verbatim on refresh. -->
<!-- /ai-qa:user -->

<!-- ai-qa:managed:components -->
## Components

| Component | Type | Path | Tech | Purpose |
|---|---|---|---|---|
| Configuration | Library (test framework) | `src/main/java/config/` | Java, `java.util.Properties` | Loads `environments/<env>.properties`; `-D` system properties override file values |
| Driver management | Library (test framework) | `src/main/java/driver/` | Selenium 4, WebDriverManager | `ThreadLocal` WebDriver per thread; Chrome (default), Edge, Firefox; headless option |
| Page objects | Library (test framework) | `src/main/java/pages/` | Selenium Page Object Model | `BasePage` shared waits/actions; `LoginPage`, `HomePage` (products "dashboard"); no assertions |
| Utilities | Library (test framework) | `src/main/java/utils/` | Selenium `WebDriverWait`, NIO | Explicit-wait helpers; failure screenshots to `target/screenshots/` |
| Environment config | Resources | `src/main/resources/environments/` | `.properties` | `local`, `int`, `qa` settings |
| Logging config | Resources | `src/main/resources/log4j2.xml` | Log4j2 | Console and file logging (`target/logs/test-execution.log`) |
| Feature files | Tests | `src/test/resources/features/` | Gherkin (Cucumber 7) | Business-language scenarios |
| Glue, hooks, runner | Tests | `src/test/java/{stepdefinitions,hooks,runners}/` | Cucumber Java, JUnit Platform Suite | Step definitions with assertions; browser lifecycle hooks; suite entry point |
| CI | Pipeline | `.github/workflows/tests.yml` | GitHub Actions | Compile, run headless tests, upload reports |

**Confidence:** High
**Sources:** `README.md` L100-150; all files under `src/` read in full at `abdb42c`
<!-- /ai-qa:managed:components -->

<!-- ai-qa:user -->
<!-- Project-authored component notes. Configure preserves this section verbatim on refresh. -->
<!-- /ai-qa:user -->

<!-- ai-qa:managed:technology-stack -->
## Technology stack

- Java 21 (`maven.compiler.release` 21); Maven 3.9+ (no Maven wrapper in the repository)
- Selenium `4.50.0`, WebDriverManager `6.4.0`
- Cucumber `7.34.9` (`cucumber-java`, `cucumber-junit-platform-engine`) on JUnit `5.14.4` (`junit-platform-suite`, `junit-jupiter` assertions)
- Log4j2 `2.26.1` (with SLF4J bridge)
- Maven Surefire `3.5.4` (includes `**/*Runner.java`), Maven Compiler `3.14.1`

**Confidence:** High
**Sources:** `pom.xml` L12-29, L57-106, L108-129; `README.md` L17-35
<!-- /ai-qa:managed:technology-stack -->

<!-- ai-qa:user -->
<!-- Project-authored stack notes. Configure preserves this section verbatim on refresh. -->
<!-- /ai-qa:user -->

<!-- ai-qa:managed:environments -->
## Environments

| Environment | Selected by | Role | Headless default |
|---|---|---|---|
| `local` | default (`pom.xml` L17, `ConfigReader.java` L18) | Local development | `false` |
| `int` | `-Denvironment=int` | Integrated environment | `true` |
| `qa` | `-Denvironment=qa`; CI default | QA environment | `true` |

All three currently point at the same public demo site; the files say to replace `baseUrl` per real environment. No production environment is defined. Test URLs and configuration keys are recorded in `conventions/testing.md`.

**Confidence:** High
**Sources:** `src/main/resources/environments/{local,int,qa}.properties` L1-7; `src/main/java/config/ConfigReader.java` L18, L23-35; `.github/workflows/tests.yml` L8-13, L40; `README.md` L70-83
<!-- /ai-qa:managed:environments -->

<!-- ai-qa:user -->
<!-- Project-authored environment notes. Configure preserves this section verbatim on refresh. -->
<!-- /ai-qa:user -->

<!-- ai-qa:managed:data-stores-and-external-dependencies -->
## Data stores and external dependencies

- No databases, queues, caches, migrations, compose or IaC files (∅, bounded search of repository at `abdb42c`).
- External dependency: the public Sauce Demo web application (system under test) reached over the internet.
- External dependency: WebDriverManager downloads browser drivers at run time; a locally installed Chrome, Edge or Firefox is required.
- Test users come from the environment `.properties` files (keys `username`, `password`) or `-D` overrides.

**Confidence:** High
**Sources:** `README.md` L8-9, L30-45; `src/main/java/driver/DriverFactory.java` L73-105; repository file listing
<!-- /ai-qa:managed:data-stores-and-external-dependencies -->

<!-- ai-qa:user -->
<!-- Project-authored dependency notes. Configure preserves this section verbatim on refresh. -->
<!-- /ai-qa:user -->

<!-- ai-qa:managed:ci-cd -->
## CI/CD

GitHub Actions workflow `UI Tests` (`.github/workflows/tests.yml`): triggers on push to `main`, all pull requests and manual dispatch (inputs `environment` = `qa|int|local`, default `qa`; `tags` = Cucumber tag expression). Single job on `ubuntu-latest`: checkout → Temurin Java 21 with Maven cache → `mvn -B -ntp test-compile` → `mvn -B -ntp test -Denvironment=<env> -Dheadless=true -Dcucumber.filter.tags="<tags>"` → upload `test-results` artefact (`target/cucumber-reports/`, `target/surefire-reports/`, `target/screenshots/`, `target/logs/`) even on failure. No deployment stage.

**Confidence:** High
**Sources:** `.github/workflows/tests.yml` L1-54; `README.md` L239-247
<!-- /ai-qa:managed:ci-cd -->

<!-- ai-qa:user -->
<!-- Project-authored CI/CD notes. Configure preserves this section verbatim on refresh. -->
<!-- /ai-qa:user -->

<!-- ai-qa:managed:test-landscape -->
## Test landscape

- **Level:** browser E2E/UI only. No unit, API or integration tests (∅).
- **Framework:** Cucumber 7 features run by JUnit Platform Suite `runners.TestRunner` (glue `stepdefinitions,hooks`).
- **Inventory:** 4 feature files, 6 scenarios (one Scenario Outline with 2 examples): availability, login, logout, navigation.
- **Structure:** feature files → thin step definitions holding JUnit assertions → page objects (extend `BasePage`, no assertions). Hooks open a fresh browser per scenario and attach a screenshot on failure.
- **Tags:** feature-level `@availability @login @logout @navigation`; scenario-level `@smoke`, `@regression`.
- **Packs selected:** `cucumber-java` (features + glue) and `selenium-java` (page objects, driver, utilities, hooks).
- **Coverage evidence:** none measured; test existence is not a pass result.

**Confidence:** High (all test files read: 4/4 features, 5/5 test Java classes)
**Sources:** `src/test/java/runners/TestRunner.java` L16-20; `src/test/java/hooks/Hooks.java` L17-38; `src/test/resources/features/*.feature`; `README.md` L143-159
<!-- /ai-qa:managed:test-landscape -->

<!-- ai-qa:user -->
<!-- Project-authored testing notes. Configure preserves this section verbatim on refresh. -->
<!-- /ai-qa:user -->

<!-- ai-qa:managed:constraints -->
## Constraints

- Never commit real credentials; pass them as `-Dusername=... -Dpassword=...` from a secrets store. The committed values are public demo credentials only.
- Tests drive a real browser against an external public website; every test run is L3 (approval required). No commands are L3-exempt (confirmed 2026-10-08).
- No `Thread.sleep()`; explicit waits only (implicit wait fixed at zero).
- Page objects contain no assertions; assertions live in step definitions.
- No edits on `main`; branch protection rules could not be checked (`?`).
- No further Do Not rules (confirmed by user 2026-10-08).

**Confidence:** High
**Sources:** `README.md` L85-86, L145-150, L200-203; `src/main/java/driver/DriverFactory.java` L44-45; user confirmation 2026-10-08
<!-- /ai-qa:managed:constraints -->

<!-- ai-qa:user -->
<!-- Project-authored constraints. Configure preserves this section verbatim on refresh. -->
<!-- /ai-qa:user -->

<!-- ai-qa:managed:documentation-sources -->
## Documentation sources

- `README.md` is the only project documentation (overview, setup, running, configuration, reports, structure, adding tests, CI, future enhancements).
- No `docs/`, ADRs, CONTRIBUTING, API contracts or wiki links (∅).
- Issue tracker: none configured (∅). `jira.md` exists but is empty. Requirements and acceptance criteria are supplied manually (pasted).

**Confidence:** High
**Sources:** `README.md`; `jira.md` (0 bytes); repository file listing at `abdb42c`; user confirmation 2026-10-08
<!-- /ai-qa:managed:documentation-sources -->

<!-- ai-qa:user -->
<!-- Project-authored documentation notes. Configure preserves this section verbatim on refresh. -->
<!-- /ai-qa:user -->

<!-- ai-qa:managed:unknowns-and-conflicts -->
## Unknowns and conflicts

| Status | Item | Detail / next check |
|---|---|---|
| ? | Branch protection on `main` | `gh` CLI not installed; GitHub MCP read did not cover settings. Check repository settings. |
| ∅ | Work-item tracker | No Jira/ADO URL or key; `jira.md` is empty. Re-run `qa-configure refresh` when a tracker is chosen. |
| ∅ | Manual test cases / AC style | None in repository; `bdd` applied as ★ default. |
| ◐ | Pack run commands vs project | Packs show `./mvnw`; no wrapper exists. Project command `mvn` takes precedence. |
| ◐ | Driver lifecycle | `selenium-java` pack suggests `@BeforeEach/@AfterEach`; project uses Cucumber `@Before/@After` hooks. Project convention takes precedence. |

No `⚠` conflicts found.

**Confidence:** High
**Sources:** `which gh` (not found); `jira.md`; `.github/ai-qa/framework/packs/{selenium-java,cucumber-java}/pack.md`; `src/test/java/hooks/Hooks.java` L17-38
<!-- /ai-qa:managed:unknowns-and-conflicts -->

<!-- ai-qa:user -->
<!-- Project-authored unknowns/conflicts notes. Configure preserves this section verbatim on refresh. -->
<!-- /ai-qa:user -->

<!-- ai-qa:managed:provenance -->
## Provenance

Discovered on branch `feature/testing-ai-tool` at commit `abdb42c` on 2026-10-08 by `qa-configure` (AI-QA framework 1.0.0). All repository files read (small repository). Read-only probes: `git remote/branch/log`, `which gh mvn java az`, `mvn -v`, GitHub MCP `list_pull_requests` (0 PRs). Decisions confirmed by the repository owner on 2026-10-08; L5 write approved on 2026-10-08.

**Confidence:** High
**Sources:** `.github/ai-qa/project/discovery.md`; `.github/ai-qa/manifest.json`
<!-- /ai-qa:managed:provenance -->

<!-- ai-qa:user -->
<!-- Project-authored provenance notes. Configure preserves this section verbatim on refresh. -->
<!-- /ai-qa:user -->
