<template>
  <div>
    <h1>Customers</h1>
    <NewCustomer />
  </div>
</template>

<script>
import NewCustomer from "@/components/NewCustomer.vue";

export default {
    name: "Customers",
    components: {
        NewCustomer
    }
}
</script>

<style scoped>

</style>