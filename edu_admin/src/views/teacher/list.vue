<template>
  <div class="app-container">
    <!--行内表单（查询条件）-->
    <el-form :inline="true" :model="searchObj" class="demo-form-inline" >
      <el-form-item label="姓名">
        <el-autocomplete
          v-model="searchObj.name"
          :fetch-suggestions="querySearch"
          :trigger-on-focus="false"
          class="inline-input"
          placeholder="请输入内容"
          value-key="name"/>
      </el-form-item>
      <el-form-item label="头衔">
        <el-select v-model="searchObj.level" placeholder="头衔">
          <el-option label="高级讲师" value="1"/>
          <el-option label="首席讲师" value="2"/>
        </el-select>
      </el-form-item>
      <el-form-item label="入驻时间">
        <el-date-picker v-model="searchObj.joinDateBegin" type="date" placeholder="开始时间" value-format="yyyy-MM-dd" />
      </el-form-item>
      <el-form-item label="-">
        <el-date-picker v-model="searchObj.joinDateEnd" type="date" placeholder="结束时间" value-format="yyyy-MM-dd"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
        <el-button type="default" @click="resetData()">清空</el-button>
      </el-form-item>
    </el-form>
    <!-- 工具按钮 -->
    <div style="margin-bottom: 20px">
      <el-button v-if="hasPerm('teacher.remove')" type="danger" @click="batchRemove()">批量删除</el-button>
    </div>
    <!--table表单-->
    <el-table :data="list" border stripe @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55"/>
      <el-table-column type="index" width="50" />
      <el-table-column prop="name" label="姓名" width="80" />
      <el-table-column label="头衔" width="91">

        <template slot-scope="scope">
          <el-tag v-if="scope.row.level === 1" :key="scope.row.level" type="success" effect="dark"> 高级讲师 </el-tag>
          <el-tag v-if="scope.row.level === 2" :key="scope.row.level" type="info" effect="light"> 首席讲师 </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="intro" label="简介" />
      <el-table-column prop="sort" label="排序" width="60" align="center" />
      <el-table-column prop="joinDate" label="入驻时间" width="160" align="center" />
      <el-table-column label="#" width="50" align="center" >
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <!--router-link:a标签，to：src属性-->
          <router-link :to="'/teacher/edit/'+scope.row.id">
            <el-button
              v-if="hasPerm('teacher.edit')"
              type="primary"
              size="mini"
              icon="el-icon-edit"
            >编辑</el-button>
          </router-link>
          <el-button
            v-if="hasPerm('teacher.remove')"
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
import teacherApi from '@/api/teacher'
export default {
  // 定义数据模型
  data() {
    return {
      list: [],
      total: 0, // 总记录数
      page: 1, // 当前页
      limit: 10, // 每页记录数
      searchObj: {}, // 查询条件
      multipleSelection: [] // 批量删除选中的记录列表
    }
  },
  // 页面渲染成功后获取数据
  // created() {
  //   this.list = this.fetchData()
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
  // 定义方法
  methods: {
    // 查询讲师列表
    fetchData() {
      teacherApi.pageList(this.page, this.limit, this.searchObj).then((response) => {
        // 这个response 是经过响应拦截器处理之后的结果
        this.list = response.data.records
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
        return teacherApi.removeById(id)
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
    // 批量删除
    batchRemove() {
      // console.log('removeRows')

      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择删除的记录！')
        return
      }
      // 确认消息,element提供的全局方法
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
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
        return teacherApi.removeByIdList(idList)
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
    },
    querySearch(queryString, cb) {
      console.log('queryString', queryString, 'cb', cb)
      // if (queryString.length === 0) {
      //   return
      // }
      teacherApi.selectNameListByKey(queryString).then((response) => {
        cb(response.data.nameList)
      })
    }
  }
}
</script>
