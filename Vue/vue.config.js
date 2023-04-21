module.exports = {
    // ...
    chainWebpack: function(config) {
        config.plugin("html").tap(args => {
            const cdn ={
                css: [],
                js: [
                    "https://webapi.amap.com/maps?v=1.4.13&key=''",
                    "//webapi.amap.com/ui/1.0/main.js"
                ]
            }
            // 判断环境
            if (process.env.NODE_ENV === "production") {
                args[0].cdn = cdn;
            }
            if (process.env.NODE_ENV === "development") {
                args[0].cdn = cdn;
            }
            return args;
        })
    },
    configureWebpack: function(config) {
        config.externals = {
            'AMap': 'AMap',
            AMap: 'AMap',

        }
    }
}
