package com.test.exceptions

class AppUncaughtException(exception: Throwable): RuntimeException("Unhandled app exception",exception)