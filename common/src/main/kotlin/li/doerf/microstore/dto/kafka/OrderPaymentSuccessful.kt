package li.doerf.microstore.dto.kafka

import li.doerf.microstore.utils.NoArg

@NoArg
data class OrderPaymentSuccessful(
        val id: String
)