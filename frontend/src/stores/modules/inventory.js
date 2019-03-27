import {AXIOS} from "@/http-common"

// initial state
const state = {
    all: []
}

// getters
const getters = {}

// actions
const actions = {
    reload({ commit }) {
        AXIOS.get(`/inventory`).then(response => {
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
    }
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}

