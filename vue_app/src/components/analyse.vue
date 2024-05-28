<template>
    <div class="layout">
        <el-container class="layout-container"   v-loading="loading" element-loading-text="加载中..." element-loading-background="rgba(122, 122, 122, 0.8)">
            <el-container class="main-container">
                <div class="left-box">
                        <div class="left-content" >
                            <el-menu default-active="0" @select="handleSelect">
                                <h3>诊断情况</h3>
                                <el-menu-item index="0">
                                    <span  ref="top1">Top1:(尚无结果)</span>
                                </el-menu-item>
                                <el-menu-item index="1">
                                    <span  ref="top2">Top2:(尚无结果)</span>
                                </el-menu-item>
                                <el-menu-item index="2">
                                    <span ref="top3">Top3:(尚无结果)</span>
                                </el-menu-item>
                                <el-menu-item index="3" disabled>
                                    ......
                                </el-menu-item>
                            </el-menu>
                        </div>
                    </div>

                    <div class="middle-box">
                        <div class="middle-content">
                            <h3>诊断图片</h3>
                            <div class="pic-box">
                                <el-image :src="fileList.url">
                                    <template #error>
                                        <div class="image-slot">
                                            <el-icon style="height: 350px"><Picture/></el-icon>
                                        </div>
                                    </template>
                                </el-image>
                            </div>
                            <el-upload
                                ref="upload"
                                class="upload-demo"
                                action="/flask_api/detect"
                                :limit="1"
                                :on-exceed="handleExceed"
                                :auto-upload="false"
                                :on-change="handleChange"
                                :on-success="handleSuccess"
                                :on-error="handleError"
                            >
                                <template #trigger>
                                    <el-button type="primary">
                                        选择图片
                                    </el-button>
                                </template>
                                <el-button class="ml-3" type="success" @click="submitUpload">
                                    上传检测
                                </el-button>
                                <template #tip>
                                    <div class="el-upload__tip text-red">
                                        仅限制一个文件， 多出的文件将会将之前的文件覆盖
                                    </div>
                                </template>
                            </el-upload>
                        </div>
                    </div>

                    <div class="right-box">
                        <div class="right-content">
                            <h3>诊疗建议</h3>
                                <transition name="fade" mode="out-in" appear>
                                    <div class="brief-box" v-show="turn">
                                        <div class="card-header">疾病简介</div>

                                        <div class="brief-content" ref="brief"></div>
                                    </div>
                                </transition>

                                <transition name="fade" mode="out-in" appear>
                                    <div class="treatment-box" v-show="turn">
                                        <div class="card-header">治疗方法</div>
                                        <div class="treatment-content" ref="treatment"></div>
                                    </div>
                                </transition>
                        </div>
                    </div>
                
            </el-container>
        </el-container>
    </div>
</template>

<script lang="ts">
import {ElMessageBox} from "element-plus";
export default {
    name: "analyse",
    data() {
        return {

            turn: true,
            timer: null,
            loading: false,
            fileList: {
                name: '',
                url: ''
            },
            id_to_class : {0: 'MEL', 1: 'NV', 2: 'BCC', 3: 'AKIEC', 4: 'BKL', 5: 'DF', 6: 'VASC'},
            disease: {
                'MEL': '黑色素瘤',
                'NV': '黑素细胞痣',
                'BCC': '基底细胞癌',
                'AKIEC': '光化性角化病',
                'BKL': '良性角化病',
                'DF': '皮肤纤维瘤',
                'VASC': '血管病变',
            },
            content_show: 3,
            advice_display:[
                {
                    brief: '',
                    treatment: ''
                },
                {
                    brief: '',
                    treatment: ''
                },
                {
                    brief: '',
                    treatment: ''
                }
            ],
            suggestions:[
                {
                    brief: "黑色素瘤，通常是指恶性黑色素瘤，是黑色素细胞来源的一种高度恶性的肿瘤，多发生于皮肤，也可见于黏膜和内脏。" +
                        "好发于成人，极少见于儿童。 恶性黑色素瘤可由先天性或获得性良性黑素细胞痣演变而成，或由发育不良性痣恶变而来，也可以是新发生的。",
                    treatment: "手术切除\n干扰素；\n化疗放射治疗"
                },
                {
                    brief: "黑色素痣是由一群良性的黑色素细胞，聚集在表皮与真皮的交界产生的。" +
                        "黑色素细胞可能会分布在网状真皮下部，结缔组织束之间，围绕皮肤的其它附属器官如汗腺、毛囊、血管、神经等等，偶尔还会延伸到皮下脂肪。",
                    treatment: "在医学上黑色素痣属于良性的肿瘤，一般而言并不需要治疗。（想要去除黑色素痣，常用的方式有，手术切除、雷射治疗、电波刀切除等。）"
                },
                {
                    brief:"基底细胞癌多见于老年人，好发于头、面、颈及手背等处。最初是皮肤色到暗褐色浸润的小结节，较典型者为蜡样、半透明状结节，有高起卷曲的边缘。" +
                        "中央开始破溃，结黑色坏死性痂，中心坏死向深部组织扩展蔓延，呈大片状侵袭性坏死，可以深达软组织和骨组织。",
                    treatment: "手术切除；\nX线照射；\n激光治疗；"
                },
                {
                    brief: "光化性角化病是一种职业病，主要受日光、紫外线、放射性热能以及沥青或煤及其提炼而物诱发本病。" +
                        "病损多见于中年以上男性日光暴露部位，如面部、耳廓、手背等。主要表现为表面粗糙，可见角化性鳞屑。揭去鳞屑，可见下方的基面红润，凹凸不平，呈乳头状。",
                    treatment: "口服：B-顺维甲酸；芳香维甲酸依曲替酯\n"+"外涂：氨苯甲酸制剂；维甲酸软膏；氟尿嘧啶软膏"
                },
                {
                    brief: "角化病的病因是角化毛孔被角栓闭塞，呈毛孔性角化小丘疹。" +
                        "部分病人有甲状腺机能低下，或有库辛氏症候群。也有一部分的病人是使用皮质类固醇以后，才发生此种皮肤病。常见于异位性倾向的病人，或遗传性，多发于同一家族。",
                    treatment: "口服：青霉胺\n" +
                        "外涂：二巯基丙醇软膏；皮质类固醇霜\n" +
                        "注射：硫代硫酸钠（静注）；二巯基丙磺酸钠（肌注）"
                },
                {
                    brief:"皮肤纤维瘤是成纤维细胞或组织细胞灶性增生引致的一种真皮内的良性肿瘤。" +
                        "本病可发生于任何年龄，中青年多见，女性多于男性。可自然发生或外伤后引起。黄褐色或淡红色的皮内丘疹或结节是本病的临床特征。病损生长缓慢，长期存在，极少自行消退。",
                    treatment:"一般不需治疗，少数损害数年内可消退。（若单个损害有疼痛引起病人痛苦时可行手术切除。皮质类固醇激素皮内注射有一定的疗效）"
                },
                {
                    brief: "血管病变是皮肤和下层组织相对常见的异常，影响多达50%的18岁及以上的女性。" +
                        "血管病变包括一系列病症，无论是腿部静脉，面部静脉，血管瘤或静脉湖。蜘蛛静脉，毛细血管扩张和弥漫性发红通常是衰老，日晒或怀孕的结果，有时也是遗传造成的。",
                    treatment: "激光和基于光的设备是安全有效地消除和减少不必要的血管问题的可行治疗选择。"
                }
            ],
        };
    },
    methods: {
        handleChange(file){
            let url = null

            url = URL.createObjectURL(file.raw)
            this.fileList.url = URL.createObjectURL(file.raw)
            this.fileList.name = file.raw.name
        },
        handleExceed(files){
            this.$refs['upload'].clearFiles()
            this.$refs['upload'].handleStart(files[0])
        },
        handleSuccess(response){
            const descend = (a, b)=>{
                return b['probability'] - a['probability'];
            }
            console.log(response)
            let res = response.sort(descend)


            this.loading = true
            this.timer = setTimeout(()=>{
                this.loading=false;
                this.$refs['top1'].innerText = 'Top1: ' + this.disease[this.id_to_class[res[0].id]] + ' - ' + (res[0].probability*100).toFixed(2).toString() + '%';
                this.$refs['top2'].innerText = 'Top2: ' + this.disease[this.id_to_class[res[1].id]]  + ' - ' + (res[1].probability*100).toFixed(2).toString() + '%';
                this.$refs['top3'].innerText = 'Top3: ' + this.disease[this.id_to_class[res[2].id]]  + ' - ' + (res[2].probability*100).toFixed(2).toString() + '%';
                this.advice_display[0] = this.suggestions[parseInt(res[0].id)];
                this.advice_display[1] = this.suggestions[parseInt(res[1].id)];
                this.advice_display[2] = this.suggestions[parseInt(res[2].id)];

                this.$refs['brief'].innerText = this.advice_display[0].brief;
                this.$refs['treatment'].innerText = this.advice_display[0].treatment;
                
                this.sendRecord(this.id_to_class[res[0].id])

            }, 500)

        },
        handleError(){
            ElMessageBox.alert("上传失败，请稍后重试或联系管理员！",{
                title:"提示",
                confirmButtonText:"确定",
                type:"error"
            })
        },
        submitUpload(){
            this.$refs['upload'].submit();
        },
        handleSelect(index){
            this.turn = false;
            this.$refs['brief'].innerText = this.advice_display[index].brief;
            this.$refs['treatment'].innerText = this.advice_display[index].treatment;
            this.turn = true;
        },
        exitLogin(){
            ElMessageBox.confirm("确定要退出登录吗？",{
                title: "提示",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            })
                .then(()=>{
                    sessionStorage.setItem('user_name', '')
                    sessionStorage.setItem('user_authenticated', 'false')
                    this.$router.push("/")
                })
                .catch((action)=>{


                })

        },
        async sendRecord(disease){
            this.axios.post(
                '/spring_api/record',
            {
                'username': sessionStorage.getItem('user_name'),
                'disease': disease
            },
            {
              headers: {
                'Content-Type': 'application/json',
              }
            }
          );
        }
    },

    beforeUnmount() {
        clearTimeout(this.timer)
    },

};
</script>

<style lang="less" scoped>

    .layout {
        width: 100%;
        height: 100%;
    }

    .layout-container {
        width: 100%;
        height: 100%;
    }

    .main-container {
        display: flex;
        width: 80%;
    }

    .el-header {
        height: 60px;
        display: flex;
        box-shadow: 0 5px 5px 0 #0083bb;
    }

    .logo-box {
        height: 50px;
        width: 50px;
        display: flex;
        margin-left: 100px;
        margin-top: 5px;
    }

    .logo {
        width: 60px;
    }

    .slogan-box {
        height: 100%;
        display: flex;
        width: 100%;
        font-size: 5vh;
        margin-top: 20px;
        color: #00419d;
        font-weight: bold;
        margin-left: 5px;
    }

    h3 {
        line-height: 36px;
        font-size: 18px;
        display: block;
    }

    .left-box, .right-box {
        display: flex;
        width: 25%;
        height: 100%;
    }

    .middle-box {
        display: flex;
        width: 50%;
        height: 100%;
    }

    .left-content, .middle-content, .right-content {
        margin: 15px;
        width: 100%;
        height: 95%;
        box-shadow: 0 0 5px 0 silver;
    }

    .pic-box {
        width: 90%;
        height: 60%;
        border: 2px dashed silver;
        margin: 5px 5px 5px 5%;
    }
    .el-image{
        width: 100%;
        height: 100%;
    }

    .el-button{
        margin: 15px;
    }

    .brief-box{
        width: 90%;
        height: 40%;
        box-shadow: 0 0 3px 0 silver;
        margin-left: 5%;
        margin-bottom: 10px;
    }

    .treatment-box{
        width: 90%;
        height: 40%;
        box-shadow: 0 0 3px 0 silver;
        margin-left: 5%;
        margin-bottom: 10px;
    }

    .brief-content, .treatment-content{
        text-align: left;
        margin: 15px;
        padding-bottom: 10px;
    }


    .card-header{
        border-bottom: 1px solid silver;
        padding-bottom: 10px;
        margin-bottom: 10px;
        padding-top: 10px;
    }

    .close-box {
        width: 30px;
        height: 30px;
        display: flex;
        margin-top: 12.5px;
        margin-right: 100px;
    }

    .close {
        height: 30px;
    }

    .close-box:hover {
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
</style>
