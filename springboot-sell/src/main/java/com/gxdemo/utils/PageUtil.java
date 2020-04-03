package com.gxdemo.utils;

import lombok.Data;

import java.util.HashMap;


/**
 * 前端分页工具类
 */
@Data
public class PageUtil {
    public Integer total; // 数据表中总记录数
    public String uri; // 自动获取url的请求地址
    public Integer listRows; // 每页显示行数
    public Integer page; // 当前页
    public String pageParam = "page=%s";
    public HashMap<String, String> config = new HashMap<>();
    public Integer pageNum;

    private Integer min = 1;
    private Integer max = 0;
    // 在分页信息中显示内容，可以自己通过set()方法设置
    public Integer listNum = 5; // 默认分页列表显示的个数

    public void init(Integer total,Integer size,Integer page,String uri){

        this.config.put("head","条记录");
        this.config.put("prev","上一页");
        this.config.put("next","下一页");
        this.config.put("first","首页");
        this.config.put("last","末页");


        this.total = total;
        this.listRows = size;
        this.uri = uri;
        this.page = page; //当前页
        this.pageNum =  (int)Math.ceil ( this.total / this.listRows );//总页数

        if(this.pageNum<=this.listNum) {
            this.min = 1;
            this.max =this.pageNum;
        }else{
            Integer im = (int)Math.floor(this.listNum/2);
            this.min = this.page - im;
            this.max = this.page + im;
            if((this.min)<=0){
                this.min = 1;
                this.max = this.listNum;
            }
            if(this.max>this.pageNum){
                this.min = (this.pageNum - this.listNum +1 );
                this.max = this.pageNum;
            }
        }
    }

    /* 在对象内部使用的私有方法，用于获取当前页开始的记录数 */
    private Integer  start() {
        if (this.total == 0)
            return 0;
        else
            return (this.page - 1) * this.listRows + 1;
    }

    /* 在对象内部使用的私有方法，用于获取当前页结束的记录数 */
    private Integer end() {
        return  this.page * this.listRows < this.total ? this.page * this.listRows : this.total;
    }

    /* 在对象内部使用的私有方法，用于获取上一页和首页的操作信息 */
    private String firstprev() {
        String str ="";
        if (this.page > 1) {
            if(this.min >1 ){
                str += "&nbsp;<a class='paginate_button' href='"+ this.href(1)+"'>"+this.config.get("first")+"</a>&nbsp;";
            }
            str += "<a class='paginate_button' href='" + this.href(this.page - 1) +"'>"+this.config.get("prev")+"</a>&nbsp;";
        }
        return str;
    }

    /* 在对象内部使用的私有方法，用于获取页数列表信息 */
    private String pageList() {
        String linkPage = "&nbsp;<span>";
        /* 当前页前面的列表 */
        for(int  i = this.min; i <= this.max; i ++) {
            if(this.page == i){
                linkPage += "<span class='paginate_button current'>"+this.page+"</span>&nbsp;";
            }else{
                linkPage += "<a class='paginate_button' href='"+ this.href(i)+"'>"+i+"</a>&nbsp;";
            }
        }
        linkPage += "</span>";
        return this.pageNum>1 ? linkPage : "";
    }

    /* 在对象内部使用的私有方法，获取下一页和尾页的操作信息 */
    private String nextlast() {
        String str ="";
        if (this.page < this.pageNum - 1) {
            str += "&nbsp;<a class='paginate_button' href='" + this.href(this.page + 1) + "'>"+this.config.get("next")+"</a>&nbsp;";
        }
        if(this.min < this.pageNum-this.listNum){
            str += "&nbsp;<a class='paginate_button' href='"+ this.href(this.pageNum) + "'>"+ this.config.get("last")+"</a>&nbsp;";
        }
        return str;
    }

    /* 在对象内部使用的私有方法，用于显示和处理表单跳转页面 */
    private String goPage() {
        if (this.pageNum > 1) {
            return "&nbsp;<input class=\"paginate_button\" style=\"width:50px;\"   type=\"text\" onkeydown=\"javascript:if(event.keyCode==13){var page=(this.value>" + this.pageNum + ") ? "+ this.pageNum +" : this.value;location='" + this.getUri() +"page='+page;}\"><input class=\"paginate_button\" type=\"button\" value=\"GO\" onclick=\"javascript:var page=(this.previousSibling.value>" + this.pageNum + ") ? "+ this.pageNum +" :this.previousSibling.value;location='"+ this.getUri() +"page='+page\">&nbsp;";
        }
        return  "";
    }

    /* 在对象内部使用的私有方法，用于获取本页显示的记录条数 */
    private Integer disnum() {
        if (this.total > 0) {
            return this.end () - this.start () + 1;
        } else {
            return 0;
        }
    }

    private String getUri(){
        return  this.uri + (this.uri.contains("?")?'&':'?');
    }

    private String href(Integer page){
        return  this.getUri()  + String.format(this.pageParam,page+"");
    }

    /**
     * 按指定的格式输出分页
     *
     *  0-7的数字分别作为参数，用于自定义输出分页结构和调整结构的顺序，默认输出全部结构
     * @return string 分页信息内容
     *
     */
    public String show(int ...arr) {
        String[] html = new String[8];
        html [0] = "<span class='p1'>&nbsp;共<b> "+this.total+" </b>"+this.config.get("head")+"&nbsp;</span>";
        html [1] = "&nbsp;本页 <b>"+ this.disnum () + "</b> 条&nbsp;";
        html [2] = "&nbsp;本页从 <b>"+this.start()+"-"+this.end()+"</b> 条&nbsp;";
        html [3] = "&nbsp;<b>"+this.page+"/"+this.pageNum+"</b>页&nbsp;";
        html [4] = this.firstprev();
        html [5] = this.pageList();
        html [6] = this.nextlast();
        html [7] = this.goPage();

        String  fpage = "<div class=\"pageinfo\">";
        if (arr.length  < 1) {
          arr = new int[]{0,1,2,3,4,5,6,7};
        }
        for(int i = 0; i < arr.length ; i ++){
            fpage += html[arr[i]];
        }
       fpage += "</div>";
        return fpage;
    }

}
