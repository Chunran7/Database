<template>
  <div class="container">
    <el-card class="card">
      <template #header>
        <div class="header">
          <span>个人中心</span>
          <el-button link @click="reload">刷新</el-button>
        </div>
      </template>

      <div v-if="loading">
        <el-skeleton :rows="6" animated />
      </div>

      <div v-else>
        <div class="top">
          <el-avatar :src="me?.userPic || defaultAvatar" :size="72" />
          <div class="info">
            <div class="name">{{ me?.nickname || me?.username }}</div>
            <div class="sub">{{ me?.email || '未设置邮箱' }}</div>
            <div class="counts">
              <span>关注 {{ me?.followingCount || 0 }}</span>
              <span>粉丝 {{ me?.followerCount || 0 }}</span>
            </div>
          </div>
        </div>

        <el-divider />

        <el-form :model="form" label-width="80px">
          <el-form-item label="昵称">
            <el-input v-model="form.nickname" placeholder="请输入昵称" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="头像URL">
            <el-input v-model="form.userPic" placeholder="请输入图片URL（可留空）" />
          </el-form-item>
        </el-form>

        <div class="actions">
          <el-button type="primary" :loading="saving" @click="save">保存</el-button>
          <el-button @click="logout">退出登录</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import defaultAvatar from '@/assets/default.png'

import { getMeService, updateProfileService } from '@/api/user'

const router = useRouter()
const token = localStorage.getItem('token')

const me = ref(null)
const loading = ref(false)
const saving = ref(false)

const form = ref({
  nickname: '',
  email: '',
  userPic: ''
})

const goLogin = () => router.push('/login')

const loadMe = async () => {
  if (!token) return goLogin()
  loading.value = true
  try {
    me.value = await getMeService()
    localStorage.setItem('user', JSON.stringify(me.value))
    form.value.nickname = me.value.nickname || ''
    form.value.email = me.value.email || ''
    form.value.userPic = me.value.userPic || ''
  } catch (e) {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    goLogin()
  } finally {
    loading.value = false
  }
}

const reload = () => loadMe()

const save = async () => {
  saving.value = true
  try {
    await updateProfileService({
      nickname: form.value.nickname,
      email: form.value.email,
      userPic: form.value.userPic
    })
    ElMessage.success('保存成功')
    await loadMe()
  } catch (e) {
    ElMessage.error(e?.message || '保存失败')
  } finally {
    saving.value = false
  }
}

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  ElMessage.success('已退出')
  router.push('/login')
}

onMounted(loadMe)
</script>

<style scoped>
.container {
  max-width: 900px;
  margin: 0 auto;
  padding: 18px 12px;
}

.card {
  border-radius: 12px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.top {
  display: flex;
  gap: 14px;
  align-items: center;
}

.info {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.name {
  font-size: 18px;
  font-weight: 800;
}

.sub {
  color: #666;
  font-size: 13px;
}

.counts {
  display: flex;
  gap: 12px;
  color: #555;
  font-size: 13px;
}

.actions {
  display: flex;
  gap: 10px;
  margin-top: 8px;
}
</style>
