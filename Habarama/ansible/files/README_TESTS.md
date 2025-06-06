# Testing plant.py

This directory contains unit tests for the `plant.py` script that runs on Raspberry Pi devices in the Hogarama system.

## Test Files

1. **`test_plant.py`** - Full unit test suite that would work with a Python 3 version of plant.py
2. **`test_plant_standalone.py`** - Standalone tests that test the logic without importing plant.py (works with current Python 2 version)

## Test Coverage

The test suite (`test_plant.py`) covers:

### Core Functions
- Command-line argument parsing (`-w`, `-d` options)
- Configuration file loading
- Logging functionality

### Actor Class
- Initialization with different types (gpio, console)
- Topic matching for MQTT messages
- GPIO pin setup and control
- Water pump activation/deactivation
- Thread safety for concurrent operations
- MQTT topic subscription

### Client Class (MQTT)
- Connection initialization with SSL/TLS
- Authentication setup
- Connection/reconnection logic (blocking and non-blocking)
- Message handling and routing to actors
- Event handlers (connect, disconnect, publish)

### Integration Tests
- Configuration loading from `habarama.json`
- Actor initialization from configuration

## Running the Tests

### Prerequisites

1. Install test dependencies:
   ```bash
   pip install -r requirements-test.txt
   ```

2. If you don't have pytest, you can use the built-in unittest module.

### Running Tests

#### For Current Python 2 plant.py

Run the standalone tests that don't require importing plant.py:
```bash
python3 -m unittest test_plant_standalone.py -v
```

#### For Future Python 3 Version

Once plant.py is converted to Python 3, you can run the full test suite:

##### Option 1: Using the test script
```bash
./run_tests.sh
```

#### Option 2: Using pytest directly
```bash
# Run all tests with coverage
pytest test_plant.py -v --cov=plant --cov-report=term-missing

# Run specific test class
pytest test_plant.py::TestActor -v

# Run specific test method
pytest test_plant.py::TestActor::test_do_water_success -v
```

#### Option 3: Using unittest
```bash
# Run all tests
python -m unittest test_plant.py

# Run with verbose output
python -m unittest test_plant.py -v

# Run specific test class
python -m unittest test_plant.TestActor

# Run specific test method
python -m unittest test_plant.TestActor.test_do_water_success
```

## Test Structure

The tests are organized into three main test classes:

1. **TestPlantScript**: Tests for module-level functions and initialization
2. **TestActor**: Tests for the Actor class functionality
3. **TestClient**: Tests for the MQTT Client class
4. **TestIntegration**: Integration tests for combined functionality

## Mocking Strategy

Since the tests run outside of a Raspberry Pi environment, the following components are mocked:

- `RPi.GPIO`: GPIO operations are mocked to avoid hardware dependencies
- `Adafruit_MCP3008`: ADC operations are mocked
- `paho.mqtt.client`: MQTT client operations can be tested without a broker
- File I/O: Configuration file reading is mocked
- Threading: Thread operations are tested but actual delays are mocked

## Coverage Report

After running tests with pytest, you'll get:
- Console output showing coverage percentages
- HTML report in `htmlcov/index.html` for detailed line-by-line coverage

## Python 2 Compatibility Note

The `plant.py` script is currently written in Python 2. The standalone test file (`test_plant_standalone.py`) tests the logic without importing the module, making it compatible with Python 3 test runners. For full integration testing, consider converting `plant.py` to Python 3.

## Future Improvements

1. Convert `plant.py` to Python 3 for better compatibility
2. Add tests for error scenarios (sensor failures, network issues)
3. Add performance tests for sensor reading intervals
4. Add integration tests with a test MQTT broker
5. Add tests for edge cases (invalid sensor values, malformed messages)
6. Mock the actual sensor reading logic with realistic test data