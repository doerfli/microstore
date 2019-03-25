<template>
  <div>
    <CustomerLine
        v-for="customer in customers" v-bind:key="customer.id" v-bind:data="customer"/>
  </div>
</template>

<script>
    import {AXIOS} from "@/http-common"
    import CustomerLine from "./CustomerLine.vue"

    export default {
        name: "ListCustomers",
        components: {CustomerLine},
        props: [
            'customers'
        ],
        data: function() {
            return {
                errors: []
            }
        }
    }
</script>

<style scoped>

</style>