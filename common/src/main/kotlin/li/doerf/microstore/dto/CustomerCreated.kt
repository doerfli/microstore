package li.doerf.microstore.dto

import li.doerf.microstore.utils.NoArg

@NoArg
data class CustomerCreated(
        val id: String,
        val email: String,
        val firstname: String,
        val lastname: String
)