<template>
  <a-layout-header class="header">
    <div class="logo"/>

    <a class="logout-menu">
      <a-popconfirm
          title="确认退出"
          ok-text="是"
          cancel-text="否"
          @confirm="logout()">
        <a href="#">退出登陆</a>
      </a-popconfirm>
    </a>

    <a class="logout-menu">
      <span>你好，{{user.identity}}</span>
    </a>



    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }">

      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>

      <a-menu-item key="/student/out-application">
        <router-link to="/student/out-application">外出申请</router-link>
      </a-menu-item>

      <a-menu-item key="/staff/daily-check">
        <router-link to="/staff/daily-check">每日打卡</router-link>
      </a-menu-item>

      <a-menu-item key="/staff/info-upload">
        <router-link to="/staff/info-upload">信息上报</router-link>
      </a-menu-item>

    </a-menu>


  </a-layout-header>

</template>


<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import axios from 'axios'
import {message} from "ant-design-vue";
import store from "@/store";
import {useRoute, useRouter} from 'vue-router';

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header-student',
  setup() {
    // 登陆后保存
    const user = computed(() => store.state.user);

    const router = useRouter();

    const logout = () => {

      // "/logout" + user.value.token

      axios.post("/logout", {
        headers:{
          'type': store.getters.getUser().type,
        }}).then((response) => {
        const data = response.data;
        if(data.success){
          message.success("已退出登陆");
          store.commit('setUser', {});
          router.push({ path: "/" });
        }else{
          message.error(data.message);
        }
      });
    };

    return {
      user,
      logout,
    }

  }
});

</script>

<style>

.logout-menu {
  float: right;
  color: white;
  padding-left: 30px;
}

.logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}
</style>
