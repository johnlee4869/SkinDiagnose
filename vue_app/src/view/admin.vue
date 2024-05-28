<template>
    <div class="layout-box" >
        <el-alert title="用户不存在" type="warning" effect="dark" center show-icon v-show="existCheck"/>
        <el-alert title="用户名或密码错误" type="error" effect="dark" center show-icon v-show="errorCheck"/>
        <el-alert title="登录成功" type="success" effect="dark" center show-icon v-show="passCheck"/>
        <div class="content-box">
            <div class="content">
                <el-form :rules="rules" :model="form" ref="ruleFormRef" v-loading="loading">
                    <el-form-item>
                        <p class="login_title">欢迎回来!</p>
                    </el-form-item>
                    <el-form-item prop="username">
                        <el-input class="username"
                                  v-model="form.username"
                                  placeholder="用户名"
                                  prefix-icon="User"
                        >

                        </el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input class="password"
                                  type="password"
                                  v-model="form.password"
                                  placeholder="密    码"
                                  prefix-icon="Lock"
                        >

                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="login">登&nbsp;&nbsp录</el-button>
                    </el-form-item>
                </el-form>

            </div>
        </div>
    </div>

</template>

<script>

import {ElMessageBox} from "element-plus"
export default {
    name: 'admin',
    data() {
        return {
            timer: null,
            loading: false,
            existCheck: false,
            errorCheck: false,
            passCheck: false,
            form: {
                username: '',
                password: '',
            },
            rules: {
                username: [
                    { required: true, message: '账号不能为空', trigger: 'blur' },
                ],
                password: [
                    { required: true, message: '密码不能为空', trigger: 'blur' },
                ],
            }
        }
    },
    beforeUnmount() {
        clearTimeout(this.timer)
    },
    methods: {
        login() {
            if(this.form.password.length === 0 || this.form.password.length === 0){
                ElMessageBox.alert("账号和密码不能为空！",
                    {
                        confirmButtonText: "返回"
                    }
                )
            }else{
                let data = {
                    "username": this.form.username,
                    "password": this.form.password
                }
                this.axios({
                    method: 'POST',
                    url: "/spring_api/admin/login",
                    data: data,
                    headers:{
                        'Content-Type':'application/json'
                    }
                })
                    .then((response) => {
                        if (response.data === "Success") {
                            sessionStorage.setItem('admin_name', this.form.username)
                            sessionStorage.setItem('admin_authenticated', 'true')

                            this.loading = true
                            this.timer = setTimeout(() => {
                                this.loading = false;
                            }, 1000)
                            this.errorCheck = false;
                            this.passCheck = true;
                            this.existCheck = false;
                            this.timer = setTimeout(() => {
                                this.$router.push('/admin/background')
                            }, 1000)
                        } else if (response.data === "InfoError") {
                            this.loading = true
                            this.timer = setTimeout(() => {
                                this.loading = false
                                this.errorCheck = true;
                                this.passCheck = false;
                                this.existCheck = false;
                            }, 1000)
                        } else if (response.data === "NotExist") {
                            this.loading = true
                            this.timer = setTimeout(() => {
                                this.loading = false
                                this.errorCheck = false;
                                this.passCheck = false;
                                this.existCheck = true;
                            }, 1000)
                        }
                    })
                    .catch((error) => {
                        console.log(error)
                        ElMessageBox.alert("系统异常，请检查并修复！",{
                            title:"警告",
                            confirmButtonText:"返回",
                            type:"warning"
                        })
                    })
            }
        }
    }
}
</script>

<style lang="less" scoped>
    .el-alert{
        position: absolute;
        top: 0;
    }
    .layout-box {
        width: 100vw;
        height: 100vh;
        background: url("../assets/admin-back.jpg") no-repeat;
        background-size: 100% 100%;
    }

    .content-box{
        width: 30vw;
        height: 70vh;
        position: absolute;
        right: 12vw;
        top: 12vh;
        background: rgba(255, 255, 255, 0.5);
        overflow: hidden;
        box-shadow: silver 0 0 25px;
        border-radius: 25px;
    }
    .content-box:before{
        content: '';
        width: 30vw;
        height: 70vh;
        background: inherit;
        position: absolute;
        left: -25px;  //giving minus -25px left position
        right: 0;
        top: -25px;   //giving minus -25px top position
        bottom: 0;
        box-shadow: inset 0 0 0 200px rgba(255,255,255,0.3);
        filter: blur(10px);

    }

    .content{
        width: 70%;
        height: 100%;
        text-align: center;
        horiz-align: center;
        justify-content: center;
        margin: 0 auto;
    }

    .login_title {
        font-size: 1.5vw;
        font-weight: bold;
        display: block;
        margin-left: 33%;
        margin-top: 20%;
        margin-bottom: 15%;
    }

    .el-form {
        width: 100%;
        height: 100%;
    }

    .el-input {
        width: 100%;
        height: 40px;
        margin-top: 2%;
        font-size: 15px;
        border: None;
    }

    .el-button {
        width: 60%;
        text-align: center;
        padding: 5px;

        background: linear-gradient(90deg, rgb(76, 163, 147), rgb(119, 182, 173));
        color: #fff;
        font-size: 2.5vh;
        font-weight: bold;
        border-radius: 25px;
        box-shadow: silver 0 0 10px;
        margin-left: 20%;
        margin-top: 20%;
    }

</style>