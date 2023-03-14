<template>
  <a-layout id="components-layout-demo-top-side-2">

    <a-layout>

      <the-header-un-login v-show="!user.type"/>
      <the-header-admin v-show="user.type === 'admin'"/>
      <the-header-student v-show="user.type === 'student'"/>
      <the-header-teacher v-show="user.type === 'teacher'"/>
      <the-header-foreigner v-show="user.type === 'foreigner'"/>
      <the-header-counselor v-show="user.type === 'counselor'"/>

      <router-view/>

      <the-footer></the-footer>

    </a-layout>
  </a-layout>
</template>


<script lang="ts">
import {computed, defineComponent} from 'vue';
import TheHeaderUnLogin from "@/components/header/the-header-unlogin.vue";
import TheHeaderAdmin from '@/components/header/the-header-admin.vue';
import TheHeaderStudent from '@/components/header/the-header-student.vue';
import TheHeaderTeacher from '@/components/header/the-header-teacher.vue';
import TheHeaderCounselor from '@/components/header/the-header-counselor.vue';
import TheHeaderForeigner from '@/components/header/the-header-foreigner.vue';
import TheFooter from "@/components/the-footer.vue";
import store from "@/store";

export default defineComponent({
  name: 'app',
  components: {
    TheHeaderAdmin,
    TheHeaderUnLogin,
    TheFooter,
    TheHeaderStudent,
    TheHeaderTeacher,
    TheHeaderCounselor,
    TheHeaderForeigner,
  },

  setup() {
    const user = computed(() => store.state.user);

    return {
      user,
    }
  }


});


</script>



