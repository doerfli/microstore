package li.doerf.microstore.dto.kafka

import li.doerf.microstore.utils.NoArg

@NoArg
data class InventoryItemReserve(
        val id: String,
        val quantity: Int
)
