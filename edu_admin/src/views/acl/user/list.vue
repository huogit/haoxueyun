<template>
  <div class="app-container">
    <el-form :inline="true" :model="userObj" class="demo-form-inline">
      <el-form-item label="用户名">
        <el-input v-model="userObj.username" placeholder="用户名"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit()">查询</el-button>
        <el-button type="default" @click="resetData()">清空</el-button>
      </el-form-item>
    </el-form>

    <!-- 工具条 -->
    <div style="margin-bottom: 20px">
      <el-button v-if="hasPerm('user.create')" type="primary" size="mini" @click="addUser()">添加</el-button>
      <el-button v-if="hasPerm('user.delete')" type="danger" size="mini" @click="removeRows()">批量删除</el-button>
    </div>

    <!-- 讲师列表 -->
    <el-table v-loading="listLoading" :data="userlist" border stripe style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55"/>
      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1 ) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="用户名" prop="username" width="150"/>
      <el-table-column label="用户昵称" prop="nickName" />
      <el-table-column label="创建时间" prop="gmtCreate" width="180"/>
      <el-table-column label="操作" width="">
        <template slot-scope="scope">
          <router-link v-if="hasPerm('user.edit')" :to="'/acl/user/update/' + scope.row.id ">
            <el-button type="primary" size="mini" icon="el-icon-edit">编辑</el-button>
          </router-link>
          <router-link v-if="hasPerm('user.assign')" :to="'/acl/user/role/' + scope.row.id">
            <el-button type="info" size="mini" icon="el-icon-info">角色</el-button>
          </router-link>
          <el-button v-if="hasPerm('user.remove')" type="danger" size="mini" icon="el-icon-delete" @click="remove(scope.row.id)">删除</el-button>
        </template>

      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
      style="padding: 30px 0; text-align: center;"
      layout="sizes, prev, pager, next, jumper, ->, total, slot"
      @current-change="handleCurrentChange"
      @size-change="changeSize"
    />
  </div>

</template>
<script>
import userApi from '@/api/acl/user'

export default {
  data() {
    return {
      listLoading: true, // 数据是否正在加载
      userlist: [], // 讲师列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      userObj: {}, // 查询表单对象
      multipleSelection: [] // 批量选择中选择的记录列表
    }
  },
  mounted() {
    // console.log('user.list', this.hasPerm('user.list'))
    if (this.hasPerm('user.list')) {
      this.fetchData()
    } else {
      this.$message({
        type: 'warning',
        message: '您没有权限查看!'
      })
      history.go(-1)
    }
  },
  methods: {
    fetchData() {
      userApi.getPageList(this.page, this.limit, this.userObj).then((response) => {
        this.userlist = response.data.items
        this.total = response.data.total
        this.listLoading = false
      })
    },
    onSubmit() {
      this.fetchData()
    },
    resetData() {
      this.user = {}
      this.fetchData()
    },
    // 添加用户
    addUser() {
      this.$router.push({ path: '/acl/user/create' })
    },
    // 选中列表
    handleSelectionChange(value) {
      this.multipleSelection = value
      console.log(this.multipleSelection)
    },
    // 批量删除
    removeRows() {
      console.log('removeRows......')

      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要删除的记录!'
        })
        return
      }

      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // promise
        // 点击确定，远程调用ajax
        // 遍历selection，将id取出放入id列表
        var idList = []
        this.multipleSelection.forEach(item => {
          idList.push(item.id) // 取id出来
        // console.log(idList)
        })
        // 调用api
        return userApi.removeRows(idList)
      }).then((response) => {
        this.fetchData(this.page)
        if (response.success) {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 删除用户
    remove(id) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // promise
        // 点击确定，远程调用ajax
        return userApi.removeById(id)
      }).then((response) => {
        this.fetchData(this.page)
        if (response.success) {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // currentPage 改变时会触发
    handleCurrentChange(page) {
      this.page = page
      this.fetchData()
    },
    // size 改变时触发
    changeSize(size) {
      this.limit = size
      this.fetchData()
    }
  }
}
</script>

