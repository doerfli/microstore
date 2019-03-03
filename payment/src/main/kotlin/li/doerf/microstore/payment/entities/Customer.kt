package li.doerf.microstore.payment.entities

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.math.BigDecimal

@RedisHash("Customer")
data class Customer(
        @Id val id: String,
        var creditLimit: BigDecimal = BigDecimal.ZERO
)