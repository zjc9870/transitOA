
var roleName = $('#roleName').val();

var $searchBar = $('#searchBar'),
$searchResult = $('#searchResult'),
$searchText = $('#searchText'),
$searchInput = $('#searchInput'),
$searchClear = $('#searchClear'),
$searchCancel = $('#searchCancel');

$(document).ready(function () {
	var dps_count = 0;
	var yps_count = 0;
	
	var dps_cons = "";
	var yps_cons = "";
	AjaxTool.post('tabRequest', {
		tab: 'dps', ym: 'swps'},function (data) {
        if(data.success) {
            var str = "";
            dps_cons = data.content;
            if(dps_cons.length>dps_count+15){
            	$('#dps_loading').attr('style','display:block');
            }
            str+="<div class='weui-cell__hd'><div class='weui-cells__title'>待批示</div>";
            for(var i=dps_count;i<dps_cons.length;i++) {
            	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
            	str+="<div onclick='seePs(\""+ dps_cons[i].id +'\",\"'+'dps'+"\")' class='weui-cell__bd weui-cell_primary'>";
            	str+="<p>"+dps_cons[i].wjbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui-cell__ft'>";
            	str+="</div>";
            	str+="</a>";
            	if(i>=dps_count+15){
            		dps_count=dps_count+16;
            		break;
            	}
            }
            $('#weui_content_dps').html(str);
        }
    });
	AjaxTool.post('tabRequest', {
		tab: 'yps', ym: 'swps'},function (data) {
        if(data.success) {
            var str = "";
            yps_cons = data.content;
            if(yps_cons.length>yps_count+15){
            	$('#yps_loading').attr('style','display:block');
            }
            str+="<div class='weui-cell__hd'><div class='weui-cells__title'>已批示</div>";
            for(var i=yps_count;i<yps_cons.length;i++) {
            	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
            	str+="<div onclick='seePs(\""+ yps_cons[i].id +'\",\"'+'yps'+"\")' class='weui-cell__bd weui-cell_primary'>";
            	str+="<p>"+yps_cons[i].wjbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui-cell__ft'>";
            	str+="</div>";
            	str+="</a>";
            	if(i>=yps_count+15){
            		yps_count=yps_count+16;
            		break;
            	}
            }
            $('#weui_content_yps').html(str);
        }
    });
	$('#weui_body_yps').hide();
	$('#yps_tab').click(function() {
		$('#weui_body_yps').show();
		$('#weui_body_dps').hide();
	});
	$('#dps_tab').click(function() {
		$('#weui_body_yps').hide();
		$('#weui_body_dps').show();
	});
	
	
	
	$('#dps_loading').click(function() {
	  var str = "";
      if(dps_cons.length<=dps_count+15){
      	$('#dps_loading').attr('style','display:none');
      }
	  for(var i=dps_count;i<dps_cons.length;i++) {
      	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
      	str+="<div onclick='seePs(\""+ dps_cons[i].id +'\",\"'+'dps'+"\")' class='weui-cell__bd weui-cell_primary'>";
      	str+="<p>"+dps_cons[i].bt+"</p>";
      	str+="</div>";
      	str+="<div class='weui-cell__ft'>";
      	str+="</div>";
      	str+="</a>";
      	if(i+1==dps_cons.length){
      		$('#dps_loading').attr('style','display:none');
      	}
      	if(i>=dps_count+15){
      		dps_count=dps_count+16;
      		break;
      	}
      }
	  $("#weui_content_dps").append(str);
	  loading = false;
	});
	
	$('#yps_loading').click(function() {
		  var str = "";
          if(yps_cons.length<=yps_count+15){
          	$('#yps_loading').attr('style','display:none');
          }
		  for(var i=yps_count;i<yps_cons.length;i++) {
	      	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
	      	str+="<div onclick='seePs(\""+ yps_cons[i].id +'\",\"'+'dps'+"\")' class='weui-cell__bd weui-cell_primary'>";
	      	str+="<p>"+yps_cons[i].bt+"</p>";
	      	str+="</div>";
	      	str+="<div class='weui-cell__ft'>";
	      	str+="</div>";
	      	str+="</a>";
	      	if(i+1==yps_cons.length){
	      		$('#yps_loading').attr('style','display:none');
	      	}
	      	if(i>=yps_count+15){
	      		yps_count=yps_count+16;
	      		break;
	      	}
	      }
		  $("#weui_content_yps").append(str);
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
                	changeResult(this.value,dps_cons,yps_cons);
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


function changeResult(keyword,dps_cons,yps_cons){
	str = "";
	str += searchCons(keyword,dps_cons,"dps");
	str += searchCons(keyword,yps_cons,"yps");
	$searchResult.html("");
	$searchResult.html(str);
}

function searchCons(keyword,cons,lx){
	var str = "";
	for(var i=0;i<cons.length;i++) {
		if(contain(cons[i].bt,keyword)==1){
	      	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
	      	str+="<div onclick='seePs(\""+ cons[i].id +'\",\"'+lx+"\")' class='weui-cell__bd weui-cell_primary'>";
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


function seePs(id,tabId) {
	
	location.href="/weixin/draftSw/swPs?id="+id+"&lx="+tabId;
}