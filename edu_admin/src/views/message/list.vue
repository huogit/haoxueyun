<template>
  <div class="app-container">
    <el-form :inline="true" :model="messageObj" class="demo-form-inline">
      <el-form-item label="消息标题">
        <el-input v-model="messageObj.title" placeholder="消息标题"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit()">查询</el-button>
        <el-button type="default" @click="resetData()">清空</el-button>
      </el-form-item>
    </el-form>

    <!-- 工具条 -->
    <div style="margin-bottom: 20px">
      <el-button v-if="hasPerm('message.create')" type="primary" size="mini" @click="addMessage()">添加</el-button>
      <el-button v-if="hasPerm('message.remove')" type="danger" size="mini" @click="removeRows()">批量删除</el-button>
    </div>

    <!-- 讲师列表 -->
    <el-table v-loading="listLoading" :data="messageList" border stripe style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55"/>
      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1 ) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="标题" prop="title" width="150"/>
      <el-table-column label="浏览量" prop="viewCount" />
      <el-table-column label="创建时间" prop="gmtCreate" width="180"/>
      <el-table-column label="操作" width="">
        <template slot-scope="scope">
          <router-link v-if="hasPerm('message.edit')" :to="'/message/edit/' + scope.row.id ">
            <el-button type="primary" size="mini" icon="el-icon-edit">编辑</el-button>
          </router-link>
          <el-button v-if="hasPerm('message.remove')" type="danger" size="mini" icon="el-icon-delete" @click="remove(scope.row.id)">删除</el-button>
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
import messageApi from '@/api/message'

export default {
  data() {
    return {
      listLoading: true, // 数据是否正在加载
      messageList: [], // 讲师列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      messageObj: {}, // 查询表单对象
      multipleSelection: [] // 批量选择中选择的记录列表
    }
  },
  // created() {
  //   this.fetchData()
  // },
  mounted() {
    if (this.hasPerm('message.list')) {
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
      messageApi.getPageList(this.page, this.limit, this.messageObj).then((response) => {
        this.messageList = response.data.items
        this.total = response.data.total
        this.listLoading = false
      })
    },
    onSubmit() {
      this.fetchData()
    },
    resetData() {
      this.message = {}
      this.fetchData()
    },
    // 添加用户
    addMessage() {
      this.$router.push({ path: '/message/create' })
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
        return messageApi.removeRows(idList)
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
    // 删除消息
    remove(id) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // promise
        // 点击确定，远程调用ajax
        return messageApi.removeById(id)
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

