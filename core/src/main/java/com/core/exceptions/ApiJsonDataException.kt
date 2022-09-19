package com.core.exceptions

import java.io.IOException

class ApiJsonDataException(override val message: String = "") : IOException(message)