package li.doerf.microstore.customer.services

import li.doerf.microstore.customer.entities.Customer
import li.doerf.microstore.customer.repositories.CustomerRepository
import li.doerf.microstore.dto.kafka.CustomerCreated
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class CustomerServiceTest {

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @Test
    fun testStore() {
        val uuid = UUID.randomUUID().toString()
        val cc = CustomerCreated(
                uuid,
                "some@buddy.com",
                "Some",
                "Buddy"
        )

        val service = CustomerService(customerRepository)
        service.store(cc)

        val argument = ArgumentCaptor.forClass(Customer::class.java)
        Mockito.verify(customerRepository).save(argument.capture())

        assertThat(argument.value.id).isEqualTo(uuid)
        assertThat(argument.value.email).isEqualTo(cc.email)
        assertThat(argument.value.firstname).isEqualTo(cc.firstname)
        assertThat(argument.value.lastname).isEqualTo(cc.lastname)
    }

}