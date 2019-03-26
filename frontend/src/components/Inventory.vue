<template>
  <div>
    <h1>Inventory</h1>
    <button v-on:click="orderItems()">Order items</button>
    <InventoryIndex />
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
            }
        }
    }
</script>

<style scoped>

</style>