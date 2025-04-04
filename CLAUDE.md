# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build commands

- Build entire project: `mvn clean install`
- Run all tests: `mvn clean test`
- Run a single test: `mvn test -Dtest=TestClassName#testMethodName`
- Compile without tests: `mvn clean install -DskipTests`
- Build with full frontend: `mvn clean install -Pfull-build`

## Code style

- Java: Use standard Java conventions with 4-space indentation
- Java version: JDK 17 (specified in pom.xml)
- Testing framework: JUnit 5 (Jupiter)
- Error handling: Use explicit exception handling with appropriate error models
- Naming: Follow Java camelCase for variables/methods, PascalCase for classes
- Documentation: Document public APIs with Javadoc
- Imports: Organize imports alphabetically, no wildcard imports
- Angular/TypeScript: Follow Angular style guide with 2-space indentation

## Project structure

- Maven multi-module project
- Backend: Java EE application with REST services
- Frontend: Angular/Ionic for UI
- Messaging: Both Kafka and JMS (AMQ) implementations