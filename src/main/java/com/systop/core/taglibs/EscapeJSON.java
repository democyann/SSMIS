package com.systop.core.taglibs;

import org.json.simple.JSONObject;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: slimx
 * Date: 12-9-27
 * Time: 下午2:18
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class EscapeJSON  extends BodyTagSupport {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int doEndTag() throws JspException {
        String _value = JSONObject.escape(value);
        JspWriter out = pageContext.getOut();

        try {
            out.print(_value);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return super.doEndTag();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
