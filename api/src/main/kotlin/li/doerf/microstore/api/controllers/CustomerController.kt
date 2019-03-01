package li.doerf.microstore.api.controllers

import li.doerf.microstore.TOPIC_CUSTOMERS
import li.doerf.microstore.api.listeners.CustomersListener
import li.doerf.microstore.api.rest.dto.CreateCustomerRequest
import li.doerf.microstore.api.rest.dto.CreateCustomerResponse
import li.doerf.microstore.dto.CustomerCreate
import li.doerf.microstore.dto.CustomerCreated
import li.doerf.microstore.services.KafkaService
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/customers")
class CustomerController @Autowired constructor(
        private val kafkaService: KafkaService,
        private val customersListener: CustomersListener
) {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @PostMapping
    fun create(@RequestBody customer: CreateCustomerRequest): CompletableFuture<CreateCustomerResponse> {
        log.info("received request to create customer $customer")
        val event = createCreateCustomerEvent(customer)
        val correlationId = UUID.randomUUID().toString()
        val future = registerCorrelationIdForResponse(correlationId)
        kafkaService.sendEvent(TOPIC_CUSTOMERS, event, correlationId)
        return future
    }

    private fun registerCorrelationIdForResponse(correlationId: String): CompletableFuture<CreateCustomerResponse> {
        return customersListener
                .registerCorrelationIdForResponse(correlationId)
                .thenApply { x -> processResponse(x)}
                .exceptionally { throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR) }

    }

    private fun processResponse(event: CustomerCreated): CreateCustomerResponse {
        return CreateCustomerResponse(
                event.id,
                event.email,
                event.firstname,
                event.lastname
        )
    }

    private fun createCreateCustomerEvent(customer: CreateCustomerRequest): CustomerCreate {
        return CustomerCreate(
                customer.email,
                customer.firstname,
                customer.lastname
        )
    }

}
