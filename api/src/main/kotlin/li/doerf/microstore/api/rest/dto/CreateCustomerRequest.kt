package li.doerf.microstore.api.rest.dto

data class CreateCustomerRequest(
        var email: String,
        var firstname: String,
        var lastname: String
        )
