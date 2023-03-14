<template>
  <a-layout>

    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0}"
      >
        <a-menu-item>
          <MailOutlined/>
          <router-link to="/admin/access-todo">代办处理</router-link>
        </a-menu-item>
        <a-menu-item>
          <AreaChartOutlined/>
          <router-link to="/admin/access-processed">历史申请</router-link>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>

    <!--    表格-->
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">

      <p>
        <a-form layout="inline" :model="param">
          <a-form-item :style="{width: '90%'}">
            <a-input v-model:value="param.identity" placeholder="名称">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
              查询
            </a-button>
          </a-form-item>
        </a-form>
      </p>


      <a-table
          :columns="columns"
          :row-key="record => record.identity"
          :data-source="accessApplication"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >

        <template v-slot:action="{text, record}">
          <a-space size="small">
            <a-popconfirm
                title="确认通过？"
                ok-text="是"
                cancel-text="否"
                @confirm="processApp(record, 1)">
              <a-button type="primary" size="small">
                通过
              </a-button>
            </a-popconfirm>


            <a-popconfirm
                title="确认拒绝？"
                ok-text="是"
                cancel-text="否"
                @confirm="processApp(record, -1)">
              <a-button type="danger" size="small">
                拒绝
              </a-button>
            </a-popconfirm>

            <a-popconfirm
                title="确认删除？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.accessId)" >
              <a-button type="danger" size="small">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>

    </a-layout-content>
  </a-layout>



</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios'
import {message} from 'ant-design-vue';
import store from "@/store";


export default defineComponent({
  name: 'AdminAccessTodo',
  setup() {
    const param = ref();
    param.value = {};
    const accessApplication = ref();
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
        title: '人员编号',
        dataIndex: 'foreignerIdentity',
        align: 'center'
      },
      {
        title: '姓名',
        dataIndex: 'name',
        align: 'center'
      },
      {
        title: '手机号码',
        dataIndex: 'phoneNumber',
        align: 'center'
      },
      {
        title: '理由',
        dataIndex: 'reason',
        align: 'center'
      },
      {
        title: '开始时间',
        dataIndex: 'startDate',
        align: 'center'
      },
      {
        title: '过期时间',
        dataIndex: 'expirationDate',
        align: 'center'
      },
      {
        title: '申请日期',
        dataIndex: 'applicationDate',
        align: 'center'
      },

      {
        title: '审批',
        key: 'action',
        slots: {customRender: 'action'},
        align: 'center'
      },
    ];


    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      console.log("params: ", params);
      axios.get("/access-application/todo",{
        params: {
          page: params.page,
          size: params.size,
          identity: param.value.identity
        },
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          accessApplication.value = data.content.list;

          console.log("todo data list: ", data.content.list);

          console.log("accessApplication: ", accessApplication.value);
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


    const processApp = (record: any, applicationState : any) => {
      let current_user = record;
      current_user.applicationState = applicationState;
      axios.post("/access-application/save", current_user,{
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          // 重新加载列表
          handleQuery({
            // 此处的属性名需要和后端对应
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    const handleDelete = (id: number) => {
      axios.delete("/access-application/delete/" + id,{
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          // 重新加载列表
          handleQuery({
            // 此处的属性名需要和后端对应
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }
      })
    }


    onMounted(() => {

      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      })
    });

    return {
      param,
      accessApplication,
      pagination,
      columns,
      loading,

      handleTableChange,
      processApp,
      handleQuery,
      handleDelete,

    }
  }
});
</script>
