<template>
  <div class="app-container">
    <!--行内表单（查询条件）-->
    <el-form :inline="true" :model="searchObj" class="demo-form-inline" >
      <el-form-item label="角色">
        <el-input
          v-model="searchObj.roleName"

          :trigger-on-focus="false"
          class="inline-input"
          placeholder="请输入内容"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit()" >查询</el-button>
        <el-button type="default" @click="resetData()">清空</el-button>
      </el-form-item>
    </el-form>
    <!-- 工具按钮 -->
    <div style="margin-bottom: 20px">
      <el-button v-if="hasPerm('role.create')" type="primary" size="mini" @click="addRole()" >添加</el-button>
      <el-button v-if="hasPerm('role.removes')" type="danger" size="mini" @click="batchRemove()">批量删除</el-button>

    </div>
    <!--table表单-->
    <el-table :data="roleList" border stripe @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55"
      />
      <el-table-column prop="index" label="序号" width="70" align="center" >
        <template slot-scope="scope">
          {{ (page-1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="roleName" label="角色名" />
      <el-table-column label="创建时间" prop="gmtCreate"/>

      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <!--router-link:a标签，to：src属性-->
          <router-link :to="'/acl/role/update'+scope.row.id">
            <el-button
              v-if="hasPerm('role.edit')"
              type="primary"
              size="mini"
              icon="el-icon-edit"
            >编辑</el-button>
          </router-link>
          <!--权限-->
          <router-link :to="'/acl/role/distribution/'+scope.row.id">
            <el-button
              v-if="hasPerm('role.acl')"
              type="info"
              size="mini"
              icon="el-icon-info"
            >权限</el-button>
          </router-link>
          <el-button
            v-if="hasPerm('role.remove')"
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click="removeById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <template>

      <!-- 分页组件 -->
      <el-pagination
        :current-page="page"
        :total="total"
        :page-size="limit"
        :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
        style="padding: 30px 0; text-align: center;"
        layout="total, sizes, prev,pager, next, jumper"
        @size-change="changePageSize"

        @current-change="changeCurrentPage"
      />

  </template></div>
</template>
<script>
// 引入api
import roleApi from '@/api/acl/role'
export default {
  // 定义数据模型
  data() {
    return {
      roleList: [],
      total: 0, // 总记录数
      page: 1, // 当前页
      limit: 10, // 每页记录数
      searchObj: {}, // 查询条件
      multipleSelection: [] // 批量删除选中的记录列表
    }
  },
  // 页面渲染成功后获取数据
  mounted() {
    if (this.hasPerm('role.list')) {
      this.fetchData()
    } else {
      this.$message({
        type: 'warning',
        message: '您没有权限查看!'
      })
      history.go(-1)
    }
  },
  // 定义方法
  methods: {
    // 查询讲师列表
    fetchData() {
      roleApi.getPageList(this.page, this.limit, this.searchObj).then((response) => {
        // 这个response 是经过响应拦截器处理之后的结果
        this.roleList = response.data.items
        this.total = response.data.total
      })
    },
    // 删除讲师
    removeById(id) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 当点击确定的时候，才会进入到这里
        console.log('删除')
        return roleApi.removeById(id)
      }).then((response) => {
        this.fetchData()
        this.$message.success(response.message)
      }).catch(error => {
        console.log('error', error)
        // 当取消时会进入catch语句:error = 'cancel'
        // 当后端服务抛出异常时：error = 'error'
        if (error === 'cancel') {
          this.$message.info('取消删除')
        }
      })
    },
    // 每页记录数改变，size：回调参数，表示当前选中的“每页条数”
    changePageSize(size) {
      this.limit = size
      this.fetchData()
    },
    // 改变页码，page：回调参数，表示当前选中的“页码”
    changeCurrentPage(page) {
      this.page = page
      this.fetchData()
    },
    // 提交查询条件
    onSubmit() {
      this.fetchData()
    },
    // 重置查询条件
    resetData() {
      this.searchObj = {}
      this.fetchData()
    },
    addRole() {
      this.$router.push('/acl/role/create')
    },
    // 批量删除
    batchRemove() {
      // console.log('removeRows')

      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择删除的记录！')
        return
      }
      // 确认消息,element提供的全局方法
      this.$confirm('此操作将永久删除这些数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 点击确定，远程调用ajax
        // 遍历selection，将id取出放入id列表
        var idList = []
        this.multipleSelection.forEach(item => {
          idList.push(item.id)
        })
        // console.log('idList', idList)
        // 调用api
        return roleApi.removeRows(idList)
      }).then((response) => {
        // 强制渲染
        this.fetchData()
        this.$message.success(response.message)
      }).catch(error => {
        // 点击取消或报错，执行该区域代码
        if (error === 'cancel') {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        }
      })
    },
    // selection-change	当选择项发生变化时会触发该事件	selection
    handleSelectionChange(selection) {
      // console.log('selection', selection)
      this.multipleSelection = selection
    }
    // querySearch(queryString, cb) {
    //   console.log('queryString', queryString, 'cb', cb)
    //   // if (queryString.length === 0) {
    //   //   return
    //   // }
    //   teacherApi.selectNameListByKey(queryString).then((response) => {
    //     cb(response.data.nameList)
    //   })
    // }
  }
}
</script>
