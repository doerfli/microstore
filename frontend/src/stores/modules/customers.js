// import shop from '../../api/shop'
import {AXIOS} from "@/http-common"

// initial state
const state = {
    all: []
}

// getters
const getters = {}

// actions
const actions = {
    getAll({ commit }) {
        AXIOS.get(`/customers`).then(response => {
            console.log(response);
            commit('setAll', response.data)
        })
        .catch(e => {
            console.log(e)
        })
    }
}

// mutations
const mutations = {
    setAll(state, customers) {
        state.all = customers
    },

    add(state, customer) {
        state.all.push(customer)
    }
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}

