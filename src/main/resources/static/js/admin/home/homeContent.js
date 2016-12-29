;

$(document).on('click', '#htdb', function(){
	setTheClickButtonSelect($(this).parent());
	getAndDisplayHtsjByFl('dsp');
});
$(document).on('click', '#htyb', function(){
	setTheClickButtonSelect($(this).parent());
	getAndDisplayHtsjByFl('ysp');
});

//点击之后把点击的按钮变绿色
function setTheClickButtonSelect(button) {
	var tabs = button.parent().children();
	for (var int = 0; int < tabs.length; int++) {
        $(tabs[int]).removeClass("tab-box-selected");
	}
	button.addClass("tab-box-selected");
}
//根据显示的分类（合同待办 ：待审批（dsp），合同已办： 已审批（ysp））获取并显示合同的数据
function getAndDisplayHtsjByFl(fl){
	AjaxTool.get('contract/sqjlTab', {
        lx: fl, bz: 'sp'},function (data) {
        	if(data.success) {
        		 var leftUlStr = "<ul>";
        		 var rightUlStr = "<ul>"
                 var cons = data.content;
                 for(var i=0;(i<cons.length && i < 7);i++) {
                	 leftUlStr += "<li><span>"+cons[i].htbt+"</sapn></li>";
                	 rightUlStr += "<li><span>"+cons[i].userName+"&nbsp;"+cons[i].date+"</sapn></li>";
                  }
                 leftUlStr += "</ul>";
                 rightUlStr += "</ul>"; 
                  $('#leftLc').html(leftUlStr);
                  $('#rightLc').html(rightUlStr);
        	}
        }
	)
}

//日历插件 备忘录
jQuery(document).ready(function() {
	var userid = $('input[name = "userId"]').val();
	$(".memo").Memo({
		requestQuery : {
			url : "memo/getMemoItems",
			param : {
				userId : userid
			}
		},
		requestAdd : {
			url:"memo/addMemoItem",
			param : {
				userId : userid
			}
		},
		requestDelete : {
			url:"memo/deleteMemoItem"
		},
		requestUpdate : {
			url:"memo/updateMemoItem"
		}
	});
});

