import Vue from 'vue'
import Vuex from 'vuex'
import customers from './modules/customers'
import inventory from "./modules/inventory";

Vue.use(Vuex)

const debug = process.env.NODE_ENV !== 'production'

export default new Vuex.Store({
    modules: {
        customers,
        inventory
    },
    strict: debug,
    // plugins: debug ? [createLogger()] : []
})