<template>
  <div class="container">
    <el-card class="card" v-if="article">
      <template #header>
        <div class="header">
          <div class="left">
            <div class="title">{{ article.title }}</div>
            <div class="meta">
              <el-avatar :src="article.authorPic || defaultAvatar" :size="28" />
              <span class="author">{{ article.author || '匿名用户' }}</span>
              <span class="time">{{ formatTime(article.createTime) }}</span>
              <span class="stat">浏览 {{ article.views || 0 }}</span>
              <span class="stat">赞 {{ article.likeCount || 0 }}</span>
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

      <img v-if="article.firstPicture" :src="article.firstPicture" class="cover" />
      <div v-if="article.description" class="desc">{{ article.description }}</div>
      <div class="content">{{ article.content }}</div>

      <div class="actions">
        <el-button :type="article.liked ? 'primary' : 'default'" :disabled="!token" @click="toggleLike">
          {{ article.liked ? '已赞' : '点赞' }} ({{ article.likeCount || 0 }})
        </el-button>
        <el-button :type="article.favorited ? 'warning' : 'default'" :disabled="!token" @click="toggleFavorite">
          {{ article.favorited ? '已藏' : '收藏' }}
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

import { getArticleByIdService, toggleArticleLikeService, toggleArticleFavoriteService } from '@/api/article'
import { getMeService, getUserProfileService, toggleFollowService } from '@/api/user'

const route = useRoute()
const router = useRouter()
const token = localStorage.getItem('token')
const id = ref(route.params.id)

const article = ref(null)
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
  if (!token) return
  try {
    me.value = await getMeService()
  } catch {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }
}

const loadArticle = async () => {
  try {
    article.value = await getArticleByIdService(id.value)
    try {
      authorProfile.value = await getUserProfileService(article.value.userId)
    } catch (_) {}
  } catch (e) {
    ElMessage.error(e?.message || '加载文章失败')
  }
}

const toggleLike = async () => {
  if (!token) return goLogin()
  try {
    const res = await toggleArticleLikeService(id.value)
    article.value.liked = !!res.liked
    article.value.likeCount = res.likeCount
  } catch (e) {
    ElMessage.error(e?.message || '操作失败')
  }
}

const toggleFavorite = async () => {
  if (!token) return goLogin()
  try {
    const res = await toggleArticleFavoriteService(id.value)
    article.value.favorited = !!res.favorited
    ElMessage.success(article.value.favorited ? '已收藏' : '已取消收藏')
  } catch (e) {
    ElMessage.error(e?.message || '操作失败')
  }
}

const toggleFollowAuthor = async () => {
  if (!token) return goLogin()
  try {
    authorProfile.value = await toggleFollowService(article.value.userId)
    ElMessage.success(authorProfile.value.followed ? '已关注' : '已取消关注')
  } catch (e) {
    ElMessage.error(e?.message || '操作失败')
  }
}

onMounted(async () => {
  await loadMe()
  await loadArticle()
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

.cover {
  width: 100%;
  max-height: 360px;
  object-fit: cover;
  border-radius: 12px;
  margin-bottom: 12px;
}

.desc {
  padding: 10px;
  border-radius: 10px;
  background: #f7f7f7;
  margin-bottom: 12px;
  color: #666;
}

.content {
  white-space: pre-wrap;
  word-break: break-word;
  font-size: 15px;
  line-height: 1.7;
  color: #333;
}

.actions {
  margin-top: 14px;
  display: flex;
  gap: 10px;
}
</style>
