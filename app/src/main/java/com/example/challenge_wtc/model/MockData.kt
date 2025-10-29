
package com.example.challenge_wtc.model

import java.util.Date

data class Operator(
    val name: String = "Jessica"
)

data class Customer(
    val id: String,
    val name: String,
    val score: Int,
    val tags: List<String>,
    val lastInteraction: Date,
    val avatarUrl: String? = null,
    val phone: String,
    val email: String,
    val status: String,
    val notes: String = "",
    val campaigns: List<Campaign> = emptyList(),
    val messages: List<Message> = emptyList()
)

data class Campaign(
    val id: String,
    val title: String,
    val message: String,
    val imageUrl: String? = null,
    val segment: String,
    val sentDate: Date
)

data class Message(
    val id: String,
    val text: String,
    val isFromOperator: Boolean,
    val timestamp: Date,
    val isImportant: Boolean = false
)

object MockData {
    val operator = Operator()

    val customers = listOf(
        Customer(
            id = "1",
            name = "John Doe",
            score = 95,
            tags = listOf("VIP", "Active"),
            lastInteraction = Date(),
            phone = "+123456789",
            email = "john.doe@example.com",
            status = "Active"
        ),
        Customer(
            id = "2",
            name = "Jane Smith",
            score = 82,
            tags = listOf("New"),
            lastInteraction = Date(),
            phone = "+987654321",
            email = "jane.smith@example.com",
            status = "Inactive"
        )
    )

    val campaigns = listOf(
        Campaign(
            id = "c1",
            title = "Summer Sale!",
            message = "Get 50% off on all items.",
            segment = "All",
            sentDate = Date()
        )
    )

    fun getMessagesForCustomer(customerId: String): List<Message> {
        return listOf(
            Message("m1", "Hello! How can I help you?", true, Date()),
            Message("m2", "I have a question about my order.", false, Date()),
        )
    }
}
