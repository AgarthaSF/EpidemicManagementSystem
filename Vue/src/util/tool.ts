export class Tool {
    /**
     * 空校验 null或""都返回true
     */
    public static isEmpty(obj: any) {
        if ((typeof obj === 'string')) {
            return !obj || obj.replace(/\s+/g, "") === ""
        } else {
            return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0);
        }
    }

    /**
     * 非空校验
     */
    public static isNotEmpty(obj: any) {
        return !this.isEmpty(obj);
    }

    /**
     * 对象复制
     * @param obj
     */
    public static copy(obj: object) {
        if (Tool.isNotEmpty(obj)) {
            return JSON.parse(JSON.stringify(obj));
        }
    }


    public static array2Tree(array: any) {

        let children = [] as any;
        const result = [];

        let currentDistrict = new Map();
        let currentCity = new Map();
        let currentParentAdcode = array[0].districtAdcode;
        currentDistrict.set("adcode", array[0].districtAdcode);
        let beforeParentAdcode = array[0].districtAdcode;
        let currentParentName = array[0].districtName;

        for (let i = 0; i < array.length; i++) {

            currentParentAdcode = array[i].districtAdcode;
            currentParentName = array[i].districtName;

            if (currentParentAdcode == beforeParentAdcode) {
                if(currentParentName != array[i].cityName){
                    currentCity.set("adcode", array[i].cityAdCode);
                    currentCity.set("name", array[i].cityName);
                    currentCity.set("currentConfirmedCount", array[i].cityCurrentConfirmedCount);
                    currentCity.set("suspectedCount", array[i].citySuspectedCount);
                    children.push(Array.from(currentCity));
                    currentCity = new Map();
                }
            } else {
                beforeParentAdcode = array[i].districtAdcode;
                currentDistrict.set("adcode", array[i - 1].districtAdcode);
                currentDistrict.set("name", array[i - 1].districtName);
                currentDistrict.set("currentConfirmedCount", array[i - 1].currentConfirmedCount);
                currentDistrict.set("suspectedCount", array[i - 1].suspectedCount);
                currentDistrict.set("children", children);

                // console.log("current district: ", currentDistrict);
                result.push(Array.from(currentDistrict));
                currentDistrict = new Map();

                children = [];
                currentCity.set("adcode", array[i].cityAdCode);
                currentCity.set("name", array[i].cityName);
                currentCity.set("currentConfirmedCount", array[i].cityCurrentConfirmedCount);
                currentCity.set("suspectedCount", array[i].citySuspectedCount);
                children.push(Array.from(currentCity));
                currentCity = new Map();
            }
        }

        currentDistrict.set("adcode", array[array.length - 1].districtAdcode);
        currentDistrict.set("name", array[array.length - 1].districtName);
        currentDistrict.set("currentConfirmedCount", array[array.length - 1].currentConfirmedCount);
        currentDistrict.set("suspectedCount", array[array.length - 1].suspectedCount);
        currentDistrict.set("children", children);
        result.push(currentDistrict);
        return result;
    }



    /**
     * 随机生成[len]长度的[radix]进制数
     * @param len
     * @param radix 默认62
     * @returns {string}
     */
    public static uuid(len: number, radix = 62) {
        const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
        const uuid = [];
        radix = radix || chars.length;

        for (let i = 0; i < len; i++) {
            uuid[i] = chars[0 | Math.random() * radix];
        }

        return uuid.join('');
    }
}
