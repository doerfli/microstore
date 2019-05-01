package li.doerf.microstore.dto.kafka

import li.doerf.microstore.utils.NoArg

@NoArg
data class OrderCreate(
        val customerId: String,
        val itemsIds: Collection<String> // TODO remove from msg (not necessary, was transmitted earlier)
)