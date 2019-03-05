package li.doerf.microstore.api

import com.github.kittinunf.fuel.Fuel
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class MicrostoreConfig {
    @Value("\${microstore.customer.hostname}")
    private lateinit var customerSvcHostname: String
    @Value("\${microstore.customer.port}")
    private lateinit var customerSvcPort: String
    @Value("\${microstore.inventory.hostname}")
    private lateinit var inventorySvcHostname: String
    @Value("\${microstore.inventory.port}")
    private lateinit var inventorySvcPort: String

    @Bean
    fun fuel(): Fuel {
        return Fuel
    }

    @Bean
    fun customerSvcBaseUrl(): String {
        return "http://$customerSvcHostname:$customerSvcPort"
    }

    @Bean
    fun inventorySvcBaseUrl(): String {
        return "http://$inventorySvcHostname:$inventorySvcPort"
    }

}