import jsonp from 'common/js/jsonp'
import { commonParams, options, baseUrl } from './config'
import axios from 'axios'

export function getRecommend() {
  const url = 'https://u.y.qq.com/cgi-bin/musicu.fcg'

  const data = Object.assign({}, commonParams, {
    platform: 'h5',
    uin: 0,
    needNewCode: 0,
    hostUin: 0,
    rnd: Math.random(),
    format: 'jsonp',
    callback: 'recommend',
    data: '{"recomPlaylist":{"method":"get_hot_recommend","param":{"async":1,"cmd":2},"module":"playlist.HotRecommendServer"},"focus":{"module":"QQMusic.MusichallServer","method":"GetFocus","param":{}}}'
  })
  options.name = 'recommend'
  return jsonp(url, data, options)
}

export function getDiscList() {
  const url = '/api/getDiscList'

  const data = Object.assign({}, commonParams, {
    platform: 'yqq',
    hostUin: 0,
    needNewCode: 0,
    rnd: Math.random(),
    format: 'json'
  })

  return axios.get(baseUrl + url, {
    params: data
  }).then((res) => {
    return Promise.resolve(res.data)
  })
}

export function getSongList(disstid) {
  const url = 'https://c.y.qq.com/qzone/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg'

  const data = Object.assign({}, commonParams, {
    disstid,
    type: 1,
    json: 1,
    utf8: 1,
    onlysong: 0,
    platform: 'yqq',
    hostUin: 0,
    needNewCode: 0
  })

  return jsonp(url, data, options)
}