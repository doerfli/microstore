package li.doerf.microstore.payment.entities

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.math.BigDecimal

@RedisHash("Order")
data class Order (
        @Id val id: String,
        val customerId: String,
        var amount: BigDecimal
)