Cms.f7 = {};
$.ajaxSettings.traditional=true;
Cms.F7Multi = function(url, name, options) {
	options = options || {};
	var idHidden = $("#" + name);
	var numberHidden = $("#" + name + "Number");
	var nameHidden = $("#" + name + "Name");
	var numberDisplay = $("#" + name + "NumberDisplay");
	var nameDisplay = $("#" + name + "NameDisplay");
	var f7Button = $("#" + name + "Button");
	var dialogDiv = null;
	var ids = [];
	var numbers = [];
	var names = [];
	var init = function() {
		idHidden.find("input[name='"+name+"']").each(function() {
			ids[ids.length] = $(this).val();
		});
		numberHidden.find("input[name='"+name+"Number']").each(function() {
			numbers[numbers.length] = $(this).val();
			numberDisplay.val(numberDisplay.val()+","+$(this).val());
		});
		nameHidden.find("input[name='"+name+"Name']").each(function() {
			names[names.length] = $(this).val();
			nameDisplay.val(nameDisplay.val()+","+$(this).val());
		});
		if(numberDisplay.length>0) {
			var numberValue = numberDisplay.val();
			var numberValueLength = numberValue.length;
			if(numberValueLength>0) {
				numberDisplay.val(numberValue.substring(1,numberValueLength));
			}			
		}
		if(nameDisplay.length>0) {
			var nameValue = nameDisplay.val();
			var nameValueLength = nameValue.length;
			if(nameValueLength>0) {
				nameDisplay.val(nameValue.substring(1,nameValueLength));
			}			
		}	
	};
	init();

	var destroy = function() {
		dialogDiv.dialog("destroy");
		dialogDiv.empty();
	};
	var applayValue = function() {
		idHidden.empty();
		numberHidden.empty();
		nameHidden.empty();
		numberDisplay.val("");
		nameDisplay.val("");
		ids = [];
		numbers = [];
		names = [];
		$("input[name='f7_ids']").each(function() {
			ids[ids.length] = $(this).val();
			idHidden.append("<input type='hidden' name='"+name+"' value='"+$(this).val()+"'/>");
		});
		$("input[name='f7_numbers']").each(function() {
			numbers[numbers.length] = $(this).val();
			numberHidden.append("<input type='hidden' name='"+name+"Number' value='"+$(this).val()+"'/>");
			numberDisplay.val(numberDisplay.val()+","+$(this).val());
		});
		$("input[name='f7_names']").each(function() {
			names[names.length] = $(this).val();
			nameHidden.append("<input type='hidden' name='"+name+"Name' value='"+$(this).val()+"'/>");
			nameDisplay.val(nameDisplay.val()+","+$(this).val());
		});
		if(numberDisplay.length>0) {
			var numberValue = numberDisplay.val();
			var numberValueLength = numberValue.length;
			if(numberValueLength>0) {
				numberDisplay.val(numberValue.substring(1,numberValueLength));
			}
		}
		if(nameDisplay.length>0) {
			var nameValue = nameDisplay.val();
			var nameValueLength = nameValue.length;
			if(nameValueLength>0) {
				nameDisplay.val(nameValue.substring(1,nameValueLength));
			}			
		}
		nameDisplay.focus();
		numberDisplay.focus();
	};

	var settings = {
		title : "F7 Multi Select",
		width : 550,
		height : 450,
		modal : true,
		position : {
			my : "center top",
			at : "center top",
			of : window
		},
		close : destroy,
		buttons : [ {
			id : "f7_ok",
			text : "OK",
			click : function() {
				applayValue();
				destroy();
				if(options.callbacks && typeof options.callbacks.ok=="function") {
					options.callbacks.ok(ids,numbers,names);
				}
			}
		}, {
			text : "Cancel",
			click : function() {
				destroy();
				if(options.callbacks && typeof options.callbacks.cancel=="function") {
					options.callbacks.cancel(ids,numbers,names);
				}
			}
		} ]
	};
	$.extend(settings, options.settings);

	f7Button.click(function() {
		var params = {"d":new Date()*1};
		params.ids = [];
		$("input[name='"+name+"']").each(function() {
			params.ids[params.ids.length] = $(this).val();
		});
		$.extend(params,options.params);
		if (!dialogDiv) {
			dialogDiv = $("<div style='display:none;'>").appendTo(document.body);
		}
		//$.param(params,true)
		dialogDiv.load(url, params, function() {
			dialogDiv.dialog(settings);
		});
	});
};
Cms.F7Single = function(url, name, names, focus, options) {
	options = options || {};
	var idField = $("#" + name);
	var f7Button = $("#" + name + "Button");
	
//	var numberHidden = $("#" + name + "Number");
//	var nameHidden = $("#" + name + "Name");
//	var numberDisplay = $("#" + name + "NumberDisplay");
//	var nameDisplay = $("#" + name + "NameDisplay");
	
	var fields = [];
	for(var i = 0,len=names.length;i<len;i++) {
		fields[i] = $("#" + name + names[i]);
	}
	var idValue;
	var values = [];
	
	var dialogDiv = null;

	var destroy = function() {
		dialogDiv.dialog("destroy");
		dialogDiv.empty();
	};
	var applayValue = function() {
		idValue = $("#f7_id").val();
		idField.val(idValue);
		for(var i = 0,len=names.length;i<len;i++) {
			values[i] = $("#f7_id_"+names[i]).val();
			fields[i].val(values[i]);
		}
//		var number = $("#f7_number").val();
//		var name = $("#f7_name").val();
//		numberHidden.val(number);
//		numberDisplay.val(number);
//		nameHidden.val(name);
//		nameDisplay.val(name);
	};
	var doFocus = function() {
		if(focus) {
			$("#"+focus).focus();
		}
//		if(numberDisplay.length>0) {
//			numberDisplay.focus();
//		} else if(nameDisplay.length>0) {
//			nameDisplay.focus();
//		} else if(numberHidden.length>0) {
//			numberHidden.focus();
//		}
	};

	var settings = {
		title : "F7 Select",
		width : 350,
		height : 450,
		modal : true,
		position : {
			my : "center top",
			at : "center top",
			of : window
		},
		close : destroy,
		buttons : [ {
			id : "f7_ok",
			text : "OK",
			click : function() {
				applayValue();
				destroy();
				if(options.callbacks && typeof options.callbacks.ok=="function") {
					options.callbacks.ok(idValue,values);
				}
				doFocus();
			}
		}, {
			text : "Cancel",
			click : function() {
				destroy();
				if(options.callbacks && typeof options.callbacks.cancel=="function") {
					options.callbacks.cancel();
				}
				doFocus();
			}
		} ]
	};
	$.extend(settings, options.settings);

	f7Button.click(function() {
		var params = {"d":new Date()*1};
		if(idField.val()!="") {
			params.id = idField.val();
		}
		$.extend(params, options.params);
		if (!dialogDiv) {
			dialogDiv = $("<div style='display:none;'>").appendTo(document.body);
		}
		dialogDiv.load(url + "?" + $.param(params), function() {
			dialogDiv.dialog(settings);
		});
	});
};
/**
 * 节点单选
 */
Cms.f7.node = function(name, focus, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/node/choose_node_tree.do";
	var settings = {title:"Node Select",width:350,height:450};
	options.settings = $.extend(settings, options.settings);
	var names = ["Number","Name"];
	var f7 = Cms.F7Single(url,name,names,focus,options);
}
/**
 * 节点单选（节点权限）
 */
Cms.f7.nodeNodePerm = function(name, focus, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/node/choose_node_tree_node_perms.do";
	var settings = {title:"Node Select",width:350,height:450};
	options.settings = $.extend(settings, options.settings);
	var names = ["Number","Name"];
	var f7 = Cms.F7Single(url,name,names,focus,options);
}
/**
 * 节点单选（信息权限）
 */
Cms.f7.nodeInfoPerms = function(name, focus, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/node/choose_node_tree_info_perms.do";
	var settings = {title:"Node Select",width:350,height:450};
	options.settings = $.extend(settings, options.settings);
	var names = ["Number","Name"];
	var f7 = Cms.F7Single(url,name,names,focus,options);
}
/**
 * 节点多选
 */
Cms.f7.nodeMulti = function(name, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/node/choose_node_tree_multi.do";
	var settings = {title:"Node Select",width:550,height:450};
	options.settings = $.extend(settings, options.settings);
	var f7 = Cms.F7Multi(url, name, options);
}
/**
 * 节点多选（信息权限）
 */
Cms.f7.nodeMultiInfoPerms = function(name, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/node/choose_node_tree_multi_info_perms.do";
	var settings = {title:"Node Select",width:550,height:450};
	options.settings = $.extend(settings, options.settings);
	var f7 = Cms.F7Multi(url, name, options);
}
/**
 * 权限多选
 */
Cms.f7.perm = function(name, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/role/choose_perm_tree.do";
	var settings = {title:"Perm Select",width:350,height:450};
	options.settings = $.extend(settings, options.settings);
	var names = ["Number"];
	var f7 = Cms.F7Single(url,name,names,name,options);
};
/**
 * 信息权限多选
 */
Cms.f7.nodePerms = function(name, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/node/choose_node_tree_perms.do";
	var settings = {title:"Node Select",width:350,height:450};
	options.settings = $.extend(settings, options.settings);
	var f7 = Cms.F7Multi(url, name, options);
};
/**
 * 组织单选
 */
Cms.f7.org = function(name, focus, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/org/choose_org_tree.do";
	var settings = {title:"Org Select",width:350,height:450};
	options.settings = $.extend(settings, options.settings);
	var names = ["Number","Name"];
	var f7 = Cms.F7Single(url,name,names,focus,options);
};
/**
 * 组织多选
 */
Cms.f7.orgMulti = function(name, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/org/choose_org_tree_multi.do";
	var settings = {title:"Orgs Select",width:550,height:450};
	options.settings = $.extend(settings, options.settings);
	var f7 = Cms.F7Multi(url, name, options);
}
/**
 * 模版单选
 */
Cms.f7.template = function(name, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/web_file/choose_template_tree.do";
	var settings = {title:"WebFile Select",width:350,height:450};
	options.settings = $.extend(settings, options.settings);
	var names = [];
	var f7 = Cms.F7Single(url,name,names,name,options);
};
/**
 * 样式单选
 */
Cms.f7.style = function(name, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/web_file/choose_style_tree.do";
	var settings = {title:"WebFile Select",width:350,height:450};
	options.settings = $.extend(settings, options.settings);
	var names = [];
	var f7 = Cms.F7Single(url,name,names,name,options);
};
/**
 * 专题多选
 */
Cms.f7.specialMulti = function(name, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/special/choose_special_multi.do";
	var settings = {title:"Special Select",width:550,height:450};
	options.settings = $.extend(settings, options.settings);
	var f7 = Cms.F7Multi(url, name, options);
}
/**
 * 站点单选
 */
Cms.f7.site = function(name, focus, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/site/choose_site_tree.do";
	var settings = {title:"Site Select",width:350,height:450};
	options.settings = $.extend(settings, options.settings);
	var names = ["Number","Name"];
	var f7 = Cms.F7Single(url,name,names,focus,options);
}
/**
 * 附件选择
 */
Cms.f7.uploads = function(name, focus, options) {
	options = options || {};
	var url = CTX + CMSCP + "/core/web_file/choose_uploads.do";
	var settings = {title:"WebFile Select",width:700,height:450};
	options.settings = $.extend(settings, options.settings);
	var names = ["Name","Length"];
	var f7 = Cms.F7Single(url,name,names,focus,options);
};





