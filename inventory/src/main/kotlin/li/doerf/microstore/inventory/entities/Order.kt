package li.doerf.microstore.inventory.entities

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("Order")
data class Order (
    @Id val id: String,
    var itemIds: Collection<String>
)