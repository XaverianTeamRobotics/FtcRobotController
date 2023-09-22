---
title: Logging
---
# Logging

The `Logging` class can be used to log data to the telemetry panel on the *Driver Station* phone.

## Usage

The `Logging` class has a couple main methods:
 - `log(String msg)` - Logs a message to the telemetry panel
 - `log(String key, Object value)` - Logs a value to the telemetry panel
 - `update()` - Send the data to the telemetry panel
 - `clear()` - Clears the telemetry panel

:::tip
All 4 of those methods are static, so there is no need to create an instance of the `Logging` class.
:::

:::note
After any of the `log()` or `clear()` methods are called, you must call the `update()` method to send the data to the telemetry panel.
:::