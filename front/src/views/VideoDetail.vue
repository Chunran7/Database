<template>
  <div class="container">
    <el-card class="card" v-if="video">
      <template #header>
        <div class="header">
          <div class="left">
            <div class="title">{{ video.title }}</div>
            <div class="meta">
              <el-avatar :src="video.authorPic || defaultAvatar" :size="28" />
              <span class="author">{{ video.author || '匿名用户' }}</span>
              <span class="time">{{ formatTime(video.createTime) }}</span>
              <span class="stat">浏览 {{ video.views || 0 }}</span>
              <span class="stat">赞 {{ video.likeCount || 0 }}</span>
            </div>
          </div>

          <div class="right">
            <el-button
              v-if="token && authorProfile && String(authorProfile.id) !== String(me?.id)"
              :type="authorProfile.followed ? 'primary' : 'default'"
              @click="toggleFollowAuthor"
            >
              {{ authorProfile.followed ? '已关注' : '关注' }}
            </el-button>
          </div>
        </div>
      </template>

      <video class="player" controls :poster="video.cover">
        <source :src="video.url" type="video/mp4" />
        你的浏览器不支持 video 标签。
      </video>

      <div v-if="video.description" class="desc">{{ video.description }}</div>

      <div class="actions">
        <el-button :type="video.liked ? 'primary' : 'default'" :disabled="!token" @click="toggleLike">
          {{ video.liked ? '已赞' : '点赞' }} ({{ video.likeCount || 0 }})
        </el-button>
        <el-button :type="video.favorited ? 'warning' : 'default'" :disabled="!token" @click="toggleFavorite">
          {{ video.favorited ? '已藏' : '收藏' }}
        </el-button>
      </div>
    </el-card>

    <el-card class="card" v-else>
      <el-skeleton :rows="8" animated />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import defaultAvatar from '@/assets/default.png'

import { getVideoByIdService, toggleVideoLikeService, toggleVideoFavoriteService } from '@/api/video'
import { getMeService, getUserProfileService, toggleFollowService } from '@/api/user'

const route = useRoute()
const router = useRouter()
const token = ref(localStorage.getItem('token'))
const refreshToken = () => { token.value = localStorage.getItem('token') }
const hasToken = () => { refreshToken(); return !!token.value }
const id = ref(route.params.id)

const video = ref(null)
const me = ref(null)
const authorProfile = ref(null)

const formatTime = (t) => {
  if (!t) return ''
  const d = new Date(t)
  if (isNaN(d.getTime())) return String(t)
  return d.toLocaleString()
}

const goLogin = () => router.push('/login')

const loadMe = async () => {
  if (!hasToken()) return
  try {
    me.value = await getMeService()
  } catch {}
}

const loadVideo = async () => {
  try {
    video.value = await getVideoByIdService(id.value)
    try {
      authorProfile.value = await getUserProfileService(video.value.userId)
    } catch (_) {}
  } catch (e) {
    ElMessage.error(e?.message || '加载视频失败')
  }
}

const toggleLike = async () => {
  if (!hasToken()) return goLogin()
  try {
    const res = await toggleVideoLikeService(id.value)
    video.value.liked = !!res.liked
    video.value.likeCount = res.likeCount
  } catch (e) {
    ElMessage.error(e?.message || '操作失败')
  }
}

const toggleFavorite = async () => {
  if (!hasToken()) return goLogin()
  try {
    const res = await toggleVideoFavoriteService(id.value)
    video.value.favorited = !!res.favorited
    ElMessage.success(video.value.favorited ? '已收藏' : '已取消收藏')
  } catch (e) {
    ElMessage.error(e?.message || '操作失败')
  }
}

const toggleFollowAuthor = async () => {
  if (!hasToken()) return goLogin()
  try {
    authorProfile.value = await toggleFollowService(video.value.userId)
    ElMessage.success(authorProfile.value.followed ? '已关注' : '已取消关注')
  } catch (e) {
    ElMessage.error(e?.message || '操作失败')
  }
}

onMounted(async () => {
  await loadMe()
  await loadVideo()
})
</script>

<style scoped>
.container {
  max-width: 980px;
  margin: 0 auto;
  padding: 18px 12px;
}

.card {
  border-radius: 12px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
}

.title {
  font-size: 18px;
  font-weight: 800;
}

.meta {
  margin-top: 8px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
  font-size: 12px;
  color: #666;
}

.player {
  width: 100%;
  border-radius: 12px;
  background: #000;
}

.desc {
  margin-top: 12px;
  padding: 10px;
  border-radius: 10px;
  background: #f7f7f7;
  color: #666;
}

.actions {
  margin-top: 14px;
  display: flex;
  gap: 10px;
}
</style>
