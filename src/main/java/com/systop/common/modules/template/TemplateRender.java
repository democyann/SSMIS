package com.systop.common.modules.template;

import com.systop.core.ApplicationException;

/**
 * Any template language which wants to support UI tag templating
 *  needs to provide an implementation of this interface
 * to handle rendering the template
 * BTW:copy from webwork
 * @author Sam Lee
 */
public interface TemplateRender {
  /**
   * Renders the template
   * @param templateContext  context for the given template.
   * @throws Exception is thrown if there is a failure when rendering.
   */
  void renderTemplate(TemplateContext templateContext) throws ApplicationException;
}
