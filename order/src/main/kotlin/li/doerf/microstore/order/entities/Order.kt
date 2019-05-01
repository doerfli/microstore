package li.doerf.microstore.order.entities

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("Order")
data class Order(
        @Id val id: String,
        val customerId: String,
        var customerOrderId: Long,
        val itemIds: Collection<String>,
        var status: OrderStatus
)