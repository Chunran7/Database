<template>
    <el-container class="home-container">
        <!-- 主体内容 -->
        <el-main class="main">
            <!-- 走马灯 -->
            <el-carousel height="200px">
                <el-carousel-item v-for="item in 4" :key="item">
                    <h3 class="carousel-text">轮播内容 {{ item }}</h3>
                </el-carousel-item>
            </el-carousel>

            <div class="content">
                <!-- 介绍我们 -->
                <section class="introduction">
                    <h1 class="section-title">介绍我们</h1>
                    <el-row gutter="40">
                        <el-col :span="12">
                            <img src="/images/introduction.png" alt="图片描述"
                                style="width: 100%; height: auto; border-radius: 6px;" />
                        </el-col>
                        <el-col :span="12">
                            <p>
                                介绍我们的基本内容
                            </p>
                        </el-col>
                    </el-row>
                </section>

                <!-- 最新文章 -->
                <section class="articles">
                    <h1 class="section-title">最新文章</h1>
                    <div v-if="loading" class="loading">
                        <el-skeleton :rows="3" animated />
                    </div>
                    <el-row :gutter="24" v-else>
                        <el-col :span="8" v-for="article in articles" :key="article.id">
                            <el-card shadow="hover" class="article-card" @click="goToArticle(article.id)">
                                <img :src="article.firstpicture || 'https://placehold.co/300x150'" alt="封面图"
                                    class="article-img" />
                                <h3 class="article-title">{{ article.title }}</h3>
                                <p class="article-description">{{ truncate(article.description, 35) }}</p>
                            </el-card>
                        </el-col>
                    </el-row>
                </section>

                <!-- 视频课堂模块 -->
                <section class="videos">
                    <h1 class="section-title">视频课堂</h1>
                    <el-row :gutter="20">
                        <el-col :span="6" v-for="(video, index) in videos" :key="video.id">
                            <el-card :body-style="{ padding: '10px' }" shadow="hover" class="video-card" @click="goToVideo(video.id)">
                                <el-image :src="video.cover" fit="cover" style="height: 150px;"></el-image>
                                <h3 style="margin: 10px 0;">{{ video.title }}</h3>
                            </el-card>
                        </el-col>
                    </el-row>

                </section>
            </div>
        </el-main>

        <!-- 页脚 -->
        <Footer />
    </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Footer from '@/components/Footer.vue'
import { getArticleLatestService } from '@/api/article.js'
import { getVideoLatestService } from '@/api/video.js'

const activeIndex = ref('1')
const isLoggedIn = ref(false)
const router = useRouter()
const articles = ref([])
const videos = ref([])
const loading = ref(true)

const handleLogin = () => {
    router.push('/login')
}

// 摘要截断函数
const truncate = (text, maxLen) => {
    return text?.length <= maxLen ? text : text?.slice(0, maxLen) + '...'
}

const getArticleLatest = async () => {
    try {
        loading.value = true
        const res = await getArticleLatestService(6)
        articles.value = res.data || []
    } catch (error) {
        console.error('获取最新文章失败:', error)
        articles.value = []
    }
}
const getVideoLatest = async () => {
    try {
        loading.value = true
        const res = await getVideoLatestService(8)
        videos.value = res.data || []
    } catch (error) {
        console.error('获取视频列表失败:', error)
        videos.value = []
    } finally {
        loading.value = false
    }
}

onMounted(() => {
    getArticleLatest()
    getVideoLatest()
})


const goToArticle = (id) => {
    router.push(`/article/${id}`)
}

const goToVideo = (id) => {
    router.push(`/video/${id}`)
}

</script>

<style scoped>
.home-container {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
}

.main {
    background-color: #ffffff;
    flex: 1;
    padding: 0px;
}

.carousel-text {
    color: #475669;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
    text-align: center;
    font-size: 24px;
}

.content {
    width: 60%;
    margin: 0 auto;
}

.section-title {
    text-align: center;
    font-size: 28px;
    color: #2c3e50;
    margin: 30px 0;
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

.video-card {
    cursor: pointer;
    transition: all 0.3s ease;
    margin-bottom: 20px;
}

.video-card:hover {
    box-shadow: 0 6px 20px rgba(0, 123, 255, 0.2);
    transform: scale(1.03);
}
</style>