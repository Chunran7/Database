// 导入request.js请求工具
import request from "@/utils/request.js";

// 获取文章列表
export const getArticleListService = () => {
  return request.get("/article/list");
};

// 根据ID获取文章详情
export const getArticleByIdService = (id) => {
  return request.get(`/article/${id}`);
};

export const getArticleLatestService = (count = 3) => {
  return request.get("/article/latest", {
    params: { count },
  });
};
