import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.sql.SQLException;

public class JSONProcessor {


    //读取json文件
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) throws SQLException {
        String path = JSONProcessor.class.getClassLoader().getResource("covid.json").getPath();
        String s = readJsonFile(path);
        JSONObject jobj = JSON.parseObject(s);

        int unknownAreaID = 1000000;


        JSONArray provinces = jobj.getJSONArray("results");
        for (int i = 0; i < provinces.size(); i++) {

            JSONObject provinceObj = (JSONObject) provinces.get(i);

            String countryName = (String) provinceObj.get("countryName");
            int provinceID = (Integer) provinceObj.get("locationId");
            // only process chinese data
            if (countryName.equals("中国") && provinceID > 0) {
                // insert the district
                DbUtil.insertDistrict((Integer) provinceObj.get("locationId"), (String) provinceObj.get("provinceShortName"),
                        (Integer) provinceObj.get("currentConfirmedCount"), (Integer) provinceObj.get("suspectedCount"));


                // get the city info
                JSONArray province = provinceObj.getJSONArray("cities");

                // some districts don't have cities, like Aomen
                if (province != null && !province.isEmpty()) {
                    // insert the cities
                    for (int j = 0; j < province.size(); j++) {
                        JSONObject cityObj = (JSONObject) province.get(j);
                        int cityId = (Integer) cityObj.get("locationId");
                        if(cityId <= 0){
                            cityId = unknownAreaID++;
                        }

                        String cityName = (String) cityObj.get("cityName");
                        int highDangerCount = (Integer) cityObj.get("highDangerCount");
                        int midDangerCount = (Integer) cityObj.get("midDangerCount");
                        int riskLevel = 0;
                        if (highDangerCount > 0) {
                            riskLevel = 3;
                        } else if (highDangerCount == 0 && midDangerCount > 0) {
                            riskLevel = 2;
                        } else {
                            riskLevel = 1;
                        }
                        DbUtil.insertCity(cityId, cityName, (Integer) cityObj.get("currentConfirmedCount"),
                                (Integer) cityObj.get("suspectedCount"), provinceID, riskLevel);
                    }
                }
            }


        }

    }
}
