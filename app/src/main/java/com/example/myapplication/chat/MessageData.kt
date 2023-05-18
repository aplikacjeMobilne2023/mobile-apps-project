package com.example.myapplication.chat

data class MessageData(val messageId: String? = null,
                       val authorId: String? = null,
                       val author: String? = null,
                       val authorImageSource: String? = null,
                       val text: String? = null)
