<template>
  <a-layout-footer style="text-align: center">

    Jrt - WIKI <span v-show="user.id">，欢迎 {{user.name}}</span>
  </a-layout-footer>
</template>

<script lang="ts">
import {computed} from "vue";
import store from "@/store";


export default {
  name: "the-footer",
  setup() {
    const user = computed(() => store.state.user);
    return {
      user,
    }
  }
}
</script>