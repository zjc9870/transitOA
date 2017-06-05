
var roleName = $('#roleName').val();
$(document).ready(function () {
	var dsp_count = 0;
	var ysp_count = 0;
	var yth_count = 0;
	
	var dsp_cons = "";
	var ysp_cons = "";
	var yth_cons = "";
	AjaxTool.get('sqjlTab', {
        lx: 'dsp', bz: 'sp'},function (data) {
        if(data.success) {
            var str = "";
            dsp_cons = data.content;
            if(dsp_cons.length>dsp_count+15){
            	$('#dsp_loading').attr('style','display:block');
            }
            str+="<div class='weui-cell__hd'><div class='weui-cells__title'>待审批</div>";
            for(var i=dsp_count;i<dsp_cons.length;i++) {
            	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApprove(\""+ dsp_cons[i].id +'\",\"'+'dsp'+"\")' class='weui-cell__bd weui-cell_primary'>";
            	str+="<p>"+dsp_cons[i].bt+"</p>";
            	str+="</div>";
            	str+="<div class='weui-cell__ft'>";
            	str+="</div>";
            	str+="</a>";
            	if(i>=dsp_count+15){
            		dsp_count=dsp_count+16;
            		break;
            	}
            }
            $('#weui_content_dsp').html(str);
        }
    });
	AjaxTool.get('sqjlTab', {
        lx: 'ysp', bz: 'sp'},function (data) {
        if(data.success) {
            var str = "";
            ysp_cons = data.content;
            if(ysp_cons.length>ysp_count+15){
            	$('#ysp_loading').attr('style','display:block');
            }
            str+="<div class='weui-cell__hd'><div class='weui-cells__title'>已审批</div>";
            for(var i=ysp_count;i<ysp_cons.length;i++) {
            	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApprove(\""+ ysp_cons[i].id +'\",\"'+'ysp'+"\")' class='weui-cell__bd weui-cell_primary'>";
            	str+="<p>"+ysp_cons[i].bt+"</p>";
            	str+="</div>";
            	str+="<div class='weui-cell__ft'>";
            	str+="</div>";
            	str+="</a>";
            	if(i>=ysp_count+15){
            		ysp_count=ysp_count+16;
            		break;
            	}
            }
            $('#weui_content_ysp').html(str);
        }
    });
	$('#weui_body_ysp').hide();
	$('#ysp_tab').click(function() {
		$('#weui_body_ysp').show();
		$('#weui_body_dsp').hide();
	});
	$('#dsp_tab').click(function() {
		$('#weui_body_ysp').hide();
		$('#weui_body_dsp').show();
	});
	
	
	
	$('#dsp_loading').click(function() {
	  var str = "";
      if(dsp_cons.length<=dsp_count+15){
      	$('#dsp_loading').attr('style','display:none');
      }
	  for(var i=dsp_count;i<dsp_cons.length;i++) {
      	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
      	str+="<div onclick='seeApprove(\""+ dsp_cons[i].id +'\",\"'+'dsp'+"\")' class='weui-cell__bd weui-cell_primary'>";
      	str+="<p>"+dsp_cons[i].bt+"</p>";
      	str+="</div>";
      	str+="<div class='weui-cell__ft'>";
      	str+="</div>";
      	str+="</a>";
      	if(i+1==dsp_cons.length){
      		$('#dsp_loading').attr('style','display:none');
      	}
      	if(i>=dsp_count+15){
      		dsp_count=dsp_count+16;
      		break;
      	}
      }
	  $("#weui_content_dsp").append(str);
	  loading = false;
	});
	
	$('#ysp_loading').click(function() {
		  var str = "";
          if(ysp_cons.length<=ysp_count+15){
          	$('#ysp_loading').attr('style','display:none');
          }
		  for(var i=ysp_count;i<ysp_cons.length;i++) {
	      	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
	      	str+="<div onclick='seeApprove(\""+ ysp_cons[i].id +'\",\"'+'dsp'+"\")' class='weui-cell__bd weui-cell_primary'>";
	      	str+="<p>"+ysp_cons[i].bt+"</p>";
	      	str+="</div>";
	      	str+="<div class='weui-cell__ft'>";
	      	str+="</div>";
	      	str+="</a>";
	      	if(i+1==ysp_cons.length){
	      		$('#ysp_loading').attr('style','display:none');
	      	}
	      	if(i>=ysp_count+15){
	      		ysp_count=ysp_count+16;
	      		break;
	      	}
	      }
		  $("#weui_content_ysp").append(str);
		});
	
});

function seeApprove(id,tabId) {
    	location.href="/weixin/document/gwspckxq?id="+id+"&lx="+tabId;
}/**
 * 
 */