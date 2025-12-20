<!-- src/views/Video.vue -->
<template>
  <el-container class="video-container">
    <el-main class="main">
      <div class="content">
        <h1 class="section-title">视频列表</h1>

        <!-- 加载状态 -->
        <div v-if="loading" class="loading">
          <el-skeleton :rows="5" animated />
        </div>

        <!-- 视频列表 -->
        <el-row :gutter="24" v-else>
          <el-col :span="8" v-for="video in pagedVideos" :key="video.id">
            <el-card shadow="hover" class="article-card" @click="goToVideo(video.id)">
              <img :src="video.url || '/images/introduction.png'" alt="封面图" class="article-img" />
              <h3 class="article-title">{{ video.title }}</h3>
              <p class="article-description">{{ truncate(video.summary, 35) }}</p>
            </el-card>
          </el-col>
        </el-row>

        <!-- 分页 -->
        <div class="pagination">
          <el-pagination background layout="prev, pager, next" :total="videos.length" :page-size="pageSize"
            v-model:current-page="currentPage" />
        </div>
      </div>
    </el-main>

    <Footer />
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Footer from '@/components/Footer.vue'
import { getVideoListService } from '@/api/video.js'

const router = useRouter()

// 视频数据
const videos = ref([])
// 加载状态
const loading = ref(true)

const pageSize = 9
const currentPage = ref(1)

// 模拟获取视频列表
const getVideoList = async () => {
  try {
    loading.value = true
    const res = await getVideoListService()
    videos.value = res.data
  } catch (error) {
    console.error('获取视频列表失败:', error)
    // 模拟数据用于演示
    videos.value = Array.from({ length: 30 }, (_, i) => ({
      id: i + 1,
      title: `教学视频 ${i + 1}`,
      summary: `这是第 ${i + 1} 个视频的简介，包含主要学习内容。`,
      cover: 'https://placehold.co/300x150'
    }))
  } finally {
    loading.value = false
  }
}

// 当前页的视频
const pagedVideos = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return videos.value.slice(start, start + pageSize)
})

// 跳转视频详情
const goToVideo = (id) => {
  router.push(`/video/${id}`)
}

// 摘要截断
const truncate = (text, maxLen) => {
  return text?.length <= maxLen ? text : text?.slice(0, maxLen) + '...'
}

onMounted(() => {
  getVideoList()
})
</script>

<style scoped>
.video-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main {
  background-color: #f5f5f5;
  flex: 1;
  padding: 20px 0;
}

.content {
  width: 70%;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.section-title {
  text-align: center;
  font-size: 28px;
  color: #333;
  margin: 0 0 50px 0;
  padding-bottom: 10px;
  border-bottom: 2px solid #007bff;
}

.article-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #ebeef5;
  border-radius: 8px;
}

.article-card:hover {
  transform: translateY(-5px) scale(1.02);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  border-color: #007bff;
}

.article-img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
}

.article-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 15px 10px 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.article-description {
  font-size: 14px;
  color: #666;
  margin: 0 10px 15px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  height: 40px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}

.loading {
  width: 100%;
}
</style>