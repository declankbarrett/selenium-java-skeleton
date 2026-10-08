# Project Discovery

Written by `qa-configure` after approval. The test suite was not run. Statuses: ✓ Observed · ◐ Inferred · ⚠ Conflict · ∅ Not found · ? Could not check · ✗ No consistent convention · ★ Default established (Configure only).

**Repository / scope:** `declankbarrett/selenium-java-skeleton`, whole repository (excluding the installed AI-QA framework under `.github/ai-qa/framework/`, `.github/skills/`, `.github/agents/`)
**Branch / revision:** `feature/testing-ai-tool` @ `abdb42c` (working tree: `.gitignore` modified by AI-QA installer; empty `jira.md` staged)
**Observed at:** 2026-10-08
**Sampling approach:** Small repository — read every relevant file: `pom.xml`, `README.md`, `.gitignore`, `.github/workflows/tests.yml`, 3 environment files, `junit-platform.properties`, 8 main Java classes, 5 test Java classes, 4 feature files.

<!-- ai-qa:managed:repo-shape -->
## Repo shape

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ✓ | Single-module Maven project; framework code in `src/main/java/{config,driver,pages,utils}`, tests in `src/test/java/{hooks,runners,stepdefinitions}` and `src/test/resources/features` | `pom.xml` L7-10 (no `<modules>`); `git ls-files` | No workspaces or multi-module layout |
<!-- /ai-qa:managed:repo-shape -->

<!-- ai-qa:managed:languages-and-build -->
## Languages and build

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ✓ | Java 21, Maven, UTF-8; Surefire 3.5.4 runs `**/*Runner.java` and passes `environment` system property | `pom.xml` L13-14, L27-28, L115-127 | — |
| ∅ | No Maven wrapper (`mvnw`), no lockfile | repository file listing | Use `mvn`; local `mvn -v` reports Maven 3.9.16 |
<!-- /ai-qa:managed:languages-and-build -->

<!-- ai-qa:managed:app-frameworks-and-data -->
## App frameworks and data

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ✓ | No application code; the system under test is the external public site `https://www.saucedemo.com` | `README.md` L8-9; `src/main/resources/environments/*.properties` L4 | — |
| ∅ | No databases, migrations, compose, IaC, OpenAPI/AsyncAPI | repository file listing | — |
<!-- /ai-qa:managed:app-frameworks-and-data -->

<!-- ai-qa:managed:test-stack-and-pack-match -->
## Test stack and pack match

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ✓ | Cucumber 7.34.9 on JUnit Platform Suite; `.feature` files + Java glue + `@Suite` runner → pack `cucumber-java` | `pom.xml` L22, L86-100; `src/test/java/runners/TestRunner.java` L16-20 | 4/4 features, 3/3 step classes |
| ✓ | Selenium 4.50.0 + WebDriverManager 6.4.0, `ThreadLocal` driver, Page Objects → pack `selenium-java` | `pom.xml` L20-21, L59-68; `src/main/java/driver/DriverFactory.java` L26, L73-105; `src/main/java/pages/BasePage.java` L17-24 | Page objects live in `src/main/java`, not `src/test/java` |
<!-- /ai-qa:managed:test-stack-and-pack-match -->

<!-- ai-qa:managed:test-structure-and-conventions -->
## Test structure and conventions

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ✓ | Feature files: one feature per file, lower-case name, user-story header, feature tag `@<feature>`, scenario tag `@smoke` or `@regression` | `login.feature` L1-17; `logout.feature` L1-11; `navigation.feature` L1-18 | 4/4 files; `availability.feature` L1 puts `@smoke @availability` at feature level |
| ✓ | Data variations via `Scenario Outline` + `Examples` | `login.feature` L14-22 | 1 occurrence |
| ✓ | Step classes `<Area>Steps` in `stepdefinitions`; thin steps calling page objects; JUnit `assertTrue/assertEquals` with failure messages | `LoginSteps.java` L38-48; `NavigationSteps.java` L26-30; `CommonSteps.java` L18-26 | 3/3 classes |
| ✓ | Reusable composite step `I am logged into the application` | `LoginSteps.java` L21-26; used in `logout.feature` L9, `navigation.feature` L9, L15 | — |
| ✓ | No DI container; page objects instantiated in step classes (instance fields or `new`) | `LoginSteps.java` L14; `NavigationSteps.java` L14; `CommonSteps.java` L15; grep `picocontainer` → none | README L256 lists PicoContainer as a future enhancement |
| ✓ | Page objects extend `BasePage`, private `By` locators (`id`, `[data-test=...]` CSS), no assertions, navigating actions return next page | `LoginPage.java` L9-27; `HomePage.java` L11-46; `README.md` L180-203 | 2/2 page objects; grep: no XPath, no `Thread.sleep` |
| ✓ | Explicit waits via `WaitUtils` using configured `timeout`; implicit wait zero | `WaitUtils.java` L16-30; `DriverFactory.java` L44-45 | — |
| ✓ | Hooks: Cucumber `@Before` starts browser, `@After` screenshots on failure and quits | `Hooks.java` L17-38 | — |
| ✓ | Config/test data from `environments/<env>.properties`, keys `baseUrl`, `browser`, `timeout`, `headless`, `username`, `password`; `-D` overrides | `ConfigReader.java` L23-51; `local.properties` L4-11 | Values not recorded here |
<!-- /ai-qa:managed:test-structure-and-conventions -->

<!-- ai-qa:managed:execution -->
## Execution

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ✓ | Documented commands: `mvn test`; `-Denvironment=local|int|qa`; `-Dcucumber.filter.tags="<expr>"`; `-Dbrowser=`, `-Dheadless=`; compile `mvn clean test-compile` | `README.md` L39-65; `.github/workflows/tests.yml` L35-42 | Documented/configured only; not run during discovery |
| ✓ | Reports: Cucumber HTML/JSON/JUnit XML in `target/cucumber-reports/`, Surefire reports, screenshots `target/screenshots/`, log `target/logs/test-execution.log` | `junit-platform.properties` L2-5; `README.md` L88-98 | — |
| ∅ | No feature-path run command documented | `README.md`; `TestRunner.java` uses `@SelectPackages("features")` | Use tags to select |
<!-- /ai-qa:managed:execution -->

<!-- ai-qa:managed:ci-cd -->
## CI/CD

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ✓ | GitHub Actions `UI Tests`: push to `main`, PRs, manual dispatch (env default `qa`, tag expression); compile, headless run, upload `test-results` artefact | `.github/workflows/tests.yml` L1-54 | No deployment stage; run history not checked |
<!-- /ai-qa:managed:ci-cd -->

<!-- ai-qa:managed:git -->
## Git

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ✓ | Default branch `main` | `git symbolic-ref refs/remotes/origin/HEAD` → `refs/remotes/origin/main` | Confirmed as base branch by user |
| ◐ | Branches use `feature/<slug>` | `feature/test-application`, `feature/testing-ai-tool` (2/2) | Small sample; no ticket keys |
| ✗ | Commit style: free-form; too few commits for a convention | `git log --all`: `Adding in skeleton of repo`, `Changing skeleton to test against test application` (2 commits) | ★ Conventional Commits adopted |
<!-- /ai-qa:managed:git -->

<!-- ai-qa:managed:prs -->
## PRs

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ✓ | No pull requests exist (open or closed) | GitHub MCP `list_pull_requests` state=all → 0 results (2026-10-08) | — |
| ∅ | No PR template or CONTRIBUTING | `.github/pull_request_template.md`, `.github/PULL_REQUEST_TEMPLATE*`, `CONTRIBUTING.md` absent | ★ Summary + List of Changes |
<!-- /ai-qa:managed:prs -->

<!-- ai-qa:managed:codeowners -->
## CODEOWNERS

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ∅ | No CODEOWNERS | `find . -name CODEOWNERS` (root, `.github/`, `docs/`) → none | — |
<!-- /ai-qa:managed:codeowners -->

<!-- ai-qa:managed:definition-of-done-and-qa-evidence -->
## Definition of done and QA evidence

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ∅ | No written DoD | no CONTRIBUTING, docs or templates | Framework candidate applied as ★ |
| ◐ | CI on PRs produces Cucumber/Surefire reports as QA evidence | `.github/workflows/tests.yml` L6, L44-54 | No required-check configuration visible |
<!-- /ai-qa:managed:definition-of-done-and-qa-evidence -->

<!-- ai-qa:managed:manual-scenario-format -->
## Manual scenario format

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ∅ | No manual test cases, templates or ticket ACs in the repository | repository file listing; `jira.md` empty | `.feature` files are automated BDD, not manual style evidence |
| ★ | `bdd` (Given / When / Then) | User confirmation 2026-10-08 | Framework default |
<!-- /ai-qa:managed:manual-scenario-format -->

<!-- ai-qa:managed:documentation -->
## Documentation

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ✓ | `README.md` is the docs root and only doc | `README.md` L1-259 | — |
| ∅ | No `docs/`, ADRs, glossary, wiki links | repository file listing | No remote docs pass (none configured) |
<!-- /ai-qa:managed:documentation -->

<!-- ai-qa:managed:integrations -->
## Integrations

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ✓ | Repository host GitHub (`github.com/declankbarrett/selenium-java-skeleton`); GitHub MCP read access verified | `git remote -v`; GitHub MCP `list_pull_requests` 2026-10-08 | MCP provided by the host session, not `.vscode/mcp.json` |
| ∅ | `gh` and `az` CLIs not installed | `which gh az` | — |
| ∅ | No work-item or docs provider; no ticket syntax | `jira.md` empty; no URLs/keys in repo, branches or commits | User chose Manual (2026-10-08) |
| ∅ | No `.vscode/mcp.json`; no environment-variable names referenced | file listing; grep of `src/`, workflow | Credentials are `-D` properties, not env vars |
<!-- /ai-qa:managed:integrations -->

<!-- ai-qa:managed:project-context -->
## Project context

| Status | Conclusion | Evidence (paths with lines / commands) | Note |
|---|---|---|---|
| ✓ | Selenium/Cucumber Java UI automation skeleton against public Sauce Demo; see `project.md` | `README.md` L1-15; `pom.xml` | High confidence; evidence as of `abdb42c` |
<!-- /ai-qa:managed:project-context -->

## Needs your input

| Question | Evidence / conflict sides | Option A (recommended) | Alternative | Decision unlocked |
|---|---|---|---|---|
| None outstanding | All questions answered 2026-10-08; branch protection remains `?` (non-blocking) | — | — | — |
