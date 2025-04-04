# Docker Infrastructure for Hogarama

This directory contains the Docker infrastructure setup for the Hogarama project.

## Prerequisites

- Docker
- Docker Compose

## Components

The infrastructure consists of the following components:

- **MongoDB**: Database for sensor data
- **PostgreSQL**: Database for user management and configuration
- **AMQ**: Message broker for sensor data
- **Keycloak**: Identity and access management
- **Hogajama**: Java application with WildFly for the backend and frontend

## Simplified Build Process

The build process has been simplified using Source-to-Image (S2I) with WildFly JDK 11. Key improvements:

1. **Automated builds**:
    - Source-to-Image automatically builds the project
    - No manual triggering of build steps required

2. **Incremental builds**:
    - Only modified parts are rebuilt
    - Maven artifacts are cached

3. **Configuration reuse**:
    - Local configuration scripts can be easily reused
    - Same configuration for development and deployment

4. **Build-Once principle**:
    - Code is compiled only once in the build process
    - Built artifacts are reused

## Usage

### Start all services

```bash
docker-compose up -d
```

### Build and start only Hogajama

```bash
docker-compose up -d --build hogajama
```

### View logs

```bash
docker-compose logs -f hogajama
```

### Stop all services

```bash
docker-compose down
```

## Development

For development, the source code is mounted into the container, allowing for live development:

1. Make changes to your code
2. The S2I process will detect changes and rebuild
3. WildFly will automatically deploy the new artifacts

## Configuration

Configuration is controlled through environment variables in the `local_env/hogarama_local.env` file.