package com.systop.common.modules.template.freemarker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

import com.systop.common.modules.template.LayoutTemplate;
import com.systop.common.modules.template.Template;
import com.systop.common.modules.template.TemplateContext;
import com.systop.core.taglibs.BaseSpringAwareBodyTagSupport;

/**
 * 使用FreeMarker模板的taglib的父类.子类只需要实现
 * {@link #setTemplateParameters(TemplateContext)}方法即可。
 * 
 * @author Sam Lee
 * 
 */
@SuppressWarnings("serial")
public abstract class BaseFreeMarkerTagSupport extends
		BaseSpringAwareBodyTagSupport {

	/**
	 * 模板上下文
	 * 
	 * @see {@link TemplateContext}
	 */
	protected TemplateContext templateContext;

	/**
	 * 模板存放的位置
	 */
	private String templateDir;

	/**
	 * 模板名称，可以不带.ftl扩展名
	 */
	private String templateName;

	/**
	 * 模板的主题，实际上是模板位置下的一个子目录
	 */
	private String theme;

	/**
	 * tag的宽度，例如：100px或95%
	 */
	private String width;

	/**
	 * tag左上角x坐标
	 */
	private Integer x = 0;

	/**
	 * tag右上角y坐标
	 */
	private Integer y = 0;

	/**
	 * tag是否可见
	 */
	private Boolean visible = true;

	/**
	 * @return the dir
	 */
	public String getTemplateDir() {
		return templateDir;
	}

	/**
	 * @param templateDir
	 *            the templetDir to set
	 */
	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	/**
	 * @return the name
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName
	 *            the templetName to set
	 */
	public void setTemplateName(String templateName) {

		this.templateName = (templateName == null ? this.getDefaultTemplate()
				: templateName);
	}

	/**
	 * @return the theme
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * @param theme
	 *            the theme to set
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * @return the x
	 */
	public Integer getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(Integer x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public Integer getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(Integer y) {
		this.y = y;
	}

	/**
	 * 初始化TemplateContext对象，当调用本方法之后，子类可以使用templateContext属性。
	 * 此时，templateContext属性中包含的大多数属性(request，template等)已经初始化。 子类只需要添加params即可.
	 * 
	 * templateContext的template属性指向一个<code>LayoutTemplate</code>的实例，
	 * 子类可以覆盖本方法以应用其他的Template
	 * 
	 * @see {@link Template}
	 */
	protected TemplateContext initTempateContext() {
		templateContext = new TemplateContext();
		templateContext.setRequest((HttpServletRequest) pageContext
				.getRequest());
		templateContext.setServletContext(pageContext.getServletContext());
		templateContext.setWriter(pageContext.getOut());

		if (StringUtils.isBlank(templateName)) {
			templateName = this.getDefaultTemplate();
		}
		LayoutTemplate template = new LayoutTemplate(templateDir, theme,
				templateName, x.toString(), y.toString(), width,
				visible.toString());
		templateContext.setTemplate(template);

		return templateContext;
	}

	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		initTempateContext(); // 初始化templateContext
		setTemplateParameters(templateContext); // 调用抽象方法，获取
		FreeMarkerTemplateRender render = (FreeMarkerTemplateRender) getBean("freeMarkerTemplateRender");
		try {
			render.renderTemplate(templateContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}

	/**
	 * 设置模板的参数.子类实现本方法，根据不同的tags，获取tags所要显示的数据， 并设置 <code>TemplateContext</code>
	 * 的param属性。<br>
	 * 例如:
	 * 
	 * <pre class="code">
	 *    protected void  setTemplateParameters(TemplateContext ctx) {
	 *      assert (ctx != null);
	 *      List list = //获取数据的代码省略
	 *      String tagName = //获取数据的代码省略
	 *      ctx.addParam(Constants.DEFAULT_LIST_NAME, list);
	 *      ctx.addParam(&quot;name&quot;, tagName);
	 *    }
	 * </pre>
	 * 
	 * <br>
	 * 在ftl文件中：
	 * 
	 * <pre>
	 *    X方向的位置：${data.x}
	 *      Y方向的位置：${data.y}
	 *      宽度：${data.width}
	 *      模板位置:${data.templateDir}
	 *      模板主题：${data.theme}
	 *      模板名字：${data.templateName}
	 *      是否可见：${data.visible}
	 *      模板中的list数据：
	 *      &lt;#list data.list as a&gt;
	 *      ${a}
	 *      &lt;/#list&gt;
	 * </pre>
	 * 
	 * @param ctx
	 */
	protected abstract void setTemplateParameters(TemplateContext ctx);

	/**
	 * @return Tag所使用的缺省的模板名称
	 */
	protected abstract String getDefaultTemplate();

	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	@Override
	public void release() {
		this.templateContext = null;
		this.theme = null;
		this.templateDir = null;
		this.x = 0;
		this.y = 0;
		this.width = null;
		this.visible = true;

		super.release();
	}
}
