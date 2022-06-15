const webpack = require("webpack")

let apiUrl

if (config.mode === "production") { // the build process makes the config object available
    apiUrl = "'/api/'"
} else {
    apiUrl = "'http://localhost:8888/'"
}

const definePlugin = new webpack.DefinePlugin(
    {
        API_URL: apiUrl
    }
)

config.plugins.push(definePlugin)

config.devServer = {
    ...config.devServer,
    historyApiFallback: true
}