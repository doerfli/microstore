package li.doerf.microstore.dto.kafka

import li.doerf.microstore.utils.NoArg
import java.math.BigDecimal

@NoArg
data class OrderFinished(
        val id: String,
        val customerId: String,
        val totalAmount: BigDecimal,
        val orderNumber: Long,
        val orderStatus: OrderEndState,
        val errorText: String?
)