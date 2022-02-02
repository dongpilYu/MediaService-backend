package com.mediaservice.exception

enum class ErrorCode(val code: Int) {
    /**
     * 400 Bad Request
     */
    ROW_DOES_NOT_EXIST(40000),
    ROW_ALREADY_EXIST(40001),
    INVALID_FORMAT(40002),
    INVALID_PERMISSION(40003),
    ROW_ALREADY_DELETED(40004),
    NO_MORE_ITEM(40005),
    /**
     *  401 Unauthorized Error
     */
    INVALID_SIGN_IN(40100),
    INVALID_JWT(40101),
    NOT_ACCESSIBLE(40102),

    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER(50000),

    /**
     * 503 Server Unavailable Error
     */
    UNAVAILABLE_MAIL_SERVER(50300)
}
