import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from 'views/Home.vue'

import SingerDetail from 'views/SingerDetail'
import AlbumDetail from 'views/AlbumDetail'
import TopDetail from 'views/TopDetail'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/singer/:id',
    name: 'SingerDetail',
    component: SingerDetail
    // component: () => import(/* webpackChunkName: "singer" */ 'views/SingerDetail')
  },
  {
    path: '/album/:id',
    name: 'AlbumDetail',
    // component: () => import(/* webpackChunkName: "album" */ 'views/AlbumDetail')
    component: AlbumDetail
  },
  {
    path: '/top/:id',
    name: 'TopDetail',
    component: TopDetail
    // component: () => import(/* webpackChunkName: "top" */ 'views/TopDetail')
  },
  {
    path: '/user',
    name: 'UserCenter',
    component: () => import(/* webpackChunkName: "user" */ 'views/UserCenter.vue')
  }
]

const router = new VueRouter({
  mode: 'hash', //  'history',
  base: process.env.BASE_URL,
  routes
})

export default router
