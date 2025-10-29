
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
            title = "Campanha de Verão 2025",
            description = "Descubra nossos novos produtos e aproveite descontos exclusivos de até 30% nesta temporada.",
            imageUrl = "https://picsum.photos/seed/summer/600/400",
            externalLink = "https://www.example.com"
        ),
        Campaign(
            id = "c2",
            title = "Inscrições Abertas: Workshop",
            description = "Aprenda com especialistas do setor em nosso workshop exclusivo. Vagas limitadas, inscreva-se já!",
            imageUrl = "https://picsum.photos/seed/workshop/600/400",
            externalLink = "https://www.example.com/workshop"
        ),
        Campaign(
            id = "c3",
            title = "Oferta Relâmpago",
            description = "Só hoje! Use o cupom RELAMPAGO20 para 20% de desconto em todo o site. Não perca!",
            imageUrl = "https://picsum.photos/seed/sale/600/400",
            externalLink = "https://www.example.com/sale"
        )
    )

    fun getMessagesForCustomer(customerId: String): List<Message> {
        return listOf(
            Message("m1", "Hello! How can I help you?", true, Date()),
            Message("m2", "I have a question about my order.", false, Date()),
        )
    }
}
