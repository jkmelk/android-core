package com.core.exceptions

import java.io.IOException

class NoConnectionException(override val message: String = "") : IOException(message)