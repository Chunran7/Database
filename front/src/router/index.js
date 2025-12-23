import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'

import LoginVue from '@/views/Login.vue'
import HomeVue from '@/views/Home.vue'
import ArticleVue from '@/views/Article.vue'
import VideoVue from '@/views/Video.vue'
import ArticleDetailVue from '@/views/ArticleDetail.vue'
import VideoDetailVue from '@/views/VideoDetail.vue'
import ForumVue from '@/views/Forum.vue'
import PostDetailVue from '@/views/PostDetail.vue'
import ProfileVue from '@/views/Profile.vue'
import AdminVue from '@/views/Admin.vue'

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', name: 'Home', component: HomeVue },
  { path: '/login', name: 'Login', component: LoginVue },

  { path: '/article', name: 'Article', component: ArticleVue, meta: { keepAlive: true } },
  { path: '/article/:id', name: 'ArticleDetail', component: ArticleDetailVue },

  { path: '/video', name: 'Video', component: VideoVue },
  { path: '/video/:id', name: 'VideoDetail', component: VideoDetailVue },

  { path: '/forum', name: 'Forum', component: ForumVue, meta: { requiresAuth: false } },
  { path: '/post/:id', name: 'PostDetail', component: PostDetailVue },

  { path: '/profile/edit', name: 'ProfileEdit', component: ProfileVue, meta: { requiresAuth: true } },

  { path: '/admin', name: 'Admin', component: AdminVue, meta: { requiresAuth: false } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    if (token) return next()
    ElMessage.warning('访问该模块前，请先登录')
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }
  next()
})

export default router
