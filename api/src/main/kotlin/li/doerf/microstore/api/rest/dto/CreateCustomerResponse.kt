package li.doerf.microstore.api.rest.dto

data class CreateCustomerResponse(
        var id: String,
        var email: String,
        var firstname: String,
        var lastname: String

)
