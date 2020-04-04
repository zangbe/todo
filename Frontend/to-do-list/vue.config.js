module.exports = {

  outputDir: '../../Backend/todo/src/main/resources/static',

  devServer: {
    proxy: {
      "^/api/": {
        target: "http://localhost:9900",
        pathRewrite: { "^/api/": "/api/" },
        changeOrigin: true,
        logLevel: "debug"
      }
    }
  },

  "transpileDependencies": [
    "vuetify"
  ],
}
