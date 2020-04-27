import 'common/stylus/index.styl'
import Vue from 'vue'
// import axios from 'axios'
import './cube-ui'
import './register'
import App from './App.vue'
import router from './router'
import store from './store'
import fastclick from 'fastclick'
import VueLazyload from 'vue-lazyload'
fastclick.attach(document.body)
Vue.config.productionTip = false

// Vue全局挂载axios
// Vue.prototype.$http = axios

Vue.use(VueLazyload, {
  loading: require('common/image/default.png')
})

window.vm = new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
