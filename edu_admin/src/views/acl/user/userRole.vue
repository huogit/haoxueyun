<template>
  <div class="app-container">
    <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
    <div style="margin: 15px 0;"/>
    <el-checkbox-group v-model="checkedRoles" @change="handleCheckedRolesChange">
      <el-checkbox v-for="role in roles" :label="role.id" :key="role.id">{{ role.roleName }}</el-checkbox>
    </el-checkbox-group>
    <br>
    <el-button :disabled="saveBtnDisabled" type="primary" @click="update">保存</el-button>

  </div>
</template>

<script>

import userApi from '@/api/acl/user'

// const cityOptions = ['上海', '北京', '广州', '深圳'];

export default {
  data() {
    return {
      checkAll: false,
      checkedRoles: [], // 已选中
      roles: [], // 所有的角色
      isIndeterminate: true, // 全选的不确定状态
      userId: '',
      saveBtnDisabled: false // 保存按钮是否禁用,
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      if (this.$route.params && this.$route.params.id) {
        this.userId = this.$route.params.id
        this.getById(this.userId)
      }
    },
    getById(userId) {
      userApi.getAssign(userId).then((response) => {
        var jsonObj = response.data.assignRoles
        // this.checkedRoles = this.getJsonToList(jsonObj, 'id')
        this.checkedRoles = this.getJsonToList(jsonObj, 'id')
        this.roles = response.data.allRolesList
      })
    },
    // 把json数据转成string再转成对象，根据Key获取value数据
    getJsonToList(json, key) {
      // // 把JSON字符串转成对象
      // var list = JSON.parse(JSON.stringify(json))
      // // var list = JSON.parse(json)
      var strText = []
      // 遍历这个集合对象，获取key的值 即id
      for (var i = 0; i < json.length; i++) {
        strText.push(json[i][key])
      }
      return strText
    },
    handleCheckAllChange(val) {
      // this.roles 是json列表数据  要转换为数组 才能满足要求
      this.checkedRoles = val ? this.getJsonToList(this.roles, 'id') : []
      this.isIndeterminate = false
    },
    handleCheckedRolesChange(value) {
      const checkedCount = value.length
      this.checkAll = checkedCount === this.roles.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.roles.length
    },
    update() {
      this.saveBtnDisabled = true // 防止表单重复提交
      var ids = this.checkedRoles.join(',')
      // 修改权限
      userApi.saveAssign(this.userId, ids).then(response => {
        if (response.success) {
          this.$message({
            type: 'success',
            message: '保存成功'
          })
          this.$router.push({ path: '/acl/user/list' })
        }
      })
    }
  }
}
</script>
