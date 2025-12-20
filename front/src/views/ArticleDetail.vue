<template>
    <el-container class="article-detail-container">
        <!-- 主体内容 -->
        <el-main class="main">
            <div class="main-container">
                <div v-if="article" class="content">
                    <div class="article-title">{{ article.title }}</div>
                    <div class="article-info">
                        <span class="article-author">作者: {{ article.author }}</span>
                        <span class="article-time">更新时间: {{ article.createTime }}</span>
                    </div>
                    <div class="article-content" v-html="renderedContent"></div>
                </div>
                <div v-else class="article-title">正在加载...</div>
            </div>
        </el-main>

        <!-- 页脚 -->
        <Footer />
    </el-container>
</template>

<script setup>
import Footer from '../components/Footer.vue'
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { marked } from 'marked';
import { computed } from 'vue';
import { getArticleByIdService } from '@/api/article.js'; // 导入文章详情服务函数


const route = useRoute();
const article = ref(null);

const renderedContent = computed(() => {
    if (article.value && article.value.content) {
        return marked(article.value.content);
    }
    return '错误';
});

const getArticleDetail = async () => {
    try {
        const id = route.params.id;
        // 使用API服务函数替代直接调用request
        const res = await getArticleByIdService(id);
        console.log('后端返回的数据:', res)
        if (res.code === 0) {
            console.log('文章数据:', res.data); // 添加这行来查看具体字段
            article.value = res.data;
        } else {
            ElMessage.error(res.msg);
        }
    } catch (err) {
        console.error('Failed to fetch article detail:', err);
    }


}

onMounted(() => {
    getArticleDetail()
})

</script>

<style scoped>
.article-detail-container {
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

.article-title {
    text-align: center;
    font-size: 30px;
    padding: 40px 0 10px 0;
}

.article-info {
    text-align: center;
    color: #888;
    margin-bottom: 20px;
}

.article-author {
    margin-right: 20px;
}

.article-time {
    margin-left: 20px;
}

.article-content {
    padding: 0 40px 80px 40px;
    line-height: 1.8;
    font-size: 18px;
    width: 70%;
    text-align: justify;
    text-align-last: left;
    text-indent: 2em;
    margin: 0 auto;


    /* 保留换行符 */
}

.article-content :deep(h1) {
    font-size: 2em;
    /* 字体大小: 2倍于父元素 */
    margin: 0.67em 0;
    /* 上下边距 */
}

.article-content :deep(h2) {
    font-size: 1.5em;
    /* 字体大小: 1.5倍于父元素 */
    margin: 0.83em 0;
    /* 上下边距 */
}

.article-content :deep(pre) {
    line-height: 1.2;
    margin: 1em 0;
}

/* 添加段落间间距控制 */
.article-content :deep(p) {
    margin: 0.8em 0;
}

p {
    margin: 0;
}
</style>