<template>
    <transition name="fade">
        <cube-popup
            v-show="visible"
            :mask-closable=true
            :z-index=90
            position="bottom"
            type="shop-cart-list"
            @mask-click="maskClick"
        >
            <transition name="move" @after-leave="onLeave">
                <div v-show="visible">
                    <div class="list-header">
                        <h1 class="title">购物车</h1>
                        <span @click="empty" class="empty">清空</span>
                    </div>
                    <cube-scroll class="list-content" ref="listContent">
                        <ul>
                            <li class="food"
                                v-for="food in selectFoods" :key="food.name"
                            >
                                <span class="name">{{food.name}}</span>
                                <div class="price"><span>￥{{food.price*food.count}}</span></div>
                                <div class="cart-control-wrapper">
                                    <cart-control @add="onAdd" :food="food"></cart-control>
                                </div>
                            </li>
                        </ul>
                    </cube-scroll>
                </div>
            </transition>
        </cube-popup>
    </transition>
</template>

<script>
import CartControl from 'components/cart-control/cart-control'
import popupMixin from 'common/mixins/popup'

const EVEN_LEAVE = 'leave'
const EVEN_ADD = 'add'
const EVEN_SHOW = 'show'

export default {
    mixins: [popupMixin],
    name: 'shop-cart-list',
    props: {
        selectFoods: {
            type: Array,
            default() {
                return []
            }
        }
    },
    created() {
        this.$on(EVEN_SHOW, () => {
            this.$nextTick(() => {
               this.$refs.listContent.refresh()
           })
        })
    },
    methods: {
        maskClick() {
            this.hide()
        },
        onLeave() {
            this.$emit(EVEN_LEAVE)
        },
        onAdd(target) {
           this.$emit(EVEN_ADD, target)
        },
        empty() {
             this.$createDialog({
                type: 'confirm',
                content: '确认清空购物车吗？',
                $events: {
                    confirm: () => {
                        this.selectFoods.forEach((food) => {
                            food.count = 0
                        })
                        this.hide()
                    }
                }
            }).show()
        }
    },
    components: {
        CartControl
    }
}
</script>

<style lang="stylus" scoped>
@import "~common/stylus/variable";
.cube-shop-cart-list
    bottom:48px
    &.fade-enter,&.fade-leave-ative
        opacity : 0
    &.fade-enter-active,&.fade-leave-active
        transition : all .3s ease-in-out
    .move-enter,.move-leave-active
        transform : translate3d(0,100%,0)
    .move-enter-active,.move-leave-active
        transition : all .3s ease-in-out
    .list-header
        background: #f3f5f7;
        height: 40px;
        line-height: 40px;
        padding: 0 18px;
        .title
            color: #333;
            float: left;
            font-size: 14px;
        .empty
            color: #00a0dc;
            float: right;
            font-size: 12px;
    .list-content
        background: #fff;
        max-height: 217px;
        overflow: hidden;
        padding: 0 18px;
        .cube-scroll-content
            position: relative;
            z-index: 1;
            .cube-scroll-list-wrapper
                overflow: hidden;
        .food
            -webkit-box-sizing: border-box;
            box-sizing: border-box;
            padding: 12px 0;
            position: relative;
            .name
                color: #333;
                font-size: 14px;
                line-height: 24px;
            .price
                bottom: 12px;
                color: #f01414;
                font-size: 14px;
                font-weight: 700;
                line-height: 24px;
                position: absolute;
                right: 90px;
            .cart-control-wrapper
                bottom: 6px;
                position: absolute;
                right: 0
</style>
