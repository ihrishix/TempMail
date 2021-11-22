package com.hrishi.tempmail

data class Mail(
    val attachments: List<Any>,
    val body: String,
    val date: String,
    val from: String,
    val htmlBody: String,
    val id: Int,
    val subject: String,
    val textBody: String
)