<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="login-title">{{ isLoginMode ? '用户登录' : '用户注册' }}</h2>
      <el-form :model="formModel" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formModel.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="formModel.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item v-if="!isLoginMode" label="确认密码" prop="repassword">
          <el-input v-model="formModel.repassword" type="password" placeholder="请再次输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">{{ isLoginMode ? '登录' : '注册' }}</el-button>
          <el-button type="text" @click="toggleMode">
            {{ isLoginMode ? '没有账号？去注册' : '已有账号？去登录' }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { userLoginService, userRegisterService } from '@/api/user'

const router = useRouter()
const isLoginMode = ref(true)
const formRef = ref(null)
const formModel = reactive({
  username: '',
  password: '',
  repassword: ''
})

const validateRepassword = (rule, value, callback) => {
  if (!isLoginMode.value && value !== formModel.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  repassword: [{ validator: validateRepassword, trigger: 'blur' }]
}

const toggleMode = () => {
  isLoginMode.value = !isLoginMode.value
  formRef.value?.resetFields()
}

const submitForm = async () => {
  await formRef.value.validate()

  if (isLoginMode.value) {
    // 登录
    try {
      const token = await userLoginService({
        username: formModel.username,
        password: formModel.password
      })
      localStorage.setItem('token', token)
      ElMessage.success('登录成功')
      router.push('/')
    } catch (e) {
      ElMessage.error(e?.message || '登录失败')
    }
  } else {
    // 注册
    try {
      await userRegisterService({
        username: formModel.username,
        password: formModel.password
      })
      ElMessage.success('注册成功，请登录')
      isLoginMode.value = true
      formRef.value?.resetFields()
    } catch (e) {
      ElMessage.error(e?.message || '注册失败')
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #f5f5f5;
}

.login-box {
  width: 400px;
  padding: 30px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.login-title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: bold;
}
</style>
