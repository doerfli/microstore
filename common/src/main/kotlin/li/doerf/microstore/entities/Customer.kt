package li.doerf.microstore.entities

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("Customer")
data class Customer(
        @Id var id: String,
        val email: String,
        var firstname: String,
        var lastname: String
)