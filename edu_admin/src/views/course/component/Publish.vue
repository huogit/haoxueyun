<template>
  <div id="app-container">
    <!--课程预览 TODO-->

    <div class="ccInfo">
      <img :src="coursePublishInfo.cover">
      <div class="main">
        <h2>{{ coursePublishInfo.title }}</h2>
        <p class="gray"><span>共{{ coursePublishInfo.lessonNum }}课时</span></p>
        <p><span>所属分类：{{ coursePublishInfo.subjectParentTitle }} > {{ coursePublishInfo.subjectTitle }}</span></p>
        <p> 课程讲师：{{ coursePublishInfo.teacherName }}</p>
        <h3 class="red">￥{{ coursePublishInfo.price }}</h3>
      </div>

      <div style="text-align:center">
        <el-button ref="prev" type="primary" @click="prev()">上一步</el-button>
        <el-button :disabled="publishBtnDisable" type="primary" @click="publishAndNext()">发布课程</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import courseApi from '@/api/course'
export default {
  data() {
    return {
      publishBtnDisable: false, // 按钮是否显示
      coursePublishInfo: {}
    }
  },
  created() {
    if (this.$parent.courseId) {
      this.fetchData(this.$parent.courseId)
    }
  },
  methods: {
    fetchData(id) {
      courseApi.coursePublishInfoById(id).then((response) => {
        console.log(response)
        this.coursePublishInfo = response.data.item
      })
    },

    // 上一步
    prev() {
      this.$parent.active = 1
    },
    // 发布并下一步
    publishAndNext() {
      this.publishBtnDisable = true
      courseApi.publishCourseById(this.$parent.courseId).then((response) => {
        this.$parent.active = 3
        this.$message.success(response.message)
      })
    }
  }
}
</script>

<style scoped>
.ccInfo {
    /* 包裹住的元素的背景色 */
    background: #f5f5f5;
    /* 定义元素边框与元素内容之间的空间，即上下左右的内边距。 */
    padding: 20px;
    /* 防止溢出 内容会被修剪，并且其余内容是不可见的。 */
    overflow: hidden;
    /* 设置对象边框的特性。 边框宽度 | 边框样式 | 边框颜色 */
    border: 1px dashed #DDD;
    /* 设置元素的下外边距 */
    margin-bottom: 40px;
    /* 规定元素的定位类型 relative生成相对定位的元素，相对于其正常位置进行定位。 */
    position: relative;
}

.ccInfo img {
    background: #d6d6d6;
    width: 500px;
    height: 278px;
    /* 	规定元素应该生成的框的类型 none	此元素不会被显示。 */
    display: block;
    /* 规定框是否应该浮动 */
    float: left;
    border: none;
}
.ccInfo .main {
  /* 设置元素的左边距。main包括的元素的左边距 */
    margin-left: 520px;
}
.ccInfo .main h2 {
    font-size: 28px;
    /* 设置元素的下外边距 */
    margin-bottom: 30px;
    /* 设置行高：一行一行的高度. */
    line-height: 1;
    /* 	规定字体的粗细 */
    font-weight: normal;
}

.ccInfo .main p {
    margin-bottom: 10px;
    /* 设置浏览器是否对过长的单词进行换行。在长单词或 URL 地址内部进行换行。 */
    word-wrap: break-word;
    line-height: 24px;
    max-height: 48px;
    /* 规定当内容溢出元素框时发生的事情 hidden 隐藏 */
    overflow: hidden;
}

.ccInfo .main p {
    /* 底部边距 */
    margin-bottom: 10px;
    word-wrap: break-word;
    line-height: 24px;
    max-height: 48px;
    overflow: hidden;
}

.ccInfo .main h3 {
    left: 540px;
    /* 距离底部多少 */
    bottom: 20px;
    line-height: 1;
    font-size: 28px;
    color: #d32f24;
    font-weight: normal;
    /* absolute：生成绝对定位的元素 */
    position: absolute;
}
</style>
