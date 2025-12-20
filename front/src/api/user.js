// 导入request.js请求工具
import request from '@/utils/request.js'

//调用注册接口函数
export const userRegisterService=(registerData)=>{
    //将json数据传递给后端，格式为x-www-form-urlencoded
    //借助url-search-params
    const params = new URLSearchParams();
    for (const key in registerData) {
        params.append(key, registerData[key]);
    }
    return request.post('/user/register',params);
}

//调用登录接口函数
export const userLoginService=(loginData)=>{
    //将json数据传递给后端，格式为x-www-form-urlencoded
    //借助url-search-params
    const params = new URLSearchParams();
    for (const key in loginData) {
        params.append(key, loginData[key]);
    }
    return request.post('/user/login',params);
}


