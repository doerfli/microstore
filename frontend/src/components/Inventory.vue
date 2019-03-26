<template>
  <div>
    <h1>Inventory</h1>
    <button v-on:click="orderItems()">Order items</button> |
    <button v-on:click="reload()">Reload</button>
    <InventoryIndex ref="inventoryindex"/>
  </div>
</template>

<script>
    import {AXIOS} from "@/http-common";
    import InventoryIndex from "./InventoryIndex";

    export default {
        name: "Inventory",
        components: {
            InventoryIndex
        },
        methods: {
            orderItems: function() {
                AXIOS.post(`/inventory`, {})
            },
            reload: function() {
                this.$refs.inventoryindex.reload()
            }
        }
    }
</script>

<style scoped>

</style>