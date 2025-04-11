# Hogarama - Onboarding Guide for New Developers

## Overview

Hogarama is a showcase project by Gepardec, serving as a demonstration platform for OpenShift and various technologies.
The system enables monitoring and control of plants using sensors and actuators.

## Architecture

The system consists of three main components:

- **Sensor Network** (Raspberry Pi with sensors/actuators)
- **Cloud Control Server** (OpenShift-based)
- **Client Application** (Web/Mobile)

## Core Components

- **AMQ/Kafka**: Messaging systems for communication
- **MongoDB**: Storage of sensor data
- **PostgreSQL**: Management data and configuration
- **Hogajama**: Backend (Java EE) and Frontend (Angular/Ionic)
- **Keycloak**: Single Sign-On and Identity Management

## Modular Structure

Hogarama uses a modular architecture with optional components:

- **Security Modules**: security-jwt (standard) or security-dummy (development)
- **Messaging Modules**: messaging-mdb (ActiveMQ), messaging-kafka, or messaging-rest (testing)

## Development Environments

1. **CodeReady Workspaces**: Browser-based development in OpenShift
2. **Local Wildfly Installation**: With Docker containers for databases

## Habarama (Raspberry Pi)

- Responsible for data collection and device control
- Uses MQTT for connecting to the backend
- Supports various sensors (moisture, etc.) and actuators (pumps)
- Setup is done via Ansible scripts

## Testing and Development

- Raspberry Pi mocks for simulation (CLI and GUI)
- Various configuration options for local development
- Maven-based build process with optional profiles

## Getting Started

1. Clone the repository
2. Choose development environment setup (Docker or local)
3. Compile with appropriate profiles: `mvn clean install -P security-dummy,messaging-rest`
4. Start Docker containers: `cd Docker-Infrastructure && docker-compose up -d`

## Documentation

More details can be found in the [Wiki](https://github.com/Gepardec/Hogarama/wiki) and in the codebase.