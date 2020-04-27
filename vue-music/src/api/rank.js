import jsonp from 'common/js/jsonp'
import { commonParams, options } from './config'

export function getTopList() {
    const url = 'https://u.y.qq.com/cgi-bin/musicu.fcg'

    const data = Object.assign({}, commonParams, {
        platform: 'h5',
        uin: 0,
        needNewCode: 0,
        hostUin: 0,
        format: 'jsonp',
        callback: 'toplist',
        data: '{"topList":{"module":"musicToplist.ToplistInfoServer","method":"GetAll","param":{}}}'
    })
    options.name = 'toplist'
    return jsonp(url, data, options)
}

export function getSongList(topId, period) {
    const url = 'https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_cp.fcg'

    const data = Object.assign({}, commonParams, {
        g_tk: 5381,
        platform: 'h5',
        needNewCode: 0,
        hostUin: 0,
        loginUin: 0,
        format: 'jsonp',
        callback: 'topSongList',
        page: 'detail',
        type: 'top',
        topid: topId
    })
    options.name = 'topSongList'
    return jsonp(url, data, options)
}

/*
export function getSongList(topId, period) {
    const url = 'https://u.y.qq.com/cgi-bin/musicu.fcg'

    const data = Object.assign({}, commonParams, {
        g_tk: 5381,
        platform: 'yqq',
        needNewCode: 0,
        hostUin: 0,
        loginUin: 0,
        format: 'jsonp',
        callback: 'topSongList',
        data: `{"detail":{"module":"musicToplist.ToplistInfoServer","method":"GetDetail","param":{"topId":${topId},"offset":0,"num":100,"period":"${period}"}}}`
    })
    options.name = 'topSongList'
    return jsonp(url, data, options)
}
export function getSongList(topId, period) {
    const url = '/api/rank/song'

    const data = Object.assign({}, commonParams, {
        platform: 'yqq',
        uin: 0,
        needNewCode: 0,
        hostUin: 0,
        format: 'json',
        data: `{"detail":{"module":"musicToplist.ToplistInfoServer","method":"GetDetail","param":{"topId":${topId},"offset":0,"num":100,"period":${period}}}`
    })

    return axios.get(url, { params: data }).then((res) => {
        console.log('getTopSongList', res)
        return Promise.resolve(res.data)
    })
}
 */