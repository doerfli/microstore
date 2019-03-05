package li.doerf.microstore.dto.rest

import java.math.BigDecimal

data class ItemDto(
        val id: String,
        var name: String,
        var price: BigDecimal,
        var quantityAvailable: Int
)