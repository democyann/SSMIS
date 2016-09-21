package com.systop.common.modules.security.acegi.taglibs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;

/**
 * 判断用户是否登录的tag，如果登录，tag中的内容正常显示，否则则隐藏。
 * @author sam
 *
 */
@SuppressWarnings("serial")
public class IfLoginTag extends BodyTagSupport {
  @Override
  public int doStartTag() throws JspException {
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    User user = UserUtil.getPrincipal(request);
    if (user != null) {
      return EVAL_BODY_INCLUDE;
    } else {
      return SKIP_BODY;
    }
  }
}
