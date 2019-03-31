package li.doerf.microstore.api.rest.dto

import java.math.BigDecimal

data class CreateOrderResponse(
        val orderId: String,
        val orderNumber: Int,
        val amount: BigDecimal,
        val orderStatus: OrderStatus,
        val errorText: String
)
