/**
 * Created by qifeng on 17/11/17.
 */
var count=1;
var nodeName = null;
var allNodeName = [];
var allRoles;
var allNodeId;
var selectedRoleId=[];

$(".add-button").click(function(){
    SweetAlert.swalInput("增加节点","请输入节点名称","",function(inputValue){
        if(allNodeName.length !=0 && isContains(inputValue)){
            Toast.show("对象添加失败",'节点描述重复');
            return;
        }
        allNodeName.push(inputValue);
        var table = document.getElementById("node_table");

        oneRow = table.insertRow();//插入一行
        cell1= oneRow.insertCell();//单单插入一行是不管用的，需要插入单元格
        cell2=oneRow.insertCell();
        cell1.innerHTML = count++;
        cell2.innerHTML=inputValue;
    });
});
function isContains(inputValue) {
    for (var i=0;i<allNodeName.length;i++){
        if (allNodeName[i] == inputValue){
            return true;
        }
    }
    return false;
}
$('#node-tbody').on('click',function(e){
    var selectedTr = e.target;
    nodeName=selectedTr.innerHTML;
    console.log(nodeName+"name");


});

$(".update-button").click(function(){
    if(nodeName==null){
        Toast.show("对象提醒","请选择修改节点对象");
        return;
    }

    var nodeId = null;
    var i=0;
    if(Math.floor(nodeName) == nodeName){
        nodeId = nodeName;
    }
    else{
        for (;i<allNodeName.length;i++){
            if (allNodeName[i] == nodeName){
                nodeId = i+1;
                break;
            }
        }
    }
    console.log(nodeId+"nodeId");
    SweetAlert.swalInput("修改节点对象","节点"+nodeId,"",function(inputValue){
        var table = document.getElementById("node_table");
        console.log(nodeId);
        table.rows[nodeId+1].cells[1].innerHTML = inputValue;
        allNodeName[i] = inputValue;
    });
});
$(".delete-button").click(function () {
    if (count ==1){
        return;
    }
    var table = document.getElementById("node_table");
    table.deleteRow(count);
    allNodeName.pop();
    count--;

});
$(".save-button").click(function () {
    var nodeNames = new String();
    if (allNodeName.length <2){
        Toast.show("节点对象添加失败",'节点数目小于2');
        return;
    }
    for(var i=0;i<allNodeName.length;i++){
        nodeNames = nodeNames +" "+allNodeName[i];
    }
    AjaxTool.post("processConfiguration/save","&allNodeName="+allNodeName,function (response) {
        if (response.result){
            Toast.show("配置审批节点提醒",'节点配置成功');


            var leftStr = "";
            var rightStr = "";
            allNodeId = response.obj;
            for (var i=0;i<response.obj.length;i++){
                // allNodeId +=" "+response.obj[i];
                var id = i+1;
                var nodeId = response.obj[i];
                leftStr +="<ul><label name ='"+nodeId+"' style='color: green'>"+"节点"+id+"</label></ul>";
                rightStr +="<ul> <select name='roleName"+id+"'  id='roleName"+id+"' th:multiple='true' ></select></ul>";
                rightStr +="</ul>";
            }

            $("#leftNodeId").html(leftStr);
            $("#rightRole").html(rightStr);

            for (var j=0;j<response.obj.length;j++){
                for (var i=0;i<allRoles.length;i++){
                    var objSelectNow=document.getElementById("roleName"+(j+1));
                    var objOption = document.createElement("OPTION");
                    objOption.text= allRoles[i].name;
                    objOption.value=allRoles[i].id;
                    objSelectNow.options.add(objOption);
                }
            }


        }
        else{
            Toast.show("配置审批节点失败",response.message);
        }
    });
});
$('.save-button2').click(function () {
    var a=$("#node_role_form").serializeArray();
    for (var i=0;i<a.length;i++){
        selectedRoleId.push(a[i].value);
    }

    AjaxTool.post("processConfiguration/bindNodeRole","&nodeId="+allNodeId+"&roleId="+selectedRoleId,function (response) {
        selectedRoleId = [];
        if (response.result){
            Toast.show("绑定节点和角色提醒",response.message);
        }
        else{
            Toast.show("绑定节点和角色失败",response.message);
        }
    });
});
$(document).ready(function() {

    AjaxTool.get("processConfiguration/getRoles",null, function(response) {
        if (response.result){
            allRoles = response.obj;
        }
    });
});
