<!-- src/components/Navbar.vue -->
<template>
  <el-header class="header" style="background-color: #ffffff; height: 60px">
    <div class="navbar-left">
      <img :src="logo" alt="网站Logo" class="navbar-logo" @click="router.push('/home')">
    </div>
    <el-menu mode="horizontal" text-color="#000000" active-text-color="#0040ff" class="top-menu"
      :default-active="activeIndex">
      <el-menu-item index="/home" @click="router.push('/home')">首页</el-menu-item>
      <el-menu-item index="/article" @click="router.push('/article')">文章</el-menu-item>
      <el-menu-item index="/video" @click="router.push('/video')">视频</el-menu-item>
      <el-menu-item index="/forum" @click="router.push('/forum')">讨论</el-menu-item>
    </el-menu>
    <div class="navbar-right">
      <el-button v-if="!isLoggedIn" @click="router.push('/login')" type="primary">登录/注册</el-button>
    </div>
  </el-header>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { computed } from 'vue'
import logo from '@/assets/logo.png'

const route = useRoute()
const router = useRouter()
const activeIndex = computed(() => route.path)

// 添加登录状态管理
const isLoggedIn = ref(false)

// 页面加载时检查登录状态
onMounted(() => {
  const token = localStorage.getItem('token')
  isLoggedIn.value = !!token
})

// 监听路由变化，检查登录状态
router.afterEach(() => {
  const token = localStorage.getItem('token')
  isLoggedIn.value = !!token
})
</script>

<style scoped>
.navbar-left {
  float: left;
  height: 60px;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.navbar-logo {
  height: 40px;
  width: auto;
  margin: 10px;
}

.navbar-right {
  position: absolute;
  right: 30px;
  top: 0;
  height: 60px;
  display: flex;
  align-items: center;
}
</style>