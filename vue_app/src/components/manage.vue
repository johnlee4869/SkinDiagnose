<template>
    <div>
        <el-table :data="filterTableData">
            <el-table-column label="账号" prop="username" width="200"/>
            <el-table-column label="密码(sha256)" prop="password" width="300"/>
            <el-table-column label="状态" prop="state" width="150"/>
            <el-table-column label="性别" prop="gender"  width="150" />
            <el-table-column label="出生日期" prop="birthday" width="150"/>
            <el-table-column label="居住地" prop="district" width="150"/>

            <el-table-column align="right">
                <template #header>
                    <el-input class="searchInput" v-model="search" size="large" placeholder="输入姓名搜索"
                              prefix-icon="Search"/>
                </template>
                <template #default="scope">
                    <el-button @click="editUser(scope.row)">编辑</el-button>
                    <el-button
                        type="danger"
                        @click="deleteUser(scope.$index, scope.row)">
                        删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog class="dialog-box" title="用户信息" v-model="showDialog">
            <el-form class="form-box" :model="newForm" :rules="rules">
                <el-form-item label="用户名" prop="username">
                    <el-input class="dialog_input" v-model="newForm.username"
                              disabled></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input class="dialog_input" v-model="newForm.password"></el-input>
                </el-form-item>

                <el-form-item label="性别" prop="gender">
                    <el-select v-model="this.newForm.gender"
                               disabled>
                        <el-option
                            v-for="item in sexOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="状态" prop="state">
                    <el-select v-model="this.newForm.state">
                        <el-option
                            v-for="item in stateOptions"
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
                        disabled
                        format="YYYY/MM/DD"
                        value-format="YYYY-MM-DD"
                    />
                </el-form-item>
            
                <el-form-item label="居住地" prop="district">
                    <el-input class="dialog_input" v-model="newForm.district" disabled></el-input>
                </el-form-item>
            </el-form>

            
            <div slot="footer" class="dialog-footer">
                <el-button @click="cancelHandle">取 消</el-button>
                <el-button type="primary" @click="submit">确 定</el-button>
            </div>
        </el-dialog>
    </div>


</template>

<script>
import {ElMessageBox} from "element-plus";

export default {
    name: "manage",
    data() {
        return {
            search: '',
            userData: [],
            showDialog: false,
            newForm: {
                username: '',
                password: '',
                name: '',
                gender: '女',
                age: '2000-01-01',
                state: "正常",
                district: "",
            },
            submitType: '',
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
            stateOptions: [
                {
                    label: "正常",
                    value: "正常"
                },
                {
                    label: "冻结",
                    value: "冻结"
                },
                {
                    label: "注销",
                    value: "注销"
                }
            ],
            sexOptions: [
                {
                    label: "女",
                    value: "女"
                },
                {
                    label: "男",
                    value: "男"
                }
            ],
            sexSelected: "女",
            pickerMinDate: "",//第一次选中的时间
            
        }
    },
    computed: {
        filterTableData() {
            return this.userData.filter((data) => {
                return this.search === "" ? data : data.name.includes(this.search)
            })
        },
    },
    mounted() {
        this.query();
    },
    methods: {
        disabledDate(time){
            let curDate = (new Date()).getTime();
            let year = 18 * 365 * 24 * 3600 * 1000;
            let maxTime = curDate - year
            return time.getTime() > maxTime;
        },
        query() {
            
            this.axios.get("/spring_api/user",
            )
                .then((response) => {
                    this.userData = response.data
                })
                .catch((error) => {
                    console.log(error)
                })
        },
        deleteUser(index, row) {
            ElMessageBox.confirm("你确定删除该项信息吗?", "提示", {
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then((action) => {
                    if (action === 'confirm') {
                        this.axios.delete('/spring_api/user/'+row['username'])
                        .then((response) => {

                            let res = response.data
                            if (res == "Success") {
                                ElMessageBox.alert("删除成功！", {
                                    title: "提示",
                                    confirmButtonText: "确定",
                                    type: "success"
                                })
                            } else if (res == "NotExist") {
                                ElMessageBox.alert("该用户不存在！", {
                                    title: "提示",
                                    confirmButtonText: "确定",
                                    type: "warning",
                                })
                            }
                            this.query()
                        })
                        .catch((error) => {
                            console.log(error)
                            ElMessageBox.alert("删除失败，请稍后重试或检查系统！", {
                                title: "提示",
                                confirmButtonText: "确定",
                                type: "error",
                            })
                            this.query()
                        })
                    }
                })
        },
        
        editUser(row) {
            this.newForm = row
            this.showDialog = true
        },
        submit() {
            let data = {
                'username': this.newForm.username,
                'password': this.newForm.password,
                'gender': this.newForm.gender,
                'birthday': this.newForm.birthday,
                'district': this.newForm.district,
                'state': this.newForm.state
            }

                this.axios.put("/spring_api/user", data)
                    .then((response) => {
                        let res = response.data
                        console.log(res)
                        if (res === "Success") {
                            ElMessageBox.alert("修改成功", {
                                title: "提示",
                                confirmButtonText: "确定",
                                type: 'success'
                            })
                        } else if (res === "NotExist"){
                            this.newForm = data
                            ElMessageBox.alert("该用户不存在！", {
                                title: "提示",
                                confirmButtonText: "返回",
                                type: 'error'
                            })
                        } else {
                            this.newForm = data
                            ElMessageBox.alert("修改失败，稍后重试！或检查系统！", {
                                title: "提示",
                                confirmButtonText: "返回",
                                type: 'error'
                            })

                        }
                        this.query()
                        this.showDialog = false
                    })
                    .catch((error) => {
                        console.log(error)
                        this.newForm = data
                        ElMessageBox.alert("修改失败，稍后重试！或检查系统！", {
                            title: "提示",
                            confirmButtonText: "返回",
                            type: 'error'
                        })
                        
                    })

            

        },
        cancelHandle() {
            this.showDialog = false
            this.query()
        },

    }
}
</script>

<style lang="less" scoped>
.form-box {
    width: 70%;
    margin-left: 20%;
}

.searchInput {
    display: inline-block;
    width: 150px;
}

.dialog_input {
    width: 80%;
}

.add-btn {
    display: inline-block;
    margin-right: 5px;
}

.el-table-column {
    resizable: true;
}
</style>