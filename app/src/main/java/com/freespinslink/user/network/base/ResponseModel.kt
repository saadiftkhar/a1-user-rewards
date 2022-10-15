package com.freespinslink.user.network.base

data class ResponseModel<T>(val successResponse: T?, val errorResponse: ErrorResponse?)