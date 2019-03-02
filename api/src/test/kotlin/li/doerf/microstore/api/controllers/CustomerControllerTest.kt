package li.doerf.microstore.api.controllers

import li.doerf.microstore.api.listeners.CustomersListener
import li.doerf.microstore.api.rest.dto.CreateCustomerRequest
import li.doerf.microstore.dto.CustomerCreated
import li.doerf.microstore.services.KafkaService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException

@ExtendWith(MockitoExtension::class)
class CustomerControllerTest {
    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
    @Mock
    private lateinit var kafkaService: KafkaService
    @Mock
    private lateinit var customersListener: CustomersListener

    @Test
    fun testCreate() {
        val req = CreateCustomerRequest(
                "some@body.com",
                "some",
                "Body"
        )
        val uuid = UUID.randomUUID().toString()
        val cc = CustomerCreated(
                uuid,
                "some@body.com",
                "some",
                "Body"
        )
        val future = CompletableFuture.supplyAsync<CustomerCreated>{cc}
        Mockito.`when`(customersListener.registerCorrelationIdForResponse(any())).thenReturn(future)
        val customerController = CustomerController(kafkaService, customersListener)

        val res = customerController.create(req)
        val resp = res.join()

        assertThat(resp.id).isEqualTo(uuid)
        assertThat(resp.email).isEqualTo("some@body.com")
        assertThat(resp.firstname).isEqualTo("some")
        assertThat(resp.lastname).isEqualTo("Body")
    }

    @Test
    fun testCreateWithException() {
        val req = CreateCustomerRequest(
                "some@body.com",
                "some",
                "Body"
        )
        val uuid = UUID.randomUUID().toString()
        val cc = CustomerCreated(
                uuid,
                "some@body.com",
                "some",
                "Body"
        )
        val future = CompletableFuture.supplyAsync<CustomerCreated>{throw IllegalArgumentException("something happened")}
        Mockito.`when`(customersListener.registerCorrelationIdForResponse(any())).thenReturn(future)
        val customerController = CustomerController(kafkaService, customersListener)

        try {
            val res = customerController.create(req)
            res.join()
            fail<String>("expected exception")
        } catch(e: CompletionException) {
            assertThat((e.cause as ResponseStatusException).status).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}