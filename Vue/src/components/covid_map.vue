<template>
  <div id="container"></div>

</template>

<script lang="ts">
import {onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import store from "@/store";

declare let AMap: any;

export default {
  name: 'CovidMap',
  setup() {

    let map: any;
    let adCode: any;
    let disProvince: any;
    let Response: any;

    const adcodes = [{"adcode": 100000, "name": "全国"}, {"adcode": 110000, "name": "北京市"}, {
      "adcode": 120000,
      "name": "天津市"
    }, {"adcode": 130000, "name": "河北省"}, {"adcode": 140000, "name": "山西省"}, {
      "adcode": 150000,
      "name": "内蒙古自治区"
    }, {"adcode": 210000, "name": "辽宁省"}, {"adcode": 220000, "name": "吉林省"}, {
      "adcode": 230000,
      "name": "黑龙江省"
    }, {"adcode": 310000, "name": "上海市"}, {"adcode": 320000, "name": "江苏省"}, {
      "adcode": 330000,
      "name": "浙江省"
    }, {"adcode": 340000, "name": "安徽省"}, {"adcode": 350000, "name": "福建省"}, {
      "adcode": 360000,
      "name": "江西省"
    }, {"adcode": 370000, "name": "山东省"}, {"adcode": 410000, "name": "河南省"}, {
      "adcode": 420000,
      "name": "湖北省"
    }, {"adcode": 430000, "name": "湖南省"}, {"adcode": 440000, "name": "广东省"}, {
      "adcode": 450000,
      "name": "广西壮族自治区"
    }, {"adcode": 460000, "name": "海南省"}, {"adcode": 500000, "name": "重庆市"}, {
      "adcode": 510000,
      "name": "四川省"
    }, {"adcode": 520000, "name": "贵州省"}, {"adcode": 530000, "name": "云南省"}, {
      "adcode": 540000,
      "name": "西藏自治区"
    }, {"adcode": 610000, "name": "陕西省"}, {"adcode": 620000, "name": "甘肃省"}, {
      "adcode": 630000,
      "name": "青海省"
    }, {"adcode": 640000, "name": "宁夏回族自治区"}, {"adcode": 650000, "name": "新疆维吾尔自治区"}, {
      "adcode": 710000,
      "name": "台湾省"
    }, {"adcode": 810000, "name": "香港特别行政区"}, {"adcode": 820000, "name": "澳门特别行政区"}]

    // [108.552500, 34.322700]

    const init = () => {
      map = new AMap.Map("container", {
        zoom: 4.0,
        center: [105.552500, 38.322700],
        pitch: 0,
        viewMode: '3D',
      });
      map.setMapStyle("amap://styles/db93efc046321a2ec3b28b20a3cbbcd3");

      let input = []
      for (let i = 1; i < adcodes.length; i++) {
        input.push(adcodes[i].adcode);
      }
      initPro(input);
    }


    const initPro = (code: any) => {
      adCode = code;
      disProvince && disProvince.setMap(null);

      disProvince = new AMap.DistrictLayer.Province({
        zIndex: 12,
        adcode: code,
        depth: 1,
        styles: {
          'fill': function (properties: any) {
            let adcode = properties.adcode;
            // console.log(properties);
            return getColorByAdcode(adcode);

          },
          'province-stroke': 'cornflowerblue',
          'city-stroke': 'white', // 中国地级市边界
          'county-stroke': 'rgba(255,255,255)' // 中国区县边界
        }
      });
      disProvince.setMap(map);
    }

    // 颜色辅助方法
    let colors = new Map();
    const getColorByAdcode = (adcode: number) => {
      if (!colors.get(adcode)) {
        let gb = Math.random() * 155 + 50;
        colors.set(adcode, 'rgb(' + gb + ',' + gb + ',255)');

        // 如果没有该地区的数据就绘制绿色
        if (!covidData.get(adcode)) {
          gb = 50 + gb % 50;
          colors.set(adcode, 'rgb(' + gb + ',' + (150 + gb % 10) + ',' + (30 + gb % 20) + ')');
        } else {
          if (covidData.get(adcode).riskLevel == 1) {
            // green
            gb = 50 + gb % 50;
            colors.set(adcode, 'rgb(' + gb + ',' + (150 + gb % 10) + ',' + (30 + gb % 20) + ')');
          } else if (covidData.get(adcode).riskLevel == 2) {
            // orange
            gb = (gb % 50) + 200;
            colors.set(adcode, 'rgb(' + gb + ', 120, 10)');
          } else {
            // red
            gb = (gb % 50) + 200;
            colors.set(adcode, 'rgb(' + gb + ', 10,' + (10 + gb % 20) + ')');
          }
        }
      }
      return colors.get(adcode);
    };


    let covidData = new Map();

    const getCovidData = () => {
      axios.get("/city-epidemic/list",{
          headers:{
        'type': store.getters.getUser().type,
      }}).then((response) => {
        const data = response.data;
        if (data.success) {
          const dataList = data.content;
          // console.log("dataList: ", dataList[0].cityAdcode);
          for (let i = 0; i < dataList.length; i++) {
            covidData.set(dataList[i].cityAdcode, dataList[i]);
          }
          // console.log("list data: ", covidData);
        } else {
          message.error(data.message);
        }
      })
    }

    let infoWindow: any;

    const openInfo = (data : any, ev : any) => {
      //构建信息窗体中显示的内容
      let info = [];

      let header = "<div style=\"padding:7px 0px 0px 0px;\"><h4>" + data.NAME_CHN + "</h4>";
      info.push(header);

      if(!covidData.get(data.adcode)){
        info.push("<p class='input-item'>暂无该地区信息</p></div></div>");
      }else{
        let selectdData = covidData.get(data.adcode);
        let currentConfirmed = "<p class='input-item'>当前确诊人数：" + selectdData.currentConfirmedCount + "</p>";
        let currentSuspected = "<p class='input-item'>当前疑似病例：" + selectdData.suspectedCount  + "</p>";
        let risk;
        if(selectdData.riskLevel == 1){
          risk = "低风险地区";
        }else if(selectdData.riskLevel == 2){
          risk = "中风险地区";
        }else{
          risk = "高风险地区";
        }
        let currentRisk = "<p class='input-item'>该地区为" + risk + "</p>";

        info.push(currentConfirmed);
        info.push(currentSuspected);
        info.push(currentRisk);
      }


      infoWindow = new AMap.InfoWindow({
        content: info.join("")  //使用默认信息窗体框样式，显示信息内容
      });
      infoWindow.open(map, ev);
    }


    onMounted(() => {
      getCovidData();
      init();

      map.on('click', (ev: any) => {
        let px = ev.pixel;
        // 拾取所在位置的行政区
        let props = disProvince.getDistrictByContainerPos(px);
        if (props) {
          openInfo(props, ev.lnglat);
        }
      })


    })

    return {}

  }
}
</script>

<style scoped>
#container {
  /*width: 700px;*/
  /*height: 500px;*/
  margin: 0;
}
</style>
