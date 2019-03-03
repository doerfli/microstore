package li.doerf.microstore.dto

import li.doerf.microstore.utils.NoArg
import java.math.BigDecimal

@NoArg
data class CustomerIncreaseLimit(
        val id: String,
        val amount: BigDecimal
)
