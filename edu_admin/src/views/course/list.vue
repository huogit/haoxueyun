<template>
  <div class="app-container">
    <!--查询表单 TODO-->
    <!--行内表单（查询条件）-->
    <el-form :inline="true" :model="searchObj" class="demo-form-inline" >
      <el-form-item label="课程类别">
        <!-- 所属分类：级联下拉列表 -->
        <!-- 一级分类 -->
        <el-select v-model="searchObj.subjectParentId" placeholder="请选择" @change="subjectChanged">
          <el-option
            v-for="item in subjectList"
            :key="item.id"
            :label="item.title"
            :value="item.id"/>
        </el-select>
        <!-- 二级分类 -->
        <el-select v-model="searchObj.subjectId" placeholder="请选择">
          <el-option
            v-for="item in subjectLevelTowList"
            :key="item.id"
            :label="item.title"
            :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="标题">
        <el-input v-model="searchObj.title" placeholder="课程标题"/>
      </el-form-item>
      <el-form-item label="讲师">
        <el-select v-model="searchObj.teacherId" placeholder="请选择">
          <el-option
            v-for="item in teacherList"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchData()">查询</el-button>
        <el-button type="default" @click="resetData()">清空</el-button>
      </el-form-item>
    </el-form>
    <!-- 工具按钮 -->
    <div style="margin-bottom: 10px">
      <el-button v-if="hasPerm('course.remove')" type="danger" size="mini" @click="batchRemove()">批量删除</el-button>
    </div>
    <!-- 表格 TODO -->
    <el-table
      :data="list"
      :stripe="true"
      height="500"
      border
      style="width: 100%">
      <!-- 索引 -->
      <el-table-column
        label="#"
        width="30">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>
      <!-- 封面 -->
      <el-table-column align="center" label="封面" width="200">
        <template slot-scope="scope">
          <i v-if="!scope.row.cover" class="el-icon-loading"/>
          <img v-else :src="scope.row.cover" alt="scope.row.title" width="150">
        </template>
      </el-table-column>
      <el-table-column label="课程信息" width="200" >
        <template slot-scope="scope">
          <a href="">{{ scope.row.title }}</a>
          <p>
            所属分类：{{ scope.row.subjectParentTitle }} > {{ scope.row.subjectTitle }}
          </p>
          <p>
            课时：{{ scope.row.lessonNum }} /
            浏览：{{ scope.row.viewCount }} /
            付费学员：{{ scope.row.buyCount }}
          </p>
        </template>
      </el-table-column>
      <!-- 讲师 -->
      <el-table-column label="讲师" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.teacherName }}
        </template>
      </el-table-column>
      <!-- 价格 -->
      <el-table-column label="价格(元)" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="Number(scope.row.price) === 0" type="success">免费</el-tag>
          <el-tag v-else>{{ scope.row.price }}</el-tag>
        </template>
      </el-table-column>
      <!-- 课程状态 -->
      <el-table-column prop="status" label="课程状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 'Draft' ? 'warning' : 'success'">{{ scope.row.status === 'Draft' ? '未发布' : '已发布' }}</el-tag>
        </template>
      </el-table-column>
      <!-- 创建时间 -->
      <el-table-column label="创建时间" width="120" align="center">
        <template slot-scope="scope">
          {{ scope.row.gmtCreate.substr(0, 10) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300" align="center">
        <template slot-scope="scope">
          <router-link :to="'/course/info/'+scope.row.id">
            <el-button v-if="hasPerm('course.edit')" type="primary" size="mini" icon="el-icon-edit">修改</el-button>
          </router-link>
          <router-link :to="'/course/chapter/'+ scope.row.id">
            <el-button v-if="hasPerm('chapter.edit')" type="primary" icon="el-icon-edit" size="mini">编辑大纲</el-button>
          </router-link>
          <el-button v-if="hasPerm('course.remove')" type="danger" icon="el-icon-delete" size="mini" @click="removeById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 TODO-->
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
  </div>
</template>
<script>
// 引入子组件
import courseApi from '@/api/course'
import teacherApi from '@/api/teacher'
import subjectApi from '@/api/subject'

export default {
  // 注册子组件
  data() {
    return {
      list: [], // 课程列表
      total: 0, // 总记录数
      page: 1, // 页码
      limit: 10, // 每页记录数
      searchObj: {
        subjectId: ''// 解决查询表单无法选中二级类别
      }, // 查询条件
      subjectList: [], // 一级科目列表
      teacherList: [], // 讲师列表
      subjectLevelTowList: []// 二级科目列表
    }
  },
  // created() {
  //   this.initTeacherList()
  //   this.initSubjectList()
  //   this.fetchData()
  // },

  mounted() {
    if (this.hasPerm('course.list')) {
      this.initTeacherList()
      this.initSubjectList()
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
      console.log(this.searchObj)
      courseApi.getPageList(this.page, this.limit, this.searchObj).then((response) => {
        this.list = response.data.rows
        this.total = response.data.total
      })
    },
    initTeacherList() {
      teacherApi.list().then((response) => {
        this.teacherList = response.data.items
      })
    },
    initSubjectList() {
      subjectApi.getNestedTreeList().then((response) => {
        this.subjectList = response.data.items
      })
    },
    // 初始化二级分类列表
    initSubjectLevelList(id) {
      this.subjectList.forEach(subject => {
        if (subject.id === id) {
          this.searchObj.subjectId = ''// 初始化为空,防止出错
          this.subjectLevelTowList = subject.children
        }
      })
    },
    // 批量删除
    batchRemove() {

    },
    // 选择器改变时
    subjectChanged(id) {
      console.log(id)
      this.initSubjectLevelList(id)
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
    // 重置查询条件
    resetData() {
      this.searchObj = {}
      this.fetchData()
    },
    // 删除
    removeById(id) {
      // confirm:确认
      this.$confirm('此操作将永久删除该课程，以及该课程下的章节和视频，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return courseApi.removeById(id)
      }).then((response) => {
        // 重新查询渲染
        this.fetchData()
        this.$message({
          type: 'success',
          message: response.message
        })
      }).catch((error) => {
        if (error === 'cancel') {
          this.$message({ // 失败
            type: 'info',
            message: '已取消删除'
          })
        }
      })
    }

  }
}
</script>
