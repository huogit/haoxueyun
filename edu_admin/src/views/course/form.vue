<template>
  <div id="app-container">
    <h2 style="text-align:center;">发布新课程</h2>
    <el-steps :active="active" finish-status="success" simple style="margin-bottom: 40px">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="发布课程"/>
    </el-steps>

    <Info v-if="active === 0"/>
    <Chapter v-if="active === 1"/>
    <Publish v-if="active === 2 || active === 3"/>
  </div>
</template>
<script>
// 引入子组件
import Info from '@/views/course/component/Info'
import Chapter from '@/views/course/component/chapter'
import Publish from '@/views/course/component/Publish'

export default {
  // 注册子组件
  components: { Info, Chapter, Publish },
  data() {
    return {
      active: 0,
      courseId: ''
    }
  },
  created() {
    console.log('routeName', this.$route.name)
    // 获取路由id
    if (this.$route.name === 'CourseInfoEdit') { // 判断是那个路由
      this.courseId = this.$route.params.id
      this.active = 0
    }

    if (this.$route.name === 'CourseChapterEdit') {
      this.courseId = this.$route.params.id
      this.active = 1
    }
  }
}
</script>
