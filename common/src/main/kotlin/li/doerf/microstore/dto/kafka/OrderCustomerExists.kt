package li.doerf.microstore.dto.kafka

import li.doerf.microstore.utils.NoArg

@NoArg
data class OrderCustomerExists(
        val id: String,
        val customerId: String,
        val customerOrderId: Long,
        val itemsIds: Collection<String>) // TODO remove from msg (not necessary, was transmitted earlier)

