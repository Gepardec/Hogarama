#!/bin/bash
# Script to run plant.py tests

echo "Running tests for plant.py..."

# Change to the directory containing the tests
cd "$(dirname "$0")"

# Determine which Python command to use
if command -v python3 &> /dev/null; then
    PYTHON_CMD=python3
elif command -v python &> /dev/null; then
    PYTHON_CMD=python
else
    echo "Error: Python not found!"
    exit 1
fi

echo "Using Python command: $PYTHON_CMD"

# Run tests with coverage
echo "Running unit tests with coverage..."
$PYTHON_CMD -m pytest test_plant.py -v --cov=plant --cov-report=term-missing --cov-report=html

# Run tests with standard unittest if pytest is not available
if [ $? -ne 0 ]; then
    echo "pytest not found, running with unittest..."
    $PYTHON_CMD -m unittest test_plant.py -v
fi

echo "Tests completed!"