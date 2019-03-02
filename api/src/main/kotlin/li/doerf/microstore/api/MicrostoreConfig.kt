package li.doerf.microstore.api

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class MicrostoreConfig {
    @Value("\${microstore.customer.hostname}")
    lateinit var customerSvcHostname: String
    @Value("\${microstore.customer.port}")
    lateinit var customerSvcPort: String
}