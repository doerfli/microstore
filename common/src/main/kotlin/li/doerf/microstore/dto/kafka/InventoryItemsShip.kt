package li.doerf.microstore.dto.kafka

import li.doerf.microstore.utils.NoArg

@NoArg
data class InventoryItemsShip(
        val id: String,
        val itemIds: Collection<String>
)