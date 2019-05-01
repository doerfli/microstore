package li.doerf.microstore.inventory.repositories

import li.doerf.microstore.inventory.entities.Order
import org.springframework.data.repository.CrudRepository

interface OrderRepository : CrudRepository<Order, String> {
}