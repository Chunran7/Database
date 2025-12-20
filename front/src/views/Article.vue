<template>
  <el-container class="article-container">
    <el-main class="main">
      <div class="content">
        <h1 class="section-title">文章列表</h1>

        <!-- 加载状态 -->
        <div v-if="loading" class="loading">
          <el-skeleton :rows="5" animated />
        </div>

        <!-- 文章列表 -->
        <el-row :gutter="24" v-else>
          <el-col :span="8" v-for="article in pagedArticles" :key="article.id">
            <el-card shadow="hover" class="article-card" @click="goToArticle(article.id)">
              <img :src="article.firstpicture || 'https://placehold.co/300x150'" alt="封面图" class="article-img" />
              <h3 class="article-title">{{ article.title }}</h3>
              <p class="article-description">{{ truncate(article.description, 35) }}</p>

            </el-card>
          </el-col>
        </el-row>

        <!-- 分页 -->
        <div class="pagination">
          <el-pagination background layout="prev, pager, next" :total="articles.length" :page-size="pageSize"
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
import { getArticleListService } from '@/api/article.js'
import Footer from '@/components/Footer.vue'

const router = useRouter()

// 文章数据
const articles = ref([])
// 加载状态
const loading = ref(true)

const pageSize = 9
const currentPage = ref(1)

// 获取文章列表
const getArticleList = async () => {
  try {
    loading.value = true
    const res = await getArticleListService()
    articles.value = res.data
  } catch (error) {
    console.error('获取文章列表失败:', error)
    // 模拟数据用于演示
    articles.value = Array.from({ length: 42 }, (_, i) => ({
      id: i + 1,
      title: `文章标题 ${i + 1}`,
      description: `这是第 ${i + 1} 篇文章的摘要内容，简要描述文章主要内容，让用户快速了解文章。`,
      img: 'https://placehold.co/300x150'
    }))
  } finally {
    loading.value = false
  }
}

// 当前页的文章
const pagedArticles = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return articles.value.slice(start, start + pageSize)
})

// 跳转文章详情
const goToArticle = (id) => {
  router.push(`/article/${id}`)
}

// 摘要截断
const truncate = (text, maxLen) => {
  return text?.length <= maxLen ? text : text?.slice(0, maxLen) + '...'
}

// 当从缓存中激活组件时调用
import { onActivated } from 'vue'
onActivated(() => {
  // 可以选择在每次返回页面时刷新数据，或者注释掉下面这行以保持数据不变
  // getArticleList()
})

onMounted(() => {
  getArticleList()
})
</script>

<style scoped>
.article-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main {
  background-color: #f5f5f5
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
  color: #2c3e50;
  margin: 0 0 30px 0;
  border-bottom: 2px solid #007bff;
  padding-bottom: 10px;
}

.article-card {
  cursor: pointer;
  transition: all 0.3s ease;
  margin: 20px 0;
}

.article-card:hover {
  box-shadow: 0 6px 20px rgba(0, 123, 255, 0.2);
  transform: scale(1.03);
}

.article-img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 10px;
}

.article-title {
  margin: 0 0 8px;
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.article-description {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
}

.article-footer {
  text-align: right;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.loading {
  padding: 20px;
}
</style>