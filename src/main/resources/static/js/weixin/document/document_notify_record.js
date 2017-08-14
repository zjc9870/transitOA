
var roleName = $('#roleName').val();

var $searchBar = $('#searchBar'),
$searchResult = $('#searchResult'),
$searchText = $('#searchText'),
$searchInput = $('#searchInput'),
$searchClear = $('#searchClear'),
$searchCancel = $('#searchCancel');

$(document).ready(function () {
	var wd_count = 0;
	var yd_count = 0;
	
	var wd_cons = "";
	var yd_cons = "";
	AjaxTool.get('fwtzRecordTab', {
        lx: 'wd'},function (data) {
        if(data.success) {
            var str = "";
            wd_cons = data.content;
            if(wd_cons.length>wd_count+15){
            	$('#wd_loading').attr('style','display:block');
            }
            str+="<div class='weui-cell__hd'><div class='weui-cells__title'>未读</div>";
            for(var i=wd_count;i<wd_cons.length;i++) {
            	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApprove(\""+ wd_cons[i].id +'\",\"'+'wd'+"\")' class='weui-cell__bd weui-cell_primary'>";
            	str+="<p>"+wd_cons[i].bt+"</p>";
            	str+="</div>";
            	str+="<div class='weui-cell__ft'>";
            	str+="</div>";
            	str+="</a>";
            	if(i>=wd_count+15){
            		wd_count=wd_count+16;
            		break;
            	}
            }
            $('#weui_content_wd').html(str);
        }
    });
	AjaxTool.get('fwtzRecordTab', {
        lx: 'yd'},function (data) {
        if(data.success) {
            var str = "";
            yd_cons = data.content;
            if(yd_cons.length>yd_count+15){
            	$('#yd_loading').attr('style','display:block');
            }
            str+="<div class='weui-cell__hd'><div class='weui-cells__title'>已读</div>";
            for(var i=yd_count;i<yd_cons.length;i++) {
            	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApprove(\""+ yd_cons[i].id +'\",\"'+'yd'+"\")' class='weui-cell__bd weui-cell_primary'>";
            	str+="<p>"+yd_cons[i].bt+"</p>";
            	str+="</div>";
            	str+="<div class='weui-cell__ft'>";
            	str+="</div>";
            	str+="</a>";
            	if(i>=yd_count+15){
            		yd_count=yd_count+16;
            		break;
            	}
            }
            $('#weui_content_yd').html(str);
        }
    });
	$('#weui_body_yd').hide();
	$('#yd_tab').click(function() {
		$('#weui_body_yd').show();
		$('#weui_body_wd').hide();
	});
	$('#wd_tab').click(function() {
		$('#weui_body_yd').hide();
		$('#weui_body_wd').show();
	});
	
	
	
	$('#wd_loading').click(function() {
	  var str = "";
      if(wd_cons.length<=wd_count+15){
      	$('#wd_loading').attr('style','display:none');
      }
	  for(var i=wd_count;i<wd_cons.length;i++) {
      	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
      	str+="<div onclick='seeApprove(\""+ wd_cons[i].id +'\",\"'+'wd'+"\")' class='weui-cell__bd weui-cell_primary'>";
      	str+="<p>"+wd_cons[i].bt+"</p>";
      	str+="</div>";
      	str+="<div class='weui-cell__ft'>";
      	str+="</div>";
      	str+="</a>";
      	if(i+1==wd_cons.length){
      		$('#wd_loading').attr('style','display:none');
      	}
      	if(i>=wd_count+15){
      		wd_count=wd_count+16;
      		break;
      	}
      }
	  $("#weui_content_wd").append(str);
	  loading = false;
	});
	
	$('#yd_loading').click(function() {
		  var str = "";
          if(yd_cons.length<=yd_count+15){
          	$('#yd_loading').attr('style','display:none');
          }
		  for(var i=yd_count;i<yd_cons.length;i++) {
	      	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
	      	str+="<div onclick='seeApprove(\""+ yd_cons[i].id +'\",\"'+'wd'+"\")' class='weui-cell__bd weui-cell_primary'>";
	      	str+="<p>"+yd_cons[i].bt+"</p>";
	      	str+="</div>";
	      	str+="<div class='weui-cell__ft'>";
	      	str+="</div>";
	      	str+="</a>";
	      	if(i+1==yd_cons.length){
	      		$('#yd_loading').attr('style','display:none');
	      	}
	      	if(i>=yd_count+15){
	      		yd_count=yd_count+16;
	      		break;
	      	}
	      }
		  $("#weui_content_yd").append(str);
		});
	
		//搜索
	
    $(function(){

        function hideSearchResult(){
            $searchResult.hide();
            $searchInput.val('');
        }
        function cancelSearch(){
            hideSearchResult();
            $searchBar.removeClass('weui-search-bar_focusing');
            $searchText.show();
        }

        $searchText.on('click', function(){
            $searchBar.addClass('weui-search-bar_focusing');
            $searchInput.focus();
        });
        $searchInput
            .on('blur', function () {
                if(!this.value.length) cancelSearch();
            })
            .on('input', function(){
                if(this.value.length) {
                	changeResult(this.value,wd_cons,yd_cons);
                    $searchResult.show();
                } else {
                    $searchResult.hide();
                }
            })
        ;
        $searchClear.on('click', function(){
            hideSearchResult();
            $searchInput.focus();
        });
        $searchCancel.on('click', function(){
            cancelSearch();
            $searchInput.blur();
        });
    });
	
});


function changeResult(keyword,wd_cons,yd_cons){
	str = "";
	str += searchCons(keyword,wd_cons,"wd");
	str += searchCons(keyword,yd_cons,"yd");
	$searchResult.html("");
	$searchResult.html(str);
}

function searchCons(keyword,cons,lx){
	var str = "";
	for(var i=0;i<cons.length;i++) {
		if(contain(cons[i].bt,keyword)==1){
	      	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
	      	str+="<div onclick='seeApprove(\""+ cons[i].id +'\",\"'+lx+"\")' class='weui-cell__bd weui-cell_primary'>";
	      	str+="<p>"+cons[i].bt+"</p>";
	      	str+="</div>";
	      	str+="<div class='weui-cell__ft'>";
	      	str+="</div>";
	      	str+="</a>";
		}
      }
	return str;
}

function contain(str,substr){
	if(str){
		if(str.indexOf(substr) >= 0 ){
			   return 1;
			}else{
				return 0;
			}
	}
	
}


function seeApprove(id,tabId) {
    	location.href="/weixin/document/gwspckxq?id="+id+"&lx="+tabId;
}/**
 * 
 */