import request from "@/utils/request";

// 获取视频列表
export const getVideoListService = () => {
  return request.get("/video/list");
};

// 获取视频详情
export const getVideoByIdService = (id) => {
  return request.get(`/video/${id}`);
};

export const getVideoLatestService = (count = 8) => {
  return request.get("/video/latest", {
    params: {
      count
    }
  });
};