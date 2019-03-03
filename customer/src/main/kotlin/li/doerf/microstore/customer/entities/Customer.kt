package li.doerf.microstore.customer.entities

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.math.BigDecimal

@RedisHash("Customer")
data class Customer(
        @Id var id: String,
        val email: String,
        var firstname: String,
        var lastname: String,
        /** only valid in payment module */
        var creditLimit: BigDecimal = BigDecimal.ZERO
)