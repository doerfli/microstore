package li.doerf.microstore.api.rest.dto

import java.util.*

data class CreateCustomerResponse(
        var id: UUID,
        var email: String,
        var firstname: String,
        var lastname: String

)
