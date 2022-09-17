<template>
  <div class="app-container">
    <div id="register_num1" name="register_num" class="chart" style="height:500;"/>
    <el-form :inline="true" class="demo-form-inline" >
      <el-form-item label="开始日期">
        <el-date-picker
          v-model="searchObj.begin"
          type="date"
          placeholder="选择开始日期"
          value-format="yyyy-MM-dd"/>
      </el-form-item>
      <el-form-item label="截止日期">
        <el-date-picker
          v-model="searchObj.end"
          placeholder="选择截止日期"
          value-format="yyyy-MM-dd"/>
      </el-form-item>
      <el-button
        :disabled="btnDisabled"
        size="norm"
        type="primary"
        icon="el-icon-search"
        @click="showChart()">查询</el-button>
    </el-form>
    <el-row>
      <el-col :span="12">
        <div id="register_num" name="register_num" class="chart" style="height:500;"/>
      </el-col>
      <el-col :span="12">
        <div id="login_num" name="login_num" class="chart" style="height:500;"/>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <div id="video_num" name="video_num" class="chart" style="height:500;"/>
      </el-col>
      <el-col :span="12">
        <div id="course_num" name="course_num" class="chart" style="height:500;"/>
      </el-col>
    </el-row>
  </div>

</template>

<script>
import statisticsApi from '@/api/statistics'
import echarts from 'echarts'

export default {

  data() {
    return {
      searchObj: {
        begin: '',
        end: ''
      },
      btnDisabled: false,
      chartData: {
        registerNum: '',
        loginNum: '',
        videoViewNum: '',
        courseNum: ''
      }
    }
  },

  methods: {
    showChart() {
      statisticsApi.getData(this.searchObj).then((response) => {
        this.chartData = response.data.chartData
        this.showChartByType('register_num', '学员登录数统计', this.chartData.registerNum)
        this.showChartByType('login_num', '学员注册数统计', this.chartData.loginNum)
        this.showChartByType('video_view_num', '课程播放数统计', this.chartData.videoViewNum)
        this.showChartByType('course_num', '每日课程新增人数', this.chartData.courseNum)
      })
    },
    showChartByType(type, title, data) {
      // 基于准备好的dom ，初始化echart实例
      var myChart = echarts.init(document.getElementById('register_num'))
      var option = {
        title: {
          text: title
        },
        xAxis: {
          data: data.xData
        },
        yAxis: {
        },
        series: [{
          type: 'line',
          data: data.yData
        }]
      }

      myChart.setOption(option)
    }

  }
}
</script>

