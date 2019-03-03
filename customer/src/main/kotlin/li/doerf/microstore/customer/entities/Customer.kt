package li.doerf.microstore.customer.entities

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("Customer")
data class Customer(
        @Id val id: String,
        val email: String,
        var firstname: String,
        var lastname: String
)