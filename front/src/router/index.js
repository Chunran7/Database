import { createRouter, createWebHistory } from "vue-router";

//导入组件
import LoginVue from "@/views/Login.vue";
import HomeVue from "@/views/Home.vue";
import ArticleVue from "@/views/Article.vue";
import VideoVue from "@/views/Video.vue";
import ArticleDetailVue from "@/views/ArticleDetail.vue";
import VideoDetailVue from "@/views/VideoDetail.vue";
import ForumVue from "@/views/Forum.vue";
import PostDetailVue from "@/views/PostDetail.vue";
import ProfileVue from "@/views/Profile.vue";
import AdminVue from "@/views/Admin.vue";

// 定义路由
const routes = [
  {
    path: "/home",
    name: "Home",
    component: HomeVue,
  },
  {
    path: "/login",
    component: LoginVue,
  },
  {
    path: "/",
    redirect: "/home",
  },
  {
    path: "/article",
    name: "Article",
    component: ArticleVue,
    meta: {
      keepAlive: true, // 需要被缓存
    },
  },
  {
    path: "/article/:id",
    name: "ArticleDetail",
    component: ArticleDetailVue,
  },
  {
    path: "/video",
    component: VideoVue,
  },
  {
    path: "/video/:id",
    name: "VideoDetail",
    component: VideoDetailVue,
  },
  {
    path: "/admin",
    name: "Admin",
    component: AdminVue,
    meta: { requiresAuth: false },
  },
  {
    path: "/forum",
    name: "forum",
    component: ForumVue,
    // 允许匿名访问帖子列表，发布主题时在组件内部检查登录状态
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: "/profile/edit",
    name: "ProfileEdit",
    component: ProfileVue,
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: "/article",
    name: "Article",
    component: ArticleVue,
    meta: {
      keepAlive: true, // 需要被缓存
    },
  },
  {
    path: "/article/:id",
    name: "ArticleDetail",
    component: ArticleDetailVue,
  },
  {
    path: "/video",
    component: VideoVue,
  },
  {
    path: "/video/:id",
    name: "VideoDetail",
    component: VideoDetailVue,
  },
  {
    path: "/post/:id",
    name: "PostDetail",
    component: PostDetailVue,
  },

  // 可以添加更多路由
  // {
  //   path: '/dashboard',
  //   name: 'Dashboard',
  //   component: () => import('../views/Dashboard.vue')
  // }
];

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 添加导航守卫
router.beforeEach((to, from, next) => {
  // 检查要去的路由是否需要认证
  if (to.meta.requiresAuth) {
    // 检查是否有token
    const token = localStorage.getItem("token");
    if (token) {
      // 有token，允许访问
      next();
    } else {
      // 没有token，重定向到登录页
      alert("访问该模块前，请先登录");
      next("/login");
    }
  } else {
    // 不需要认证的路由，直接访问
    next();
  }
});

export default router;
