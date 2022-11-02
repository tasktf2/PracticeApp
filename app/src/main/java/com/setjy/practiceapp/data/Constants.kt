package com.setjy.practiceapp.data

object Constants {
    const val DATABASE_NAME = "zulip.db"

    const val NARROW_STREAM = "stream"

    const val NARROW_TOPIC = "topic"

    const val MESSAGE_SEND_TYPE = "stream"

    const val MESSAGES_TO_LOAD_BEFORE = 20
    const val MESSAGES_TO_LOAD_AFTER = 0
    const val ANCHOR_NEWEST = "newest"
    const val MESSAGES_TO_SAVE: Int = 50
}

object HttpClient {
    const val CONNECT_TIMEOUT = 10L
    const val READ_TIMEOUT = 10L
    const val WRITE_TIMEOUT = 10L
}