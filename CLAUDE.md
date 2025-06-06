# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build commands
- Build entire project: `mvn clean install`
- Run all tests: `mvn clean test`
- Run a single test: `mvn test -Dtest=TestClassName#testMethodName`
- Compile without tests: `mvn clean install -DskipTests`
- Build with full frontend: `mvn clean install -Pfull-build`

## Docker commands

- Start all containers: `cd Docker-Infrastructure && docker-compose up -d`
- Build and start Hogajama: `cd Docker-Infrastructure && docker-compose up -d --build hogajama`
- View logs: `cd Docker-Infrastructure && docker-compose logs -f hogajama`
- Stop all containers: `cd Docker-Infrastructure && docker-compose down`

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

## Additional Technical Details

### Project Naming Clarification

- Hogarama: The overall project name (Home and Garden Automation)
- Habarama: The hardware component (Python on Raspberry Pi)
- Hogajama: The backend/frontend software component (Java/Angular)

### Sensor Hardware and Troubleshooting

- Hardware Components:
  - Moisture Sensors: YL-69 (probe) and YL-39 (logic) or SparkFun soil moisture sensors
  - Analog-to-Digital Converter: MCP3008 chip (Raspberry Pi has no analog inputs)
  - Controller: Raspberry Pi 3 Model B+
- Sensor Reading Interpretation:
  - Values range from 0-1023 (10-bit resolution)
  - Higher values (800-1023): dry soil
  - Lower values (0-200): wet soil
  - Moisture percentage = 1 - (Raw value/1024)
  - In logs, values like "809" indicate dry soil (~21% moisture)

### Logs and Monitoring

- Raspberry Pi Logs:
  ```
  sudo journalctl -u habarama        # View all logs
  sudo journalctl -u habarama -f     # Follow logs in real-time
  sudo journalctl -u habarama -n 50  # Show last 50 entries
  ```
- Backend System Logs:
  - JBoss/Wildfly logs: /standalone/log/server.log
  - Docker logs: docker-compose logs -f hogajama
  - OpenShift: Access via OpenShift console
- Database Access:
  ```
  mongo -u admin -p *** --authenticationDatabase admin
  use admin
  db.habarama.find()  # Shows all sensor data
  ```

### Ansible's Role

- Automates Raspberry Pi configuration for Habarama
- Configures network, SSH security, and sensor interface
- Installs required Python libraries
- Sets up systemd service for automatic startup
- Provides reproducible deployment process

### Remote Access to Raspberry Pi

- Connect via SSH: ssh pi@IP_ADDRESS
- Default credentials: username: pi, password: raspberry (if not changed)
- SSH key authentication may be configured

### Testing Raspberry Pi Components

- Test files location: `/Habarama/ansible/files/`
- Main script: `plant.py` (Python 2 - runs on Raspberry Pi)
- Test files:
  - `test_plant.py` - Full test suite (requires Python 3 conversion)
  - `test_plant_standalone.py` - Logic tests that work with Python 2 version
- Running tests:
  ```bash
  cd Habarama/ansible/files
  python3 -m unittest test_plant_standalone.py -v
  ```
- Test coverage includes:
  - Command-line argument parsing
  - Actor/sensor initialization
  - MQTT client functionality
  - GPIO operations (mocked)
  - Sensor data formatting
  - Configuration validation