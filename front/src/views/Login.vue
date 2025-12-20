<template>
  <div class="login-container">
    <div class="login-box">
      <!-- Logo区域 -->
      <div class="logo-area">
        <h2>欢迎使用</h2>
      </div>
      
      <!-- 登录/注册标签页 -->
      <el-tabs v-model="activeTab" stretch class="login-tabs">
        <!-- 登录标签页 -->
        <el-tab-pane label="登录" name="login">
          <el-form 
            :model="loginForm" 
            :rules="loginRules" 
            ref="loginFormRef" 
            class="login-form"
          >
            <el-form-item prop="username" label="用户名">
              <el-input 
                v-model="loginForm.username" 
                placeholder="请输入用户名"
                size="large"
              />
            </el-form-item>
            
            <el-form-item prop="password" label="密码">
              <el-input 
                v-model="loginForm.password" 
                type="password" 
                placeholder="请输入密码"
                show-password
                size="large"
              />
            </el-form-item>
            
            <el-form-item>
              <el-button 
                type="primary" 
                @click="handleLogin" 
                size="large" 
                class="submit-btn"
                :loading="loginLoading"
                style="width: 100%"
              >
                登录
              </el-button>
            </el-form-item>
            
            <div class="form-footer">
              <span>还没有账户？</span>
              <el-button type="text" @click="activeTab = 'register'">立即注册</el-button>
            </div>
          </el-form>
        </el-tab-pane>
        
        <!-- 注册标签页 -->
        <el-tab-pane label="注册" name="register">
          <el-form 
            :model="registerForm" 
            :rules="registerRules" 
            ref="registerFormRef" 
            class="register-form"
          >
            <el-form-item prop="username" label="用户名">
              <el-input 
                v-model="registerForm.username" 
                placeholder="请输入用户名"
                size="large"
              />
            </el-form-item>
            
            <el-form-item prop="password" label="密码">
              <el-input 
                v-model="registerForm.password" 
                type="password" 
                placeholder="请输入密码"
                show-password
                size="large"
              />
            </el-form-item>
            
            <el-form-item prop="confirmPassword" label="确认密码">
              <el-input 
                v-model="registerForm.confirmPassword" 
                type="password" 
                placeholder="请确认密码"
                show-password
                size="large"
              />
            </el-form-item>
            
            <el-form-item class="register-button-container">
              <el-button 
                type="primary" 
                @click="handleRegister" 
                size="large" 
                class="submit-btn"
                :loading="registerLoading"
                style="width: 100%"
              >
                注册
              </el-button>
            </el-form-item>
            
            <div class="form-footer">
              <span>已有账户？</span>
              <el-button type="text" @click="activeTab = 'login'">立即登录</el-button>
            </div>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

// 标签页控制
const activeTab = ref('login')
const loginLoading = ref(false)
const registerLoading = ref(false)

// 登录表单数据和规则
const loginForm = ref({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const loginFormRef = ref()

// 注册表单数据和规则
const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, max: 16, message: '长度在 5 到 16 个非空字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 16, message: '长度在 5 到 16 个非空字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.value.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const registerFormRef = ref()

// 登录处理函数
import {userLoginService,userRegisterService}from '@/api/user.js'
import { useRouter } from 'vue-router'
const router = useRouter()

const handleLogin = async () => {
  try {
    // 验证表单
    await loginFormRef.value.validate();
    
    // 调用登录服务
    let result = await userLoginService(loginForm.value);
    
    // 根据后台返回的code判断是否成功
    if (result.code === 0) {
      ElMessage({
        message: '登录成功',
        type: 'success'
      })
      // 保存token到localStorage
      localStorage.setItem('token', result.data)
      // 登录成功后跳转到首页
      router.push('/')
    } else {
      // 表单验证失败
      ElMessage({
        message: '请输入正确的用户名和密码',
        type: 'error'
      })
    }
  } catch (error) {
  
      // 表单验证失败
      ElMessage({
        message: '请输入正确的用户名和密码',
        type: 'error'
      })
    }
}

// 注册处理函数
const handleRegister = async () => {
  try {
    // 验证表单
    await registerFormRef.value.validate();
    
    // 调用注册服务
    let result = await userRegisterService(registerForm.value);
    
    // 根据后台返回的code判断是否成功
    if (result.code === 0) {
      ElMessage({
        message: '注册成功',
        type: 'success'
      })
      // 注册成功后切换到登录标签页
      activeTab.value = 'login';
    } else {
      // 注册失败，显示后台返回的错误信息
      ElMessage({
        message: result.msg || '注册失败',
        type: 'error'
      })
    }
  } catch (error) {
    // 区分表单验证失败和后端处理失败
    if (error && typeof error === 'object' && 'code' in error) {
      // 后端返回的错误（如用户名已存在）
      ElMessage({
        message: error.msg || '注册失败',
        type: 'error'
      })
    } else {
      // 表单验证失败
      ElMessage({
        message: '请输入正确的用户名或密码',
        type: 'error'
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #466dee8a 50%, #764ba2 100%);
  padding: 20px;
}

.login-box {
  width: 100%;
  max-width: 400px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.logo-area {
  text-align: center;
  padding: 30px 20px 20px;
  background: linear-gradient(to right, #409EFF, #1890ff);
  color: white;
}

.logo-area h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 500;
}

.login-tabs {
  padding: 20px;
}

.login-tabs :deep(.el-tabs__nav-wrap::after) {
  height: 1px;
}

.login-tabs :deep(.el-tabs__item) {
  font-size: 16px;
  font-weight: 500;
}

:deep(.el-form-item) {
  margin-bottom: 25px;
  display: flex;
  justify-content: center;
  align-items: center;
}

:deep(.el-form-item__label) {
  text-align: left;
  padding-right: 15px;
  width: auto;
  flex-shrink: 0;
  color: #333;
  font-weight: normal;
}

:deep(.el-form-item .el-input) {
  flex: none;
  max-width: 250px;
  width: 250px;
}

:deep(.el-form-item .el-input__wrapper) {
  width: 100%;
  background-color: #f8f9fa;
  box-shadow: 0 0 0 1px #e2e8f0 inset;
  height: 40px;
}

:deep(.el-form-item .el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409EFF inset;
}

:deep(.el-form-item__content) {
  flex: none;
  display: flex;
  width: 250px;
  justify-content: center;
}

:deep(.el-form-item__error) {
  color: rgb(255, 0, 0);
}

:deep(.el-input--large .el-input__wrapper) {
  height: 40px;
  align-items: center;
}

:deep(.el-input__prefix) {
  display: flex;
  align-items: center;
}

:deep(.el-input__inner) {
  height: 100%;
}

.submit-btn {
  width: 100%;
  max-width: 280px;
  margin-top: 10px;
}

.register-button-container {
  display: flex;
  justify-content: center;
  width: 100%;
}

.form-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.form-footer .el-button {
  margin-left: 5px;
}
</style>