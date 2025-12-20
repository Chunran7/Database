// 导入request.js请求工具
import request from "@/utils/request.js";

// 获取帖子列表
export const getAllpostsService = ({ page, pageSize, sortBy, order } = {}) => {
  return request.get("/post/list", {
    params: { page, pageSize, sortBy, order },
  });
};

//插入帖子接口

export const insertPostService = (post) => {
  return request.post("/post", post);
};
