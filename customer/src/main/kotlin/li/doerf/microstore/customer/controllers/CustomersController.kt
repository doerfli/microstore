package li.doerf.microstore.customer.controllers

import li.doerf.microstore.customer.repositories.CustomerRepository
import li.doerf.microstore.dto.rest.CustomerDto
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
class CustomerController @Autowired constructor(
        private val customerRepository: CustomerRepository
){
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @GetMapping
    fun getAllCustomers(): List<CustomerDto> {
        log.debug("received request to get all customers")
        return customerRepository.findAll().map { c -> CustomerDto(c.id, c.email, c.firstname, c.lastname) }
    }
}