package li.doerf.microstore.dto.kafka

import java.math.BigDecimal

data class OrderItemsReserved(
        val id: String,
        val totalAmount: BigDecimal
)