<template>
    <div class="chat-container">
      <div class="chat-messages" ref="chatMessages">      
        <div v-for="(message, index) in messages" :key="index" class="message-container" :class="{ 'user-message-container': message.sender === 'user', 'bot-message-container': message.sender === 'bot' }">
          <div style="white-space: pre-wrap; " class="message" :class="{ 'user-message': message.sender === 'user', 'bot-message': message.sender === 'bot' }">{{ message.content }}</div>
        </div>
        <div v-if="loading" class="loading-animation"></div>
      </div>
      
    <div class="input-container">
        <el-input class="chat-input" v-model="newMessage" @keyup.enter="sendMessage" style="width: 90%; font-size: 15px;" :rows="3" type="textarea" placeholder="请输入您的问题。。。"/>
        <el-button type="primary" plain @click="sendMessage" style="width: 8%; height: 40%; margin-left: 2%; font-size: 15px;">发送</el-button>
    </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  import {api_key} from '../../gemini_api-key.js'
  export default {
    data() {
      return {
        newMessage: '',
        messages: [{content: '您好，很高兴为您服务！请问您有什么需要帮助的吗？', sender: 'bot'}],
        loading: false
      };
    },
    methods: {
      async sendMessage() {
        if (!this.newMessage.trim()) return;
        this.messages.push({ content: this.newMessage, sender: 'user' });
        this.newMessage = '';

        await this.$nextTick(); // 等待DOM更新
        this.scrollToBottom(); // 滚动到底部

        this.loading = true
  
        const reply = await this.getBotReply(this.messages[this.messages.length - 1].content);

        this.loading = false
        this.messages.push({ content: '', sender: 'bot' });

        let index = 0
        const timer = setInterval(() => {
          if (index < reply.length) {
            let lastPos = this.messages.length - 1
            this.messages[lastPos].content += reply[index]
            index++;
            
          } else {
            clearInterval(timer);
          }
          this.$nextTick(); // 等待DOM更新
          this.scrollToBottom(); // 滚动到底部
      }, 50);

      

      },
      async getBotReply(message) {
        try {
          const response = await axios.post(
            'https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key='+api_key,
            {
                contents:[{parts:[{text: message+"（请按照皮肤专科医生的专业口吻来回答问题）"}]}]
            },
            {
              headers: {
                'Content-Type': 'application/json',
              }
            }
          );
          return response.data["candidates"][0]["content"]["parts"][0]["text"]
        } catch (error) {
          return '出现问题，请检查Gemini API-Key是否正确，或是否正确使用代理！';
        }
      },
      scrollToBottom() {
      this.$refs.chatMessages.scrollTop = this.$refs.chatMessages.scrollHeight;
    }
    },
    mounted() {
    this.scrollToBottom();
  }
  };
  </script>
  
<style scoped>
.chat-container {
    width: 100%;
    height: 100%;
  display: flex;
  flex-direction: column;
  border: 1px solid #ccc;
  border-radius: 5px;
  overflow: hidden;
  background-color: #f0f0f0; /* 将聊天框的背景色修改为灰色 */
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  display: flex;
  flex-direction: column;
}

.message-container {
  display: flex;
  margin-bottom: 10px;
}

.user-message-container {
  justify-content: flex-end; /* 用户对话框右对齐 */
}

.bot-message-container {
  justify-content: flex-start; /* 机器人对话框左对齐 */
}

.message {
  padding: 5px 10px;
  border-radius: 10px;
  display: inline-block;
  max-width: 80%;
  text-align: left;
}

.user-message {
  background-color: #00A67E; /* 用户对话框颜色 */
  color: white; /* 用户对话框文字颜色 */
}

.bot-message {
  background-color: #007bff; /* 机器人对话框颜色 */
  color: white; /* 机器人对话框文字颜色 */
}

.input-container {
  display: flex;
  align-items: center;
  padding: 10px;
  height: 100px;
}

.chat-input {
    vertical-align: top;
    padding: 8px;
    border-radius: 5px;
    border: 1px solid #ccc;
    max-height: 100px; /* 设置输入框的最大高度 */
      /* 其他样式 */
    width: 100%; /* 让输入框充满容器 */
    overflow-y: auto; /* 允许内容溢出时垂直滚动 */
    white-space: pre-wrap; /* 在换行符处自动换行 */
}

.input-container button {
  padding: 8px 15px;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: #fff;
  cursor: pointer;
}

.loading-animation {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background-color: #007bff;
  animation: bubble-scale 1s infinite alternate;
  margin-left: 10px;
}

@keyframes bubble-scale {
  0% { transform: scale(0.5); }
  100% { transform: scale(1.5); }
  0% { transform: scale(0.5); }
  100% { transform: scale(1.5); }
}
</style>