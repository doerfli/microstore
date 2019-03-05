package li.doerf.microstore.inventory.controllers

import li.doerf.microstore.dto.rest.ItemDto
import li.doerf.microstore.inventory.repositories.ItemRepository
import li.doerf.microstore.utils.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/inventory")
class InventryController @Autowired constructor(
        private val itemRepository: ItemRepository
){

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass)
    }

    @GetMapping
    fun getAll(): List<ItemDto> {
        log.debug("received request to get all items from inventory")
        val items = itemRepository.findAll().map { i -> ItemDto(i.id, i.name, i.price, i.quantity - i.quantityReserved) }
        log.info("returning ${items.size} items")
        return items
    }

}