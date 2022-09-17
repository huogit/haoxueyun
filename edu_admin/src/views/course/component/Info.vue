<template>
  <div id="app-container">
    <el-form label-width="120px">
      <el-form-item label="课程标题">
        <el-input v-model="courseInfo.title" placeholder="示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"/>
      </el-form-item>
      <!-- 课程讲师 TODO -->
      <el-form-item label="课程讲师">
        <el-select v-model="courseInfo.teacherId" placeholder="请选择">
          <el-option
            v-for="item in teacherList"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
        </el-select>
      </el-form-item>
      <!-- 课程类别 TODO -->
      <el-form-item label="课程类别">
        <!-- 所属分类：级联下拉列表 -->
        <!-- 一级分类 -->
        <el-select v-model="courseInfo.subjectParentId" placeholder="请选择" @change="subjectChanged">
          <el-option
            v-for="item in subjectList"
            :key="item.id"
            :label="item.title"
            :value="item.id"/>
        </el-select>
        <!-- 一级分类 -->
        <el-select v-model="courseInfo.subjectId" placeholder="请选择">
          <el-option
            v-for="item in subjectLevelTowList"
            :key="item.id"
            :label="item.title"
            :value="item.id"/>
        </el-select>
      </el-form-item>
      <!-- 总课时  TODO -->
      <el-form-item label="总课时">
        <el-input-number v-model="courseInfo.lessonNum" :min="0" size="small" label="请填写课程的总课时数" />
      </el-form-item>
      <!-- 课程简介  TODO -->
      <el-form-item label="课程简介">
        <Tinymce :height = "300" v-model="courseInfo.description" />
      </el-form-item>
      <!-- 课程封面  TODO -->
      <el-form-item label="课程封面">
        <el-upload
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :on-error="handleCoverError"
          class="cover-uploader"
          action="http://localhost:8120/admin/oss/file/upload?module=cover">
          <img v-if="courseInfo.cover" :src="courseInfo.cover" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"/>
        </el-upload>
      </el-form-item>
      <!-- 课程价格  TODO -->
      <el-form-item label="课程价格">
        <el-input-number v-model="courseInfo.price" :min="0" size="small" label="免费课程请设置为0元" /> 元
      </el-form-item>
      <div style="text-align:center">
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveAndNext()">保存并下一步</el-button>
      </div>
    <!-- 课程信息表单 TODO -->
    </el-form>
  </div>
</template>

<script>
import teacherApi from '@/api/teacher'
import subjectApi from '@/api/subject'
import courseApi from '@/api/course'
// 引入 vue-element-admin封装的Tinymce
import Tinymce from '@/components/Tinymce'
export default {
  // 注册组件
  components: {
    Tinymce
  },
  data() {
    return {
      saveBtnDisabled: false, // 按钮是否被禁用
      courseInfo: {
        // 表单数据
        title: '',
        teacherId: '',
        subjectParentId: '',
        // 以下解决表单数据不全时insert语句非空校验
        subjectId: '',
        lessonNum: 0,
        cover: '',
        description: '',
        price: 0
      },
      teacherList: [],
      subjectList: [], // 一级分类列表
      subjectLevelTowList: [] // 二级分类列表
    }
  },
  created() {
    console.log('courseId', this)
    if (this.$parent.courseId) {
      this.fetchCourseInfoById(this.$parent.courseId)
    } else {
      // 初始化分类列表
      this.initSubjectList()
    }
    // 获取讲师列表
    this.initTeacherList()
  },
  methods: {
    // 获取课程基本信息
    fetchCourseInfoById(id) {
      courseApi.getCourseInfoById(id).then((response) => {
        this.courseInfo = response.data.item

        subjectApi.getNestedTreeList().then((response) => {
          this.subjectList = response.data.items

          // 重新填充二级菜单：遍历一级菜单列表，
          this.subjectList.forEach(subject => {
            if (subject.id === this.courseInfo.subjectParentId) {
              this.subjectLevelTowList = subject.children
            }
          })
        })
      })
    },
    saveData() {
      // 保存数据，但未发布
      courseApi.saveCourseInfo(this.courseInfo).then((response) => {
        this.$parent.courseId = response.data.courseId // 用于之后的查询
        this.$message.success(response.message)
      })
    },
    // 更新课程信息
    updateData() {
      courseApi.updateCourseInfoById(this.courseInfo).then((response) => {
        this.$message.success(response.message)
        this.$parent.active = 1 // 下一步
      })
    },
    // 展示下一个组件
    saveAndNext() {
      this.saveBtnDisabled = true
      if (!this.$parent.courseId) {
        this.saveData()
      } else {
        this.updateData()
      }
      this.$parent.active = 1
    },
    // 初始化讲师列表
    initTeacherList() {
      teacherApi.list().then((response) => {
        // console.log(response)
        this.teacherList = response.data.items
      })
    },
    // 初始化课程分类列表
    initSubjectList() {
      subjectApi.getNestedTreeList().then((response) => {
        this.subjectList = response.data.items
      })
    },
    // 初始化二级分类列表
    initSubjectLevelList(id) {
      this.subjectList.forEach(subject => {
        if (subject.id === id) {
          this.courseInfo.subjectId = ''// 初始化为空,防止出错
          this.subjectLevelTowList = subject.children
        }
      })
    },
    // 选择器改变时
    subjectChanged(id) {
      console.log(id)
      this.initSubjectLevelList(id)
    },
    // 上传文件之前的钩子 function(file)
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }

      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }

      return isJPG && isLt2M
    },
    // on-success	文件上传成功时的钩子	function(response, file, fileList)
    handleAvatarSuccess(response) {
      if (response.success) {
        this.courseInfo.cover = response.data.url
        // 强制渲染
        this.$forceUpdate()
      } else {
        this.$message.error('上传失败')
      }
    },
    // on-error	文件上传失败时的钩子	function(err, file, fileList)
    // 没能传到服务器的错误
    handleCoverError(error) {
      console.error(error)
      this.$message.error('上传失败2')
    }
  }
}
</script>

<style scoped>
.tinymce-container {
  position: relative;
  line-height: normal;
}
.cover-uploader .el-upload{
  border: 1px dashed #d9d9d9; /*周围边界 */
  border-radius: 6px;/*边界半径 */
  cursor:pointer;
  position: relative;
  overflow: hidden;
}
.cover-uploader .el-upload:hover {
  border-color: #409eff;
}
.cover-uploader .avatar-uploader-icon{
  font-size: 28px;
  color: #8c939d;
  width: 640px;
  line-height: 357px;
  text-align: center;
}
.cover-uploader img{
  width: 640px;
  height: 357px;
  display: block;
}
</style>
