<template>
    <div class="container">
        <el-alert title="用户不存在" type="warning" effect="dark" center show-icon v-show="existCheck"/>
        <el-alert title="用户名或密码错误" type="error" effect="dark" center show-icon v-show="errorCheck"/>
        <el-alert title="登录成功" type="success" effect="dark" center show-icon v-show="passCheck"/>
        <div class="content">
            <div class="left-box">
                <div class="left-content">
                    <img src="/src/assets/medicine.jpg" alt="logo"/>
                </div>
            </div>
            <div class="right-box">
                <div class="right-content">
                    <el-form :rules="rules" :model="form" ref="ruleFormRef" v-loading="loading">
                            <p id="login_title">欢迎回来!</p>
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
    </div>
</template>

<script>
import {ElMessageBox} from "element-plus"
export default {
    name: 'user',
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
                ]
            }
        }
    },
    beforeUnmount() {
        clearTimeout(this.timer)
    },
    methods: {
        login() {
            if(this.form.username.length === 0 || this.form.password.length === 0){
                ElMessageBox.alert("账号和密码不能为空！",{
                    title:"警告",
                    confirmButtonText:"返回",
                    type:"warning"
                })
            }else{
                let data = {
                    "username": this.form.username,
                    "password": this.form.password
                }
                this.axios.post('/spring_api/user/login', data)
                    .then((response) => {
                        if (response.data === "Success") {
                            sessionStorage.setItem('user_name', this.form.username)
                            sessionStorage.setItem('user_authenticated', 'true')

                            this.loading = true
                            this.timer = setTimeout(() => {
                                this.loading = false;
                            }, 1000)
                            this.errorCheck = false;
                            this.passCheck = true;
                            this.existCheck = false;
                            this.timer = setTimeout(() => {
                                this.$router.push('/user/QAChat')
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
                        ElMessageBox.alert("系统异常登录失败，请稍后重试或联系管理员！",{
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
    .el-alert {
        position: absolute;
        top: 0;
    }

    .container {
        width: 100vw;
        height: 100vh;
        background: url("../assets/user-back.jpg") no-repeat;
        background-size: cover;
    }

    .content {
        width: 65%;
        height: 65%;
        position: relative;
        top: 50%;
        left: 48%;
        transform: translate(-50%, -50%);
        box-shadow: silver 0 0 30px;
    }

    .left-box {
        width: 60%;
        height: 100%;
        position: absolute;
        top: 0;
        left: 0;
        z-index: -1;
    }

    .left-content {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        z-index: 1;
    }

    img {
        width: 100%;
        height: 100%;
        position: absolute;
        top: 0;
        opacity: 0.9;
    }

    .right-box {
        width: 40%;
        height: 100%;
        position: absolute;
        top: 0;
        left: 60%;
        z-index: 1;
        background: rgba(255,255,255,0.95);
        box-shadow: silver 0 0 30px;
    }

    .right-content {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }

    #login_title {
        font-size: 1.5vw;
        font-weight: bold;
        display: block;
        margin-top: 20%;
        margin-bottom: 20%;
    }

    .el-form {
        border: 1px solid silver;
        border-radius: 5px;
        width: 65%;
        height: 90%;
        padding:0 20px 0 20px;
    }

    .el-input {
        width: 100%;
        height: 40px;
        margin-top: 2%;
        font-size: 15px;
        border: None;
    }

    .el-button {
        width: 70%;
        text-align: center;
        padding: 5px;
        background: linear-gradient(90deg, rgb(1, 192, 210), rgb(0, 65, 157));
        color: #fff;
        font-size: 2.5vh;
        font-weight: bold;
        border-radius: 25px;
        box-shadow: silver 0 0 10px;
        margin: 50px auto;
    }

</style>