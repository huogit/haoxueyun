import request from '~/utils/request'
import cookie from 'js-cookie'

// const BASEURL = 'http://127.0.0.1:8160'
const URI = '/api/ucenter/member'

export default{
  submitLogin(LoginObj) {
    return request({
      url: URI + '/login',
      method: 'post',
      data: LoginObj
    })
  },
  getLoginInfo() {
    return request({
      url: URI + '/get-login-info',
      method: 'get',
      // 通过请求头发送token
      headers: { 'token': cookie.get('edu_jwt_token') }
    })
  }
}
