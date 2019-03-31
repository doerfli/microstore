package li.doerf.microstore.api.rest.dto

data class CreateOrderRequest(
        val customerId: String,
        val itemsIds: Collection<String>
)
