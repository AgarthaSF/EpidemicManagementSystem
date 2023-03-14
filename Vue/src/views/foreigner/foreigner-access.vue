<template>
  <a-layout>

    <a-layout-content
        :style="{ background: '#fff', padding: '10px', margin: 0, minHeight: '280px' }">

      <p>
        <a-form layout="inline" :style="{float: 'right', marginRight: '85px', marginBottom: '15px'}">
          <a-button type="primary" @click="add()">
            新增
          </a-button>
        </a-form>
      </p>

      <a-table
          :columns="columns"
          :row-key="record => record.identity"
          :data-source="users"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >

        <template v-slot:action="{text, record}">
          <a-tag color="#f50" v-show="record.applicationState===-1">申请未通过</a-tag>
          <a-tag color="#87d068" v-show="record.applicationState===1">申请已通过</a-tag>
          <a-tag color="cyan" v-show="record.applicationState===0">申请待处理</a-tag>
        </template>

      </a-table>

    </a-layout-content>
  </a-layout>

  <a-modal
      title="新增申请"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOK"
  >
    <a-form :model="current_application" :label-col="{span: 6}" :wrapper-col="{span: 18}">
      <a-form-item label="申请理由">
        <a-textarea v-model:value="current_application.reason" placeholder="请输入申请理由" :rows="6" />
      </a-form-item>

      <a-form-item label="14日行程轨迹">
        <a-select
            v-model:value="travelInfo"
            mode="multiple"
            style="width: 100%"
            placeholder="选择14日旅居史"
            :options="cityInfo"
        >
        </a-select>
      </a-form-item>

      <a-form-item label="开始时间">
        <a-date-picker show-time placeholder="选择访问开始时间"
                       v-model:value="current_application.startDate" :style="{width: '100%'}" format="YYYY-MM-DD HH:mm:ss"/>
      </a-form-item>
      <a-form-item label="结束时间">
        <a-date-picker show-time placeholder="选择访问结束时间"
                       v-model:value="current_application.expirationDate" :style="{width: '100%'}" format="YYYY-MM-DD HH:mm:ss"/>
      </a-form-item>


    </a-form>
  </a-modal>


</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref} from 'vue';
import axios from 'axios'
import {message} from 'ant-design-vue';
import store from "@/store";
import moment from 'moment';

export default defineComponent({
  name: 'ForeignerAccess',
  setup() {
    const users = ref();
    const loading = ref(false);

    const pagination = ref({
      current: 1,
      pageSize: 8,
      total: 0
    });


    const columns = [
      {
        title: '申请编号',
        dataIndex: 'accessId',
        align: 'center'
      },
      {
        title: '申请理由',
        dataIndex: 'reason',
        align: 'center'
      },
      {
        title: '开始时间',
        dataIndex: 'startDate',
        align: 'center'
      },
      {
        title: '结束时间',
        dataIndex: 'expirationDate',
        align: 'center'
      },
      {
        title: '申请时间',
        dataIndex: 'applicationDate',
        align: 'center'
      },
      {
        title: '申请状态',
        key: 'action',
        slots: {customRender: 'action'},
        align: 'center'
      }
    ];

    const user = computed(() => store.state.user);

    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/access-application/list", {
        params: {
          page: params.page,
          size: params.size,
          identity: store.getters.getUser().identity,
        },
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          users.value = data.content.list;
          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }
      });
    };

    // 表格点击页码时触发
    const handleTableChange = (pagination: any) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize,
      });
    };


    // 表单
    const current_application = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    const getUserInfo= () =>{
      let userIdentity = store.getters.getUser().identity;
      axios.get("/foreign-staff/list", {
        params: {
          page: 1,
          size: pagination.value.pageSize,
          identity: userIdentity,
        },
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        const userData = response.data;
        if(userData.success){
          let dataList = userData.content.list[0];
          console.log("dataList: ", dataList);
          current_application.value.name = dataList.name;
          current_application.value.phoneNumber = dataList.phoneNumber;
        }else{
          message.error(userData.message);
        }
      })
    }

    const postData = ()=>{
      let userIdentity = store.getters.getUser().identity;
      current_application.value.foreignerIdentity = userIdentity;
      current_application.value.applicationState = 0;

      let moment = require('moment-timezone');
      let tz = 'Asia/Shanghai'  //时区
      current_application.value.startDate       =   moment.utc(current_application.value.startDate).tz(tz).format('YYYY-MM-DD HH:mm:ss');
      current_application.value.expirationDate  =   moment.utc(current_application.value.expirationDate).tz(tz).format('YYYY-MM-DD HH:mm:ss');
      current_application.value.applicationDate =   moment().locale('zh-cn').format('YYYY-MM-DD HH:mm:ss');
      // console.log(moment().locale('zh-cn').format('YYYY-MM-DD HH:mm:ss'));

      axios.post("/access-application/save", current_application.value,{
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        const data = response.data;
        modalLoading.value = false;
        if (data.success) {
          modalVisible.value = false;
          postTravelInfo();
          handleQuery({
            // 此处的属性名需要和后端对应
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    }

    const travelInfo = ref();
    travelInfo.value = [];

    const cityInfo = ref();
    cityInfo.value = [];

    const postTravelInfo = () => {
      const travelReq = {
        identity: store.getters.getUser().identity,
        cityList: travelInfo.value,
      };
      axios.post("/travel-info/save", travelReq,{
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        const data = response.data;
        if (!data.success) {
          message.error(data.message);
        }
      })
    }

    const getCityInfo = () => {
      axios.get("/city-epidemic/name",{
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          const cityList = data.content;
          cityInfo.value = [];
          for (let i = 0; i < data.content.length; i++) {
            cityInfo.value.push({
              value: cityList[i],
            })
          }
          console.log("cityInfo: ", cityInfo.value);
        } else {
          message.error(data.message);
        }
      })
    }

    const handleModalOK = () => {
      modalLoading.value = true;
      postData();
    };

    // 新增
    const add = () => {
      modalVisible.value = true;
      current_application.value = {};
      travelInfo.value = [];
      getUserInfo();
    }

    onMounted(() => {
      getCityInfo();
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      })
    });

    return {
      user,
      users,
      pagination,
      columns,
      loading,

      modalVisible,
      modalLoading,
      current_application,
      cityInfo,
      travelInfo,

      handleModalOK,
      handleTableChange,
      add,
      handleQuery,
    }
  }
});
</script>
