import axios from 'axios'

export const AXIOS = axios.create({
    baseURL: `http://${process.env.VUE_APP_API_HOST}:8080`,
    headers: {
        // 'Access-Control-Allow-Origin': 'http://localhost:8070'
    }
})

console.log(process.env);