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
          :row-key="record => record.infoId"
          :data-source="users"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >

      </a-table>

    </a-layout-content>
  </a-layout>

  <a-modal
      title="信息上报"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOK"
  >
    <a-form :model="current_upload" :label-col="{span: 6}" :wrapper-col="{span: 18}">

      <a-form-item label="上次核酸时间">
        <a-date-picker show-time placeholder="选择上次核酸时间"
                       v-model:value="current_upload.lastCovidDate" :style="{width: '100%'}"
                       format="YYYY-MM-DD HH:mm:ss"/>
      </a-form-item>

      <a-form-item label="疫苗接种次数">
        <a-input v-model:value="current_upload.vaccineCount"></a-input>
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
  name: 'StaffInfoUpload',
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
        title: '打卡编号',
        dataIndex: 'infoId',
        align: 'center'
      },
      {
        title: '上次核酸时间',
        dataIndex: 'lastCovidDate',
        align: 'center'
      },
      {
        title: '疫苗接种数量',
        dataIndex: 'vaccineCount',
        align: 'center'
      },
      {
        title: '上次核酸检测日期',
        dataIndex: 'lastCovidDate',
        align: 'center'
      },
      {
        title: '上报日期',
        dataIndex: 'infoDate',
        align: 'center'
      },

    ];

    const user = computed(() => store.state.user);

    const travelInfo = ref();
    travelInfo.value = [];

    const cityInfo = ref();
    cityInfo.value = [];

    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/info-upload/list", {
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
    const current_upload = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);


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

    const postData = () => {
      let userIdentity = store.getters.getUser().identity;
      current_upload.value.identity = userIdentity;

      if (current_upload.value.lastCovidDate == null) {
        message.error("请填写时间");
        modalLoading.value = false;
        return;
      }
      current_upload.value.infoDate = moment().locale('zh-cn').format('YYYY-MM-DD HH:mm:ss');
      current_upload.value.lastCovidDate = current_upload.value.lastCovidDate.locale('zh-cn').format('YYYY-MM-DD HH:mm:ss');

      axios.post("/info-upload/save", current_upload.value,{
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        const data = response.data;
        modalLoading.value = false;
        if (data.success) {
          postTravelInfo();
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


    const handleModalOK = () => {
      console.log("travelInfo: ", travelInfo.value);
      modalLoading.value = true;
      postData();
    };

    // 新增
    const add = () => {
      modalVisible.value = true;
      current_upload.value = {};
      travelInfo.value = [];
    }

    const getCityInfo = () => {
      axios.get("/city-epidemic/name",{
        headers:{
          'type': store.getters.getUser().type,
        }
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
      current_upload,
      travelInfo,
      cityInfo,

      handleModalOK,
      handleTableChange,
      add,
      handleQuery,
    }
  }
});
</script>
