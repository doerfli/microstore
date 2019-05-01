package li.doerf.microstore.inventory.repositories

import li.doerf.microstore.inventory.entities.Order
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : CrudRepository<Order, String> {
}