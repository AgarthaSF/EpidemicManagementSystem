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

      <a-menu-item key="/admin/staff-management">
        <router-link to="/admin/staff-management">人员管理</router-link>
      </a-menu-item>

      <a-menu-item key="/admin/access-todo">
        <router-link to="/admin/access-todo">访问申请审批</router-link>
      </a-menu-item>


      <a-menu-item key="/admin/daily-check-management">
        <router-link to="/admin/daily-check-management">每日打卡详情</router-link>
      </a-menu-item>

      <a-menu-item key="/admin/info-upload-management">
        <router-link to="/admin/info-upload-management">信息上报详情</router-link>
      </a-menu-item>

      <a-menu-item key="/admin/diagnosed">
        <router-link to="/admin/diagnosed">确诊病例</router-link>
      </a-menu-item>

      <a-menu-item key="/admin/anomaly-management">
        <router-link to="/admin/anomaly-management">风险管理</router-link>
      </a-menu-item>

      <a-menu-item key="/admin/epidemic-data-management">
        <router-link to="/admin/epidemic-data-management">疫情数据管理</router-link>
      </a-menu-item>

      <a-menu-item key="/admin/announcement">
        <router-link to="/admin/announcement">发布通知</router-link>
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
  name: 'the-header-admin',
  setup() {
    // 登陆后保存
    const user = computed(() => store.state.user);

    const router = useRouter();

    const logout = () => {

      // "/logout" + user.value.token

      axios.post("/logout", {
        headers:{
        'type': store.getters.getUser().type,
      }
      }).then((response) => {
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
