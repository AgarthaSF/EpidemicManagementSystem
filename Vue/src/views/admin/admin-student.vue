<template>
  <a-layout>


    <!--    表格-->
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <!--      查询与新增-->
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item :style="{width: '80%'}">
            <a-input v-model:value="param.identity" placeholder="名称">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">
              新增
            </a-button>
          </a-form-item>
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

        <template v-slot:counselor="{text, record}">
          <span>{{ (record.counselorName) }}</span>
        </template>


        <template v-slot:action="{text, record}">
          <a-space size="small">
            <a-button type="primary" @click="resetPassword(record)">
              重置密码
            </a-button>
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="确认删除？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.identity)" >
              <a-button type="danger" >
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>


    </a-layout-content>
  </a-layout>

  <!--  人员信息编辑-->
  <a-modal
      title="编辑"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOK"
  >

    <a-form :model="current_user" :label-col="{span: 6}" :wrapper-col="{span: 18}">
      <a-form-item label="编号">
        <a-input v-model:value="current_user.identity" :disabled="current_user.isStudent"/>
      </a-form-item>
      <a-form-item label="姓名">
        <a-input v-model:value="current_user.name" />
      </a-form-item>
      <a-form-item label="密码" v-show="!current_user.isStudent">
        <a-input-password v-model:value="current_user.password"/>
      </a-form-item>
      <a-form-item label="地址">
        <a-input v-model:value="current_user.address"/>
      </a-form-item>
      <a-form-item label="手机号码">
        <a-input v-model:value="current_user.phoneNumber"/>
      </a-form-item>
      <a-form-item label="身份证号">
        <a-input v-model:value="current_user.idCard" />
      </a-form-item>
      <a-form-item label="辅导员">

        <a-select ref="select"
                  v-model:value="currentCounselor">
          <a-select-option v-for="c in counselors"
                           :key="c.counselorIdentity"
                           :value="c.counselorIdentity">
            {{c.name}}
          </a-select-option>

        </a-select>

      </a-form-item>
    </a-form>
  </a-modal>

  <!--  重置密码-->
  <a-modal
      title="重置密码"
      v-model:visible="resetModalVisible"
      :confirm-loading="resetModalLoading"
      @ok="handleResetModalOK">
    <a-form :model="current_user" :label-col="{span: 6}" :wrapper-col="{span: 18}">
      <a-form-item label="新密码">
        <a-input-password v-model:value="current_user.password" :typeof="password"/>
      </a-form-item>
    </a-form>
  </a-modal>


</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios'
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool"
import store from "@/store";

const md5 = require('@../../../public/js/md5.js')
declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'AdminStudent',
  setup() {
    const param = ref();
    param.value = {};
    const users = ref();
    const loading = ref(false);

    const pagination = ref({
      current: 1,
      pageSize: 7,
      total: 0
    });


    const columns = [
      {
        title: '编号',
        dataIndex: 'identity',
        align: 'center'
      },
      {
        title: '姓名',
        dataIndex: 'name',
        align: 'center'
      },
      {
        title: '地址',
        dataIndex: 'address',
        align: 'center'
      },
      {
        title: '手机号码',
        dataIndex: 'phoneNumber',
        align: 'center'
      },
      {
        title: '身份证号',
        dataIndex: 'idCard',
        align: 'center'
      },
      {
        title: '辅导员',
        slots: {customRender: 'counselor'},
        align: 'center'
      },
      {
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'},
        align: 'center'
      }
    ];


    // 数据查询
    const handleQuery = (params: any) => {
      loading.value = true;
      console.log("params: ", params);
      axios.get("/school-staff/student/list",{
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
          users.value = data.content.list;
          console.log("users: ", users.value);

          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;

          // 如果查到的数据条数为0，且当前页数不为1，则说明删除完数据后该页面为空，将页面置为当前页的上一页
          if(users.value.length == 0 && params.page != 1){
            handleQuery({
              page: params.page-1,
              size: pagination.value.pageSize,
            })
          }

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
    const current_user = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    const handleModalOK = () => {
      modalLoading.value = true;

      // user.value.password = md5.hexMd5(user.value.password + md5.KEY);
      // current_user.value.password = hexMd5(current_user.value.password + KEY);

      current_user.value.counselorIdentity = currentCounselor.value;

      axios.post("school-staff/student/save", current_user.value,{
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        const data = response.data;
        modalLoading.value = false;
        if (data.success) {
          modalVisible.value = false;
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

    // 编辑
    const edit = (record: any) => {
      modalVisible.value = true;
      current_user.value = Tool.copy(record);
      console.log("current_user value: ", current_user.value.counselorIdentity);
      currentCounselor.value = current_user.value.counselorIdentity;
    }

    // 新增
    const add = () => {
      modalVisible.value = true;
      current_user.value = {};
      currentCounselor.value = "";
    }

    const handleDelete = (id: number) => {
      axios.delete("/account/delete/" + id,{
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

    const resetModalVisible = ref(false);
    const resetModalLoading = ref(false);

    const resetPassword = (record: any) => {
      resetModalVisible.value = true;
      current_user.value = Tool.copy(record);
      current_user.value.password = null;
    }

    const handleResetModalOK = () => {
      resetModalLoading.value = true;
      // user.value.password = md5.hexMd5(user.value.password + md5.KEY);
      // current_user.value.password = hexMd5(current_user.value.password + KEY);

      axios.post("/account/reset-password", current_user.value,{
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        resetModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          message.success("密码重置成功");
          resetModalVisible.value = false;
        } else {
          message.error(data.message);
        }
      });
    };


    const counselors = ref();
    const currentCounselor = ref();

    const handleCounselor = () => {
      axios.get("/counselor/list", {
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        const data = response.data;
        if(data.success){
          handleQuery({
            // 此处的属性名需要和后端对应
            page: 1,
            size: pagination.value.pageSize,
          });
          counselors.value = data.content.list;
        }else{
          message.error(data.message);
        }
      })
    }

    onMounted(() => {

      // 请求辅导员列表
      handleCounselor();
    });

    return {
      param,
      users,
      pagination,
      columns,
      loading,

      modalVisible,
      modalLoading,
      current_user,
      resetModalVisible,
      resetModalLoading,
      counselors,
      currentCounselor,

      handleModalOK,
      handleTableChange,
      edit,
      add,
      handleDelete,
      handleQuery,
      handleResetModalOK,
      resetPassword,
    }
  }
});
</script>
