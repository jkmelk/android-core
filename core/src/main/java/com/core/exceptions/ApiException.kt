package com.core.exceptions

import java.io.IOException

class ApiException(override val message: String = "") : IOException(message)