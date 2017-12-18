$('#btnSave').click(function(){
    //获取模态框数据
    var hydd = $("#hydd option:selected").text();
    var hys = $("#hys option:selected").text();
    var hyrq = $("#hyrq").val().toString();
    var kssj = $("#kssj").val().toString();
    var jssj = $("#jssj").val().toString();
    if(hydd == "请选择" || hys == "请选择" || hyrq == "" || kssj == "" || jssj == ""){
        alert("请填写完整信息！");
        return false;
    }else{
        $.ajax({type:'post',url:'meeting/checkTime',data:"hydd="+hydd+"&hys="+hys+"&hyrq="+hyrq+"&kssj="+kssj+"&jssj="+jssj+"&meetingId="+$("#id").val(),dataType:'json',
            success:function(data){
                isRight = data;
                if(isRight == true){
                    $('#ddsj').val( hydd + hys + " " + hyrq + " " + kssj + "-" + jssj);
                    $('#myModal').modal("hide");
                    return true;
                }else{
                    $("#error").show();
                    return false;
                }
            }
        });
    }
})

$('.changeStyle').change(function(){
    $("#error").hide();
})

function keypress() //textarea输入长度处理
{
    var text1=document.getElementById("hynr").value;
    var len;//记录剩余字符串的长度
    if(text1.length>=300)//textarea控件不能用maxlength属性，就通过这样显示输入字符数了
    {
        document.getElementById("hynr").value=text1.substr(0,300);
        len=0;
    }
    else
    {
        len=300-text1.length;
    }
    var show="(你还可以输入"+len+"个字)";
    document.getElementById("pinglun").innerText=show;
}

function loadMeetingRoom(){
    $.ajax({type:'get', url:'meeting/selectHydd', dataType:'json',
        success:function(data){
            hydds=data.content;
            // op = "<option value=\"\" disabled=\"true\">请选择</option>";
            // $("#hydd").append(op);
            var CompareHydd = $("#hydd").val()
            $("#hydd").empty();
            for (i = 0; i < hydds.length;i++){
                if(hydds[i] == CompareHydd){
                    option="<option value='"+hydds[i]+"' selected=\"true\">"+hydds[i]+"</option>";
                }else{
                    option="<option value='"+hydds[i]+"'>"+hydds[i]+"</option>";
                }
                $("#hydd").append(option);
            }
        },
        error:function(){
            alert("failed.");
        }
    });
    var hydd = $("#hydd").val();
    $.ajax({type:'get', url:'meeting/selectHys',data:"hydd=" + hydd, dataType:'json',
        success:function(data){

            hys=data.content;
            var CompareHys = $("#hys").val();
            $("#hys").empty();
            // op = "<option value=\"\" selected=\"true\" disabled=\"true\">请选择</option>";
            // $("#hys").append(op);
            for (i = 0; i < hys.length;i++){
                if(hys[i] == CompareHys){
                    option="<option value='"+hys[i]+"' selected=\"true\">"+hys[i]+"</option>";
                }else{
                    option="<option value='"+hys[i]+"'>"+hys[i]+"</option>";
                }
                $("#hys").append(option);
            }
        },
        error:function(){
            alert("failed.");
        }
    });
}

$("#hydd").change(
    function () {
        var hydd = $("#hydd").val();
        $.ajax({type:'get', url:'meeting/selectHys',data:"hydd=" + hydd, dataType:'json',
            success:function(data){
                //   console.log(data.content);
                hys=data.content;
                $("#hys").empty();
                op = "<option value=\"\" selected=\"true\" disabled=\"true\">请选择</option>";
                $("#hys").append(op);
                for (i = 0; i < hys.length;i++){
                    option="<option value='"+hys[i]+"'>"+hys[i]+"</option>";
                    $("#hys").append(option);

                }
            },
            error:function(){
                alert("failed.");
            }
        });
    }
)

var syqk = null;
function searchSyqk(){
    var hydd = $("#hydd option:selected").text();
    var hys = $("#hys option:selected").text();
    var hyrq = $("#hyrq").val();

    if(hydd == "请选择" || hys == "请选择" || hyrq == ""){
        alert("请填写完整信息！");
        return false;
    }else{
        AjaxTool.post('meeting/meetingRoomSyqk',"hydd="+hydd+"&hys="+hys+"&hyrq="+hyrq,function (data) {
            if (data.success){
                syqk = data.content;
                var tasks = [{
                    name: hydd+hys,
                    intervals: []
                }];
                if (syqk.length != 0){
                    for(i = 0; i < syqk.length; i++){
                        ks = syqk[i][0];
                        ksStr = ks.split(":");
                        ksHour = ksStr[0];
                        ksMin = ksStr[1];
                        js = syqk[i][1];
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
                }
// re-structure the tasks into line seriesvar series = [];
                var series = [];
                $.each(tasks.reverse(), function (i, task) {
                    var item = {
                        // name: task.name,
                        name: hydd+hys,
                        data: []
                    };
                    $.each(task.intervals, function (j, interval) {
                        if (syqk.length != 0){
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
                            // add a null value between intervals
                            if (task.intervals[j + 1]) {
                                item.data.push(
                                    [(interval.to + task.intervals[j + 1].from) / 2, null]
                                );
                            }
                        }else{
                            item.data.push({
                                x: interval.from,
                                y: i,
                                //label: interval.label,
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

// create the chart

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
                            if(syqk.length != 0){
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
            }
        })
    }
}

(function () {
    function createDelete() {
        var span = document.createElement('span');
        span.innerHTML = 'x';
        span.setAttribute('style','margin-left:10px;color:red;font-weight:bold;cursor:pointer');
        span.setAttribute('class','delete');
        return span;
    }
    function deletefj() {
        var deletes = document.getElementsByClassName('delete');
        for(var i=0;i<deletes.length;i++) {
            deletes[i].onclick = function () {
                ids.splice(ids.indexOf(this.id),1);
                this.parentNode.setAttribute('style','display:none;');
            }
        }
    }



    var ids = [];
    var s1 = createDelete();
    var fjlbLi = $('#fjlb li');
    fjlbLi.append(s1);
    for (var i=0;i<fjlbLi.length;i++) {
        ids.push(fjlbLi[i].id);
    }
    deletefj();


    $('#uploadFile').click(function () {
        DatatableTool.modalShow("#upload-modal", "#fileUploadForm");
        var uploader = $("#fileUploadForm").FileUpload({
            url: "meeting/uploadMeetingAttachment",
            isMultiFile: true,
        });
        uploader.done(function(data) {
            ids.push(data.result.id);
            var li = document.createElement('li');
            var s2 = createDelete();
            s2.setAttribute('id',data.result.id);
            li.innerHTML = data.result.name;
            li.appendChild(s2);
            $('#fjlb').append(li);
            deletefj();
        });
    });


    //附件查看
    var n = 1;
    $('#fjck').click(
        function () {
            var attachList = JSON.parse($('#attachList').val());
            var meetingId = $('#meetingId').val();
            if(attachList.length == 0) {
                alert('无附件');
            } else {
                if(n%2==1) {
                    for (var i = 0; i < attachList.length; i++) {
                        var li = document.createElement('li');
                        var div = document.createElement('div');
                        div.innerHTML = attachList[i].name;
                        div.setAttribute('style', 'cursor:pointer;');
                        div.setAttribute('class', 'attachment');
                        li.appendChild(div);
                        li.setAttribute('class', 'attList')
                        this.parentNode.appendChild(li);
                        div.id = attachList[i].id;              //将变量保存给对象,避免循环闭包
                        div.onclick = function () {
                            window.location = "meeting/meetingAttachmentDownload?attachmentId=" + this.id+"&meetingId="+meetingId;
                            // window.location = "attachment/download?id=" + this.id;
                        }
                    }
                }
                else {
                    $('.attList').hide();
                }
                n += 1;
            }
        }
    );




    //判断两个引用该js的文件，如果是可编辑页面调用该方法
    if(document.getElementById("hynr")){
        keypress();
    }



    //提交
    $('#tj').click(function() {
        var formContents = document.getElementsByClassName('form-content');
        for(var i=0; i<formContents.length; i++ ) {
            if(/^\s*$/.test(formContents[i].value)) {
                alert('请填写完整信息!');
                return false;
            }
        }
        console.log( $('#m_apply_form').serialize());
        AjaxTool.post('meeting/saveMeeting', $('#m_apply_form').serialize()+ '&' +$('#form_data').serialize()+"&bczl="+this.id+"&fileId="+ids, function (data) {
                alert(data.message);
                if(data.success) toSqjl();
            }
        )
    });

    //保存
    $('#bc').click(function() {
        var formContents = document.getElementsByClassName('form-content');
        for(var i=0; i<formContents.length; i++ ) {
            if(/^\s*$/.test(formContents[i].value)) {
                alert('请填写完整信息!');
                return false;
            }
        }
        console.log( $('#m_apply_form').serialize());
        AjaxTool.post('meeting/saveMeeting', $('#m_apply_form').serialize()+ '&' +$('#form_data').serialize()+"&bczl="+this.id+"&fileId="+ids, function (data) {
                alert(data.message);
                if(data.success) toSqjl();
            }
        )
    });

    var date = new Date();
    $('#hyrq').datetimepicker({
        //    startDate: date,
        format:'yyyy/mm/dd',
        language: 'zh-CN',
        weekStart: 1,
        todayBtn:  1,
        autoclose: 1,
        todayHighlight: true,
        startView: 2,
        minView: 2,
    }).on('changeDate', function(ev){
        var date1 = new Date(($("#hyrq").val()).replace("/",","));
        var date2 = new Date(date1.getTime()+24*60*60*1000-1);
        $('#kssj').datetimepicker('setStartDate', date1);
        $('#kssj').datetimepicker('setEndDate', date2);
        $('#jssj').datetimepicker('setStartDate', date1);
        $('#jssj').datetimepicker('setEndDate', date2);
    });

    $('#kssj').datetimepicker({
        startDate: new Date(date.toLocaleDateString()),
        format:'hh:ii',
        language: 'zh-CN',
        autoclose: 1,
        startView: 1,
        maxView: 1,
    }).on('changeDate',function (ev) {
        if($("#hyrq").val() == ""){
            var newdate = new Date();
            var str = newdate.toLocaleDateString().replace("/",",");
            var str2 = str+" "+$("#kssj").val()+":00";
            $('#kssj').datetimepicker('setStartDate',new Date(str));
            $('#jssj').datetimepicker('setStartDate',new Date(str2));
        }else{
            var string = $("#hyrq").val().replace("/",",")+" "+$("#kssj").val()+":00";
            var date = new Date(string)
            $('#jssj').datetimepicker('setStartDate',date);
        }
    });

    $('#jssj').datetimepicker({
        format:'hh:ii',
        language: 'zh-CN',
        autoclose: 1,
        startView: 1,
        maxView: 1,
    }).on('changeDate',function (ev) {
        if($("#hyrq").val() == ""){
            var newdate = new Date();
            var str = newdate.toLocaleDateString().replace("/",",")+" "+$("#jssj").val()+":00";
            $('#kssj').datetimepicker('setEndDate',new Date(str));
        }else{
            var string = $("#hyrq").val().replace("/",",")+" "+$("#jssj").val()+":00";
            var date = new Date(string)
            $('#kssj').datetimepicker('setEndDate',date);
        }
    });

    function toSqjl() {
        AjaxTool.getHtml('meeting/sqjl',function (html) {
            $('.page-content').html(html);
        });
    }

    $('#back').click(function () {
        var tabId = ($('#back').data('tabId'));
        AjaxTool.getHtml('meeting/sqjl',function (html) {
            $('.page-content').html(html);
            $('#'+tabId).trigger('click');
        });
    });

})();


