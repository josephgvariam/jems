package com.dubaidrums.jems.util.tag;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

public class ControlGroupTag extends AbstractDataBoundFormElementTag {

	private TagWriter tagWriter;

	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		this.tagWriter = tagWriter;
		tagWriter.startTag("div");

		String[] errorMessages = getBindStatus().getErrorMessages();
		if (errorMessages.length == 0) {
			tagWriter.writeAttribute("class", "control-group");
		} else {
			tagWriter.writeAttribute("class", "control-group error");
		}

		tagWriter.forceBlock();

		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		this.tagWriter.endTag();
		return EVAL_PAGE;
	}

}
