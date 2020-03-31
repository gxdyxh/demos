import axios from 'axios';

const  ERROR_OK = 0

const urlMap= {
    development: '/',
    production: '',
}
const baseUrl = urlMap[process.env.NODE_ENV]

export function get (url) {
    return function (params) {
        return axios.get(baseUrl + url,{ params }).then((res) => {
            const { errno,data } = res.data;
            if (errno === ERROR_OK) {
                    return data;
            }
        }).catch(() => {})
    }
}
