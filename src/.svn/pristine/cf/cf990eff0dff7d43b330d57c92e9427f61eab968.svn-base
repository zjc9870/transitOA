//人员选择框的实现
var pSelector = {
        init : function() {
            pSelector.initTreeView();
    },

    initSearch: function(){
        var form = $("<form id='s'></form>")
        var input = $("<input type='search' id='q'/>").appendTo(form);
        var button = $("<button type='submit'>搜索</button>").appendTo(form)
        $('#search-box').append(form);
        $("#s").submit(function(e) {
            e.preventDefault();
            $("#person-tree").jstree(true).search($("#q").val());
        });

    },

    initTreeView : function() {
        $.jstree.destroy();
        $('#person-tree').jstree({
            'plugins' : [ "wholerow", "checkbox", "types", "search" ],
            'core' : {
                "themes" : {
                    "responsive" : false
                },
                'data' : function(node, cb) {
                    AjaxTool.post("/getPersonTree",
                        null
                    ,function(data){
                        cb(data);
                    });
                },
            //     'data':[ {
            //         "id" : "gg",
            //         "text" : "高管",
            //         "state" : {
            //             "selected" : false,
            //             "opened" : false,
            //             "disabled" : false
            //         },
            //         "icon" : null,
            //         "sequence" : 0,
            //         "children" : [ {
            //             "id" : "dw",
            //             "text" : "党委",
            //             "state" : {
            //                 "selected" : false,
            //                 "opened" : false,
            //                 "disabled" : false
            //             },
            //             "icon" : null,
            //             "sequence" : 0,
            //             "children" : [ ]
            //         }, {
            //             "id" : "dsh",
            //             "text" : "董事会",
            //             "state" : {
            //                 "selected" : false,
            //                 "opened" : false,
            //                 "disabled" : false
            //             },
            //             "icon" : null,
            //             "sequence" : 0,
            //             "children" : [ ]
            //         }, {
            //             "id" : "jsh",
            //             "text" : "监事会",
            //             "state" : {
            //                 "selected" : false,
            //                 "opened" : false,
            //                 "disabled" : false
            //             },
            //             "icon" : null,
            //             "sequence" : 0,
            //             "children" : [ ]
            //         },{
            //             "id" : "zjl",
            //             "text" : "总经理",
            //             "state" : {
            //                 "selected" : false,
            //                 "opened" : false,
            //                 "disabled" : false
            //             },
            //             "icon" : null,
            //             "sequence" : 0,
            //             "children" : [ ]
            //         } ]
            //     } ,{
            //         "id" : "bm",
            //         "text" : "部门",
            //         "state" : {
            //             "selected" : false,
            //             "opened" : false,
            //             "disabled" : false
            //         },
            //         "icon" : null,
            //         "sequence" : 0,
            //         "children" : [ {
            //             "id": "bgs",
            //             "text": "办公室",
            //             "state": {
            //                 "selected": false,
            //                 "opened": false,
            //                 "disabled": false
            //             },
            //             "icon": null,
            //             "sequence": 0,
            //             "children": [ ]
            //         },{
            //             "id": "cwb",
            //             "text": "财务部",
            //             "state": {
            //                 "selected": false,
            //                 "opened": false,
            //                 "disabled": false
            //             },
            //             "icon": null,
            //             "sequence": 0,
            //             "children": [ ]
            //         },{
            //             "id": "rlzyb",
            //             "text": "人力资源部",
            //             "state": {
            //                 "selected": false,
            //                 "opened": false,
            //                 "disabled": false
            //             },
            //             "icon": null,
            //             "sequence": 0,
            //             "children": [ ]
            //         },{
            //             "id": "dqgzb",
            //             "text": "党群工作部",
            //             "state": {
            //                 "selected": false,
            //                 "opened": false,
            //                 "disabled": false
            //             },
            //             "icon": null,
            //             "sequence": 0,
            //             "children": [ ]
            //         },
            //             {
            //                 "id": "zcglb",
            //                 "text": "资产管理部",
            //                 "state": {
            //                     "selected": false,
            //                     "opened": false,
            //                     "disabled": false
            //                 },
            //                 "icon": null,
            //                 "sequence": 0,
            //                 "children": [ ]
            //             },{
            //                 "id": "sjb",
            //                 "text": "审计部",
            //                 "state": {
            //                     "selected": false,
            //                     "opened": false,
            //                     "disabled": false
            //                 },
            //                 "icon": null,
            //                 "sequence": 0,
            //                 "children": [ ]
            //             },{
            //                 "id": "qyglb",
            //                 "text": "企业管理部",
            //                 "state": {
            //                     "selected": false,
            //                     "opened": false,
            //                     "disabled": false
            //                 },
            //                 "icon": null,
            //                 "sequence": 0,
            //                 "children": [ ]
            //             },{
            //                 "id": "aqbwb",
            //                 "text": "安全保卫部",
            //                 "state": {
            //                     "selected": false,
            //                     "opened": false,
            //                     "disabled": false
            //                 },
            //                 "icon": null,
            //                 "sequence": 0,
            //                 "children": [ ]
            //             }]
            //     }, {
            //         "id": "dsgj",
            //         "text": "南京东山公交客运有限公司",
            //         "state": {
            //             "selected": false,
            //             "opened": false,
            //             "disabled": false
            //         },
            //         "icon": null,
            //         "sequence": 0,
            //         "children": [ ]
            //     }, {
            //         "id": "tsczqc",
            //         "text": "南京江宁公共交通集团有限公司通盛出租汽车分公司",
            //         "state": {
            //             "selected": false,
            //             "opened": false,
            //             "disabled": false
            //         },
            //         "icon": null,
            //         "sequence": 0,
            //         "children": []
            //     },{
            //         "id": "czjs",
            //         "text": "南京江宁公共交通场站建设有限公司",
            //         "state": {
            //             "selected": false,
            //             "opened": false,
            //             "disabled": false
            //         },
            //         "icon": null,
            //         "sequence": 0,
            //         "children": []
            //     },{
            //         "id": "xxkj",
            //         "text": "南京江宁公共交通信息科技有限公司",
            //         "state": {
            //             "selected": false,
            //             "opened": false,
            //             "disabled": false
            //         },
            //         "icon": null,
            //         "sequence": 0,
            //         "children": []
            //     },{
            //         "id": "qcfw",
            //         "text": "南京江宁公共交通汽车服务有限公司",
            //         "state": {
            //             "selected": false,
            //             "opened": false,
            //             "disabled": false
            //         },
            //         "icon": null,
            //         "sequence": 0,
            //         "children": []
            //     },{
            //         "id": "wz",
            //         "text": "南京江宁公共交通物资有限公司",
            //         "state": {
            //             "selected": false,
            //             "opened": false,
            //             "disabled": false
            //         },
            //         "icon": null,
            //         "sequence": 0,
            //         "children": []
            //     },{
            //         "id": "zxcfw",
            //         "text": "南京江宁公共交通自行车服务有限公司",
            //         "state": {
            //             "selected": false,
            //             "opened": false,
            //             "disabled": false
            //         },
            //         "icon": null,
            //         "sequence": 0,
            //         "children": []
            //     },{
            //         "id": "xcfw",
            //         "text": "南京江宁校车服务有限公司",
            //         "state": {
            //             "selected": false,
            //             "opened": false,
            //             "disabled": false
            //         },
            //         "icon": null,
            //         "sequence": 0,
            //         "children": []
            //     },{
            //         "id": "tywh",
            //         "text": "南京江宁公共交通体育文化有限公司",
            //         "state": {
            //             "selected": false,
            //             "opened": false,
            //             "disabled": false
            //         },
            //         "icon": null,
            //         "sequence": 0,
            //         "children": []
            //     }]



            },
            "types" : {
                "default" : {
                    "icon" : "fa fa-folder icon-state-warning icon-lg"
                },
                "file" : {
                    "icon" : "fa fa-file icon-state-warning icon-lg"
                }
            }
        });
        pSelector.initSearch();
    },
};

jQuery(document).ready(function() {
    pSelector.init();
});
