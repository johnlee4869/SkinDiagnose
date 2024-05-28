import {createApp} from 'vue'
import './style.css'
import App from './App.vue'
import router from "./router"


import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import axios from 'axios'
import VueAxios from 'vue-axios'

import {createStore} from 'vuex'

const app = createApp(App)

const store = createStore({
    state() {
        return {
            user_list: [],
            admin_list: []
        }
    },
    mutations: {
        addUser(user_list, data) {
            user_list.push(data)
        },
        addAdmin(admin_list, data) {
            admin_list.push(data)
        }
    }
})
app.use(store)

app.use(router)

app.use(VueAxios, axios)


for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}


app.mount('#app')
