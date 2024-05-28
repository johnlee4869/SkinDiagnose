import{
    createRouter,
    createWebHistory
} from "vue-router"

import home from '../view/home.vue'


import userLogin from "../view/userLogin.vue"


import admin from "../view/admin.vue"
import background from "../view/background.vue"

import UserPage from "../view/UserPage.vue"
import chat from "../components/chat.vue"
import analyse from "../components/analyse.vue"


import manage from "../components/manage.vue"
import statistics from "../components/statistics.vue"

import page404 from "../components/page404.vue"

import {ElMessageBox} from "element-plus";

const routes = [
    {
        path: "/",
        name: "home",
        component: home
    },
    {
        path: "/login",
        name: "userLogin",
        component: userLogin
    },
    {
        path: "/user",
        component: UserPage,
        redirect: "/user/QAChat",
        children: [
            {
                path: "/user/QAChat",
                component: chat
            },
            {
                path: "/user/analyse",
                component: analyse
            }
        ],
        beforeEnter: (from, to, next)=>{
            if(sessionStorage.getItem('user_authenticated')==='true'){
                next()
            }else{
                ElMessageBox.alert("未登录，请先登录获得授权！", {
                    title: "提示",
                    confirmButtonText: '返回',
                    type: "error",
                    callback: () => {
                        router.push("/login").then()
                    }
                }).then()
            }
        }
    },
    {
        path: "/admin",
        redirect: "/admin/login",
    },
    {
        path: "/admin/login",
        name: "admin",
        component: admin,
    },
    {
        path: "/admin/background",
        component: background,
        redirect: "/admin/background/manage",
        children: [
            {
                path: "/admin/background/manage",
                component: manage
            },
            {
                path: "/admin/background/statistics",
                component: statistics
            }
        ],
        beforeEnter: (from, to, next)=>{
            if(sessionStorage.getItem('admin_authenticated')==='true'){
                next()
            }else{
                ElMessageBox.alert("未登录，请先登录获得授权！", {
                    title: "提示",
                    confirmButtonText: '返回',
                    type: "error",
                    callback: () => {
                        router.push("/admin/login").then()
                    }
                }).then()
            }
        }
    },
    {
        path: '/404',
        component: page404
    },
    {
        path: '/:pathMatch(.*)', //匹配未定义的路由
        redirect: '/404'//重定向
    },
]

const router = createRouter({
    history:createWebHistory(),
    routes:routes
})

export default router

