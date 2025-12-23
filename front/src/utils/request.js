// 定制请求实例（统一处理 Result{code,msg,data} + 自动携带 JWT）
import axios from "axios";
import { ElMessage } from "element-plus";

const baseURL = "/api";
const instance = axios.create({
  baseURL,
  timeout: 15000,
});

// 请求拦截器：自动在请求头中加入 Authorization
instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      const auth = token.startsWith("Bearer ") ? token : `Bearer ${token}`;
      config.headers = config.headers || {};
      // axios v1: 可能是 AxiosHeaders，优先用 set
      if (typeof config.headers.set === "function") {
        config.headers.set("Authorization", auth);
      } else {
        config.headers["Authorization"] = auth;
      }
      // 兼容：部分旧后端可能读取 token 字段（可留着不影响）
      if (typeof config.headers.set === "function") {
        config.headers.set("token", token);
      } else {
        config.headers["token"] = token;
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 响应拦截器：成功直接返回 data，失败抛出 Error(msg)
instance.interceptors.response.use(
  (res) => {
    const payload = res?.data;
    if (payload && payload.code === 0) return payload.data;
    const msg = payload?.msg || "操作失败";
    return Promise.reject(new Error(msg));
  },
  (error) => {
    const status = error?.response?.status;
    const payload = error?.response?.data;
    const msg = payload?.msg || error?.message || "网络异常，请稍后重试";

    if (status === 401) {
      // 登录过期/未登录：清理并跳转登录页
      if (currentPath.startsWith("/admin")) {
        ElMessage.error("管理员登录已过期或无权限");
        return Promise.reject(err);
      }
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      ElMessage.error(payload?.msg || "请先登录");
      const redirect = encodeURIComponent(
        window.location.pathname + window.location.search + window.location.hash
      );
      window.location.replace(`/login?redirect=${redirect}`);
      return Promise.reject(new Error(payload?.msg || "请先登录"));
    }

    return Promise.reject(new Error(msg));
  }
);

export default instance;
