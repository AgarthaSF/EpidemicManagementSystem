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
      <a-form-item label="外出地点" :style="{width: '100%'}">
        <a-select
            v-model:value="selecteAddress"
            show-search
            placeholder="选择外出地点"
            :default-active-first-option="false"
            :show-arrow="false"
            :filter-option="false"
            :not-found-content="null"
            :options="selectData"
            @search="handleSearch"
            @change="handleChange"
            :style="{width: '100%'}"
        >
        </a-select>


      </a-form-item>
      <a-form-item label="外出时间">
        <a-date-picker show-time placeholder="选择外出时间"
                       v-model:value="current_application.outTime" :style="{width: '100%'}"
                       format="YYYY-MM-DD HH:mm:ss"/>
      </a-form-item>
      <a-form-item label="返回时间">
        <a-date-picker show-time placeholder="选择返回时间"
                       v-model:value="current_application.backTime" :style="{width: '100%'}"
                       format="YYYY-MM-DD HH:mm:ss"/>
      </a-form-item>

      <a-form-item label="申请理由">
        <a-textarea v-model:value="current_application.reason" placeholder="请输入申请理由" :rows="6"/>
      </a-form-item>


    </a-form>
  </a-modal>


</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref} from 'vue';
import axios from 'axios'
import {message} from 'ant-design-vue';
import store from "@/store";
import moment from "moment-timezone";

export default defineComponent({
  name: 'StudentOutApplication',
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
        dataIndex: 'applicationId',
        align: 'center'
      },
      {
        title: '外出地点',
        dataIndex: 'addressName',
        align: 'center'
      },
      {
        title: '申请理由',
        dataIndex: 'reason',
        align: 'center'
      },
      {
        title: '外出时间',
        dataIndex: 'outTime',
        align: 'center'
      },
      {
        title: '返回时间',
        dataIndex: 'backTime',
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
      axios.get("/out-application/list", {
        params: {
          page: params.page,
          size: params.size,
          identity: store.getters.getUser().identity,
        },
        headers:{
          'type': store.getters.getUser().type,
        }
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

    const getUserInfo = () => {
      let userIdentity = store.getters.getUser().identity;
      axios.get("/school-staff/list", {
        params: {
          page: 1,
          size: pagination.value.pageSize,
          identity: userIdentity,
        },
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        const userData = response.data;
        if (userData.success) {

          console.log("user data: ", userData);
          let dataList = userData.content.list[0];
          // let moment = require('moment-timezone');
          // let tz = 'Asia/Shanghai'  //时区
          current_application.value.name = dataList.name;
          current_application.value.phoneNumber = dataList.phoneNumber;
          // current_application.value.outTime = moment.utc(current_application.value.outTime).tz(tz).format('YYYY-MM-DD HH:mm:ss');
          // current_application.value.backTime = moment.utc(current_application.value.backTime).tz(tz).format('YYYY-MM-DD HH:mm:ss');
          // current_application.value.applicationDate = moment().locale('zh-cn').format('YYYY-MM-DD HH:mm:ss');
        } else {
          message.error(userData.message);
        }
      })
    }

    const postData = () => {
      let userIdentity = store.getters.getUser().identity;
      current_application.value.studentIdentity = userIdentity;
      current_application.value.applicationState = 0;

      let moment = require('moment-timezone');
      let tz = 'Asia/Shanghai'  //时区
      current_application.value.outTime = moment.utc(current_application.value.outTime).tz(tz).format('YYYY-MM-DD HH:mm:ss');
      current_application.value.backTime = moment.utc(current_application.value.backTime).tz(tz).format('YYYY-MM-DD HH:mm:ss');
      current_application.value.applicationDate = moment().locale('zh-cn').format('YYYY-MM-DD HH:mm:ss');

      const outApplication = {
        studentIdentity: store.getters.getUser().identity,
        reason: current_application.value.reason,
        outTime: moment.utc(current_application.value.outTime).tz(tz).format('YYYY-MM-DD HH:mm:ss'),
        backTime: moment.utc(current_application.value.backTime).tz(tz).format('YYYY-MM-DD HH:mm:ss'),
        applicationDate: moment().locale('zh-cn').format('YYYY-MM-DD HH:mm:ss'),
        applicationState: 0,
      }

      current_application.value.outApplication = outApplication;

      axios.post("/out-application/insert", current_application.value,{
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        const data = response.data;
        modalLoading.value = false;
        if (data.success) {
          modalVisible.value = false;
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

    const selectData = ref<any[]>([]);
    const selecteAddress = ref();
    let timeout: any;
    let currentValue = '';

    function fetch(value: string, callback: any) {
      if (timeout) {
        clearTimeout(timeout);
        timeout = null;
      }
      currentValue = value;

      function fake() {
        axios.get("https://restapi.amap.com/v5/place/text", {
          params: {
            key: '',
            keywords: value,
          }
        }).then((response) => {
          if (currentValue === value) {
            const result = response.data.pois;
            const selectData: any[] = [];
            for (let i = 0; i < result.length; i++) {
              selectData.push({
                value: result[i].name,
                label: result[i].name,
              })
            }
            console.log("selectData processed", selectData);
            callback(selectData);
          }
        });
      }

      timeout = setTimeout(fake, 300);
    }

    const handleSearch = (val: string) => {
      fetch(val, (d: any[]) => (selectData.value = d));
    };
    const handleChange = (val: string) => {
      selecteAddress.value = val;
      fetch(val, (d: any[]) => (selectData.value = d));
    };

    const handleModalOK = () => {
      // 首先根据当前选中的地区详情查询具体地址信息
      axios.get("https://restapi.amap.com/v5/place/text", {
        params: {
          key: 'f6f43d7b8324f2f24402c5fd201ab78b',
          keywords: selecteAddress.value,
        }
      }).then((response) => {
        const result = response.data.pois;
        console.log("result: ", result);
        const poi = result[0];
        console.log("POI: ", poi);

        console.log("POI location: ", poi.location);

        let position = poi.location;
        console.log("position: ", position);
        let coordinate = position.split(',');

        const address = {
          addressName: poi.name,
          areaName: poi.adname,
          cityName: poi.cityname,
          longitude: coordinate[0],
          latitude: coordinate[1],
          addressPosition: poi.address,
        }
        current_application.value.address = address;
        postData();
      });

      modalLoading.value = true;
    };

    // 新增
    const add = () => {
      modalVisible.value = true;
      current_application.value = {};
      // current_application.value.outTime = "";
      // current_application.value.backTime = "";
      selecteAddress.value = "";
      getUserInfo();
    }




    onMounted(() => {

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
      selectData,
      selecteAddress,

      modalVisible,
      modalLoading,
      current_application,

      handleModalOK,
      handleTableChange,
      add,
      handleQuery,
      handleSearch,
      handleChange,
    }
  }
});
</script>
