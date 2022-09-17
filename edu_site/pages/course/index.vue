<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- 课程列表 开始 -->
    <section class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">全部课程</span>
        </h2>
      </header>
      <section class="c-sort-box">
        <section class="c-s-dl">
          <dl>
            <dt>
              <span class="c-999 fsize14">课程类别</span>
            </dt>
            <dd class="c-s-dl-li">
              <!-- 一级类别 开始-->
              <ul class="clearfix">
                <li
                  :class="{current:!$route.query.subjectParentId}">
                  <a
                    title="全部"
                    href="javascript:void(0);"
                    @click="searchSubjectLevelOne('')">全部</a>
                </li>
                <li
                  v-for="item in subjectNestedList"
                  :key="item.id"
                  :class="{current:$route.query.subjectParentId === item.id}">
                  <a
                    :title="item.title"
                    href="javascript:void(0);"
                    @click="searchSubjectLevelOne(item.id)">{{ item.title }}</a>
                </li>
                <!-- <li>
                  <a
                    title="前端开发"
                    href="javascript:void(0);">前端开发</a>
                </li> -->

              </ul>
              <!-- /一级类别 结束-->
            </dd>
          </dl>
          <dl>
            <dt>
              <span class="c-999 fsize14"/>
            </dt>
            <dd v-if="$route.query.subjectParentId" class="c-s-dl-li">
              <!-- 二级类别 开始-->
              <ul class="clearfix">
                <li :class="{current:!$route.query.subjectId}">
                  <a
                    title="全部"
                    href="javascript:void(0);"
                    @click="searchSubjectLevelTwo('')">全部</a>
                </li>
                <li
                  v-for="item in subjectList"
                  :key="item.id"
                  :class="{current:$route.query.subjectId === item.id}">
                  <a
                    :title="item.title"
                    href="javascript:void(0);"
                    @click="searchSubjectLevelTwo(item.id)">{{ item.title }}</a>
                </li>
                <!-- <li>
                  <a
                    title="Python"
                    href="javascript:void(0);">Python</a>
                </li> -->
              </ul>
              <!-- /二级类别 结束-->
            </dd>
          </dl>
          <div class="clear"/>
        </section>
        <div class="js-wrap">
          <section class="fr">
            <span class="c-ccc">
              <i class="c-master f-fM">1</i>/
              <i class="c-666 f-fM">1</i>
            </span>
          </section>
          <section class="fl">
            <!-- 排序 开始-->
            <ol class="js-tap clearfix">
              <li :class="{'current bg-green': $route.query.buyCountSort}">
                <a title="销量" href="javascript:void(0);" @click="searchBuyCount()">销量
                  <i>↓</i>
                </a>
              </li>
              <li :class="{'current bg-green': $route.query.gmtCreateSort}">
                <a title="最新" href="javascript:void(0);" @click="searchGmtCreate()">最新
                  <i>↓</i>
                </a>
              </li>
              <li :class="{'current bg-green': $route.query.priceSort}">
                <a v-if="$route.query.type == 2 " title="价格" href="javascript:void(0);" @click="searchPrice(1)">价格
                  <i>↓</i>
                </a>
                <!-- 这里全等 数据类型也要相同  -->
                <a v-if="!$route.query.type || $route.query.type == 1" title="价格" href="javascript:void(0);" @click="searchPrice(2)">价格
                  <i>↑</i>
                </a>
              </li>
            </ol>
            <!-- /排序 结束-->
          </section>
        </div>
        <div class="mt40">
          <!-- /无数据提示 开始-->
          <section v-if="courseList.length === 0" class="no-data-wrap">
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam">没有相关数据，小编正在努力整理中...</span>
          </section>
          <!-- /无数据提示 结束-->

          <!-- 数据列表 开始-->
          <article v-if="courseList.length >0 " class="comm-course-list">
            <ul id="bna" class="of">
              <li v-for="item in courseList" :key="item.id">
                <div class="cc-l-wrap">
                  <section class="course-img">
                    <img :alt="item.title" :src="item.cover" class="img-responsive">
                    <div class="cc-mask">
                      <a :href="'/course/'+item.id" title="开始学习" class="comm-btn c-btn-1">开始学习</a>
                    </div>
                  </section>
                  <h3 class="hLh30 txtOf mt10">
                    <a :href="'/course/'+item.id" :title="item.title" class="course-title fsize18 c-333">{{ item.title }}</a>
                  </h3>
                  <section class="mt10 hLh20 of">
                    <span v-if="item.price === 0" class="fr jgTag bg-green">
                      <i class="c-fff fsize12 f-fA">免费</i>
                    </span>
                    <span v-else class="fr jgTag ">
                      <i class="c-orange fsize12 f-fA"> ￥{{ item.price }}</i>
                    </span>
                    <span class="fl jgAttr c-ccc f-fA">
                      <i class="c-999 f-fA">{{ item.viewCount }}人学习</i>
                      |
                      <i class="c-999 f-fA">{{ item.buyCount }}人购买</i>
                    </span>
                  </section>
                </div>
              </li>
              <!-- <li>
                <div class="cc-l-wrap">
                  <section class="course-img">
                    <img alt="Linux集群教程" src="~/assets/photo/course/02.jpg" class="img-responsive">
                    <div class="cc-mask">
                      <a href="/course/1" title="开始学习" class="comm-btn c-btn-1">开始学习</a>
                    </div>
                  </section>
                  <h3 class="hLh30 txtOf mt10">
                    <a href="/course/1" title="Linux集群教程" class="course-title fsize18 c-333">Linux集群教程</a>
                  </h3>
                  <section class="mt10 hLh20 of">
                    <span class="fr jgTag ">
                      <i class="c-orange fsize12 f-fA"> ￥99</i>
                    </span>
                    <span class="fl jgAttr c-ccc f-fA">
                      <i class="c-999 f-fA">100人学习</i>
                      |
                      <i class="c-999 f-fA">100人购买</i>
                    </span>
                  </section>
                </div>
              </li> -->
            </ul>
            <div class="clear"/>
          </article>
          <!-- /数据列表 结束-->
        </div>
      </section>
    </section>
    <!-- /课程列表 结束 -->
  </div>
</template>

<script>
import courseApi from '~/api/course'
import subjectApi from '~/api/subject'
import querystring from 'querystring' // url参数拼接工具

export default {
  // async :异步
  async asyncData(page) { // 此处传递当前页面的上下文对象，以便获取查询条件
    // 组装查询参数
    const queryObj = {}
    // 从url地址栏中获取查询参数
    const query = page.route.query
    queryObj.subjectParentId = query.subjectParentId || ''
    queryObj.subjectId = query.subjectId || ''
    queryObj.buyCountSort = query.buyCountSort || ''
    queryObj.gmtCreateSort = query.gmtCreateSort || ''
    queryObj.priceSort = query.priceSort || ''
    queryObj.type = query.type || 1

    console.log('queryObj', queryObj)
    // 获取课程分类嵌套列表
    const subjectResponse = await subjectApi.nestedList()
    const subjectNestedList = subjectResponse.data.items

    let subjectList = []
    // 遍历一级分类
    for (let i = 0; i < subjectNestedList.length; i++) {
      // 如果查询参数中的一级分类id和当前一级分类id一致
      if (subjectNestedList[i].id === queryObj.subjectParentId) {
        // 则获取二级分类列表
        subjectList = subjectNestedList[i].children
      }
    }
    // 获取课程列表
    // const courseResponse = await courseApi.list(queryObj)
    const courseResponse = await courseApi.list(queryObj)
    const courseList = courseResponse.data.courseList

    return {
      // 合并到 data
      subjectNestedList, // 一级分类列表
      subjectList, // 二级分类列表
      courseList, // 课程列表
      queryObj// 查询参数
    }
  },
  methods: {
    // 一级分类查询参数
    searchSubjectLevelOne(subjectParentId) {
      // 自动组装queryString
      const obj = {
        subjectParentId: subjectParentId
      }
      const querys = querystring.stringify(obj)
      window.location = '/course?' + querys
    },
    // 二级分类查询参数
    searchSubjectLevelTwo(subjectId) {
      // 自动组装queryString
      const obj = {
        subjectParentId: this.queryObj.subjectParentId,
        subjectId: subjectId
      }
      const querys = querystring.stringify(obj)
      window.location = '/course?' + querys
    },

    // 选择按销量倒序
    searchBuyCount() {
      // 自动组装queryString
      const obj = {
        subjectParentId: this.queryObj.subjectParentId,
        subjectId: this.queryObj.subjectId,
        buyCountSort: 1
      }
      const querys = querystring.stringify(obj)
      window.location = '/course?' + querys
    },

    // 选择按创建时间倒序
    searchGmtCreate() {
      // 自动组装queryString
      const obj = {
        subjectParentId: this.queryObj.subjectParentId,
        subjectId: this.queryObj.subjectId,
        gmtCreateSort: 1
      }
      const querys = querystring.stringify(obj)
      window.location = '/course?' + querys
    },
    // 选择按价格升倒序
    searchPrice(type) {
      // 自动组装queryString
      const obj = {
        subjectParentId: this.queryObj.subjectParentId,
        subjectId: this.queryObj.subjectId,
        priceSort: 1,
        type: type
      }
      this.queryObj.type = type
      const querys = querystring.stringify(obj)
      window.location = '/course?' + querys
    }
  }
}
</script>
