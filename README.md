# Selenium Java Test Application Tests

A Selenium 4, Cucumber BDD, and JUnit 5 UI test framework for the local
Docker-based Test Application. The tests cover the same scope as the
Playwright TypeScript skeleton for this application.

## Prerequisites

- Java 21 or newer
- Maven 3.9 or newer
- Chrome, Edge, or Firefox
- The Test Application running in Docker, with its database initialized

The app's README describes startup and database initialization. Its frontend
must be reachable at `http://localhost:8081`, and its API at
`http://localhost:8080`.

## Run tests

```shell
mvn clean test
```

By default, tests use the `local` configuration, Chrome, and a visible browser.
Choose a browser, run headlessly, or override the application URL as needed:

```shell
mvn test -Dbrowser=firefox
mvn test -Dheadless=true
mvn test -DbaseUrl=http://localhost:8081
```

Filter scenarios by Cucumber tags:

```shell
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@functional"
```

## Test coverage

- **Smoke:** users and projects pages load; seeded users are listed and their details can be viewed.
- **Functional:** create users and projects; validate empty forms; navigate between users and projects.

Newly created test users and projects use unique identifiers so runs do not
collide with existing data in the shared local database.

## Framework structure

```text
src
├── main
│   ├── java
│   │   ├── config       # Environment configuration
│   │   ├── driver       # Thread-local WebDriver setup
│   │   ├── pages        # Page objects for users and projects
│   │   └── utils        # Wait and screenshot helpers
│   └── resources
│       ├── environments # local, int, and qa properties
│       └── log4j2.xml
└── test
    ├── java
    │   ├── hooks        # Browser lifecycle and failure screenshots
    │   ├── runners      # JUnit 5 Cucumber runner
    │   └── stepdefinitions
    └── resources
        └── features     # Availability, users, projects, and navigation
```

## Reports

Test reports, logs, and failure screenshots are written under `target/`:

- `target/cucumber-reports/`
- `target/surefire-reports/`
- `target/screenshots/`
- `target/logs/`
