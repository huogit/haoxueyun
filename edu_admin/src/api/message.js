import request from '@/utils/request'

const api_name = '/admin/ucenter/message'

export default {

  getPageList(page, limit, messageObj) {
    return request({
      url: `${api_name}/list/${page}/${limit}`,
      method: 'get',
      params: messageObj // url查询字符串或表单键值对
    })
  },
  getById(id) {
    return request({
      url: `${api_name}/get/${id}`,
      method: 'get'
    })
  },

  save(message) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data: message
    })
  },

  updateById(message) {
    return request({
      url: `${api_name}/update`,
      method: 'put',
      data: message
    })
  },

  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: 'delete'
    })
  },
  removeRows(idList) {
    return request({
      url: `${api_name}/batchRemove`,
      method: 'delete',
      data: idList
    })
  }
}
