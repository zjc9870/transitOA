var SweetAlert = function() {
	
	var defaultConfig= {
			allowOutsideClick: false,
			showConfirmButton: true,
			showCancelButton: false,
			confirmButtonClass: "btn-info",
			cancelButtonClass: "btn-danger",
			closeOnConfirm: true,
			closeOnCancel: true,
			confirmButtonText: "确定",
			cancelButtonText: "取消",
	}
	
	var initSweetAlert=function(sweetalertId){
		$(sweetalertId).each(function(){
    		var sa_title = $(this).data('title');
    		var sa_message = $(this).data('message');
    		var sa_type = $(this).data('type');	
    		var sa_allowOutsideClick = $(this).data('allow-outside-click');
    		var sa_showConfirmButton = $(this).data('show-confirm-button');
    		var sa_showCancelButton = $(this).data('show-cancel-button');
    		var sa_closeOnConfirm = $(this).data('close-on-confirm');
    		var sa_closeOnCancel = $(this).data('close-on-cancel');
    		var sa_confirmButtonText = $(this).data('confirm-button-text');
    		var sa_cancelButtonText = $(this).data('cancel-button-text');
    		var sa_popupTitleSuccess = $(this).data('popup-title-success');
    		var sa_popupMessageSuccess = $(this).data('popup-message-success');
    		var sa_popupTitleCancel = $(this).data('popup-title-cancel');
    		var sa_popupMessageCancel = $(this).data('popup-message-cancel');
    		var sa_confirmButtonClass = $(this).data('confirm-button-class');
    		var sa_cancelButtonClass = $(this).data('cancel-button-class');
    	
    		$(this).click(function(){
    			swal({
				  title: sa_title,
				  text: sa_message,
				  type: sa_type,
				  allowOutsideClick: sa_allowOutsideClick,
				  showConfirmButton: sa_showConfirmButton,
				  showCancelButton: sa_showCancelButton,
				  confirmButtonClass: sa_confirmButtonClass,
				  cancelButtonClass: sa_cancelButtonClass,
				  closeOnConfirm: sa_closeOnConfirm,
				  closeOnCancel: sa_closeOnCancel,
				  confirmButtonText: sa_confirmButtonText,
				  cancelButtonText: sa_cancelButtonText,
				},
				function(isConfirm){
			        if (isConfirm){
			        	swal(sa_popupTitleSuccess, sa_popupMessageSuccess, "success");
			        } else {
						swal(sa_popupTitleCancel, sa_popupMessageCancel, "error");
			        }
				});
    		});
    	});
	}
	
	var initSwal = function(title,text,type,config,confirm,notConfirm){
		if(config.allowOutsideClick == undefined){
			config.allowOutsideClick=defaultConfig.allowOutsideClick;
		}
		if(config.showConfirmButton == undefined){
			config.showConfirmButton=defaultConfig.showConfirmButton;
		}
		if(config.showCancelButton == undefined){
			config.showCancelButton=defaultConfig.showCancelButton;
		}
		if(config.confirmButtonClass == undefined){
			config.confirmButtonClass=defaultConfig.confirmButtonClass;
		}
		if(config.cancelButtonClass == undefined){
			config.cancelButtonClass=defaultConfig.cancelButtonClass;
		}
		if(config.closeOnConfirm == undefined){
			config.closeOnConfirm=defaultConfig.closeOnConfirm;
		}
		if(config.closeOnCancel == undefined){
			config.closeOnCancel=defaultConfig.closeOnCancel;
		}
		if(config.confirmButtonText == undefined){
			config.confirmButtonText=defaultConfig.confirmButtonText;
		}
		if(config.cancelButtonText == undefined){
			config.cancelButtonText=defaultConfig.cancelButtonText;
		}
		swal({
			  title: title,
			  text: text,
			  type: type,
			  allowOutsideClick: config.allowOutsideClick,
			  showConfirmButton: config.showConfirmButton,
			  showCancelButton: config.showCancelButton,
			  confirmButtonClass: config.confirmButtonClass,
			  cancelButtonClass: config.cancelButtonClass,
			  closeOnConfirm: config.closeOnConfirm,
			  closeOnCancel: config.closeOnCancel,
			  confirmButtonText: config.confirmButtonText,
			  cancelButtonText: config.cancelButtonText,
			},
			function(isConfirm){
		        if (isConfirm){
		        	if(confirm!=null){
		        		confirm();
		        	}
		        } else {
		        	if(notConfirm!=null){
		        		notConfirm();
		        	}
		        }
			});
	}
	
	var initSwalInput = function(title,text,inputPlaceholder,confirm){
		swal({   
			title: title,   
            text: text,   
            type: "input",   
            showCancelButton: true,   
            closeOnConfirm: true,   
            animation: "slide-from-top",   
            inputPlaceholder: inputPlaceholder,
            confirmButtonText: "确定",
			cancelButtonText: "取消",
            },function(inputValue){   
                if (inputValue === false) {
                	return false;
                }      
                if (inputValue === "") {     
                	swal.showInputError("请输入内容");     
                	return false   
                }
                if(confirm){
                	confirm(inputValue);
           }
       });
	}

	var initSwalContent = function (title,content,confirm) {
		swal({
			title: title,
			type: "content",
			content: content,
			showCancelButton: true,
			closeOnConfirm: true,
			animation: "slide-from-top",
			confirmButtonText: "确定",
			cancelButtonText: "取消"
		},function (isConfirm) {
			if(isConfirm && confirm) {
				confirm();
			}
		})
	}

	var initSwalPselect = function (title,content,confirm) {
		swal({
			title: title,
			type: "content",
			content: content,
			showCancelButton: true,
			closeOnConfirm: true,
			animation: "slide-from-top",
			confirmButtonText: "确定",
			cancelButtonText: "取消"
		},function (isConfirm) {
			if(isConfirm && confirm) {
				confirm();
			}
		})
		pSelector.init();
		$('.sweet-alert').css('margin-top','-200px');
		$('.sweet-alert-content').css('text-align','left');
		$('#person-tree').css({'max-height':'270px','overflow':'auto'});
	}

	return {
		init: initSweetAlert,
    	swal:initSwal,
    	swalInput: initSwalInput,
		swalContent: initSwalContent,
		swalPselect: initSwalPselect
	}
}();