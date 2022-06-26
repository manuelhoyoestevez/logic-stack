const path = require('path');
const CopyPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
require("babel-polyfill");

module.exports = {
  mode: 'development',
  entry: ['babel-polyfill', './src/index.js'],
  output: {
    publicPath: '',
    path: path.resolve(__dirname, 'build'),
    filename: 'bundled.js'
  },
  devtool: 'eval-source-map',
  devServer: {
    historyApiFallback: true
  },
  externals: {
    Forms: 'Forms',
    Settings: 'Settings',
    Translations: 'Translations'
  },
  resolve: {
    extensions: ['.js', '.jsx'],
  },
  module: {
    rules: [
      {
        test: /\.jpg|jpeg|png|gif|ico$/,
        exclude: /node_modules/,
        use: ['file-loader']
      },
      {
        test: /\.ttf(\?\S*)?$/,
        exclude: /node_modules/,
        use: [{ loader: 'url-loader', options: { limit: 100000, mimetype: 'application/font-ttf' } }]
      },
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: ['babel-loader']
      },
      {
        test: /\.sa?css$/,
        use: ['style-loader', { loader: 'css-loader' }, { loader: 'sass-loader' }]
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './public/index.html'
    }),
    new CopyPlugin({
      patterns: [
        {
          from: 'src/config/forms.js',
          to: 'src/config'
        },
        {
          from: 'src/config/settings.js',
          to: 'src/config'
        },
        {
          from: 'src/config/translations.js',
          to: 'src/config'
        },
        {
          from: 'src/config/*.txt'
        },
        {
          from: 'README.md'
        }
      ]
    })
  ]
}