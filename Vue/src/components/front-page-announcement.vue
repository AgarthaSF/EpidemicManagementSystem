<template>

  <a-row :gutter="24">

    <a-col :span="8">
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item :style="{width: '80%'}">
            <a-input v-model:value="param.announcementId" placeholder="名称">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})" size="small">
              查询
            </a-button>
          </a-form-item>
        </a-form>
      </p>

      <a-table
          :columns="columns"
          :row-key="record => record.announcementId"
          :data-source="announcements"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
          size="small"
      >
        <template v-slot:action="{text, record}">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)" size="small">
              查看
            </a-button>
          </a-space>
        </template>
      </a-table>

    </a-col>


    <a-col :span="16">

      <a-form :model="current_announcement">


        <a-form layout="inline" :model="current_announcement" >
          <a-form-item label="标题">
            {{current_announcement.title}}
          </a-form-item>
          <a-form-item label="发布时间" :style="{marginLeft: '20px'}">
            {{current_announcement.annoDate}}
          </a-form-item>
        </a-form>

        <a-divider/>
        <a-form-item>
            <div id="toolbar-container" class="toolbar" v-show="false"></div>
            <div id="text-container" class="text"></div>
        </a-form-item>
      </a-form>

    </a-col>
  </a-row>


</template>

<script lang="ts">
import E from "wangeditor";
import {onMounted, ref} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import store from "@/store";

export default {
  name: "front-page-announcement",


  setup() {
    const param = ref();
    param.value = {};

    const announcements = ref();
    const current_announcement = ref();
    current_announcement.value = {
      annoDate: "",
      title: "",
    }
    const loading = ref(false);
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });


    const columns = [
      {
        title: '标题',
        dataIndex: 'title',
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
      axios.get("/announcement/list", {
        params: {
          page: params.page,
          size: params.size,
          announcementId: param.value.announcementId
        },
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          announcements.value = data.content.list;
          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;

          // 如果查到的数据条数大于0，那么先加载出第一条数据
          if (announcements.value.length > 0) {
            edit(announcements.value[0]);
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

    // 编辑
    const edit = (record: any) => {
      current_announcement.value = Tool.copy(record);
      editor.txt.html(current_announcement.value.content);
      // editor.disable();
    }


    const editor = new E('#toolbar-container', '#text-container');
    editor.config.zIndex = 0;
    editor.config.height = 380;
    editor.config.focus = false;

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      })
      editor.create();
      // editor.disable();
    })


    return {
      columns,
      loading,
      pagination,
      announcements,
      current_announcement,
      param,

      handleQuery,
      handleTableChange,
      edit,
    }


  }
}
</script>

<style scoped>




</style>