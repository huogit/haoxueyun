<template>
  <!--普通组件用 命名第一个字母大写-->
  <div id="app-container">
    <!-- 添加和修改章节表单 -->
    <el-dialog :visible="dialogFormVisible" title="添加章节" @close="close()">
      <el-form :model="chapter">
        <el-form-item label="章节标题" label-width="120px">
          <el-input v-model="chapter.title" />
        </el-form-item>
        <el-form-item label-width="120px" label="章节排序">
          <el-input-number v-model="chapter.sort" :min="0"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="close()">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import chapterApi from '@/api/chapter'
export default {
  data() {
    return {
      chapter: {
        sort: 0
      },
      dialogFormVisible: false
    }
  },
  methods: {
    save() {
      this.chapter.courseId = this.$parent.$parent.courseId
      chapterApi.save(this.chapter).then((response) => {
        this.$message.success(response.message)
        // 关闭组件
        this.close()
        // 刷新组件
        this.$parent.fetchNestedChapterList(this.$parent.$parent.courseId)
      })
    },
    // 根据chapterId获取章节
    fetchChapter(chapterId) {
      chapterApi.getById(chapterId).then((response) => {
        this.chapter = response.data.item
      })
    },
    // 更新章节
    update() {
      chapterApi.update(this.chapter).then((response) => {
        this.$message.success(response.message)
        // 关闭组件
        this.close()
        // 刷新组件
        this.$parent.fetchNestedChapterList(this.$parent.$parent.courseId)
      })
    },
    open(chapterId) {
      this.dialogFormVisible = true
      // console.log('chapterId', chapterId)
      if (chapterId) {
        this.fetchChapter(chapterId)
      }
    },
    // 重置表单
    resetForm() {
      this.chapter = {
        sort: 0
      }
    },
    close() {
      this.dialogFormVisible = false
      // 重置表单
      this.resetForm()
    },
    saveOrUpdate() {
      if (!this.chapter.id) {
        this.save()
      } else {
        this.update()
      }
    }
  }
}
</script>
