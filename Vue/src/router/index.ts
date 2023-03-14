import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import store from "@/store";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/admin/announcement',
    name: 'AdminAnnouncement',
    component: () => import('../views/admin/admin-announcement.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'admin',
    }
  },
  {
    path: '/admin/staff-management',
    name: 'AdminStaffManagement',
    component: () => import('../views/admin/admin-staff-management.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'admin',
    }
  },
  {
    path: '/admin/access-management',
    name: 'AdminAccessManagement',
    component: () => import('../views/admin/admin-access-management.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'admin',
    }
  },
  {
    path: '/admin/daily-check-management',
    name: 'AdminDailyCheckManagement',
    component: () => import('../views/admin/admin-daily-check-management.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'admin',
    }
  },
  {
    path: '/admin/info-upload-management',
    name: 'AdminInfoUploadManagement',
    component: () => import('../views/admin/admin-info-upload-management.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'admin',
    }
  },
  {
    path: '/admin/diagnosed',
    name: 'AdminDiagnosed',
    component: () => import('../views/admin/admin-diagnosed.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'admin',
    }
  },
  {
    path: '/admin/access-todo',
    name: 'AdminAccessTodo',
    component: () => import('../views/admin/admin-access-todo.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'admin',
    }
  },
  {
    path: '/admin/access-processed',
    name: 'AdminAccessProcessed',
    component: () => import('../views/admin/admin-access-processed.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'admin',
    }
  },
  {
    path: '/admin/anomaly-management',
    name: 'AdminAnomalyManagement',
    component: () => import('../views/admin/admin-anomaly-management.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'admin',
    }
  },
  {
    path: '/admin/epidemic-data-management',
    name: 'AdminEpidemicDataManagementManagement',
    component: () => import('../views/admin/admin-epidemic-data-management.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'admin',
    }
  },
  {
    path: '/foreigner/access',
    name: 'ForeignerAccess',
    component: () => import('../views/foreigner/foreigner-access.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'foreigner',
    }
  },
  {
    path: '/staff/daily-check',
    name: 'StaffDailyCheck',
    component: () => import('../views/staff/staff-daily-check.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'staff',
    }
  },
  {
    path: '/staff/info-upload',
    name: 'StaffInfoUpload',
    component: () => import('../views/staff/staff-info-upload.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'staff',
    }
  },
  {
    path: '/student/out-application',
    name: 'StudentOutApplication',
    component: () => import('../views/student/student-out-application.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'student',
    }
  },
  {
    path: '/counselor/out-management',
    name: 'CounselorOutManagement',
    component: () => import('../views/counselor/counselor-out-management.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'counselor',
    }
  },
  {
    path: '/counselor/out-todo',
    name: 'CounselorOutTodo',
    component: () => import('../views/counselor/counselor-out-todo.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'counselor',
    }
  },
  {
    path: '/counselor/out-processed',
    name: 'CounselorOutProcessed',
    component: () => import('../views/counselor/counselor-out-processed.vue'),
    meta:{
      typeRequire: true,
      typeCondition: 'counselor',
    }
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由权限拦截
// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 要不要对meta.loginRequire属性做监控拦截

  let type : any;
  if (to.matched.some(function (item) {
    console.log(item, "是否需要权限校验：", item.meta.typeRequire);
    console.log(item, "权限类型：", item.meta.typeCondition);
    type = item.meta.typeCondition;
    return item.meta.typeRequire;
  })) {
    // const loginUser = store.state.user;
    const currentType = store.getters.getUser().type;
    if (type == 'staff') {
      if (currentType == 'student' || currentType == 'teacher') {
        next();
      } else {
        console.log("权限不匹配");
        next('/');
      }
    }else{
      if (store.getters.getUser().type == type) {
        next();
      }else{
        console.log("权限不匹配");
        next('/');
      }
    }
  }
 else {
    next();
  }
});

export default router
