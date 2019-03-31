package li.doerf.microstore.dto.kafka

import li.doerf.microstore.utils.NoArg

@NoArg
data class OrderFinished(
        val id: String,
        val customerId: String,
        val orderStatus: OrderStatus,
        val errorText: String
)