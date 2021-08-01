<template>
  <a-layout-header class="header">
    <div class="logo"/>
    <a-menu
        v-model:selectedKeys="selectedKeys1"
        :style="{ lineHeight: '64px' }"
        mode="horizontal"
        theme="dark"
    >
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/ebook">电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="/About">
        <router-link to="/About">关于我们</router-link>
      </a-menu-item>

      <a-popconfirm
          title="确认退出登录?"
          ok-text="是"
          cancel-text="否"
          @confirm="logout()"
      >
        <a class="login-menu" v-show="user.id" >
          <span>退出登录</span>
        </a>
      </a-popconfirm>


      <a class="login-menu" v-show="user.id">
        <span>你好，{{ user.name }}</span>
      </a>

      <a class="login-menu" @click="showLoginModal" v-show="!user.id">
        <span>登录</span>
      </a>
    </a-menu>
  </a-layout-header>

  <a-modal
      v-model:visible="loginModalVisible"
      :confirm-loading="loginModalLoading"
      title="登录"
      @ok="login"
  >
    <a-form :label-col="{ span: 6 }" :model="loginUser" :wrapper-col="{ span: 18 }">
      <a-form-item label="登录名">
        <a-input v-model:value="loginUser.loginName"/>
      </a-form-item>
      <a-form-item label="密码">
        <a-input v-model:value="loginUser.password" type="password"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>


<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import axios from 'axios'
import {message} from 'ant-design-vue'
import store from '@/store';

declare let hexMd5: any
declare let KEY: any

export default defineComponent({
  name: 'the-header',
  setup() {

    const user=computed(()=> store.state.user);

    const loginUser = ref({
      loginName: "t2",
      password: "test",
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      console.log("showLoginModal");
      loginModalVisible.value = true;
    };

    // 登录
    const login = () => {
      console.log("开始登录");

      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功！");
          console.log(data.content);
          console.log(user.value);
          store.commit("setUser",data.content);
          console.log(user);
        } else {
          message.error(data.message);
        }
      });
    };

    // 退出登录
    const logout = () => {
      console.log("退出登录");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.get('/user/logout/'+ user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("退出登录成功！");
          store.commit("setUser",{})
        } else {
          message.error(data.message);
        }
      });
    };

    return {
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,
      logout,
      user,
    }
  }
});
</script>

<style>
.login-menu {
  float: right !important ;
  color: white;
  margin-right: 20px;
}
</style>