<template>
  <div>
    {{data.firstname}} {{data.lastname}} &lt;{{data.email}}&gt;
  </div>
</template>

<script>
    export default {
        name: "CustomerLine",
        props: {
            data: {}
        }
    }
</script>

<style scoped>

</style>