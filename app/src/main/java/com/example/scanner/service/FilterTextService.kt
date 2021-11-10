package com.example.scanner.service

interface FilterTextService {
    fun filterTextForPhoneNumbers(text: String): List<Pair<String, String>>

    fun filterTextForEmails(text: String): List<Pair<String, String>>

    fun filterTextForLinks(text: String): List<Pair<String, String>>
}