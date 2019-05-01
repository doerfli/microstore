package li.doerf.microstore.inventory.entities

import org.springframework.data.annotation.Id

data class Order (
    @Id val id: String,
    var itemIds: Collection<String>
)