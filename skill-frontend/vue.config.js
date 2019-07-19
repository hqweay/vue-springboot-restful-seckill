
module.exports = {
    baseUrl: process.env.NODE_ENV === 'production' ? '/online/' : '/',
    // outputDir: 在npm run build时 生成文件的目录 type:string, default:'dist'
    // outputDir: 'dist',
    // pages:{ type:Object,Default:undfind }
    devServer: {
        port: 8888, // 端口号
        host: 'localhost',
        https: false, // https:{type:Boolean}
        open: false, //配置自动启动浏览器
        // proxy: 'http://localhost:4000' // 配置跨域处理,只有一个代理
        proxy: {
            '/api': {
                target: 'http://localhost:8082',
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    '^/api': '/api'
                    //路径重写 因为原来的api路径含有 /api
                    //http://localhost:8089/api/chatRoom/addMessage
                    //不重写的话 使用就是 /api/api/chatRoom/addMEssage
                    //因为 使用"/api"来代替"http://localhost:8089"
                    //重写 就可以 用 /api/chatRoom/addMEssage
                }
            },
            '/foo': {
                target: '<other_url>'
            }
        }, // 配置多个代理
    }
}
