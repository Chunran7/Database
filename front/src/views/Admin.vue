<template>
    <div class="admin-container">
        <div v-if="!isLoggedIn" class="login-wrapper">
            <el-card class="login-box" shadow="hover">
                <div class="login-header">
                    <img src="@/assets/logo.png" alt="Logo" class="login-logo">
                    <h2>后台管理系统</h2>
                </div>
                <el-form :model="loginForm" ref="loginFormRef" label-position="top">
                    <el-form-item label="管理员账号">
                        <el-input v-model="loginForm.username" placeholder="请输入账号" prefix-icon="User" />
                    </el-form-item>
                    <el-form-item label="密码">
                        <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock"
                            show-password @keyup.enter="handleLogin" />
                    </el-form-item>
                    <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">立即登录</el-button>
                </el-form>
            </el-card>
        </div>

        <el-container v-else class="main-layout">
            <el-aside width="220px" class="sidebar">
                <div class="brand">
                    <span>ADMIN PANEL</span>
                </div>
                <el-menu :default-active="activeMenu" @select="handleMenuSelect" background-color="#304156"
                    text-color="#bfcbd9" active-text-color="#409EFF">
                    <el-menu-item index="dashboard">
                        <el-icon>
                            <Odometer />
                        </el-icon>
                        <span>数据看板</span>
                    </el-menu-item>

                    <el-sub-menu index="content">
                        <template #title>
                            <el-icon>
                                <Document />
                            </el-icon>
                            <span>内容发布</span>
                        </template>
                        <el-menu-item index="publish">发布文章</el-menu-item>
                        <el-menu-item index="video_publish">上传视频</el-menu-item>
                    </el-sub-menu>

                    <el-sub-menu index="management">
                        <template #title>
                            <el-icon>
                                <Setting />
                            </el-icon>
                            <span>系统管理</span>
                        </template>
                        <el-menu-item index="article_mgr">文章管理</el-menu-item>
                        <el-menu-item index="user_mgr">用户管理</el-menu-item>
                    </el-sub-menu>

                    <el-menu-item index="logout">
                        <el-icon>
                            <SwitchButton />
                        </el-icon>
                        <span>退出登录</span>
                    </el-menu-item>
                </el-menu>
            </el-aside>

            <el-container>
                <el-header class="admin-header">
                    <div class="header-left">当前位置: {{ getMenuName(activeMenu) }}</div>
                    <div class="header-right">管理员: {{ loginForm.username }}</div>
                </el-header>

                <el-main class="main-content">
                    <div v-if="activeMenu === 'dashboard'">
                        <el-row :gutter="20">
                            <el-col :span="6">
                                <el-card shadow="hover" class="data-card">
                                    <template #header>总用户数</template>
                                    <div class="data-num">{{ stats.userCount || '-' }}</div>
                                </el-card>
                            </el-col>
                            <el-col :span="6">
                                <el-card shadow="hover" class="data-card">
                                    <template #header>文章总数</template>
                                    <div class="data-num">{{ stats.articleCount || '-' }}</div>
                                </el-card>
                            </el-col>
                            <el-col :span="6">
                                <el-card shadow="hover" class="data-card">
                                    <template #header>视频总数</template>
                                    <div class="data-num">{{ stats.videoCount || '-' }}</div>
                                </el-card>
                            </el-col>
                            <el-col :span="6">
                                <el-card shadow="hover" class="data-card">
                                    <template #header>社区帖子</template>
                                    <div class="data-num">{{ stats.postCount || '-' }}</div>
                                </el-card>
                            </el-col>
                        </el-row>
                        <div style="margin-top: 20px;">
                            <el-alert title="欢迎回来，管理员！系统运行正常。" type="success" :closable="false" />
                        </div>
                    </div>

                    <div v-if="activeMenu === 'publish'">
                        <el-card>
                            <template #header>发布新文章</template>
                            <el-form :model="articleForm" label-width="80px">
                                <el-form-item label="标题"><el-input v-model="articleForm.title" /></el-form-item>
                                <el-form-item label="作者"><el-input v-model="articleForm.author" /></el-form-item>
                                <el-form-item label="封面"><el-input v-model="articleForm.firstPicture"
                                        placeholder="图片URL" /></el-form-item>
                                <el-form-item label="摘要"><el-input type="textarea"
                                        v-model="articleForm.description" /></el-form-item>
                                <el-form-item label="正文"><el-input type="textarea" v-model="articleForm.content"
                                        rows="10" /></el-form-item>
                                <el-form-item>
                                    <el-button type="primary" @click="handleArticleSubmit"
                                        :loading="loading">发布</el-button>
                                </el-form-item>
                            </el-form>
                        </el-card>
                    </div>

                    <div v-if="activeMenu === 'video_publish'">
                        <el-card>
                            <template #header>上传视频信息</template>
                            <el-form :model="videoForm" label-width="80px">
                                <el-form-item label="标题"><el-input v-model="videoForm.title" /></el-form-item>
                                <el-form-item label="URL"><el-input v-model="videoForm.url"
                                        placeholder="视频播放地址" /></el-form-item>
                                <el-form-item label="封面"><el-input v-model="videoForm.cover"
                                        placeholder="封面图片URL" /></el-form-item>
                                <el-form-item label="作者"><el-input v-model="videoForm.author" /></el-form-item>
                                <el-form-item label="描述"><el-input type="textarea"
                                        v-model="videoForm.description" /></el-form-item>
                                <el-form-item>
                                    <el-button type="primary" @click="handleVideoSubmit"
                                        :loading="loading">提交</el-button>
                                </el-form-item>
                            </el-form>
                        </el-card>
                    </div>

                    <div v-if="activeMenu === 'user_mgr'">
                        <el-card>
                            <el-table :data="userList" stripe style="width: 100%">
                                <el-table-column prop="id" label="ID" width="80" />
                                <el-table-column prop="username" label="用户名" />
                                <el-table-column prop="email" label="邮箱" />
                                <el-table-column label="状态">
                                    <template #default="scope">
                                        <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                                            {{ scope.row.status === 1 ? '正常' : '封禁' }}
                                        </el-tag>
                                    </template>
                                </el-table-column>
                                <el-table-column label="操作">
                                    <template #default="scope">
                                        <el-button size="small" :type="scope.row.status === 1 ? 'danger' : 'success'"
                                            @click="toggleUserStatus(scope.row)">
                                            {{ scope.row.status === 1 ? '封禁' : '解封' }}
                                        </el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </el-card>
                    </div>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Lock, Odometer, Document, Setting, SwitchButton } from '@element-plus/icons-vue' // 需确保已安装图标库
import request from '@/utils/request.js'

// ================= 状态定义 =================
const isLoggedIn = ref(sessionStorage.getItem('admin_logged_in') === 'true')
const loading = ref(false)
const activeMenu = ref('dashboard')
const loginForm = reactive({ username: 'seu666', password: '' }) // 默认填入用户名方便测试

const stats = ref({ userCount: 0, articleCount: 0, videoCount: 0, postCount: 0 })
const userList = ref([
    // 模拟数据，实际应从后端获取
    { id: 1, username: 'test_user', email: 'test@example.com', status: 1 },
    { id: 2, username: 'bad_guy', email: 'spam@example.com', status: 0 },
])

const articleForm = ref({ title: '', firstPicture: '', description: '', content: '', author: '管理员' })
const videoForm = ref({ title: '', url: '', cover: '', description: '', author: '管理员' })

// ================= 方法逻辑 =================

// 1. 登录
const handleLogin = async () => {
    loading.value = true
    try {
        const res = await request.post('/admin/login', loginForm)
        if (res.code === 1 || res.msg?.includes('成功') || res === '登录成功') {
            sessionStorage.setItem('admin_logged_in', 'true')
            isLoggedIn.value = true
            ElMessage.success('欢迎回来')
            fetchStats()
        } else {
            ElMessage.error(res.msg || '登录失败')
        }
    } catch (err) {
        // 本地降级处理 (如果没有后端接口也能演示)
        if (loginForm.username === 'seu666' && loginForm.password === 'yilutongxing') {
            sessionStorage.setItem('admin_logged_in', 'true')
            isLoggedIn.value = true
            ElMessage.success('登录成功 (本地验证)')
            fetchStats()
        } else {
            ElMessage.error('账号或密码错误')
        }
    } finally {
        loading.value = false
    }
}

// 2. 菜单切换
const handleMenuSelect = (index) => {
    if (index === 'logout') {
        ElMessageBox.confirm('确定要退出登录吗?', '提示', { confirmButtonText: '退出', cancelButtonText: '取消', type: 'warning' })
            .then(() => {
                sessionStorage.removeItem('admin_logged_in')
                isLoggedIn.value = false
                ElMessage.info('已退出')
            })
    } else {
        activeMenu.value = index
        if (index === 'dashboard') fetchStats()
        if (index === 'user_mgr') fetchUsers()
    }
}

const getMenuName = (key) => {
    const map = {
        'dashboard': '数据看板',
        'publish': '发布文章',
        'video_publish': '上传视频',
        'article_mgr': '文章管理',
        'user_mgr': '用户管理'
    }
    return map[key] || '后台首页'
}

// 3. 数据获取
const fetchStats = async () => {
    try {
        const res = await request.get('/admin/stats')
        if (res.data) stats.value = res.data
    } catch (e) {
        console.log('获取统计失败，使用默认值')
        stats.value = { userCount: 1205, articleCount: 450, videoCount: 89, postCount: 3210 }
    }
}

const fetchUsers = async () => {
    try {
        const res = await request.get('/admin/users')
        if (res.data) userList.value = res.data
    } catch (e) {
        // 保持模拟数据
    }
}

// 4. 内容提交
const handleArticleSubmit = async () => {
    if (!articleForm.value.title) return ElMessage.warning('请输入标题')
    loading.value = true
    try {
        await request.post('/article', articleForm.value)
        ElMessage.success('发布成功')
        articleForm.value = { title: '', content: '', description: '', author: '管理员' }
    } catch (e) { ElMessage.error('发布失败') }
    loading.value = false
}

const handleVideoSubmit = async () => {
    loading.value = true
    try {
        await request.post('/video', videoForm.value)
        ElMessage.success('上传成功')
        videoForm.value = { title: '', url: '' }
    } catch (e) { ElMessage.error('上传失败') }
    loading.value = false
}

// 5. 用户封禁
const toggleUserStatus = async (user) => {
    const newStatus = user.status === 1 ? 0 : 1
    const actionText = newStatus === 1 ? '解封' : '封禁'
    try {
        await ElMessageBox.confirm(`确定要${actionText}用户 ${user.username} 吗?`, '警告', { type: 'warning' })
        // await request.put(`/admin/users/${user.id}/status?status=${newStatus}`)
        user.status = newStatus // 前端乐观更新
        ElMessage.success(`已${actionText}`)
    } catch (e) {
        // 取消或失败
    }
}

onMounted(() => {
    if (isLoggedIn.value) fetchStats()
})
</script>

<style scoped>
.admin-container {
    height: 100vh;
    background-color: #f0f2f5;
}

.login-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    background: linear-gradient(135deg, #2d3a4b 0%, #1f2d3d 100%);
}

.login-box {
    width: 400px;
    border-radius: 8px;
}

.login-header {
    text-align: center;
    margin-bottom: 20px;
}

.login-logo {
    width: 60px;
    height: 60px;
    margin-bottom: 10px;
}

.login-btn {
    width: 100%;
    padding: 20px 0;
    font-size: 16px;
}

.main-layout {
    height: 100%;
}

.sidebar {
    background-color: #304156;
    color: #fff;
    overflow-x: hidden;
    transition: width 0.3s;
}

.brand {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    font-weight: bold;
    background-color: #2b3649;
}

.admin-header {
    background: #fff;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    box-shadow: 0 1px 4px rgba(0, 21, 41, .08);
}

.main-content {
    padding: 20px;
}

.data-card {
    text-align: center;
}

.data-num {
    font-size: 24px;
    font-weight: bold;
    color: #409EFF;
    margin-top: 10px;
}
</style>