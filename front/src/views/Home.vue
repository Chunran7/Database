<template>
    <el-container class="home-container">
        <!-- 主体内容 -->
        <el-main class="main">
            <!-- 走马灯 -->
            <el-carousel height="400px" :interval="3000" arrow="hover" indicator-position="outside">
                <el-carousel-item>
                    <img src="https://images.unsplash.com/photo-1506905925346-21bda4d32df4?ixlib=rb-4.0.3&auto=format&fit=crop&w=1920&q=80"
                        style="width: 100%; height: 100%; object-fit: cover;">
                </el-carousel-item>
                <el-carousel-item>
                    <img src="https://images.unsplash.com/photo-1519681393784-d120267933ba?ixlib=rb-4.0.3&auto=format&fit=crop&w=1920&q=80"
                        style="width: 100%; height: 100%; object-fit: cover;">
                </el-carousel-item>
                <el-carousel-item>
                    <img src="https://images.unsplash.com/photo-1542281286-9e0a16bb7366?ixlib=rb-4.0.3&auto=format&fit=crop&w=1920&q=80"
                        style="width: 100%; height: 100%; object-fit: cover;">
                </el-carousel-item>
                <el-carousel-item>
                    <img src="https://images.unsplash.com/photo-1513694203232-719a280e022f?ixlib=rb-4.0.3&auto=format&fit=crop&w=1920&q=80"
                        style="width: 100%; height: 100%; object-fit: cover;">
                </el-carousel-item>
            </el-carousel>

            <div class="content">
                <!-- 介绍我们 -->
                <section class="introduction">
                    <h1 class="section-title">介绍我们</h1>
                    <el-row gutter="40">
                        <el-col :span="12">
                            <img src="@/assets/logo.png" alt="网站logo"
                                style="width: 100%; height: auto; border-radius: 6px;" />
                        </el-col>
                        <el-col :span="12">

                            欢迎来到我们的风景旅游网站！这里是旅行者的天堂，我们致力于为您提供最精彩的旅游资讯、最实用的旅行攻略和最美丽的风景展示。


                            我们的网站汇聚了全球各地的旅游文章，涵盖自然风光、人文景观、美食文化等多个方面。同时，我们还提供丰富的旅游视频，让您身临其境地感受世界各地的美景。

                            在我们的论坛社区，您可以与其他旅行者分享您的旅行经历，交流旅行心得，结识志同道合的朋友。无论您是经验丰富的旅行达人，还是刚刚开始探索世界的新手，都能在这里找到属于您的旅行灵感。

                            让我们一起踏上这场精彩的旅程，探索世界的每一个角落，创造属于您的美好回忆！
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
                                <img :src="normalizeMedia(article.firstPicture) || 'https://placehold.co/300x150'"
                                    alt="封面图" class="article-img" />
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
                            <el-card :body-style="{ padding: '10px' }" shadow="hover" class="video-card"
                                @click="goToVideo(video.id)">
                                <el-image :src="normalizeMedia(video.cover)" fit="cover"
                                    style="height: 150px;"></el-image>
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
import { normalizeMedia } from '@/utils/media.js'

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
        articles.value = res || []
    } catch (error) {
        console.error('获取最新文章失败:', error)
        articles.value = []
    }
}
const getVideoLatest = async () => {
    try {
        loading.value = true
        const res = await getVideoLatestService(8)
        videos.value = res || []
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