import request from "@/utils/request.js";

// 文章列表
export const getArticleListService = ({ page = 1, pageSize = 10, sortBy = "create_time", order = "DESC", keyword = "" } = {}) => {
  return request.get("/article/list", { params: { page, pageSize, sortBy, order, keyword } });
};

// 文章详情
export const getArticleByIdService = (id) => request.get(`/article/${id}`);

// 最新文章
export const getArticleLatestService = (count = 3) => request.get("/article/latest", { params: { count } });

// 点赞 / 取消点赞（需要登录）
export const toggleArticleLikeService = (id) => request.post(`/article/${id}/like/toggle`);

// 收藏 / 取消收藏（需要登录）
export const toggleArticleFavoriteService = (id) => request.post(`/article/${id}/favorite/toggle`);
