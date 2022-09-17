<template>
  <!--普通组件用 命名第一个字母大写-->
  <div id="app-container">
    <!-- 章节 TODO -->
    <!-- 添加章节按钮 -->
    <div>
      <el-button type="primary" @click="addChapter()">添加章节</el-button>
    </div>
    <!-- 章节列表 -->
    <ul class="chapterList">
      <li v-for=" chapter in chapterList" :key="chapter.id">
        <p>
          {{ chapter.title }}
          <span class="acts">
            <el-button type="text" @click="addVideo(chapter.id)">添加课时</el-button>
            <el-button type="text" @click="editChapter(chapter.id)">编辑</el-button>
            <el-button type="text" @click="removeChapterById(chapter.id)">删除</el-button>
          </span>
        </p>
        <!-- 视频 -->
        <ul class="chapterList videoList">
          <li v-for="video in chapter.children" :key="video.id">

            <p>
              {{ video.title }}
              <el-tag v-if="!video.videoSourceId" size="mini" type="danger">
                {{ '尚未上传视频' }}
              </el-tag>
              <span class="acts">
                <el-button v-if="video.free" size="mini" type="success" round plain>{{ '免费观看' }}</el-button>
                <el-button type="text" @click="editVideo(chapter.id,video.id)">编辑</el-button>
                <el-button type="text" @click="removeVideo(video.id)">删除</el-button>
              </span>
            </p>
          </li>
        </ul>
      </li>
    </ul>
    <!-- 章节表单对话框 使用组件：驼峰 要变成“-”-->
    <Chapter-form ref="chapterForm"/>
    <Video-form ref="videoForm"/>
    <div style="text-align:center">
      <el-button ref="prev" type="primary" @click="prev()">上一步</el-button>
      <el-button ref="next" type="primary" @click="next()">下一步</el-button>
    </div>
  </div>
</template>

<script>
import ChapterForm from '@/views/course/component/chapter/Form'
import chapterApi from '@/api/chapter'
import VideoForm from '@/views/course/component/Video/Form'
import videoApi from '@/api/video'
export default {
  components: {
    ChapterForm,
    VideoForm
  },
  data() {
    return {
      chapterList: []
    }
  },
  created() {
    if (this.$parent.courseId) {
      this.fetchNestedChapterList(this.$parent.courseId)
    }
  },
  methods: {
    // 嵌套章节列表
    fetchNestedChapterList(courseId) {
      console.log(courseId)
      chapterApi.nestedListByCourseId(courseId).then((response) => {
        // console.log(response)
        this.chapterList = response.data.items
      })
    },
    // 删除章节
    removeChapterById(chapterId) {
      this.$confirm('此操作将永久删除该章节, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return chapterApi.removeById(chapterId)
      }).then((response) => {
        this.fetchNestedChapterList(this.$parent.courseId)
        this.$message.success(response.message)
      }).catch((error) => {
        if (error === 'cancel') {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        }
      })
    },
    // 删除课时
    removeVideo(videoId) {
      this.$confirm('此操作将永久删除该课时, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return videoApi.removeById(videoId)
      }).then((response) => {
        this.fetchNestedChapterList(this.$parent.courseId)
        this.$message.success(response.message)
      }).catch((error) => {
        if (error === 'cancel') {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        }
      })
    },
    // 添加章节
    addChapter() {
      // 调用ChapterForm组件里open方法
      this.$refs.chapterForm.open()
    },
    // 更新章节
    editChapter(chapterId) {
      this.$refs.chapterForm.open(chapterId)
    },
    // 添加课时
    addVideo(chapterId) {
      this.$refs.videoForm.open(chapterId)
    },
    // 编辑课时
    editVideo(chapterId, videoId) {
      console.log('videoId', videoId)
      this.$refs.videoForm.open(chapterId, videoId)
    },
    // 该框架已经把父组件的信息封装到 $parent 里面 包括data
    // 上一步
    prev() {
      console.log(this)
      console.log(this.$parent._data.active)
      this.$parent.active = 0
    },
    // 下一步
    next() {
      console.log(this)
      this.$parent.active = 2
    }
  }
}
</script>

<style>
.chapterList{
  position: relative;
  list-style: none;
  margin: 0;
  padding: 0;
}
.chapterList li{
  position: relative;
}
.chapterList p{
  float: left;
  font-size: 20px;
  margin: 10px 0;
  padding: 10px;
  height: 70px;
  line-height: 50px;
  font-weight:bolder;
  width: 100%;
  border: 1px solid #DDD;
}
.chapterList .acts{
  float: right;
  font-size: 14px;
}
.videoList {
  /* padding 时 按钮无法点击 */
  padding-left: 50px;

}
.videoList p{
  float: left;
  font-size: 14px;
  margin: 10px 0;
  padding: 10px;
  height: 50px;

  line-height: 30px;
  width: 100%;
  border: 1px dashed #DDD;
}
</style>
