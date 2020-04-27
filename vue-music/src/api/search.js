import jsonp from 'common/js/jsonp'
import { commonParams, options, baseUrl } from './config'
import axios from 'axios';

export function getHotKey() {
    const url = 'https://c.y.qq.com/splcloud/fcgi-bin/gethotkey.fcg'

    const data = Object.assign({}, commonParams, {
        g_tk_new_20200303: 5381,
        platform: 'h5',
        loginUin: 0,
        needNewCode: 0,
        format: 'jsonp',
        callback: 'hotkey'
    })
    options.name = 'hotkey'
    return jsonp(url, data, options)
}

export function search(query, page, zhida, perpage) {
    const url = '/api/search'

    const data = Object.assign({}, commonParams, {
        g_tk_new_20200303: 5381,
        platform: 'h5',
        uin: 0,
        needNewCode: 0,
        format: 'json',
        w: query,
        p: page,
        catZhida: zhida ? 1 : 0,
        flag: 1,
        sem: 1,
        aggr: 0,
        perpage: perpage,
        n: 20,
        remoteplace: 'txt.mqq.all'
    })

    return axios.get(baseUrl + url, { params: data }).then((res) => {
        // console.log('search', res)
        return Promise.resolve(res.data)
    })
}