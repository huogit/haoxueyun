import request from '@/utils/request'

export default {

  // 教师列表
  list() {
    return request({
      url: '/admin/edu/teacher/list',
      method: 'get'
    })
  },
  // 分页查询
  pageList(page, limit, searchObj) {
    return request({
      url: `admin/edu/teacher/listPage/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  // 删除讲师
  removeById(id) {
    return request({
      url: `/admin/edu/teacher/remove/${id}`,
      method: 'delete'
    })
  },
  // 新增讲师
  save(teacherObj) {
    // return request()({ 这里（）没有删掉
    return request({
      url: '/admin/edu/teacher/save',
      method: 'post',
      data: teacherObj
    })
  },
  // 查看讲师
  getById(id) {
    return request({
      url: `/admin/edu/teacher/get/${id}`,
      method: 'get'
    })
  },
  // 编辑讲师
  update(teacherObj) {
    return request({
      url: '/admin/edu/teacher/update',
      method: 'put',
      teacher: teacherObj
    })
  },
  // 批量删除
  removeByIdList(idList) {
    return request({
      url: '/admin/edu/teacher/batch-remove',
      method: 'delete',
      data: idList
    })
  },
  // 查询教师名字
  selectNameListByKey(key) {
    return request({
      url: `/admin/edu/teacher/list/name/${key}`,
      method: 'get'
    })
  }
}
