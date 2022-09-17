<template>
  <div class="app-container">
    <el-input
      v-model="filterText"
      placeholder="输入关键字进行过滤"
      style="margin-bottom:30px;"
    />
    <el-tree
      ref="subjectTree"
      :data="subjectList"
      :props="defaultProps"
      :filter-node-method="filterNode"
      style="margin-top:10px;"
      @node-click="handleNodeClick"/>
  </div>
</template>

<script>
import subjectApi from '@/api/subject'
export default {
  data() {
    return {
      filterText: '', // 关键字过滤
      subjectList: [], // 数据列表
      defaultProps: { // 属性列表数据属性的key
        label: 'title',
        children: 'children'
      }
    }
  },
  // 监听器
  watch: {
    // 监听filterText属性
    filterText(keyword) {
      // 当filterText属性的值发生改变，触发该监听器
      this.$refs.subjectTree.filter(keyword)
    }
  },
  // created() {
  //   this.fetchNodeList()
  // },
  mounted() {
    if (this.hasPerm('message.list')) {
      this.fetchNodeList()
    } else {
      this.$message({
        type: 'warning',
        message: '您没有权限查看!'
      })
      history.go(-1)
    }
  },
  methods: {
    // 获取分类列表数据
    fetchNodeList() {
      subjectApi.getNestedTreeList().then((response) => {
        this.subjectList = response.data.items
      })
    },
    handleNodeClick() {
      console.log('handleNodeClick')
    },
    // 过滤节点
    filterNode(keyword, data) {
      if (!keyword) return true // 没关键字时，全部展示
      // return data.title.indexOf(keyword) !== -1 // 返回data里面title包含keyword的节点
      return data.title.toLowerCase().indexOf(keyword.toLowerCase()) !== -1// 忽略大小写
    }
  }
}
</script>
