package li.doerf.microstore.customer.services

import li.doerf.microstore.customer.repositories.CustomerRepository
import li.doerf.microstore.dto.kafka.CustomerCreate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class CustomerServiceTest {

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @Test
    fun testCreate() {
        val customerToCreate = CustomerCreate(
                "some@body.com",
                "Some",
                "Body"
        )

        val customerService = CustomerService(customerRepository)

        val cust = customerService.create(customerToCreate)
        assertThat(cust.id).isNotNull()
        assertThat(cust.email).isEqualTo(customerToCreate.email)
        assertThat(cust.firstname).isEqualTo(customerToCreate.firstname)
        assertThat(cust.lastname).isEqualTo(customerToCreate.lastname)

        Mockito.verify(customerRepository).save(Mockito.any())
    }

}