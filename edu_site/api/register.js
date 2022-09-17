import request from '~/utils/request'

export default{
  getCode(mobile) {
    return request({
      url: `/api/sms/send/${mobile}`,
      method: 'get'
    })
  },
  register(memberObj) {
    return request({
      url: '/api/ucenter/member/register',
      method: 'post',
      data: memberObj
    })
  }
}
