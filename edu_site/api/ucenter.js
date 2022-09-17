import request from '~/utils/request'

const URI = '/api/ucenter/member'

export default {
  getById() {
    return request({
      url: URI + '/auth/get/',
      method: 'get'
    })
  },
  updateData(memberObj) {
    return request({
      url: URI + '/auth/update',
      method: 'post',
      data: memberObj
    })
  },
  // 获取验证码
  getCode(mobile) {
    return request({
      url: `/api/sms/send/${mobile}`,
      method: 'get'
    })
  },
  updatePhone(phoneObj) {
    return request({
      url: URI + '/auth/update-mobile',
      method: 'post',
      data: phoneObj
    })
  },
  updatePassword(PasswordObj) {
    return request({
      url: URI + '/auth/update-password',
      method: 'post',
      data: PasswordObj
    })
  },
  getMessageList(page, limit) {
    return request({
      url: `/api/ucenter/message/list/${page}/${limit}`,
      method: 'get'
    })
  },
  getMessageById(id) {
    return request({
      url: `/api/ucenter/message/get/${id}`,
      method: 'get'
    })
  }
}
