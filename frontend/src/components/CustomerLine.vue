<template>
  <div>
    {{data.firstname}} {{data.lastname}} &lt;{{data.email}}&gt; <br/>
    <small>{{data.id}}</small>
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
