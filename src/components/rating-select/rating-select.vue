<template>
<div  class="rating-select">
    <div class="rating-type border-bottom-1px">
        <span class="block positive" @click="select(2)" :class="{'active':selectType===2}">{{desc.all}}<span class="count">{{ratings.length}}</span></span>
        <span class="block positive" @click="select(0)" :class="{'active':selectType===0}">{{desc.positive}}<span class="count">{{positives.length}}</span></span>
        <span class="block negative" @click="select(1)" :class="{'active':selectType===1}">{{desc.negative}}<span class="count">{{negatives.length}}</span></span>
    </div>
    <div class="switch"  @click="toggle" :class="{'on':onlyContent}">
        <span class="icon-check_circle"></span>
        <span class="text">只看有内容的评价</span>
    </div>
</div>
</template>

<script>
const POSITIVE = 0
const NEGATIVE = 1
const ALL = 2
const EVEN_SELECT = 'select'
const EVEN_TOGGLE = 'toggle'
export default {
       props: {
          ratings:{
              type: Array,
              default () {
                  return []
              }
          },
          selectType:{
              type: Number,
              default: ALL
          },
          onlyContent: {
              type: Boolean,
              default: false
          },
          desc: {
              type: Object,
              default () {
                      return {
                          all:'全部',
                          positive: '满意',
                          negative: '不满意'
                      }  
              }
          }
       },
       methods: {
           select (type) {
                this.$emit(EVEN_SELECT,type)
           },
           toggle () {
               this.$emit(EVEN_TOGGLE)
           }
       },
       computed: {
           positives () {
               return this.ratings.filter((rating) => {
                   return rating.rateType === POSITIVE
               })
           },
           negatives () {
               return this.ratings.filter((rating) => {
                   return rating.rateType === NEGATIVE
               })
           }
       }
}
</script>

<style lang="stylus" scoped>
@import "~common/stylus/variable"
@import "~common/stylus/mixin.styl"

.rating-select
    .rating-type 
        margin: 0 18px;
        padding: 18px 0;
        .block 
            border-radius: 1px;
            color: #666;
            display: inline-block;
            font-size: 12px;
            line-height: 16px;
            margin-right: 8px;
            padding: 8px 12px;
            &.active
                color: #fff;
            &.positive
                background: rgba(0,160,220,.2);
            &.negative
                background: #ccc;
            &.positive.active
                background: #00a0dc;
             &.negative.active
                background: #666;    
           
            .count
                margin-left: 2px;
    .switch
        align-items: center;
        border-bottom: 1px solid rgba(7,17,27,.1);
        color: #999;
        display: flex;
        line-height: 24px;
        padding: 12px 18px;
        .icon-check_circle
            font-size: 24px;
            margin-right: 4px;
        &.on
            .icon-check_circle
                color: #00b43c;
        .text
            font-size: 12px;
</style>
