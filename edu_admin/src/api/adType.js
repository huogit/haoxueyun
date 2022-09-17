import request from '@/utils/request'

const URI = '/admin/cms/ad-type'

export default{
  list() {
    return request({
      url: URI + '/list',
      method: 'get'
    })
  },
  getById(id) {
    return request({
      url: URI + `/get/${id}`,
      method: 'get'
    })
  },
  pageList(page, limit) {
    return request({
      url: URI + `/list/${page}/${limit}`,
      method: 'get'
    })
  },
  saveData(adTypeObj) {
    return request({
      url: URI + '/save',
      method: 'post',
      data: adTypeObj
    })
  },
  updateById(adTypeObj) {
    return request({
      url: URI + '/update',
      method: 'put',
      data: adTypeObj
    })
  },
  removeById(id) {
    return request({
      url: URI + `/remove/${id}`,
      method: 'delete'
    })
  }
}
