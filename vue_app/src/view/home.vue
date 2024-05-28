<template>
    <div class="layout-box">
      <el-container>
        <el-header>
            <div class="header-box">
            <img id="logo" src="../assets/logo.png">
            <span id="banner">Intelligent Medicine</span>
            <span id="sub-banner">——Health is better than wealth.</span>

            <el-button class="login-btn" type="success" round @click="goLogin">登录</el-button>
            <el-button class="sign-btn" type="primary" round @click="goSignUp">注册</el-button>
        </div>
        </el-header>
        <el-main>
            <el-carousel height="55vh" style="width: 80vw; margin: 20vh auto;" pause-on-hover=true interval="5000">
                <el-carousel-item key=1>
                    <div id="trend" style="height: 54vh; width: 80vw; margin: 0 auto;"></div>
                </el-carousel-item>
                <el-carousel-item key=2>
                    <div id="death" style="height: 54vh; width: 80vw; margin: 0 auto;"></div>
                </el-carousel-item>
            </el-carousel>
        </el-main>
      </el-container>

      <div class="dialog-box">
        <el-dialog  title="用户信息" v-model="showSignUpDialog" width="30%" :align-center="alignCenter">
            <el-form class="form-box" :model="newForm" :rules="rules">
                <el-form-item label="用户名" prop="username">
                    <el-input class="dialog_input" v-model="newForm.username"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input class="dialog_input" v-model="newForm.password"></el-input>
                </el-form-item>

                <el-form-item label="性别" prop="gender">
                    <el-select v-model="this.newForm.gender">
                        <el-option
                            v-for="item in genderOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="出生日期" prop="birthday">
                    <el-date-picker
                        v-model="newForm.birthday"
                        type="date"
                        placeholder="选择出生日期"
                        format="YYYY/MM/DD"
                        value-format="YYYY-MM-DD"
                    />
                </el-form-item>

                <el-form-item label="居住地" prop="district">
                    <el-input class="dialog_input" v-model="newForm.district"></el-input>
                </el-form-item>
            </el-form>

            <div slot="footer" class="dialog-footer">
                <el-button @click="cancelHandle">取 消</el-button>
                <el-button type="primary" @click="submit">确 定</el-button>
            </div>
        </el-dialog>
      </div>
      
    </div>

    
  </template>
  

<script lang="ts">
import * as echarts from 'echarts/core';
import {ElMessageBox} from "element-plus";
import {
  ToolboxComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent,
  DatasetComponent
} from 'echarts/components';
import { BarChart } from 'echarts/charts';
import { PieChart } from 'echarts/charts';
import { LabelLayout } from 'echarts/features';
import { CanvasRenderer } from 'echarts/renderers';

export default {
    name: "home",
    data(){
        return {
            alignCenter: true,
            showSignUpDialog: false,
            newForm: {
                username: '',
                password: '',
                name: '',
                gender: '',
                birthday: '2000-01-01',
                district: "",
            },
            rules: {
                username: [
                    {required: true, message: '账号不能为空', trigger: 'blur'}
                ],
                password: [
                    {required: true, message: '密码不能为空', trigger: 'blur'}
                ],
                name: [
                    {required: true, message: '姓名不能为空', trigger: 'blur'}
                ],
                gender: [
                    {required: true, message: '性别不能为空', trigger: 'blur'}
                ],
                age: [
                    {required: true, message: '年龄不能为空', trigger: 'blur'}
                ]
            },
            genderOptions: [
                {
                    label: "女",
                    value: "女"
                },
                {
                    label: "男",
                    value: "男"
                }
            ],
        }
    },
    methods:{
        goSignUp(){
            this.showSignUpDialog = true
        },        
        submit() {
            let newUser = {
                'username': this.newForm.username,
                'password': this.newForm.password,
                'gender': this.newForm.gender,
                'birthday': this.newForm.birthday,
                'district': this.newForm.district
            }
        
                this.axios.post('/spring_api/user', newUser)
                    .then((response) => {
                        let res = response.data
                        if (res === "Success") {
                            ElMessageBox.alert("注册成功！", {
                                title: "提示",
                                confirmButtonText: "确定",
                                type: "success"
                            })
                            this.showSignUpDialog = false;
                        } else if (res === "Exist") {
                            ElMessageBox.alert("已存在相同账号，请换个账号名重试！", {
                                title: "提示",
                                confirmButtonText: "返回",
                                type: "warning"
                            })
                        } else {
                            ElMessageBox.alert("添加失败，稍后重试！或检查系统！", {
                                title: "提示",
                                confirmButtonText: "返回",
                                type: "error"
                            })
                        }
                    })
                    .catch((error) => {
                        console.log(error)
                        ElMessageBox.alert("添加失败，稍后重试！或检查系统！", {
                                title: "提示",
                                confirmButtonText: "返回",
                                type: "error"
                            })
                    })            

        },
        cancelHandle() {
            this.showSignUpDialog = false
        },
        goLogin(){
            this.$router.push('/login')
        },
        drawTrend(){
            this.trendLine = echarts.init(document.getElementById('trend'));
            let option = {
                title:[
                    {
                        subtext: '1990-2019年中国皮肤疾病流行病学情况调查：发病率高达53%以上，流行率高达25%以上',
                        left: '50%',
                        bottom: '0%',
                        textAlign: 'center',
                        subtextStyle: {
                            fontSize: 20,
                            color: 'black'
                        }
                    },
                ],
                legend: {
                    textStyle:{
                        fontSize: 18,
                        color: 'black'
                    },
                    top: "5%"
                },
                tooltip: {},
                dataset: {
                    source: [
                    ['product', '1990', '2019'],
                    ['发病人数', 606509566, 784395281],
                    ['患病人数', 290124075, 369127390],
                    ]
                },
                xAxis: {
                    data: ['发病人数', '患病人数'],
                    type: 'category', 
                    axisLabel:{
                        textStyle:{
                        fontSize: 15,
                        color: 'black'
                        },
                    },
                    
                    
                },
                yAxis: {
                    axisLabel:{
                        textStyle:{
                        fontSize: 15,
                        color: 'black'
                        },
                    },
                    splitLine:{
                        show: true,
                        lineStyle: {
                            color: 'black',
                            width: 1,
                            type: 'dashed'
                        }
                    },
                },
                color: ['#2878B5', '#C82423'],
                // Declare several bar series, each will be mapped
                // to a column of dataset.source by default.
                series: [
                    {
                        type: 'bar',
                        barGap: '20%',
                        barCategoryGap: '40%',
                        label: {
                            show: true,
                            position: 'top',
                            textStyle: {
                                fontSize: 15,
                                color: 'black'
                            }
                        },
                    },
                    {
                        type: 'bar',
                        label: {
                            show: true,
                            position: 'top',
                            textStyle: {
                                fontSize: 15,
                                color: 'black'
                            }
                        },
                    }
                ]
            };
            this.trendLine.setOption(option);
        },
        drawDeath(){
            this.deathPie = echarts.init(document.getElementById('death'));
            let option = {
                title: [
                    {
                        text: '黑色素瘤不同阶段的5年存活率',
                        left: '50%',
                        top: '5%',
                        textAlign: 'center',
                        textStyle: {
                            fontSize: 20,
                            color: 'black'
                        }
                    },
                    {
                    subtext: 'I期中位生存期：5年',
                    left: '25%',
                    top: '45%',
                    textAlign: 'center',
                    subtextStyle: {
                        fontSize: 18,
                        color: 'black'
                    }
                    },
                    {
                    subtext: 'II期中位生存期：4.25年',
                    left: '75%',
                    top: '45%',
                    textAlign: 'center',
                    subtextStyle: {
                        fontSize: 18,
                        color: 'black'
                    }
                    },
                    {
                    subtext: 'III期中位生存期：2.83年',
                    left: '25%',
                    top: '90%',
                    textAlign: 'center',
                    subtextStyle: {
                        fontSize: 18,
                        color: 'black'
                    }
                    },
                    {
                        subtext: 'IV期中位生存期：1.42年',
                        left: '75%',
                        top: '90%',
                        textAlign: 'center',
                        subtextStyle: {
                            fontSize: 18,
                            color: 'black'
                        }
                    },
                    
                ],
                legend: {
                    textStyle:{
                        fontSize: 18,
                        color: 'black'
                    },
                    top: "10%"
                },
                tooltip: {},
                dataset: {
                    source: [
                    ['stage', 'I期', 'II期', 'III期', 'IV期'],
                    ['存活', 94, 44, 34, 4.6],
                    ['死亡', 6, 56, 66, 95.4],
                    ]
                },
                color: ['#2878B5', '#C82423'],
                series: [
                    {
                        type: 'pie',
                        radius: '25%',
                        center: ['25%', '30%'],
                        encode: {
                            itemName: 'stage',
                            value: 'I期'
                        }
                    },
                    {
                        type: 'pie',
                        radius: '25%',
                        center: ['75%', '30%'],
                        encode: {
                            itemName: 'stage',
                            value: 'II期'
                        }
                    },
                    {
                        type: 'pie',
                        radius: '25%',
                        center: ['25%', '75%'],
                        encode: {
                            itemName: 'stage',
                            value: 'III期'
                        },

                    },
                    {
                        type: 'pie',
                        radius: '25%',
                        center: ['75%', '75%'],
                        encode: {
                            itemName: 'stage',
                            value: 'IV期'
                        }
                    }
                ]
            };
            this.deathPie.setOption(option);
        }
    },
    mounted() {
        this.drawTrend()
        this.drawDeath()
    },
}
</script>

<style lang="less" scoped>
    .layout-box{
        background: url("../assets/background.jpg");
        width: 100vw;
        height: 100vh;
        background-size: cover;
        overflow: hidden;
    }
    .header-box{
        height: 10vh;
        width: 100vw;
        margin-left: 0;
        background: white;
    }
    #logo{
        display: inline-block;
        height: 10vh;
        position:absolute;
        left: 10vw;
    }
    #banner{
        display: inline-block;
        font-size: 60px;
        line-height: 10vh;
        color: #08a6f3;
        position:absolute;
        left: 18vw;
    }
    #sub-banner{
        display: inline-block;
        font-size: 30px;
        line-height: 10vh;
        color: #2fd2bd;
        top: 2vh;
        position:absolute;
        right: 20vw;
        bottom: 0;
    }
    .login-btn{
        font-size: 20px;
        line-height: 30px;
        color: white;
        top: 3vh;
        position: absolute;
        right: 10vw;
    }
    .sign-btn{
        font-size: 20px;
        line-height: 30px;
        color: white;
        top: 3vh;
        position: absolute;
        right: 5vw;
    }

    .demonstration {
        color: var(--el-text-color-secondary);
    }
    
    .el-carousel__item{
        background-color:rgba(255,255,255,0.5);
    }

    .el-carousel__item h3 {
        opacity: 0.75;
        line-height: 200px;
        margin: 0;
        text-align: center;
    }

    .form-box {
        width: 70%;
        margin: 0 auto;
    }

</style>