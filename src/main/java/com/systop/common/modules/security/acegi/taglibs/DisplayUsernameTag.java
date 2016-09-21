package com.systop.common.modules.security.acegi.taglibs;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
/**
 * 用于在页面上显示用户名
 * @author Sam Lee
 *
 */
@SuppressWarnings({"serial"})
public class DisplayUsernameTag extends BodyTagSupport {
  /**
   * @see BodyTagSupport#doEndTag()
   */
  @Override
  public int doEndTag() throws JspException {
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    User user = UserUtil.getPrincipal(request);
    if (user != null) {
      try {
        pageContext.getOut().print(
            (user.getUsername() != null) ? user.getUsername() : user
                .getLoginId());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return EVAL_PAGE;
  }

}
