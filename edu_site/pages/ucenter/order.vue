<template>
  <article class="col-7 fl">
    <div class="u-r-cont">
      <section>
        <div>
          <section class="c-infor-tabTitle c-tab-title">
            <a href="javascript: void(0)" title="我的订单" class="current">
              订单列表
            </a>
          </section>
        </div>
        <div class="mt40">
          <section v-if="orderList.length === 0" class="no-data-wrap">
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam" >您还没有订单哦！</span>
          </section>
      </div></section>

      <!-- 表格 -->
      <el-table
        v-if="orderList.length>0"
        :data="orderList"
        border
        fit
        highlight-current-row>
        <el-table-column label="课程封面" align="center" >
          <template slot-scope="scope">
            <div class="info" >
              <div class="pic">
                <img :src="scope.row.courseCover" alt="scope.row.courseTitle" width="150px">
              </div>
          </div></template>
        </el-table-column>
        <el-table-column label="课程标题" align="center" >
          <template slot-scope="scope">
            <div class="info" >
              <div class="title">
                <a :href="'/course/'+scope.row.courseId">{{ scope.row.courseTitle }}</a>
              </div>
          </div></template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope" >
            <router-link v-if="scope.row.status === 0" :to="'/order/'+scope.row.id">
              <el-button type="text" size="mini" icon="el-icon-edit" @click="toPay(scop.row.orderId)">去支付</el-button>
            </router-link>

            <router-link v-if="scope.row.status === 1" :to="'/course/'+scope.row.courseId">
              <el-button type="text" size="mini" icon="el-icon-edit">去学习</el-button>
            </router-link>

            <i
              class="el-icon-delete"
              style="cursor:pointer"
              title="删除订单"
              @click="removeById(scope.row.id)"/>
          </template>
        </el-table-column>

      </el-table>
    </div>
  </article>
</template>

<script>
import orderApi from '~/api/order'
import cookie from 'js-cookie'

export default {
  data() {
    return {
      orderList: []
    }
  },
  created() {
    if (cookie.get('edu_jwt_token')) {
      this.fetchOrderList()
    }
  },
  methods: {
    fetchOrderList() {
      orderApi.getList().then((response) => {
        this.orderList = response.data.items
      })
    },
    toPay(orderId) {
      this.$router.push({ path: '/order/' + orderId })
    },
    // 根据id删除数据
    removeById(id) {
      this.$confirm('此操作将永久删除该该订单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return orderApi.removeById(id)
      }).then(() => {
        this.fetchOrderList()
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      }).catch((error) => {
        if (error === 'cancel') {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        }
      })
    }
  }
}
</script>
