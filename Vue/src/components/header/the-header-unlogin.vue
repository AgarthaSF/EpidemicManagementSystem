<template>
  <a-layout-header class="header">
    <div class="logo"/>


    <a class="login-menu" @click="showLoginModal" v-show="!user.id">
      <span>登录</span>
    </a>

    <a class="register-menu" @click="showRegisterModal" v-show="!user.id">
      <span>外来人员注册</span>
    </a>

    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }">
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
    </a-menu>

    <a-modal
        title="登陆"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        @ok="login">
      <a-form :model="loginUser" :label-col="{span: 6}" :wrapper-col="{span: 18}">
        <a-form-item label="账号">
          <a-input v-model:value="loginUser.identity"/>
        </a-form-item>

        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password"/>
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal
        title="外来人员注册"
        v-model:visible="registerModalVisible"
        :confirm-loading="registerModalLoading"
        @ok="register">
      <a-form :model="registerUser" :label-col="{span: 6}" :wrapper-col="{span: 18}">

        <a-form-item label="姓名">
          <a-input v-model:value="registerUser.name"/>
        </a-form-item>

        <a-form-item label="住址">
          <a-input v-model:value="registerUser.address"/>
        </a-form-item>

        <a-form-item label="手机号码">
          <a-input v-model:value="registerUser.phoneNumber"/>
        </a-form-item>

        <a-form-item label="身份证号">
          <a-input v-model:value="registerUser.idCard"/>
        </a-form-item>

        <a-form-item label="密码">
          <a-input v-model:value="registerUser.password" type="password"/>
        </a-form-item>

      </a-form>
    </a-modal>

  </a-layout-header>

</template>


<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import axios from 'axios'
import {message} from "ant-design-vue";
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header-un-login',
  setup() {

    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);

    // 用户登陆
    const loginUser = ref({
      identity: "",
      password: ""
    });

    // 用户注册
    const registerUser = ref({
      name: "",
      address: "",
      password: "",
      phoneNumber: "",
      idCard: "",
    });

    // 登陆后保存
    const user = computed(() => store.state.user);


    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    const registerModalVisible = ref(false);
    const registerModalLoading = ref(false);

    const showRegisterModal = () => {
      registerModalVisible.value = true;
    };


    const login = () => {
      loginModalLoading.value = true;
      // loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post("/login", loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登陆成功");
          store.commit('setUser', data.content);
          loginUser.value.identity = "";
          loginUser.value.password = "";
        } else {
          message.error(data.message);
        }
      });
    };

    const register = () => {
      registerModalLoading.value = true;
      axios.post("/foreign-staff/register", registerUser.value).then((response) => {
        registerModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          registerModalLoading.value = false;
          message.success("注册成功");
          message.success(data.message);

          let currentUser = {
            "identity": data.content,
            "password": registerUser.value.password,
          };

          axios.post("/login", currentUser).then((response) => {
            const data = response.data;
            if (data.success) {
              message.success("登陆成功");
              store.commit('setUser', data.content);
              registerModalVisible.value = false;
            } else {
              message.error(data.message);
            }
          });

          registerUser.value.name = "";
          registerUser.value.address = "";
          registerUser.value.password = "";
          registerUser.value.phoneNumber = "";
          registerUser.value.idCard = "";
        } else {
          message.error(data.message);
        }
      });
    };


    return {
      loginUser,
      loginModalVisible,
      loginModalLoading,
      user,

      registerModalVisible,
      registerModalLoading,
      registerUser,


      login,
      register,
      showLoginModal,
      showRegisterModal,
    }

  }
});

</script>

<style>

.login-menu {
  float: right;
  color: white;
  padding-left: 30px;
}

.register-menu {
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
