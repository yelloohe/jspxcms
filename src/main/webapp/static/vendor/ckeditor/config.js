/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	config.font_names = '宋体,SimSun;'
		+ '楷体,楷体_GB2312, SimKai;'
		+ '微软雅黑,Microsoft YaHei;'
		+ '黑体, SimHei;'
		+ '隶书, SimLi;'		
//		+ 'andale mono;'
//		+ 'arial, helvetica,sans-serif;'
//		+ 'arial black,avant garde;'
//		+ 'comic sans ms;'
//		+ 'impact,chicago;'
//		+ 'times new roman;'
		+ config.font_names;
	config.skin = 'office2003'; 
	config.toolbar = 'StandardPage';
	config.toolbar_Standard = [
		{ name:'document', items:['Source','Maximize','Preview','Templates']},
		{ name:'clipboard', items:['PasteText','PasteFromWord','-','Undo','Redo']},
		{ name:'editing', items:['Find','Replace','SelectAll']},
		{ name:'basicstyles', items:['Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat']},
		{ name:'paragraph', items:['NumberedList','BulletedList','-','Outdent','Indent','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock',] },
		{ name:'links', items:['Link','Unlink','Anchor']},
		{ name:'insert', items:['Image','Flash','Table','SpecialChar']},
		{ name:'styles', items:['Styles','Format','Font','FontSize']},
		{ name:'colors', items:['TextColor','BGColor']}
	];
	config.toolbar_StandardPage = [
		{ name:'document', items:['Source','Maximize','Preview','Templates']},
		{ name:'clipboard', items:['PasteText','PasteFromWord','-','Undo','Redo']},
		{ name:'editing', items:['Find','Replace','SelectAll']},
		{ name:'basicstyles', items:['Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat']},
		{ name:'paragraph', items:['NumberedList','BulletedList','-','Outdent','Indent','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock',] },
		{ name:'links', items:['Link','Unlink','Anchor']},
		{ name:'insert', items:['Image','Flash','Table','SpecialChar']},
		{ name:'styles', items:['Styles','Format','Font','FontSize']},
		{ name:'colors', items:['TextColor','BGColor']},
		{ name:'tools', items:['Page']}
	];
	config.toolbar_Full = [
		{ name:'document', items:['Source','-','Save','NewPage','DocProps','Preview','Print','-','Templates']},
		{ name:'clipboard', items:['Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo']},
		{ name:'editing', items:['Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt']},
		{ name:'forms', items:['Form','Checkbox','Radio','TextField','Textarea','Select','Button','ImageButton','HiddenField']},
		'/',
		{ name:'basicstyles',items:['Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat']},
		{ name:'paragraph', items:['NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv',
		'-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl']},
		{ name:'links', items:['Link','Unlink','Anchor']},
		{ name:'insert', items:['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','Iframe']},
		'/',
		{ name:'styles', items:['Styles','Format','Font','FontSize']},
		{ name:'colors', items:['TextColor','BGColor']},
		{ name:'tools', items:['Maximize','ShowBlocks']}
	];
	config.toolbar_FullPage = [
		{ name:'document', items:['Source','-','Save','NewPage','DocProps','Preview','Print','-','Templates']},
		{ name:'clipboard', items:['Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo']},
		{ name:'editing', items:['Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt']},
		{ name:'forms', items:['Form','Checkbox','Radio','TextField','Textarea','Select','Button','ImageButton','HiddenField']},
		'/',
		{ name:'basicstyles',items:['Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat']},
		{ name:'paragraph', items:['NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv',
		'-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl']},
		{ name:'links', items:['Link','Unlink','Anchor']},
		{ name:'insert', items:['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','Iframe']},
		'/',
		{ name:'styles', items:['Styles','Format','Font','FontSize']},
		{ name:'colors', items:['TextColor','BGColor']},
		{ name:'tools', items:['Maximize','ShowBlocks','Page']}
	];
	config.toolbar_Basic = [
		['Bold','Italic','-','NumberedList','BulletedList','-','Link','Unlink']
	];
	config.toolbar_BasicPage = [
		['Bold','Italic','-','NumberedList','BulletedList','-','Link','Unlink','-','Page']
	];
	config.extraPlugins = 'page';
};
CKEDITOR.on('instanceReady', function(ev){
    ev.editor.dataProcessor.writer.setRules('p', {
        indent : true,
        breakBeforeOpen : true,
        breakAfterOpen : false,
        breakBeforeClose : false,
        breakAfterClose : true
    });
    ev.editor.dataProcessor.writer.setRules('div', {
        indent : true,
        breakBeforeOpen : true,
        breakAfterOpen : false,
        breakBeforeClose : false,
        breakAfterClose : true
    });
});