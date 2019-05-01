package li.doerf.microstore.payment.entities

import org.springframework.data.annotation.Id
import java.math.BigDecimal

data class Order (
        @Id val id: String,
        val customerId: String,
        var amount: BigDecimal
)