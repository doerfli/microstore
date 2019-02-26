package li.doerf.microstore.api.controllers

import li.doerf.microstore.api.rest.dto.CreateCustomerRequest
import li.doerf.microstore.api.rest.dto.CreateCustomerResponse
import li.doerf.microstore.utils.getLogger
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/customers")
class CustomerController {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @PostMapping
    fun create(@RequestBody customer: CreateCustomerRequest): CreateCustomerResponse {
        log.info("received request to create customer $customer")
        val response = CreateCustomerResponse(
                UUID.randomUUID(),
                customer.email,
                customer.firstname,
                customer.lastname
        )
        return response
    }

}
