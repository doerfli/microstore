package li.doerf.microstore.inventory.entities

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.math.BigDecimal

@RedisHash("Item")
data class Item (
    @Id val id: String,
    var name: String,
    var price: BigDecimal,
    var quantity: Int,
    var quantityReserved: Int
)