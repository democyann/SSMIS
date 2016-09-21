package com.systop.common.modules.security.acegi.taglibs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
/**
 * 与{@link IfLoginTag}的作用相反，判断当前用户是否没有登录。
 * @author sam
 *
 */
@SuppressWarnings("serial")
public class IfNotLoginTag extends BodyTagSupport {
  @Override
  public int doStartTag() throws JspException {
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    User user = UserUtil.getPrincipal(request);
    if (user != null) {
      return SKIP_BODY;
    } else {
      return EVAL_BODY_INCLUDE;
    }
  }
}
