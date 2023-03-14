<template>
  <div id="container" :style="{height: '600px', width: '1300px'}"></div>
  <a-button type="primary" @click="changeLayer" :style="{float: 'right'}">
    {{currentInfo}}
  </a-button>


</template>

<script lang="ts">
import {onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import store from "@/store";

declare let AMap: any;

export default {
  name: 'AdminDiagnosedMap',
  setup() {

    let map: any;
    let adCode: any;
    let disProvince: any;
    let Response: any;

    const adcodes = [{"adcode": 100000, "name": "全国"}, {
      "adcode": 420000,
      "name": "湖北省"
    }]

    const init = () => {
      map = new AMap.Map("container", {
        zoom: 9.0,
        center: [114.51, 30.68],
        pitch: 0,
        viewMode: '3D',
      });
      // map.setMapStyle("amap://styles/db93efc046321a2ec3b28b20a3cbbcd3");

      let input = ['420000'];
      initPro(input);
    }


    const initPro = (code: any) => {
      adCode = code;
      disProvince && disProvince.setMap(null);

      disProvince = new AMap.DistrictLayer.Province({
        zIndex: 12,
        adcode: code,
        adcode_cit: 420100,
        depth: 2,
        styles: {
          'fill': function (properties: any) {
            let adcode = properties.adcode;
            // console.log(properties.adcode_pro);
            if (properties.adcode_cit == 420100) {
              return getColorByAdcode(adcode);
            }
          },
          'province-stroke': 'cornflowerblue',
          'city-stroke': 'white',
          'county-stroke': 'rgb(255, 255, 255)',
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
      axios.get("/area-epidemic/list/" + "420100",{
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          const dataList = data.content;
          console.log("dataList: ", dataList);
          for (let i = 0; i < dataList.length; i++) {
            covidData.set(dataList[i].areaId, dataList[i]);
          }
          console.log("list data: ", covidData);
        } else {
          message.error(data.message);
        }
      })
    }

    const SurveyData = new Map();
    const SurveyList = ref();

    const getDiagnosedData = () => {
      axios.get("/diagnosed-case/risk", {
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        const data = response.data;
        console.log("diagnosed-case all: ", data);
        if (data.success) {
          const dataList = data.content;
          SurveyList.value = dataList;
          for (let i = 0; i < dataList.length; i++) {
            SurveyData.set(dataList[i].caseId, dataList[i]);
          }
          initMarker();
        } else {
          message.error(data.message);
        }
      })
    }


    let dataList = [];

    const initMarker = () => {
      dataList = SurveyList.value;

      for (let i = 0; i < dataList.length; i++) {
        console.log("current: ", i);
        const marker = new AMap.Marker({
          position: [dataList[i].longitude, dataList[i].latitude],
          icon: "https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png",
          map: map,
        });
        console.log(marker);

        let info = [];
        let confirmedAddress = "<p class='input-item'>确诊地点：" + dataList[i].addressName + "</p>";
        let confirmedPosition = "<p class='input-item'>确诊位置：" + dataList[i].addressPosition + "</p>";
        let confirmedTime = "<p class='input-item'>确诊时间：" + dataList[i].diagnosedDate + "</p>";
        let caseId = "caseId" + dataList[i].caseId;

        info.push(confirmedAddress);
        info.push(confirmedPosition);
        info.push(confirmedTime);
        info.push(caseId);

        marker.content = info.join("");
        marker.on('click', markerClick);
      }
    }

    let surveyMarker: any;
    let surveyPolyline: any;
    let surveyPassedPolyline: any;
    let surveyInfoWindow = new AMap.InfoWindow({offset: new AMap.Pixel(0, -30)});


    const markerShow = (e: any) => {
      let spilitted = e.target.content.split("caseId");
      surveyInfoWindow.setContent(spilitted[0]);
      surveyInfoWindow.open(map, e.target.getPosition());
    }

    const markerClick = (e: any) => {
      map.clearMap();
      let spilitted = e.target.content.split("caseId");
      surveyInfoWindow.setContent(spilitted[0]);
      surveyInfoWindow.open(map, e.target.getPosition());

      // 请求流调信息并绘制轨迹
      let caseId = spilitted[1];

      axios.get("/diagnosed-survey/list/" + caseId,{
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        console.log("list response: ", response);
        const data = response.data;
        if (data.success) {
          getCaseIdMarker(caseId);
          initMarker();
          let addrList = data.content;
          let lineList = [];
          for (let i = 0; i < addrList.length; i++) {
            lineList.push([addrList[i].longitude, addrList[i].latitude]);
          }
          let lineArr = lineList;
          surveyMarker = new AMap.Marker({
            map: map,
            position: [addrList[0].longitude, addrList[0].latitude],
            icon: "https://a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
          });
          // 绘制轨迹
          surveyPolyline = new AMap.Polyline({
            map: map,
            path: lineArr,
            showDir: true,
            strokeColor: "#28F",  //线颜色
            strokeWeight: 6,      //线宽
          });
          surveyPassedPolyline = new AMap.Polyline({
            map: map,
            strokeColor: "#AF5",  //线颜色
            strokeWeight: 6,      //线宽
          });
          surveyMarker.moveAlong(lineArr, 30000);
          map.setZoomAndCenter(13, [addrList[0].longitude, addrList[0].latitude]);
        } else {
          message.error(data.message);
        }

      })
    }

    const getCaseIdMarker = (caseId: any) => {
      axios.get("/diagnosed-survey/list/" + caseId,{
        headers:{
          'type': store.getters.getUser().type,
        }
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          let dataList = data.content;

          for (let i = 0; i < dataList.length; i++) {
            const marker = new AMap.Marker({
              position: [dataList[i].longitude, dataList[i].latitude],
              icon: "https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png",
              map: map,
            });

            let info = [];
            let confirmedAddress = "<p class='input-item'>途径地点：" + dataList[i].addressName + "</p>";
            let confirmedPosition = "<p class='input-item'>途径位置：" + dataList[i].addressPosition + "</p>";
            let confirmedTime = "<p class='input-item'>途径时间：" + dataList[i].arrDate + "</p>";
            let caseId = "caseId" + dataList[i].caseId;

            info.push(confirmedAddress);
            info.push(confirmedPosition);
            info.push(confirmedTime);
            info.push(caseId);

            marker.content = info.join("");
            marker.on('click', markerShow);
          }
        } else {
          message.error(data.message);
        }
      })
    }


    let infoWindow: any;

    const openInfo = (data: any, ev: any) => {
      //构建信息窗体中显示的内容
      let info = [];
      let header = "<div style=\"padding:7px 0px 0px 0px;\"><h4>" + data.NAME_CHN + "</h4>";
      info.push(header);

      if (!covidData.get(data.adcode)) {
        info.push("<p class='input-item'>暂无该地区信息</p></div></div>");
      } else {
        let selectdData = covidData.get(data.adcode);
        let risk;
        if (selectdData.riskLevel == 1) {
          risk = "低风险地区";
        } else if (selectdData.riskLevel == 2) {
          risk = "中风险地区";
        } else {
          risk = "高风险地区";
        }
        let currentRisk = "<p class='input-item'>该地区为" + risk + "</p>";
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
      getDiagnosedData();
      map.on('click', (ev: any) => {
        map.clearMap();
        initMarker();
        let px = ev.pixel;
        // 拾取所在位置的行政区
        let props = disProvince.getDistrictByContainerPos(px);
        console.log(props);
        if (props) {
          openInfo(props, ev.lnglat);
        }
      })
    })

    const currentInfo = ref();
    currentInfo.value = "隐藏图层";
    let current_state = 1;
    const changeLayer = () =>{
      if(current_state == 1){
        current_state = 0;
        currentInfo.value = "显示图层";
        disProvince.setMap(null);
      }else{
        current_state = 1;
        currentInfo.value = "隐藏图层";
        disProvince.setMap(map);
      }
    }


    return {
      changeLayer,
      currentInfo,


    }

  }
}
</script>

<style scoped>
#container {
  margin: 0;
}
</style>
