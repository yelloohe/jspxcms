editormd.toolbar_Full = [ "undo", "redo", "|", "bold", "del", "italic",
		"quote", "ucwords", "uppercase", "lowercase", "|", "h1", "h2", "h3",
		"h4", "h5", "h6", "|", "list-ul", "list-ol", "hr", "|", "link",
		"reference-link", "image", "code", "preformatted-text", "code-block",
		"table", "datetime", "emoji", "html-entities", "pagebreak", "|",
		"goto-line", "watch", "preview", "fullscreen", "clear", "search", "|",
		"help", "info" ];
editormd.toolbar_FullPage = editormd.toolbar_Full.concat([]);
editormd.toolbar_Standard = [ "undo", "redo", "|", "bold", "del", "italic",
		"quote", "uppercase", "lowercase", "|", "h1", "h2", "h3", "h4", "h5",
		"h6", "|", "list-ul", "list-ol", "hr", "|", "link", "image", "table", "|",
		"watch", "preview", "fullscreen" ];
editormd.toolbar_StandardPage = editormd.toolbar_Standard.concat([]);
editormd.toolbar_Base = [ "bold", "del", "italic", "quote", "|", "h1", "h2",
		"h3", "h4", "h5", "h6", "|", "list-ul", "list-ol", "|", "fullscreen" ];
editormd.toolbar_BasePage = editormd.toolbar_Base.concat([]);
function base64UploadPlugin(emd, base64Url) {
	var userAgent = navigator.userAgent;
	var chrome = /Chrome\//.test(userAgent);
	// /Firefox/i.test(userAgent)
	var gecko = /gecko\/\d/i.test(userAgent);
	var trident = /Trident/i.test(userAgent);
	var base64ToBlob = function(base64) {
		var index = base64.lastIndexOf(',');
		if (index >= 0) {
			base64 = base64.substring(index + 1);
		}
		var binary = window.atob(base64);
		var len = binary.length;
		var buffer = new ArrayBuffer(len);
		var view = new Uint8Array(buffer);
		for (var i = 0; i < len; i++) {
			view[i] = binary.charCodeAt(i);
		}
		return new Blob([ view ], {
			type : 'image/png'
		});
		// return new Blob([buffer], {type : 'image/png'});
	};
	var xhrImageUpload = function(base64, cm) {
		var formData = new FormData();
		formData.append("file", base64ToBlob(base64), "blob.png");
		var xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function() {
			if (xhr.readyState === 4 && xhr.status === 200) {
				var json = $.parseJSON(xhr.responseText);
				cm.replaceSelection('![](' + json.fileUrl + ')');
			}
		};
		xhr.upload.onerror = function() {
			alert("upload image error!");
		};
		xhr.open("POST", base64Url, true);
		xhr.setRequestHeader("Cache-Control", "no-cache");
		xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
		xhr.send(formData);
	};

	// ie6-10获取剪切板的方式： window.clipboardData.getData(type)
	var forChrome = function(cm, event) {
		var items = event.clipboardData.items;
		for (var i = 0, len = items.length; i < len; i++) {
			var item = items[i];
			if (item.type.match(/^image\//)) {
				var file = item.getAsFile();
				var reader = new FileReader();
				reader.onload = function(e) {
					var base64 = e.target.result;
					xhrImageUpload(base64, cm);
				}
				reader.readAsDataURL(file);
			}
		}
	};
	var pasteDiv = $("<div contenteditable='true'/>").css({
		"overflow" : "hidden",
		"height" : "0"
	}).appendTo(document.body);
	var forFirefox = function(cm, event) {
		if ((event.ctrlKey || event.metaKey)
				&& (event.keyCode === 86 || event.key === 'v')) {
			pasteDiv.focus();
			setTimeout(function() {
				var isBase64 = false;
				var imgs = pasteDiv.find("img");
				cm.focus();
				if (imgs && imgs.length > 0) {
					var base64 = imgs[0].src;
					if (/^data:image/i.test(base64)) {
						isBase64 = true;
						xhrImageUpload(base64, cm);
					}
				}
				if (!isBase64) {
					cm.replaceSelection(pasteDiv.text());
				}
				pasteDiv.empty();
			}, 5);
		}
	};
	if (chrome) {
		emd.cm.on("paste", function(cm, e) {
			forChrome(cm, e);
		});
	} else if (gecko) {
		emd.cm.on("keypress", function(cm, e) {
			forFirefox(cm, e);
		});
	} else if (trident) {
		emd.cm.on("keydown", function(cm, e) {
			forFirefox(cm, e);
		});
	}
}