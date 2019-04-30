package li.doerf.microstore.dto.kafka

data class OrderCustomerExists(
        val id: String,
        val customerId: String,
        val customerOrderId: Long,
        val itemsIds: Collection<String>)

