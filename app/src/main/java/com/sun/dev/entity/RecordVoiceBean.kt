package com.sun.dev.entity

data class RecordVoiceBean(
    val header: Header,
    val payload: Payload
)

data class Payload(
    val duration: Int,
    val result: String
)

data class Header(
    val message_id: String,
    val name: String,
    val namespace: String,
    val status: Int,
    val status_text: String,
    val task_id: String
)