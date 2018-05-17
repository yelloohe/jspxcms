(function() {
	CKEDITOR.plugins.add('page', {
	    init: function(editor) {
	    	// Register the command.
			editor.addCommand('page', CKEDITOR.plugins.pageCmd);
	
			// Register the toolbar button.
			editor.ui.addButton('Page', {
				label: editor.lang.pagebreak,
				command: 'page'
			});
	    },
	    requires: ['fakeobjects']
	});
	CKEDITOR.plugins.pageCmd = {
		exec : function(editor) {
			var pagebreak = CKEDITOR.dom.element.createFromHtml('<div>[PageBreak][/PageBreak]</div>', editor.document);
			var ranges = editor.getSelection().getRanges( true );
	
			editor.fire('saveSnapshot');
	
			for (var range,i=ranges.length-1; i >= 0; i--) {
				range = ranges[i];
	
				if (i < ranges.length -1 ) {
					pagebreak = pagebreak.clone(true);				
				}
	
				range.splitBlock( 'p' );
				range.insertNode( pagebreak );
				if (i==ranges.length-1 ) {
					var next = pagebreak.getNext();
					range.moveToPosition(pagebreak, CKEDITOR.POSITION_AFTER_END);
					// If there's nothing or a non-editable block followed by, establish a new paragraph
					// to make sure cursor is not trapped.
					if (!next || next.type==CKEDITOR.NODE_ELEMENT && !next.isEditable()) {
						range.fixBlock(true, editor.config.enterMode==CKEDITOR.ENTER_DIV ? 'div' : 'p');					
					}
					range.select();
				}
			}
			editor.fire('saveSnapshot');
		}
	}
})();