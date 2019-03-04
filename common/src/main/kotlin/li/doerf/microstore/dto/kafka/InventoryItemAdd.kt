package li.doerf.microstore.dto.kafka

import li.doerf.microstore.utils.NoArg
import java.math.BigDecimal

@NoArg
data class InventoryItemAdd(
        val id: String,
        val name: String,
        val price: BigDecimal,
        val initialQuantity: Int)
