<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">

      <p>
        <a-form layout="inline" :model="param">
          <a-form-item :style="{width: '80%'}">
            <a-input v-model:value="param.identity" placeholder="名称">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery()">
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
          :row-key="(record,index)=> index"
          :data-source="treeData"
          :loading="loading"
          :pagination="false">

        <template v-slot:riskLevel="{text, record}">
          <a-tag color="#B22222" v-show="record.riskLevel == 3">高风险地区</a-tag>
          <a-tag color="#FF8000" v-show="record.riskLevel == 2">中风险地区</a-tag>
          <a-tag color="#228B22" v-show="record.riskLevel == 1">低风险地区</a-tag>
        </template>

        <template v-slot:action="{text, record}">
          <a-space size="small" v-show="record.children == null">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="确认删除？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.adcode)">
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>


      </a-table>

    </a-layout-content>
  </a-layout>


  <a-modal
      title="编辑"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOK"
  >
    <a-form :model="current_city" :label-col="{span: 6}" :wrapper-col="{span: 18}">
      <a-form-item label="行政区划代码">
        <a-input v-model:value="current_city.adcode" :disabled="currentState !== 'add'"/>
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="current_city.name"/>
      </a-form-item>
      <a-form-item label="当前确诊人数">
        <a-input v-model:value="current_city.currentConfirmedCount"/>
      </a-form-item>
      <a-form-item label="当前疑似人数">
        <a-input v-model:value="current_city.suspectedCount"/>
      </a-form-item>

      <a-form-item label="当前风险等级">

        <a-select ref="select"
                  v-model:value="current_city.riskLevel">
          <a-select-option v-for="c in riskLevels"
                           :key="c.riskLevel"
                           :value="c.riskLevel">
            {{ c.content }}
          </a-select-option>
        </a-select>

      </a-form-item>


      <a-form-item label="城市所属行政区">

        <a-select ref="select"
                  v-model:value="current_city.parentAdcode">
          <a-select-option v-for="c in treeData"
                           :key="c.adcode"
                           :value="c.adcode">
            {{ c.name }}
          </a-select-option>

        </a-select>

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


export default defineComponent({
  name: 'AdminCovidData',
  setup() {
    const param = ref();
    param.value = {};
    const loading = ref(false);

    const pagination = ref({
      current: 1,
      pageSize: 7,
      total: 0
    });

    const columns = [
      {
        title: '行政区划代码',
        key: 'adcode',
        dataIndex: 'adcode',
        align: 'center',
      },
      {
        title: '名称',
        key: 'name',
        dataIndex: 'name',
        align: 'center',
      },
      {
        title: '当前确诊人数',
        key: 'currentConfirmedCount',
        dataIndex: 'currentConfirmedCount',
        align: 'center',
      },
      {
        title: '当前疑似人数',
        key: 'suspectedCount',
        dataIndex: 'suspectedCount',
        align: 'center',
      },
      {
        title: '当前风险等级',
        slots: {customRender: 'riskLevel'},
        align: 'center',
      },
      {
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'},
        align: 'center'
      }
    ];


    // 表单
    const level1 = ref();

    const treeData = ref();

    // 数据查询
    const handleQuery = () => {
      loading.value = true;
      axios.get("/district-epidemic/list", {
        params: {
          page: 1,
          size: 1000,
          identity: param.value.identity
        },
        headers: {
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        loading.value = false;

        const data = response.data;
        console.log("raw: ", data);

        if (data.success) {
          treeData.value = data.content;
          console.log("Tree data: ", treeData.value);
        } else {
          message.error(data.message);
        }
      });
    };

    const current_city = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    const riskLevels = [{
      "riskLevel": "1",
      "content": "低风险区域",
    },{
      "riskLevel": "2",
      "content": "中风险区域",
    },{
      "riskLevel": "3",
      "content": "高风险区域",
    }]


    const currentState = ref();
    currentState.value = "";


    // 编辑
    const edit = (record: any) => {
      modalVisible.value = true;
      current_city.value = Tool.copy(record);
      currentState.value = "edit";
    }


    const add = ()=>{
      modalVisible.value = true;
      current_city.value = {};
      currentState.value = "add";
    }


    const handleModalOK = () => {
      modalLoading.value = true;

      current_city.value.cityAdcode = current_city.value.adcode;
      current_city.value.cityName = current_city.value.name;
      console.log("currentCity: ", current_city.value);

      axios.post("/city-epidemic/save", current_city.value,{
        headers:{
          'type': store.getters.getUser().type}
      }).then((response) => {
        const data = response.data;
        modalLoading.value = false;
        if (data.success) {
          modalVisible.value = false;
          // 重新加载列表
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    const handleDelete = (id: number) => {
      axios.delete("/city-epidemic/delete/" + id,{
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          // 重新加载列表
          handleQuery();
        }
      })
    }



    onMounted(() => {
      handleQuery();
    });

    return {
      param,
      columns,
      loading,
      current_city,
      modalVisible,
      modalLoading,
      riskLevels,

      treeData,
      pagination,
      currentState,

      handleQuery,
      edit,
      add,
      handleModalOK,
      handleDelete,
    }
  }
});
</script>
