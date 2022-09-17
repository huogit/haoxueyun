<template>
  <!-- 内容区域 -->
  <div class="bg-fa of">
    <section>
      <div class="u-body mt40">
        <menu class="col-3 fl uMenu">
          <header class="comm-title"><h2 class="fl tac"><span class="c-333">个人中心</span></h2></header>
          <dl>
            <!-- 切换子页面 -->
            <dd class="u-m-dd">
              <ul>
                <li>
                  <span>Wo的资料</span>
                  <ol>
                    <li>
                      <a href="/ucenter/person" title="">
                        基本资料
                      </a>
                    </li>
                    <li>
                      <a href="/ucenter/phone" title="">
                        更改手机号
                      </a>
                    </li>
                    <li>
                      <a href="/ucenter/password" title="">
                        密码设置
                      </a>
                    </li>
                  </ol>
                </li>
              </ul>
              <ul>
                <li>
                  <span>Wo的学习</span>
                  <ol>
                    <router-link to="/ucenter/order" tag="li" active-class="current" exact>
                      <a>我的订单</a>
                    </router-link>

                    <router-link to="/ucenter/collect" tag="li" active-class="current" exact>
                      <a>我的收藏</a>
                    </router-link>
                  </ol>
                </li>
              </ul>
              <!-- <ul>
                <li>
                  <span>Wo的问答</span>
                  <ol>
                    <li>
                      <a href="uc_question.html" title="">
                        Wo的提问
                      </a>
                    </li>
                  </ol>
                  <ol>
                    <li>
                      <a href="uc_anwser.html" title="">
                        Wo的回答
                      </a>
                    </li>
                  </ol>
                </li>
              </ul> -->

              <ul>
                <li>
                  <span>Wo的消息</span>
                  <ol>
                    <li>
                      <a href="/ucenter/message" title="">系统消息</a>
                    </li>
                  </ol>
                </li>
              </ul>
            </dd>
          </dl>
        </menu>
        <!-- 子页面组件的占位符 -->
        <nuxt-child />
        <div class="clear"/>
      </div>
    </section>
  </div>
  <!-- /内容区域 -->
</template>

<script>
import cookie from 'js-cookie'

export default {
  mounted() {
    if (!cookie.get('edu_jwt_token')) {
      window.location.href = '/login'
    }
  }
}
</script>
