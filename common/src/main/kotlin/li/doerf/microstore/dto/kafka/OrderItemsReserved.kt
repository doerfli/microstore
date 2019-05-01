package li.doerf.microstore.dto.kafka

import li.doerf.microstore.utils.NoArg
import java.math.BigDecimal

@NoArg
data class OrderItemsReserved(
        val id: String,
        val totalAmount: BigDecimal
)