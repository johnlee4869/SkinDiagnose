<template>
    <div class="layout">
        <el-container class="layout-container">
            <el-aside class="aside-container">
                <el-row class="tac">
                    <el-col>
                        <el-menu default-active="0" @select="handleSelect">
                            <div>
                                <p class="aside-top">菜单栏</p>
                            </div>

                            <el-menu-item index="0">
                                <el-icon><user /></el-icon>
                                用户管理
                            </el-menu-item>
                            <el-menu-item index="1">
                                <el-icon><search /></el-icon>
                                数据统计
                            </el-menu-item>

                        </el-menu>
                    </el-col>
                </el-row>
            </el-aside>
            <el-container class="main-container">
                <el-header>
                    <div class="logo-box">
                        <img class="logo" src="../assets/logo.png" alt="Logo"/>
                    </div>
                    <div class="slogan-box">
                        Intelligent Medicine
                    </div>
                    <div class="middle-banner">

                    </div>
                    <div class="close-box" @click="exitLogin">
                        <span style="margin-right: 15px;">管理员：{{ adminname }}</span>
                        <el-tooltip effect="dark" content="退出登录">
                            <img class="close" src="../assets/close.png" alt="exit"/>
                        </el-tooltip>
                    </div>
                </el-header>
                <el-main>
                    <router-view v-slot="{Component}">
                        <transition name="fade" mode="out-in" appear>
                            <component :is="Component" />
                        </transition>
                    </router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script lang="ts">
import {ElMessageBox} from "element-plus";

export default {
    name: "backend",
    data() {
        return {
            choose: 0,
            adminname: sessionStorage.getItem('admin_name')
        };
    },
    methods: {
        handleSelect(index){
            switch (index) {
                case "0":   this.$router.push("/admin/background/manage");
                break;
                case "1":   this.$router.push("/admin/background/statistics");
                break;
            }
        },
        exitLogin(){
            ElMessageBox.confirm("确定要退出登录吗？",{
                title: "提示",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            })
                .then(key=>{
                    sessionStorage.setItem('admin_name', '')
                    sessionStorage.setItem('admin_authenticated', 'false')
                    this.$router.push("/admin/login")
                })
                .catch((action)=>{

                })
        }
    }
};
</script>

<style lang="less" scoped>

    .layout {
        width: 100vw;
        height: 100vh;

    }

    .layout-container {
        width: 100vw;
        height: 100vh;
    }

    .aside-container {
        width: 10%;
    }
    .aside-top{
        border-bottom: 1px solid silver;
        padding-bottom: 18px;
        font-size: 18px;
        font-weight: bold;
    }
    .main-container {
        display: flex;
        width: 95vw;
    }

    .el-header {
        padding: 0;
        height: 60px;
        display: flex;
        box-shadow: 0 5px 5px 0 #0083bb;
    }


    .logo-box {
        height: 60px;
        width: 60px;
        display: flex;
        margin-left: 10px;
    }

    .logo {
        width: 60px;
    }

    .slogan-box {
        height: 100%;
        display: flex;
        width: 1000px;
        font-size: 5vh;
        margin-top: 20px;
        color: #00419d;
        font-weight: bold;
    }

    .avatar-box {
        height: 60px;
        display: flex;
        margin-right: 20px;
        margin-top: 10px;
    }

    .avatar {
        height: 60%;
    }

    .middle-banner {
        width: 100%;
        display: flex;
    }

    .close-box {
        width: 300px;
        height: 30px;
        display: flex;
        margin-top: 12.5px;
        margin-right: 20px;
    }

    .close {
        height: 30px;
    }

    .toggle-box:hover, .close-box:hover{
        cursor: pointer;
    }

    .fade-enter-active,
    .fade-leave-active {
        transition: opacity 0.4s ease;
    }

    .fade-enter-from,
    .fade-leave-to {
        opacity: 0;
    }

    .el-menu-item{
        font-weight: bold;
    }
</style>