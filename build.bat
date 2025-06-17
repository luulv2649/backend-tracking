@echo off
setlocal enabledelayedexpansion

REM ===========================================
REM Spring Boot Docker Container Manager
REM ===========================================

echo ========================================
echo  Spring Boot Container Manager
echo ========================================
echo.

REM Configuration variables
set IMAGE_NAME=spring-boot-app
set CONTAINER_NAME=spring-boot-container
set HOST_PORT=8888
set CONTAINER_PORT=8888
set NETWORK_NAME=spring-boot-network

REM Check if Docker is running
docker version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker is not running or not installed!
    echo Please start Docker Desktop and try again.
    pause
    exit /b 1
)

echo Docker is running. Proceeding with container operations...
echo.

REM Stop and remove existing container if it exists
echo Checking for existing container...
docker ps -a --format "table {{.Names}}" | findstr /i "%CONTAINER_NAME%" >nul 2>&1
if %errorlevel% equ 0 (
    echo Stopping existing container: %CONTAINER_NAME%
    docker stop %CONTAINER_NAME% >nul 2>&1
    echo Removing existing container: %CONTAINER_NAME%
    docker rm %CONTAINER_NAME% >nul 2>&1
)

REM Create network if it doesn't exist
echo Creating Docker network if not exists...
docker network ls | findstr /i "%NETWORK_NAME%" >nul 2>&1
if %errorlevel% neq 0 (
    docker network create %NETWORK_NAME%
    echo Network %NETWORK_NAME% created successfully.
) else (
    echo Network %NETWORK_NAME% already exists.
)

REM Build the Docker image
echo.
echo ========================================
echo Building Docker image: %IMAGE_NAME%
echo ========================================
docker build -t %IMAGE_NAME% .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build Docker image!
    pause
    exit /b 1
)

echo Docker image built successfully!
echo.

REM Run the container
echo ========================================
echo Running container: %CONTAINER_NAME%
echo ========================================
docker run -d ^
    --name %CONTAINER_NAME% ^
    --network %NETWORK_NAME% ^
    -p %HOST_PORT%:%CONTAINER_PORT% ^
    -e SPRING_PROFILES_ACTIVE=prod ^
    --restart unless-stopped ^
    --memory=2g ^
    --cpus=1.5 ^
    %IMAGE_NAME%

if %errorlevel% neq 0 (
    echo ERROR: Failed to start container!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Container started successfully!
echo ========================================
echo Container Name: %CONTAINER_NAME%
echo Application URL: http://localhost:%HOST_PORT%
echo Health Check: http://localhost:%HOST_PORT%/actuator/health
echo.

REM Wait for application to start
echo Waiting for application to start...
timeout /t 10 /nobreak >nul

REM Check container status
echo Checking container status...
docker ps --filter "name=%CONTAINER_NAME%" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo.
echo ========================================
echo Useful Commands:
echo ========================================
echo View logs:     docker logs %CONTAINER_NAME%
echo Follow logs:   docker logs -f %CONTAINER_NAME%
echo Stop container: docker stop %CONTAINER_NAME%
echo Remove container: docker rm %CONTAINER_NAME%
echo Access shell:  docker exec -it %CONTAINER_NAME% /bin/bash
echo.

pause >nul