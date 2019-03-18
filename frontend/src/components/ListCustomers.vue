<template>
  <div>
    <CustomerLine v-for="customer in customers" v-bind:key="customer.id" v-bind:data="customer"/>
  </div>
</template>

<script>
    import {AXIOS} from "@/http-common"
    import CustomerLine from "./CustomerLine.vue"

    export default {
        name: "ListCustomers",
        components: {CustomerLine},
        data: function() {
            return {
                customers: [],
                errors: []
            }
        },
        created: function () {
            AXIOS.get(`/customers`).then(response => {
                console.log(response)
                this.customers = response.data
            })
            .catch(e => {
                this.errors.push(e)
            })
        }
    }
</script>

<style scoped>

</style>