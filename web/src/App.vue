<template>
  <a-layout id="components-layout-demo-top-side-2" class="ant-layout">
    <the-header/>
    <router-view/>
    <the-footer/>
  </a-layout>
</template>


<script lang="ts">
import { defineComponent } from 'vue';

import {Layout} from 'ant-design-vue';
import TheHeader from '@/components/the-header.vue';
import TheFooter from '@/components/the-footer.vue';
export default defineComponent({
  name: 'app',
  components:{
    aLayout:Layout,
    aLayoutContent:Layout.Content,
    TheHeader,
    TheFooter,
  },
});
</script>


<style>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>
