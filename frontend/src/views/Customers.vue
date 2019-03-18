<template>
  <div>
    <h1>Customers</h1>
    <ListCustomers />
    <NewCustomer />

  </div>
</template>

<script>
import NewCustomer from "@/components/NewCustomer.vue";
import ListCustomers from "@/components/ListCustomers.vue";

export default {
    name: "Customers",
    components: {
        NewCustomer,
        ListCustomers
    }
}
</script>

<style scoped>

</style>