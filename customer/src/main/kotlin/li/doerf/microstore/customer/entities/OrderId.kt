package li.doerf.microstore.customer.entities

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash
data class OrderId(
        @Id val customerId: String,
        var nextOrderId: Long
)