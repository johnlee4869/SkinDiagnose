<template>
    <div class="layout-box">
        <div class="content-box">
            <div id="trend"></div>
            <div id="total"></div>
        </div>
    </div>

</template>

<script>
import * as echarts from 'echarts'
export default {
    name: "statistics",
    data() {
        this.trendLine = null
        this.totalBar = null
        return {
            timeSelect:'7days',
            timeOptions:[
                {
                    label: '近7天',
                    value: '7days'
                },
                {
                    label: '近1个月',
                    value: '30days'
                },
                {
                    label: '近半年',
                    value: '6months'
                },
                {
                    label: '近1年',
                    value: '1year'
                },
                {
                    label: '近3年',
                    value: '3years'
                },
                {
                    label: '近5年',
                    value: '5years'
                }
            ],
            trend:{},
            total:{}
        }
    },
    mounted() {
        this.getTotal()
            .then((response)=>{
                this.drawBar()
            })
        this.getTrend()
            .then((response)=>{
                this.drawLine()
            })
    },
    methods:{
        changeHandle(){
            this.getInfo()
                .then((response)=>{
                    this.drawLine()
                    this.drawBar()
                })
        },
        async getTotal(){
            await this.axios.get('/spring_api/record/queryCount')
                .then((response)=>{
                    this.total = response.data
                })
                .catch((error) => {
                        console.log(error)
                        ElMessageBox.alert("系统异常，请检查并修复！",{
                            title:"警告",
                            confirmButtonText:"返回",
                            type:"warning"
                        })
                    })
        },
        async drawBar(){
            if(this.totalBar != null)
                this.totalBar.dispose()
            this.totalBar = echarts.init(document.getElementById('total'))
            let barOption = {
                title: {
                    text:"各项疾病统计",
                    textStyle:{
                        fontSize: 14
                    }
                },
                tooltip: {
                    trigger: 'axis',
                    backgroundColor: "white", // 提示框浮层的背景颜色
                    padding: 15, // 提示框浮层内边距，单位px
                    textStyle: {
                        color: "black", // 文字的颜色
                        fontStyle: "normal", // 文字字体的风格（'normal'，无样式；'italic'，斜体；'oblique'，倾斜字体）
                        fontWeight: "bold", // 文字字体的粗细（'normal'，无样式；'bold'，加粗；'bolder'，加粗的基础上再加粗；'lighter'，变细；数字定义粗细也可以，取值范围100至700）
                    },
                    valueFormatter: (value) => {
                        return value;
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                color: ['#cd9823', '#bf7545', '#f22304', '#a1ba06', '#2e92c2', '#f79e88', '#b44ab3'],
                xAxis: {
                    type: 'category',
                    boundaryGap: true,
                    data: ['NV', 'MEL', 'BCC', 'AKIEC', 'BKL', 'DF',  'VASC']
                },
                yAxis: {
                    type: 'value'
                },
                series: []
            }

            //async返回Promise 对象, 需要使用.then来提取信息
            let res=this.total;

            let se = {
                type: 'bar',
                data: [],
                itemStyle: {
                    normal: {
    　　　　　　　　    //这里是重点
                        color: function(params) {
                        //注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
                                    var colorList = ['#cd9823', '#bf7545', '#f22304', '#a1ba06', '#2e92c2', '#f79e88', '#b44ab3'];
                                    return colorList[params.dataIndex]
                                }
                    }
                },
                barWidth: '50%'
            }
            for(var i=0; i<res.length; i++){
                se.data.push(res[i]['count'])
            }
            barOption.series.push(se)
            this.totalBar.setOption(barOption)
        },

        async getTrend(){
            await this.axios.get('/spring_api/record/queryByMonth')
                .then((response)=>{
                    this.trend = response.data
                })
                .catch((error) => {
                        console.log(error)
                        ElMessageBox.alert("系统异常，请检查并修复！",{
                            title:"警告",
                            confirmButtonText:"返回",
                            type:"warning"
                        })
                    })
        },
        
        async drawLine(){
            if(this.trendLine!=null)
                this.trendLine.dispose()
            this.trendLine = echarts.init(document.getElementById('trend'))
            let lineOption = {
                title: {
                    text: '各项疾病趋势',
                    textStyle:{
                        fontSize: 14
                    }
                },
                tooltip: {
                    trigger: 'axis',
                    backgroundColor: "white", // 提示框浮层的背景颜色
                    padding: 15, // 提示框浮层内边距，单位px
                    textStyle: {
                        color: "black", // 文字的颜色
                        fontStyle: "normal", // 文字字体的风格（'normal'，无样式；'italic'，斜体；'oblique'，倾斜字体）
                        fontWeight: "bold", // 文字字体的粗细（'normal'，无样式；'bold'，加粗；'bolder'，加粗的基础上再加粗；'lighter'，变细；数字定义粗细也可以，取值范围100至700）
                    },
                    valueFormatter: (value) => {
                        return value;
                    }
                },
                legend: {
                    data: ["NV", "MEL", "BCC", "AKIEC", "BKL", "DF", "VASC"]
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                color: ['#cd9823', '#bf7545', '#f22304', '#a1ba06', '#2e92c2', '#f79e88', '#b44ab3'],
                xAxis: {
                    type: 'category',
                    boundaryGap: true,
                    axisLabel:{
                        interval:0,
                        rotate:40,
                    },
                    data: []
                },
                yAxis: {
                    type: 'value'
                },
                series: []
            }

            let res = this.trend;
            
            let timeList = new Array()

            for(let time in Object.keys(res)){
                timeList.unshift(Object.keys(res)[time])
            }
            timeList.sort(function(a, b){
                if (a < b)  return -1;
                else if (a > b)   return 1;
                else    return 0;
            })
            
            
            let nv = new Array()
            let mel = new Array()
            let bcc = new Array()
            let akiec = new Array()
            let bkl = new Array()
            let df = new Array()
            let vasc = new Array()

            for(let i=0; i<timeList.length; i++){
                let currTime = timeList[i]
                lineOption.xAxis.data.push(currTime)
                let currDiseaseData = res[currTime]
                console.log(currDiseaseData)
                if (currDiseaseData['NV'] != undefined)
                    nv.push(currDiseaseData['NV'])
                else
                    nv.push(0)
                if (currDiseaseData['MEL']!=undefined)
                    mel.push(currDiseaseData['MEL'])
                else
                    mel.push(0)
                if (currDiseaseData['BCC'] != undefined)
                    bcc.push(currDiseaseData['BCC'])
                else
                    bcc.push(0)
                if (currDiseaseData['AKIEC'] != undefined)
                    akiec.push(currDiseaseData['AKIEC'])
                else
                    akiec.push(0)
                if (currDiseaseData['BKL'] != undefined)
                    bkl.push(currDiseaseData['BKL'])
                else
                    bkl.push(0)
                if (currDiseaseData['DF'] != undefined)
                    df.push(currDiseaseData['DF'])
                else
                    df.push(0)
                if (currDiseaseData['VASC'])
                    vasc.push(currDiseaseData['VASC'])
                else
                    vasc.push(0)
            }

            let dataList = new Array()
            dataList['NV'] = nv
            dataList['MEL'] = mel
            dataList['BCC'] = bcc
            dataList['AKIEC'] = akiec
            dataList['BKL'] = bkl
            dataList['DF'] = df
            dataList['VASC'] = vasc
    
            let index = 0
            for(let key in dataList){
                let se = {
                    name: key,
                    type: 'line',
                    data: dataList[key],
                    lineStyle: {
                        color: lineOption.color[index++]
                    }
                }
                lineOption.series.push(se)
            }
            console.log(lineOption.series)
            this.trendLine.setOption(lineOption);
        },
    }
}
</script>

<style scoped>
    .layout-box{
        width: 80vw;
        height: 80vh;
    }
    .content-box{
        margin-top: 5vh;
        margin-left: 5vw;
    }
    #trend{
        width: 80vw;
        height: 40vh;
    }
    #total{
        width: 80vw;
        height: 40vh;
    }

</style>