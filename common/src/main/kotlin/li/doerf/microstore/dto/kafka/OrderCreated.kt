package li.doerf.microstore.dto.kafka

import li.doerf.microstore.utils.NoArg

@NoArg
data class OrderCreated(
        val id: String
)