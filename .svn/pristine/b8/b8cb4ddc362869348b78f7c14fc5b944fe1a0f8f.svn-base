var notifyObject = {
    init : function() {
        notifyObject.initNotifyObjectList();
        notifyObject.initNotifyObjectTable();
        notifyObject.addNotifyObject();
        notifyObject.updateNotifyObject();
        notifyObject.deleteNotifyObject();
        notifyObject.clickTable();


    },

    initNotifyObjectTable:function () {
        tzdxlx=$(".list-group-item.active").data("tzdxlx");
        AjaxTool.get("notifyObject/getNotifyObject",{tzdxlx:tzdxlx},function (data) {
            if(data.success){

                var str = "";
                var no = data.content;

                for (var i=0;i<no.length;i++){
                    str += "<tr class='notify-object-tbody-row'>";
                    str +="<td class="+no[i].id +">"+no[i].tzdx+"</td>";
                    str += "</tr>";
                }
                $('#notify-object-tbody').html(str);
            }
        });

    },

    initNotifyObjectList:function(){
        $(".list-group-item").unbind("click");
        $(".list-group-item").bind("click",function(){
            var tzdxlx=$(this).data("tzdxlx");
            var preTzdxlx=$(".list-group-item.active").data("tzdxlx");
            if(tzdxlx==preTzdxlx){
                return;
            }
            $(".list-group-item.active").removeClass("active");
            $(this).addClass("active");
            notifyObject.initNotifyObjectTable();
        });
    },

    addNotifyObject:function () {
        $(".add-button").click(function(){
            if(tzdxlx ==null){
                Toast.show("对象添加失败",'请选择通知对象类型');
                return;
            }
            SweetAlert.swalInput("增加对象","请输入用户名和姓名","董事长(王伟)",function(inputValue){

                AjaxTool.post("notifyObject/save",{notifyObject:inputValue,tzdxlx:tzdxlx},function(response){
                    if(response.result){
                        Toast.show("对象提醒",'对象添加成功');
                        // $('#notify-object-table').DataTable().row.add([inputValue]).draw(false);
                        notifyObject.initNotifyObjectTable();
                    }else{
                        // Toast.show("对象提醒",'对象添加失败');
                        Toast.show("对象添加失败",response.message);
                    }
                });
            });
        });
    },
    updateNotifyObject:function(){
        $(".update-button").click(function(){
            if(id==null){
                Toast.show("对象提醒","请选择修改对象");
                return;
            }
            var text=tzdx;
            SweetAlert.swalInput("修改对象","修改的对象名:"+text,"董事长(王伟)",function(inputValue){
                AjaxTool.post("notifyObject/update",{id:id,tzdx:inputValue,tzdxlx:tzdxlx},function(response){
                    if(response.result){
                        Toast.show("对象提醒",'对象修改成功');
                        notifyObject.initNotifyObjectTable();
                    }else{
                        // Toast.show("对象提醒",'对象修改失败');
                        Toast.show("对象修改失败",response.message);

                    }
                });
            });
        });
    },

    deleteNotifyObject:function(){
        $(".delete-button").click(function(){
            if(id==null){
                Toast.show("对象提醒","请选择删除对象");
                return;
            }
            SweetAlert.swal("删除对象提醒","","warning",{
                showCancelButton:true
            },function(){
                AjaxTool.post("notifyObject/delete",{id:id},function(response){
                    if(response.result){
                        notifyObject.initNotifyObjectTable();
                        Toast.show("对象提醒",'对象删除成功');
                    }else{
                        Toast.show("对象提醒",'对象删除失败');
                    }
                });
            });
        });
    },
    clickTable:function () {
        $('#notify-object-tbody').on('click',function(e){
            var selectedTr = e.target;
            id= selectedTr.className;
            tzdx=selectedTr.innerHTML;
        })
    },



};


var tzdxlx =null;
var selectedTr = null;
var id=null;
var tzdx=null;

jQuery(document).ready(function() {
    notifyObject.init();
});

