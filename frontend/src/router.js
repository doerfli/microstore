import Vue from "vue";
import Router from "vue-router";
import Home from "./views/Home.vue";
import CustomerView from "./views/CustomerView.vue";
import InventoryView from "./views/InventoryView";

Vue.use(Router);

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "home",
      component: Home
    },
    {
      path: "/customers",
      name: "customers",
      component: CustomerView
    },
    {
      path: "/inventory",
      name: "inventory",
      component: InventoryView
    }
  ]
});
