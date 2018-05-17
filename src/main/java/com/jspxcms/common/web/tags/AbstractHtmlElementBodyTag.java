package com.jspxcms.common.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * AbstractHtmlElementBodyTag
 * 
 * @author liufang
 * 
 */
public abstract class AbstractHtmlElementBodyTag extends AbstractHtmlElementTag
		implements BodyTag {
	private static final long serialVersionUID = 1L;

	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		onWriteTagContent();
		this.tagWriter = tagWriter;
		if (shouldRender()) {
			exposeAttributes();
			return EVAL_BODY_BUFFERED;
		} else {
			return SKIP_BODY;
		}
	}

	/**
	 * If {@link #shouldRender rendering}, flush any buffered
	 * {@link BodyContent} or, if no {@link BodyContent} is supplied,
	 * {@link #renderDefaultContent render the default content}.
	 * 
	 * @return Tag#EVAL_PAGE
	 */
	@Override
	public int doEndTag() throws JspException {
		if (shouldRender()) {
			if (this.bodyContent != null
					&& StringUtils.hasText(this.bodyContent.getString())) {
				renderFromBodyContent(this.bodyContent, this.tagWriter);
			} else {
				renderDefaultContent(this.tagWriter);
			}
		}
		return EVAL_PAGE;
	}

	/**
	 * Render the tag contents based on the supplied {@link BodyContent}.
	 * <p>
	 * The default implementation simply {@link #flushBufferedBodyContent
	 * flushes} the {@link BodyContent} directly to the output. Subclasses may
	 * choose to override this to add additional content to the output.
	 */
	protected void renderFromBodyContent(BodyContent bodyContent,
			TagWriter tagWriter) throws JspException {
		flushBufferedBodyContent(this.bodyContent);
	}

	/**
	 * Clean up any attributes and stored resources.
	 */
	@Override
	public void doFinally() {
		super.doFinally();
		removeAttributes();
		this.tagWriter = null;
		this.bodyContent = null;
	}

	/**
	 * Called at the start of {@link #writeTagContent} allowing subclasses to
	 * perform any precondition checks or setup tasks that might be necessary.
	 */
	protected void onWriteTagContent() {
	}

	/**
	 * Should rendering of this tag proceed at all. Returns '<code>true</code>'
	 * by default causing rendering to occur always, Subclasses can override
	 * this if they provide conditional rendering.
	 */
	protected boolean shouldRender() throws JspException {
		return true;
	}

	/**
	 * Called during {@link #writeTagContent} allowing subclasses to add any
	 * attributes to the {@link javax.servlet.jsp.PageContext} as needed.
	 */
	protected void exposeAttributes() throws JspException {
	}

	/**
	 * Called by {@link #doFinally} allowing subclasses to remove any attributes
	 * from the {@link javax.servlet.jsp.PageContext} as needed.
	 */
	protected void removeAttributes() {
	}

	/**
	 * The user customised the output of the error messages - flush the buffered
	 * content into the main {@link javax.servlet.jsp.JspWriter}.
	 */
	protected void flushBufferedBodyContent(BodyContent bodyContent)
			throws JspException {
		try {
			bodyContent.writeOut(bodyContent.getEnclosingWriter());
		} catch (IOException e) {
			throw new JspException("Unable to write buffered body content.", e);
		}
	}

	protected abstract void renderDefaultContent(TagWriter tagWriter)
			throws JspException;

	// ---------------------------------------------------------------------
	// BodyTag implementation
	// ---------------------------------------------------------------------
	private BodyContent bodyContent;

	private TagWriter tagWriter;

	public void doInitBody() throws JspException {
		// no op
	}

	public void setBodyContent(BodyContent bodyContent) {
		this.bodyContent = bodyContent;
	}
}
