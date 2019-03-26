<template>
  <div>
    <InventoryItem
        v-for="item in items" v-bind:key="item.id" v-bind:data="item"/>
  </div>
</template>

<script>
    import {AXIOS} from "@/http-common";
    import InventoryItem from "./InventoryItem";

    export default {
        name: "InventoryIndex",
        components: {
            InventoryItem
        },
        data: function() {
            return {
                items: []
            }
        },
        created: function () {
            AXIOS.get(`/inventory`).then(response => {
                console.log(response);
                this.items = response.data
            })
            .catch(e => {
                this.errors.push(e)
            })
        }
    }
</script>

<style scoped>

</style>