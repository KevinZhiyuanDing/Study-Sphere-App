## Study Sphere (UBC)

This repository contains two main components for the Study Sphere project:

- App — a Java desktop client (JavaFX) that provides the UI and client-side networking.
- Server — a Python service that stores user/session data and provides an API used by the App.

Both projects are developed side-by-side in this mono-repo to simplify integration and testing.

## Repository layout

- App/ — Java (Gradle) project containing the desktop client. The main module is `cpen.app` and the entry point is `cpen.app.Main`.
  - The Java sources and JavaFX FXML files live under `App/src/main/java` and `App/src/main/resources`.
  - Gradle wrapper files are included (`gradlew`, `gradlew.bat`, `gradle/wrapper`).

- Server/ — Python application providing the backend API and web-scraping helpers.
  - The Python entry script is `Server/src/main.py` and app package is under `Server/src/app`.
  - Example data and DB files (for local development) may be present in `Server/` (e.g. `study_sphere.db`, pickles).

- Other files
  - Top-level `.gitignore` (added) now ignores build artifacts across the repo.
  - Tests for each component are kept inside `App/test` and `Server/tests` respectively.

## What's changed recently

- A repository-level `.gitignore` was added to ignore generated build artifacts (for example `**/build/`) so compiled files from the App and other temporary build outputs are not accidentally committed. The App subproject still contains its own `.gitignore` for finer rules.

## Build & run (Windows / PowerShell)

App (Java/Gradle)

1. From the repo root, change into the `App` folder and build using the bundled Gradle wrapper:

```powershell
cd .\App
.\gradlew.bat build
```

2. To run the application locally (JavaFX runtime defined in Gradle):

```powershell
.\gradlew.bat run
```

If you prefer to run from an IDE, import `App` as a Gradle project (IntelliJ is recommended for JavaFX projects).

Server (Python)

1. Create a virtual environment and install dependencies (from `Server/`):

```powershell
cd .\Server
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
```

2. Start the server for local development:

```powershell
python src\main.py
```

Adjust the above commands for your shell or OS as needed.

## Tests

- App: Gradle tests can be run with `.\gradlew.bat test` inside the `App` directory.
- Server: Python tests can be run with your preferred test runner (e.g., `pytest`) from the `Server` directory after installing dependencies.

## Notes and next steps

- The repository is intentionally monorepo-style to make end-to-end testing easier.
- If you want the `.gitignore` to be stricter (for example, ignore only `**/build/` and nothing else), tell me and I will update it.
- I can also help: removing already-committed build files from git history, adding a CI task to run builds/tests, or creating a lightweight CONTRIBUTING.md with developer setup steps.

## Problem statement (brief)

UBC students struggle to find study room availability because information is scattered across multiple platforms. Study Sphere aggregates room availability and helps students find study partners for courses.

---

For questions or to get the development environment set up, open an issue or ask for help in the repository.

* App
  * package: cpen.app
    * desktop user interface
  * package: cpen.network
    * server communication code
* Server
  * package: cpen.database
    * storage of user data
  * package: cpen.network
    * client communication code
  * package: cpen.ui
    * starting point of the server code
  * package: cpen.webscrape
    * code for pulling room availability off of website
* DesignSolutionScreenshots
  * draft of screenshots
* Stubs
  * initial code plan (deprecated)
* .md files
  * initial planning
* Notes
  * All code is in App and Server
  * App is an intellij project
    * Starts in cpen.app
  * Server is a pycharm project
    * Starts in cpen.ui, structure is to run the server starting from this package, all other packages are helpers
## Problem statement
UBC students struggle to stay informed about study room locations, availability because of the scattered postings across various platforms. Students often have to check multiple sources, 
such as websites and help desks to find a suitable space. This can make it difficult to plan study sessions effectively, especially during peak times when rooms are in high demand. Additionally, 
it can be hard to find study buddies. Currently, there is no platform that allows students to connect to study courses together. Many students may be hesitant to reach out to peers and 
may not know their availability. 


