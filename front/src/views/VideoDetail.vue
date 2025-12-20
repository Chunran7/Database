<template>
    <el-container class="video-detail-container">
        <!-- 主体内容 -->
        <el-main class="main">
            <div class="main-container">
                <div v-if="video" class="content">
                    <div class="video-title">{{ video.title }}</div>
                    <div class="video-info">
                        <span class="video-time">更新时间: {{ video.createTime }}</span>
                    </div>
                    <div class="video-wrapper">
                        <video class="video-content" controls :src="video.url" :poster="video.cover"></video>
                    </div>

                </div>
                <div v-else-if="loading" class="video-title">正在加载...</div>
                <div v-else class="video-title">视频不存在或已被删除</div>
            </div>

        </el-main>

        <!-- 页脚 -->
        <Footer />
    </el-container>
</template>

<script setup>
import Footer from '../components/Footer.vue'
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { marked } from 'marked';
import { computed } from 'vue';
import { getVideoByIdService } from '@/api/video.js'; // 导入文章详情服务函数


const route = useRoute();
const router = useRouter();
const video = ref(null);
const loading = ref(true);

const renderedContent = computed(() => {
    if (video.value && video.value.content) {
        return marked(video.value.content);
    }
    return '错误';
});

const getvideoDetail = async () => {
    try {
        const id = route.params.id;
        // 使用API服务函数替代直接调用request
        const res = await getVideoByIdService(id);
        console.log('后端返回的数据:', res)
        if (res.code === 0) {
            console.log('视频数据:', res.data); // 添加这行来查看具体字段
            video.value = res.data;
        } else {
            ElMessage.error(res.msg);
            // 视频不存在时返回视频列表页
            setTimeout(() => {
                router.push('/video');
            }, 2000);
        }
    } catch (err) {
        console.error('Failed to fetch video detail:', err);
        ElMessage.error('获取视频详情失败');
        // 发生错误时返回视频列表页
        setTimeout(() => {
            router.push('/video');
        }, 2000);
    } finally {
        loading.value = false;
    }
}

onMounted(() => {
    getvideoDetail()
})

</script>

<style scoped>
.video-detail-container {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
}

.main {
    border: 10cm;
    background-color: #f3f3f3;
    flex: 1;
    padding: 20px 0;
}

.main-container {
    width: 60%;
    min-height: 60vh;
    background-color: #ffffff;
    margin: 30px auto;
    border-radius: 25px;
}

.content {}


.video-title {
    text-align: center;
    font-size: 30px;
    padding: 40px 0 10px 0;
}

.video-wrapper {
    width: 80%;
    margin: 0 auto;
    padding: 50px 0 100px 0;
    /* 居中 wrapper */
    display: flex;
    justify-content: center;
    /* 视频居中 */
}

.video-content {
    width: 100%;
    height: auto;
    max-width: 100%;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.video-info {
    text-align: center;
    color: #888;
    margin-bottom: 20px;
}


.video-time {
    margin-left: 20px;
}
</style>