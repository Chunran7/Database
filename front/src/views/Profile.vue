<template>
    <div class="profile-page container">
        <div class="profile-card">
            <div class="avatar-wrap">
                <img :src="user.avatar || defaultAvatar" alt="avatar" />
            </div>
            <div class="info">
                <div class="row"><label>昵称：</label><span>{{ user.nickname || '未设置昵称' }}</span></div>
                <div class="row"><label>邮箱：</label><span>{{ user.email || '未设置' }}</span></div>
                <div class="actions">
                    <el-button type="primary" @click="save">保存修改</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive } from 'vue'
import defaultAvatar from '@/assets/default.png'

// 简单示例：从 localStorage 读取 user 信息，真实项目应调用 API
const stored = localStorage.getItem('user')
const user = reactive(stored ? JSON.parse(stored) : { avatar: '', nickname: '', email: '' })

const save = () => {
    // 本示例仅将数据写回 localStorage
    localStorage.setItem('user', JSON.stringify(user))
    // 可提示保存成功
    // import { ElMessage } if you want to show message
}
</script>

<style scoped>
.profile-page {
    padding: 24px;
}

.profile-card {
    display: flex;
    gap: 16px;
    background: #fff;
    padding: 16px;
    border-radius: 8px;
    align-items: center
}

.avatar-wrap img {
    width: 96px;
    height: 96px;
    border-radius: 50%;
    object-fit: cover
}

.info .row {
    margin-bottom: 8px
}

.info label {
    font-weight: 700;
    margin-right: 8px
}

.actions {
    margin-top: 12px
}
</style>
