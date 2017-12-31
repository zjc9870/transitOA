var Meetingroom = {
    inputId : $("input[name='id']"),
    inputHysname:$("input[name='hysname']"),
    inputLocation:$("input[name='location']"),
    inputCapacity:$("input[name='capacity']"),
    inputDescription:$("input[name='description']"),
    // inputHydd:$("input[name='hydd']"),
    inputHydd:$("#hydd").val(),
    init : function() {
        var mTable=DatatableTool.initDatatable("meetingroom-table", [ {
            'orderable' : false,
            'targets' : [ 0, 5 ]
        }, {
            "searchable" : false,
            "targets" : [ 0, 5 ]
        },{
            "width" : "100px",
            "targets" : 5
        } ], [ [ 1, "asc" ] ]);

        Meetingroom.initModal();
        Meetingroom.initSaveUpdate();
    },
    initModal:function(){
        //初始化modal,增加/修改/删除/批量删除
        DatatableTool.initModal(function(){
            DatatableTool.modalShow("#meetingroom-modal", "#meetingroom-form");

            Meetingroom.getFormPage(-1);

            $("#save").removeClass("hidden");
            $("#update").addClass("hidden");
        },function(id){
            DatatableTool.modalShow("#meetingroom-modal", "#meetingroom-form");
            Meetingroom.getFormPage(id);

            $("#save").addClass("hidden");
            $("#update").removeClass("hidden");
        },function(id){
            DatatableTool.deleteRow("meetingroom-table","meeting/meetingroomDelete",id);

        },function(ids){
            DatatableTool.deleteRows("meetingroom-table","meeting/meetingroomDelBatch",ids);
        });
        DatatableTool.initAddModal(function(id){
            DatatableTool.modalShow("#meetingrecord-modal", "#meetingrecord-form");
            // $("input[name='id']").val(id);
            // sendInputId = id;

            Meetingroom.getRecordPage(id);
        });
    },
    initSaveUpdate:function(){
        //绑定保存和修改按钮
        DatatableTool.bindSaveAndUpdate(function(){
            DatatableTool.saveRow("meeting/meetingroomSave",$("#meetingroom-form").serialize(),"meetingroom-table",function(rowNode,response){
                $("#meetingroom-modal").modal('hide');
                Meetingroom.initModal();
            });
        },function(){
            DatatableTool.updateRow("meeting/meetingroomUpdate",$("#meetingroom-form").serialize(),"meetingroom-table",function(rowNode,response){
                $("#meetingroom-modal").modal('hide');
                console.log($("#meetingroom-form").serialize());
                var data=response.addData;
                if(data&&data.length>0){
                    for(i in data){
                        $("#"+data[i]).find("td").eq(5).html(response.obj.name);
                    }
                }
                Meetingroom.initModal();
            });
        });
    },
    getFormPage:function(id){
        AjaxTool.html("meeting/meetingroomFormPage",{
            id:id+""
        },function(html){
            $("#meetingroom-modal .modal-body").html(html);

            var inputInt = document.getElementById('capacity');
            inputInt.onkeyup = function() {
                var num = this.value;
                var re =/^[0-9]*[1-9][0-9]*$/;

                if (!re.test(num)&&num!=""&&num!=null) {

                    document.getElementById("capacity").value="";
                    var show="（请输入大于0的数字）";
                    document.getElementById("tishi1").innerText=show;
                }
            };



                keypress = function () {
                    var len = $("#description").val();

                    console.log(len.length);
                    if(len.length >= 30){
                        var show2="（字数达最大限制）";
                        document.getElementById("tishi2").innerText=show2;
                    };
                    }

                $.ajax({
                    url: "/admin/meeting/retHydd",
                    type: "post",
                    traditional: true,
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    success: function(data) {
                        var retHydd=data.content;
                        console.log(retHydd);
                        console.log(retHydd.indexOf("/"));
                        if(retHydd.indexOf("/")>-1){
                            var utils = retHydd.split("/");
                            console.log(utils);
                            for ( var i = 0; i < utils.length;i++){
                                var option="<option value='"+utils[i]+"'>"+utils[i]+"</option>";
                                $("#hydd").append(option);
                            }

                        }else{
                            var option="<option value='"+retHydd+"'>"+retHydd+"</option>";
                            $("#hydd").append(option);
                        }

                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        // alert(XMLHttpRequest.status);
                        // alert(XMLHttpRequest.readyState);
                        // alert(textStatus);
                        alert("查询不到相关数据，请检查");
                    },
                });
        })


    },
    getRecordPage:function(id){

        AjaxTool.html("meeting/meetingrecordFormPage",{
            id:id+""
        },function(html){
            $("#meetingrecord-modal .modal-body").html(html);


            $("#ck").click(function() {
                $('#container').html("");
                var m_date= $("#xzrq").val();
                if(m_date==""){
                    alert("请选择日期再进行查看")
                }else{
                    var m_info={
                        mid:id,
                        mdate:m_date
                    }
                    $.ajax({
                        url: "/admin/meeting/pointedMrecord",
                        type: "get",
                        traditional: true,
                        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                        data:m_info,
                        success: function(data) {
                            syqk = data.obj;
                            var tasks = [{
                                name: "使用详情",
                                intervals: []
                            }];
                            if (syqk !=null){
                                for(i = 0; i < syqk.length; i++){
                                    ks = syqk[i].kssj;
                                    ksStr = ks.split(":");
                                    ksHour = ksStr[0];
                                    ksMin = ksStr[1];
                                    js = syqk[i].jssj;
                                    jsStr = js.split(":");
                                    jsHour = jsStr[0];
                                    jsMin = jsStr[1];
                                    tasks[0].intervals.push({ // From-To pairs
                                        from: Date.UTC(0, 0, 0, ksHour, ksMin),
                                        to: Date.UTC(0, 0, 0, jsHour, jsMin)
                                    });
                                }
                            }else{
                                tasks[0].intervals.push({
                                    from: Date.UTC(0, 0, 0, 8),
                                    to: Date.UTC(0, 0, 0, 20)
                                });
                                alert("该天会议室没有被使用");
                            }
                            var series = [];
                            $.each(tasks.reverse(), function (i, task) {
                                var item = {
                                    // name: task.name,
                                    name: "使用详情",
                                    data: []
                                };
                                $.each(task.intervals, function (j, interval) {
                                    if (syqk != null){
                                        item.data.push({
                                            x: interval.from,
                                            y: i,
                                            //label: interval.label,
                                            from: interval.from,
                                            to: interval.to
                                        }, {
                                            x: interval.to,
                                            y: i,
                                            from: interval.from,
                                            to: interval.to
                                        });

                                        if (task.intervals[j + 1]) {
                                            item.data.push(
                                                [(interval.to + task.intervals[j + 1].from) / 2, null]
                                            );
                                        }
                                    }else{
                                        item.data.push({
                                            x: interval.from,
                                            y: i,
                                            from: interval.from,
                                            to: interval.to
                                        }, {
                                            x: interval.to,
                                            i: null,
                                            from: interval.from,
                                            to: interval.to
                                        });
                                    }
                                });
                                series.push(item);
                            });

                            var chart = new Highcharts.Chart({
                                chart:{
                                    renderTo:'container'
                                },
                                title: {
                                    text: '会议室使用情况'
                                },
                                credits: {
                                    enabled: false
                                },
                                xAxis: {
                                    title: {
                                        text: ''
                                    },
                                    type: 'datetime',
                                    tickInterval :2 * 3600 * 1000,
                                    maxZoom: 12*60*60*1000,
                                    dateTimeLabelFormats: {
                                        day: '%H:%M'
                                    }
                                },
                                yAxis: {
                                    tickInterval: 1,
                                    labels: {
                                        formatter: function () {
                                            if (tasks[this.value]) {
                                                return tasks[this.value].name;
                                            }
                                        }
                                    },
                                    startOnTick: false,
                                    endOnTick: false,
                                    title: {
                                        text: ''
                                    },
                                    minPadding: 0.2,
                                    maxPadding: 0.2
                                },
                                legend: {
                                    enabled: false
                                },
                                tooltip: {
                                    formatter: function () {
                                        if(syqk != null){
                                            return '<b>' + tasks[this.y].name + '</b><br/>' +
                                                Highcharts.dateFormat('%H:%M', this.point.options.from) +
                                                ' - ' + Highcharts.dateFormat('%H:%M', this.point.options.to);
                                        }else{

                                        }
                                    }
                                },
                                plotOptions: {
                                    line: {
                                        lineWidth: 9,
                                        marker: {
                                            enabled: false
                                        },
                                        dataLabels: {
                                            enabled: 'false',
                                            align: 'left',
                                            formatter: function () {
                                                return this.point.options && this.point.options.label;
                                            }
                                        }
                                    }
                                },
                                series: series
                            });


                        },
                        error: function(XMLHttpRequest, textStatus, errorThrown) {
                            // alert(XMLHttpRequest.status);
                            // alert(XMLHttpRequest.readyState);
                            // alert(textStatus);
                            alert("提交失败，请检查");
                        },
                    });
                }



            });

        });
    },
};

jQuery(document).ready(function() {
    Meetingroom.init();

    // $(".form-datetime").datetimepicker({
    //     format:'yyyy/mm/dd',
    //     language: 'zh-CN',
    //     weekStart: 1,
    //     todayBtn:  1,
    //     autoclose: 1,
    //     todayHighlight: true,
    //     startView: 2,
    //     minView: 2,
    // });

});
// $(function(){
//     $("#green-button").click(function(){
//         console.log("aa");
//     }),
//
// });
// $(function(){
//     var hh={
//         mid:"402850815f959a89015f959afb240003",
//         mdate:"2017/10/04",
//     }
//
//     $.ajax({
//         url: "/admin/meeting/pointedMrecord2",
//         type: "get",
//         traditional: true,
//         contentType: "application/x-www-form-urlencoded; charset=UTF-8",
//         data:hh,
//         success: function(data) {
//             console.log(data);
//
//         },
//         error: function(XMLHttpRequest, textStatus, errorThrown) {
//             // alert(XMLHttpRequest.status);
//             // alert(XMLHttpRequest.readyState);
//             // alert(textStatus);
//             alert("提交失败，请检查");
//         },
//     });
//
//     // var trydata={
//     //     id:"3",
//     //     hys:"99999",
//     //     hysdd:"999",
//     //     fzr:"2222",
//     //     qt:"ahah9成功啦9ha"
//     // }
//     // $.ajax({
//     //     url: "/admin/meeting/updateMeetingroom",
//     //     type: "post",
//     //     traditional: true,
//     //     contentType: "application/x-www-form-urlencoded; charset=UTF-8",
//     //     data:trydata,
//     //     success: function (data) {
//     //         console.log("hahah");
//     //     },
//     //     error: function (XMLHttpRequest, textStatus, errorThrown) {
//     //         // alert(XMLHttpRequest.status);
//     //         // alert(XMLHttpRequest.readyState);
//     //         // alert(textStatus);
//     //         alert("提交失败，请检查");
//     //     },
//     // });
//
// });