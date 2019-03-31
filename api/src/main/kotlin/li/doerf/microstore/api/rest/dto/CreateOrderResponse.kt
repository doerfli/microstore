package li.doerf.microstore.api.rest.dto

import li.doerf.microstore.dto.kafka.OrderStatus
import java.math.BigDecimal

data class CreateOrderResponse(
        val orderId: String,
        val orderNumber: Int,
        val amount: BigDecimal,
        val orderStatus: OrderStatus,
        val errorText: String
)
