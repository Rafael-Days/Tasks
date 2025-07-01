package com.devmasterteam.tasks.service.model

class ValidationModel (message: String = ""){

    private var status: Boolean = true
    private var errorMessage = ""

    init{
        if(message != ""){
            status = false
            errorMessage = message
        }
    }

    fun status() = status
    fun message() = errorMessage
}