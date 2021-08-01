import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import About from '../views/About.vue'
import AdminEbook from '../views/admin/admin-ebook.vue'
import AdminCategory from '../views/admin/admin-category.vue'
import AdminDoc from '../views/admin/admin-doc.vue'
import Doc from '../views/doc.vue'
import AdminUser from '../views/admin/admin-user.vue'
import {Tool} from "@/util/tool";
import store from "@/store";


const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home

  },
  {
    path: '/about',
    name: 'About',
    component:About

  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component:AdminEbook,
    meta:{
      loginRequire:true
    }
  } ,
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component:AdminCategory,
    meta:{
      loginRequire:true
    }
  } ,
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component:AdminDoc,
    meta:{
      loginRequire:true
    }

  } ,
  {
    path: '/doc',
    name: 'Doc',
    component: Doc

  },
  {
    path: '/admin/user',
    name: 'AdminUser',
    component:AdminUser,
    meta:{
      loginRequire:true
    }
  }
]


const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 要不要对meta.loginRequire属性做监控拦截
  if (to.matched.some(function (item) {
    console.log(item, "是否需要登录校验：", item.meta.loginRequire);
    return item.meta.loginRequire
  })) {
    const loginUser = store.state.user;
    if (Tool.isEmpty(loginUser)) {
      console.log("用户未登录！");
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
});

