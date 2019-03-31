package li.doerf.microstore.api.rest.dto

enum class OrderStatus {
    SHIPPED,
    SHIPPING_FAILED,
    CUSTOMER_NOT_FOUND,
    ITEM_NOT_FOUND,
    ITEM_NOT_ON_STOCK,
    PAYMENT_FAILED
}
