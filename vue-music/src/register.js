import { createAPI } from 'cube-ui';
import Vue from 'vue'
import AddSong from 'components/add-song/add-song'
import TopTip from 'components/top-tip/top-tip'
createAPI(Vue, AddSong)
createAPI(Vue, TopTip)