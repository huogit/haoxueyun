<template>
  <div class="app-container">
    <!-- 输入表单 -->
    <el-form ref="form" v-model="adTypeForm" label-width="120px">
      <el-form-item label="推荐位名称">
        <el-input v-model="adTypeForm.title"/>
      </el-form-item>
      <el-form-item label="是否展示">
        <el-radio-group v-model="form.isShow">
          <el-radio :label="1" >是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate()">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import adTypeApi from '@/api/adType'
export default{
  data() {
    return {
      adTypeForm: {},
      saveBtnDisabled: false
    }
  },
  created() {
    if (this.$route.params.id) {
      this.fetchDataById(this.$route.params.id)
      this.adTypeForm.id = this.$route.params.id
    }
  },
  methods: {
    // 根据id查询记录
    fetchDataById(id) {
      adTypeApi.getById(id).then((response) => {
        this.adTypeForm = response.data.item
      })
    },
    // 根据id更新记录
    update() {
      // this.adForm.id = this.$route.params.id
      adTypeApi.updateById(this.adTypeForm).then((response) => {
        console.log(response)
        this.$message.success(response.message)
        this.saveBtnDisabled = true
        this.$router.push({ path: '/ad/type-list' })
      })
    },
    // 新增
    save() {
      adTypeApi.saveData(this.adTypeForm).then((response) => {
        this.$message.success(response.message)
        this.saveBtnDisabled = true
        this.$router.push({ path: '/ad/type-list' })
      })
    },
    // 保存事件
    saveOrUpdate() {
      if (this.adTypeForm.id) {
        this.update()
      } else {
        this.save()
      }
    }
  }
}
</script>
