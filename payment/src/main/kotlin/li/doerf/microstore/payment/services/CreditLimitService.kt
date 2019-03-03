package li.doerf.microstore.payment.services

import li.doerf.microstore.dto.CustomerIncreaseLimit
import li.doerf.microstore.payment.repositories.CustomerRepository
import li.doerf.microstore.services.KafkaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CreditLimitService @Autowired constructor(
        val customerRepository: CustomerRepository,
        val kafkaService: KafkaService
){
    fun increaseLimit(input: CustomerIncreaseLimit) {
        val customer = customerRepository.findById(input.id)
        if(customer.isEmpty) {
            throw IllegalArgumentException("customer with id ${input.id} unknown")
        }
        customer.get().creditLimit = customer.get().creditLimit.add(input.amount)
        customerRepository.save(customer.get())
    }
}