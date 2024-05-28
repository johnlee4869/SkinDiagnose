# 皮肤康-SkinDiagnose 

[(点击进入GitHub项目主页)](https://github.com/AnonymMOMO/SkinDiagnose)

皮肤康（SkinDiagnose）是一款基于深度学习技术的皮肤病诊断系统，并采用了前后端分离技术对模型进行服务封装。

前端采用Vue.js作为开发框架，
后端采用双后端混合模式，
即以Flask作为诊断模型的服务节点，提供访问诊断模型的API，
以SpringBoot作为后台数据管理节点，提供数据持久化的API。
同时，为了方便部署，我们将Vue.js(Nginx)、诊断模型(Flask)、SpringBoot、MySQL都部署在Docker容器中，以实现一键启动。

皮肤病模型训练数据集来源：ISIC2019

（PS: 由于数据集问题，目前仅支持诊断少量类型的疾病）


# 1. 预备工作
## 1.1 安装软件

本项目依赖Docker Desktop实现一键启动，所以需要下载并安装Docker Desktop(Windows & MacOS)

对于Linux系统，请按照官方文档安装docker与docker-compose等组件。

请保证docker和docker-compose版本能够匹配，且支持version 3以上的compose配置文件。

## 1.2 设置Gemini API-Key
打开docker/vue_app文件夹中的api_key.js文件，
在引号中输入个人申请的Gemini API-Key，
否则无法使用在线问答功能。

## 1.3 注意事项
端口占用：8080(前端), 5000(Flask), 8888(SpringBoot), 3306(数据库)

容器名称占用：vue-service, flask-service, springboot-service, mysql-service

在启动应用前注意检查以上端口是否被占用，以及与现有运行中的docker容器名称是否存在重复，
如果被存在这些情况会导致软件系统无法正常运行。

# 2. 开始

## 2.1 启动应用
启动Docker，打开命令行终端，进入到SkinDiagnose项目根目录下，输入`docker-compose up --build`指令

## 2.2 进入应用
打开浏览器

（1）用户：输入http://localhost:8080 进入首页

（2）管理员：输入http://localhost:8080/admin 进入后台管理页面，
用户名为admin,密码为123456


