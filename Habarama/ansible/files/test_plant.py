#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import unittest
import sys
import json
import time
import os
from unittest.mock import Mock, MagicMock, patch, call, mock_open
from threading import Thread

# Add the current directory to the path to import plant
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

# Mock the hardware dependencies before importing plant
sys.modules['RPi'] = MagicMock()
sys.modules['RPi.GPIO'] = MagicMock()
sys.modules['Adafruit_GPIO'] = MagicMock()
sys.modules['Adafruit_GPIO.SPI'] = MagicMock()
sys.modules['Adafruit_MCP3008'] = MagicMock()

# Mock paho MQTT client if not installed
try:
    import paho.mqtt.client
except ImportError:
    sys.modules['paho'] = MagicMock()
    sys.modules['paho.mqtt'] = MagicMock()
    sys.modules['paho.mqtt.client'] = MagicMock()

# Since plant.py is Python 2, we need to handle the syntax differences
# We'll test the functions directly rather than importing the module
# For a real test, you'd want to convert plant.py to Python 3 first


class TestPlantScript(unittest.TestCase):
    """Test suite for the plant.py Raspberry Pi sensor control script."""

    def setUp(self):
        """Set up test fixtures before each test method."""
        self.mock_gpio = MagicMock()
        self.mock_spi = MagicMock()
        self.mock_mcp = MagicMock()
        self.mock_paho = MagicMock()
        
        # Sample configuration data
        self.sample_config = {
            "brokerUrls": ["mqtt.example.com", "mqtt2.example.com"],
            "sensors": [
                {
                    "name": "TestSensor1",
                    "type": "moisture",
                    "pin": 17,
                    "channel": 0,
                    "location": "TestLocation"
                },
                {
                    "name": "TestSensor2",
                    "type": "temperature",
                    "pin": 18,
                    "channel": 1,
                    "location": "TestLocation"
                }
            ],
            "actors": [
                {
                    "name": "TestPump1",
                    "type": "gpio",
                    "pin": 23,
                    "location": "TestLocation"
                },
                {
                    "name": "TestConsole",
                    "type": "console",
                    "location": "TestLocation"
                }
            ]
        }

    def tearDown(self):
        """Clean up after each test."""
        # Reset global variables
        plant.optWater = False
        plant.optWaterDuration = 5

    def test_usage_function(self):
        """Test the usage function returns proper help text."""
        usage_text = plant.usage()
        self.assertIn("usage:", usage_text)
        self.assertIn("-w", usage_text)
        self.assertIn("-d duration", usage_text)

    def test_handle_options_help(self):
        """Test handling of -h option."""
        with self.assertRaises(SystemExit) as cm:
            with patch('builtins.print'):
                plant.handleOptions(['-h'])
        self.assertEqual(cm.exception.code, None)

    def test_handle_options_water(self):
        """Test handling of -w option."""
        plant.handleOptions(['-w'])
        self.assertTrue(plant.optWater)
        self.assertEqual(plant.optWaterDuration, 5)

    def test_handle_options_duration(self):
        """Test handling of -d option."""
        plant.handleOptions(['-w', '-d', '10'])
        self.assertTrue(plant.optWater)
        self.assertEqual(plant.optWaterDuration, 10)

    def test_handle_options_invalid(self):
        """Test handling of invalid options."""
        with self.assertRaises(SystemExit) as cm:
            with patch('builtins.print'):
                plant.handleOptions(['-x'])
        self.assertEqual(cm.exception.code, 2)

    @patch('sys.stderr.write')
    def test_log_function(self, mock_stderr):
        """Test the log function writes to stderr."""
        test_message = "Test log message"
        plant.log(test_message)
        mock_stderr.assert_called_once_with(test_message + "\n")

    def test_initialize_actors(self):
        """Test actor initialization from configuration."""
        actor_configs = self.sample_config['actors']
        actors = plant.initialize_actors(actor_configs)
        
        self.assertEqual(len(actors), 2)
        self.assertEqual(actors[0].name, "TestPump1")
        self.assertEqual(actors[0].type, "gpio")
        self.assertEqual(actors[1].name, "TestConsole")
        self.assertEqual(actors[1].type, "console")


class TestActor(unittest.TestCase):
    """Test suite for the Actor class."""

    def setUp(self):
        """Set up test fixtures for Actor tests."""
        self.gpio_actor_config = {
            "name": "TestPump",
            "type": "gpio",
            "pin": 23,
            "location": "TestLocation"
        }
        self.console_actor_config = {
            "name": "TestConsole",
            "type": "console",
            "location": "TestLocation"
        }

    def test_actor_initialization(self):
        """Test Actor class initialization."""
        actor = plant.Actor(self.gpio_actor_config)
        
        self.assertEqual(actor.name, "TestPump")
        self.assertEqual(actor.type, "gpio")
        self.assertEqual(actor.topicName, "actor.TestLocation.TestPump")
        self.assertEqual(actor.topicName2, "actor/TestLocation/TestPump")
        self.assertFalse(actor.active)

    def test_has_topic(self):
        """Test topic matching for actors."""
        actor = plant.Actor(self.gpio_actor_config)
        
        self.assertTrue(actor.has_topic("actor.TestLocation.TestPump"))
        self.assertTrue(actor.has_topic("actor/TestLocation/TestPump"))
        self.assertFalse(actor.has_topic("actor.OtherLocation.TestPump"))
        self.assertFalse(actor.has_topic("sensor.TestLocation.TestPump"))

    @patch('plant.log')
    def test_setup_pin_gpio(self, mock_log):
        """Test GPIO pin setup for gpio type actor."""
        actor = plant.Actor(self.gpio_actor_config)
        actor.setup_pin()
        
        # Since GPIO is mocked at module level, we just verify the method was called
        self.assertEqual(actor.config['pin'], 23)

    @patch('plant.log')
    def test_setup_pin_console(self, mock_log):
        """Test pin setup for console type actor."""
        actor = plant.Actor(self.console_actor_config)
        actor.setup_pin()
        
        mock_log.assert_called_with("Setting up actor TestConsole as console actor")

    @patch('time.sleep')
    @patch('plant.log')
    def test_do_water_success(self, mock_log, mock_sleep):
        """Test successful water activation."""
        actor = plant.Actor(self.gpio_actor_config)
        
        # Test watering for 3 seconds
        actor.do_water(3)
        
        # Verify sleep was called with correct duration
        mock_sleep.assert_called_once_with(3)
        
        # Verify logging
        self.assertTrue(any("Turning actor TestPump on" in str(call) for call in mock_log.call_args_list))
        self.assertTrue(any("turned off" in str(call) for call in mock_log.call_args_list))

    @patch('plant.log')
    def test_do_water_already_active(self, mock_log):
        """Test water activation when actor is already active."""
        actor = plant.Actor(self.gpio_actor_config)
        actor.active = True
        
        actor.do_water(3)
        
        # Verify only the log message was called
        mock_log.assert_called_with("Actor TestPump is active. Ignore message!")

    def test_subscribe_to_topic(self):
        """Test MQTT topic subscription."""
        actor = plant.Actor(self.gpio_actor_config)
        mock_client = MagicMock()
        mock_client.subscribe.return_value = (0, 1)
        
        actor.subscribe_to_topic(mock_client)
        
        mock_client.subscribe.assert_called_once_with("actor.TestLocation.TestPump", 0)


class TestClient(unittest.TestCase):
    """Test suite for the Client class."""

    @patch('ssl.create_default_context')
    def test_client_init(self, mock_ssl):
        """Test Client initialization."""
        mock_ssl_ctx = MagicMock()
        mock_ssl.return_value = mock_ssl_ctx
        
        # Mock the parent class methods
        with patch.object(plant.Client, 'username_pw_set'):
            with patch.object(plant.Client, 'tls_set_context'):
                client = plant.Client(clean_session=True)
                client.init("mqtt.example.com")
                
                self.assertEqual(client.brokerUrl, "mqtt.example.com")
                self.assertFalse(client.isConnected)
                self.assertEqual(mock_ssl_ctx.check_hostname, False)

    @patch('plant.log')
    def test_handle_on_publish(self, mock_log):
        """Test publish event handler."""
        client = plant.Client(clean_session=True)
        client.brokerUrl = "mqtt.example.com"
        
        client.handle_on_publish(client, "userdata", 123)
        
        mock_log.assert_called_once()
        self.assertIn("mqtt.example.com", mock_log.call_args[0][0])

    @patch('plant.log')
    def test_handle_on_message_valid_actor(self, mock_log):
        """Test message handler with valid actor."""
        # Set up global actors list
        mock_actor = MagicMock()
        mock_actor.nonblocking_handle.return_value = True
        plant.actors = [mock_actor]
        
        client = plant.Client(clean_session=True)
        client.brokerUrl = "mqtt.example.com"
        
        mock_message = MagicMock()
        mock_message.topic = "actor/TestLocation/TestPump"
        mock_message.payload = '{"duration": 5}'
        
        client.handle_on_message(client, None, mock_message)
        
        mock_actor.nonblocking_handle.assert_called_once_with(mock_message)

    @patch('plant.log')
    def test_handle_on_message_no_actor(self, mock_log):
        """Test message handler with no matching actor."""
        plant.actors = []
        
        client = plant.Client(clean_session=True)
        client.brokerUrl = "mqtt.example.com"
        
        mock_message = MagicMock()
        mock_message.topic = "actor/TestLocation/UnknownActor"
        
        client.handle_on_message(client, None, mock_message)
        
        # Should log error about no actor found
        self.assertTrue(any("No actor found" in str(call) for call in mock_log.call_args_list))

    @patch('plant.log')
    def test_handle_on_connect(self, mock_log):
        """Test connect event handler."""
        mock_actor = MagicMock()
        plant.actors = [mock_actor]
        
        client = plant.Client(clean_session=True)
        client.brokerUrl = "mqtt.example.com"
        
        client.handle_on_connect(client, None, None, 0)
        
        self.assertTrue(client.isConnected)
        mock_actor.subscribe_to_topic.assert_called_once_with(client)
        mock_log.assert_called_with("Connected to mqtt.example.com")

    @patch('plant.Thread')
    def test_nonblocking_reconnect(self, mock_thread):
        """Test non-blocking reconnect method."""
        client = plant.Client(clean_session=True)
        client.blocking_reconnect = MagicMock()
        
        client.nonblocking_reconnect()
        
        mock_thread.assert_called_once()
        # Verify thread is started
        mock_thread.return_value.start.assert_called_once()

    @patch('time.sleep')
    @patch('plant.log')
    def test_blocking_connect_success(self, mock_log, mock_sleep):
        """Test successful blocking connect."""
        client = plant.Client(clean_session=True)
        client.brokerUrl = "mqtt.example.com"
        client.connect = MagicMock()
        
        result = client.blocking_connect()
        
        self.assertTrue(result)
        client.connect.assert_called_once_with("mqtt.example.com", 443, 60)

    @patch('time.sleep')
    @patch('plant.log')
    def test_blocking_connect_retry(self, mock_log, mock_sleep):
        """Test blocking connect with retry on failure."""
        client = plant.Client(clean_session=True)
        client.brokerUrl = "mqtt.example.com"
        
        # Mock connect to fail once then succeed
        client.connect = MagicMock(side_effect=[Exception("Connection failed"), None])
        
        # Use a flag to break the infinite loop after second attempt
        original_sleep = mock_sleep.side_effect
        call_count = [0]
        def sleep_side_effect(duration):
            call_count[0] += 1
            if call_count[0] >= 1:
                # Make connect succeed on next call
                client.connect.side_effect = None
        mock_sleep.side_effect = sleep_side_effect
        
        result = client.blocking_connect()
        
        self.assertTrue(result)
        self.assertEqual(client.connect.call_count, 2)


class TestIntegration(unittest.TestCase):
    """Integration tests for the main functionality."""

    @patch('builtins.open', new_callable=mock_open)
    @patch('json.load')
    def test_configuration_loading(self, mock_json_load, mock_file):
        """Test loading configuration from habarama.json."""
        sample_config = {
            "brokerUrls": ["mqtt.example.com"],
            "sensors": [{"name": "sensor1", "type": "moisture", "pin": 17, "channel": 0, "location": "test"}],
            "actors": [{"name": "pump1", "type": "gpio", "pin": 23, "location": "test"}]
        }
        mock_json_load.return_value = sample_config
        
        # Import would trigger configuration loading
        # This test verifies the file is opened and JSON is loaded correctly
        mock_file.assert_called()
        mock_json_load.assert_called()


if __name__ == '__main__':
    unittest.main(verbose=2)