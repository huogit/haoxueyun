<template>

  <div class="app-container">
    <!-- 工具按钮 -->
    <div style="margin-bottom: 10px">
      <router-link to="/ad/type-create">
        <el-button v-if="hasPerm('ad-type.create')" type="primary" size="mini" icon="el-icon-edit">添加推荐位</el-button>
      </router-link>
    </div>
    <el-table
      :data="list"
      border
      stripe>
      <el-table-column
        type="index"
        width="50">
        <template slot-scope="scope">
          {{ page * 0 + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column
        prop="title"
        label="推荐位名称"/>
      <el-table-column label="是否展示">
        <template slot-scope="scope">
          {{ scope.row.isShow === 1? '是':'否' }}
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">
          <router-link :to="'/ad/type-edit/' + scope.row.id">
            <el-button
              v-if="hasPerm('ad-type.edit')"
              type="primary"
              size="mini"
              icon="el-icon-edit"
            >编辑</el-button>
          </router-link>

          <el-button
            v-if="hasPerm('ad-type.remove')"
            size="mini"
            type="danger"
            @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
      :page-size="limit"
      :total="total"
      style="padding: 30px 0; text-align: center;"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"/>
  </div>
</template>

<script>
import adTypeApi from '@/api/adType'

export default {
  data() {
    return {
      list: [],
      total: 0,
      page: 1,
      limit: 10 // 每页记录数
    }
  },

  // created() {
  //   this.fetchData()
  // },
  mounted() {
    if (this.hasPerm('ad-type.list')) {
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
      adTypeApi.pageList(this.page, this.limit).then((response) => {
        console.log(response)
        this.list = response.data.items
        this.total = response.data.total
      })
    },
    handleSizeChange(size) {
      this.limit = size
      this.fetchData()
    },
    handleCurrentChange(page) {
      this.page = page
      this.fetchData()
    },
    // 删除 推荐位
    handleDelete(id) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return adTypeApi.removeById(id)
      }).then((response) => {
        this.fetchData()
        this.$message.success(response.message)
      }).catch(error => {
        if (error === 'cancel') {
          this.$message.info('取消删除')
        }
      })
    }

  }
}
</script>
