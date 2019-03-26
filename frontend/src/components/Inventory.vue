<template>
  <div>
    <h1>Inventory</h1>
    <button v-on:click="orderItems()">Order items</button>
  </div>
</template>

<script>
    import {AXIOS} from "@/http-common";

    export default {
        name: "Inventory",
        methods: {
            orderItems: function() {
                AXIOS.post(`/inventory`, {})
            }
        }
    }
</script>

<style scoped>

</style>