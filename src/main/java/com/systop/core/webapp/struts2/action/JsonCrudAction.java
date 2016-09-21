package com.systop.core.webapp.struts2.action;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.xwork.StringUtils;

import com.opensymphony.xwork2.util.ArrayUtils;
import com.systop.core.model.BaseModel;
import com.systop.core.service.BaseGenericsManager;
import com.systop.core.util.RequestUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

/**
 * 继承JsonCrudAction的类可以返回json数据，前提是RequestHeader中的Accept属性必须 包含x-json。
 * JavaScript中设置Request Header的方法:<br>
 * 
 * <pre>
 *  //ExtJs:
 *  Ext.Ajax.defaultHeaders = {
 *  'Accept': 'application/x-json;text/x-json;charset=UTF-8'
 *  };
 *  Ext.Ajax.request({
 *      url: 'user/index.do',
 *      params: 'username=sam'
 *  });
 *  //Jquery:
 *  $.ajax({
 *      type: 'GET',
 *      url: '/user/index.do',
 *      beforeSend: function(xhr){
 *          xhr.setRequestHeader('Accept', 
 *          'application/x-json;text/x-json;charset=UTF-8'},
 *      async: true
 *      data: {username:'sam'}
 *      success: function(msg){
 *      }
 *  });
 * </pre>
 * 
 * 我们使用Json-plugin（http://code.google.com/p/jsonplugin/）
 * 作为Json对象的生成工具，它会自动递归遍历Action的某个属性（在result设置中用root指定）
 * ，遍历的时候会激活Hibnerate的延迟加载，导致过多的SQL查询。可以通过exculdeProperties 参数指定：<br>
 * 
 * <pre>
 * &lt;result type="json" name="json"&gt;
 *        &lt;param name="ignoreHierarchy"&gt;false&lt;/param&gt;
 *        &lt;param name="root"&gt;page&lt;/param&gt;
 *        &lt;param name="excludeProperties"&gt;
 *          .*\.roles.*\.permissions
 *          .*\.roles.*\.depts
 *          .*\.roles.*\.childRoles
 *          .*\.roles.*\.parentRole
 *          .*\.dept
 *        &lt;/param&gt;
 *    &lt;/result&gt;
 * </pre>
 * 
 * @author Sam Lee
 * 
 * @param <T>
 * @param <M>
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
public abstract class JsonCrudAction<T extends BaseModel, M extends BaseGenericsManager<T>>
		extends DefaultCrudAction<T, M> {
	/**
	 * 返回json数据
	 */
	public static final String JSON = "json";
	public static final String JSON_EDIT = "json_edit";

	/**
	 * 如果是Json请求，则返回{@link #JSON},否则返回{@link #INDEX}. {@link #index()}
	 * 方法在json请求的时候，会调用{@link #batchConvertToMap(Collection)}
	 * 方法，将page中的数据转换为list of map，以防止激活hibernate延迟加载机制。
	 * {@link #batchConvertToMap(Collection)}实际上调用了{@link #convertToMap(Object)}
	 * 方法转换单个bean。当子类不覆盖{@link #convertToMap(Object)}方法的时候，
	 * {@link #batchConvertToMap(Collection)} 不做任何转换工作。
	 */
	@Override
	public String index() {
		page = pageQuery();
		logger.debug("index方法查询数据{}条.", page.getData().size());

		if (isJsonReq()) {
			page.setData(batchConvertToMap(page.getData()));
		} else {
			restorePageData(page);
		}
		return (isJsonReq()) ? JSON : INDEX;
	}

	/**
	 * 使用Ajax方式调用edit()方法的时候，往往希望只是返回Action中某个属性的json对象， 此时需要在struts配置文件中设置:<BR>
	 * 
	 * <pre>
	 * &lt;result type="json" name="json_edit"&gt;
	 *             &lt;param name="ignoreHierarchy"&gt;false&lt;/param&gt;
	 *             &lt;param name="root"&gt;your property&lt;/param&gt;
	 * &lt;/result&gt;
	 * </pre>
	 */
	@Override
	public String edit() {
		return (isJsonReq()) ? JSON_EDIT : super.edit();
	}

	/**
	 * 与{@link RequestUtil#isJsonRequest(javax.servlet.http.HttpServletRequest)}
	 * 相同.
	 */
	protected boolean isJsonReq() {
		return RequestUtil.isJsonRequest(getRequest());
	}

	/**
	 * <code>batchConvertToMap()</code>方法将 "list of beans" 转换为一个"list of map".<br>
	 * 为了避免JsonPlugin或json_lib在转换bean为json的过程中激活hibernate的延迟加载.
	 * 有时候需要将bean转换为Map. 子类需要实现内部类{@link BeanToMap}或方法
	 * {@link #convertToMap(Object)}以转换原list中的单个对象。
	 * 
	 * @param converter
	 *            如果为null, 则表示调用{@link #convertToMap(Object)}方法
	 * @param data
	 *            被转换的的数据
	 */
	protected List batchConvertToMap(BeanToMap converter, Collection data) {
		if (!isJsonReq()) {
			return Collections.EMPTY_LIST;
		}

		if (CollectionUtils.isEmpty(data)) {
			return Collections.EMPTY_LIST;
		}
		List list = new ArrayList(data.size());
		for (Iterator itr = data.iterator(); itr.hasNext();) {
			Object bean = itr.next();
			if (bean != null) { // null不进行转换
				Object map = null; // 转换结果
				if (converter != null) { // 如果提供了BeanToMap的实现类
					map = converter.toMap(bean);
				} else {// 没有提供BeanToMap的实现类
					try {
						map = convertToMap(bean); // 调用convert方法
					} catch (UnsupportedOperationException e) { // convert方法也没有实现
						map = bean; // 不进行转换了
					}
				}
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 相当于调用<code>batchConvertToMap(null, data)</code>. {@link #index()}
	 * 方法缺省的会调用这个方法.
	 * 
	 * @see {@link #batchConvertToMap(JsonCrudAction.BeanToMap, Collection)}
	 */
	protected List batchConvertToMap(Collection data) {
		return batchConvertToMap(null, data);
	}

	/**
	 * 将单个Bean转换为Map，子类可以覆盖本方法以实现不同的转换策略. <br>
	 * 缺省的，convertToMap直接抛出UnsupportedOperationException
	 * 
	 * @param bean
	 *            给定的Bean
	 * @return Map of the bean.
	 * @throws if
	 *             user will have not implemented the method, it always throws
	 *             <code>UnsupportedOperationException</code>
	 */
	protected Map convertToMap(Object bean) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 将一个Bean转换为Map的接口，通常用于比较复杂的Action，此类Action需要用不同的模式转换多个Bean， 此时
	 * {@link convertToMap方法不再适用}，需要实现BeanToMap接口实现不同的转换策略。此时，也许还需要 覆盖
	 * {@link #index()}方法，因为这个方法缺省的调用了{@link #batchConvertToMap(Collection)}
	 * 
	 * @author Sam Lee
	 * 
	 */
	protected static interface BeanToMap {
		Map toMap(Object bean);
	}

	/**
	 * 使用XStream，将一个对象转变为json字符串，例如：<br>
	 * 
	 * <pre>
	 * // 输出{&quot;user&quot;:{&quot;loginId&quot;:&quot;sss&quot;,&quot;isOpen&quot;:1,&quot;createTime&quot;:&quot;2010-02-06 22:29:44.156 CST&quot;,&quot;photos&quot;:[&quot;&quot;]}}
	 * toJson(&quot;user&quot;, user, new String[] { &quot;roles&quot;, &quot;articles&quot; });
	 * </pre>
	 * 
	 * @param alias
	 *            别名，被转换的类型在json对象中的属性名称。
	 * @param target
	 *            被转换的对象
	 * @param omitFields
	 *            需要忽略的、位于<code>target</code>
	 *            中的字段名称。也可以用@XStreamOmitField在字段上做标注。
	 * @return 如果<code>target</code>为<code>null</code>
	 *         ，返回'{"null":""}'字符串，否则返回json字符串。
	 */
	protected String toJson(String alias, Object target, String... omitFields) {
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.autodetectAnnotations(true); // 自动处理XStream Annotations
		if (target == null) {
			xstream.toXML(null);
		}
		// 别名
		if (StringUtils.isNotBlank(alias)) {
			xstream.alias(alias, target.getClass());
		}
		// 忽略的字段
		if (ArrayUtils.isNotEmpty(omitFields)) {
			for (String omitField : omitFields) {
				xstream.omitField(target.getClass(), omitField);
			}
		}
		return xstream.toXML(target);
	}

	/**
	 * 使用XStream，将一个对象转变为json字符串，例如：<br>
	 * 
	 * <pre>
	 * // 输出{&quot;user&quot;:{&quot;loginId&quot;:&quot;sss&quot;,&quot;isOpen&quot;:1,&quot;createTime&quot;:&quot;2010-02-06 22:29:44.156 CST&quot;,&quot;photos&quot;:[&quot;&quot;]}}
	 * toJson(&quot;user&quot;, user, new String[] { &quot;roles&quot;, &quot;articles&quot; });
	 * </pre>
	 * 
	 * @param aliasMap
	 *            Class和别名Map，被转换的类型在json对象中的属性名称。
	 * @param target
	 *            被转换的对象
	 * @param omitFieldsMap
	 *            需要忽略的字段名称，Key值为字段所在的Class,
	 *            value为该Class中被忽略的字段。也可以用@XStreamOmitField在字段上做标注。
	 * @return 如果<code>target</code>为<code>null</code>
	 *         ，返回'{"null":""}'字符串，否则返回json字符串。
	 */
	protected String toJson(Map<Class, String> aliasMap, Object target,
			Map<Class, String[]> omitFieldsMap) {
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.autodetectAnnotations(true); // 自动处理XStream Annotations
		if (target == null) {
			xstream.toXML(null);
		}
		// 别名
		if (MapUtils.isNotEmpty(aliasMap)) {
			Set<Class> keys = aliasMap.keySet();
			for (Class clazz : keys) {
				xstream.alias(aliasMap.get(clazz), clazz);
			}
		}
		// 忽略的字段
		if (MapUtils.isNotEmpty(omitFieldsMap)) {
			Set<Class> keys = omitFieldsMap.keySet();
			for (Class clazz : keys) {
				String[] fields = omitFieldsMap.get(clazz);
				for (String field : fields) {
					xstream.omitField(clazz, field);
				}

			}
		}
		return xstream.toXML(target);
	}

	/**
	 * 使用XStream，将一个对象转变为json字符串，例如：<br>
	 * 
	 * <pre>
	 * // 输出{&quot;loginId&quot;:&quot;sss&quot;,&quot;isOpen&quot;:1,&quot;createTime&quot;:&quot;2010-02-06 22:29:44.156 CST&quot;,&quot;photos&quot;:[&quot;&quot;]}
	 * toJson(user);
	 * </pre>
	 * 
	 * @param target
	 *            被转换的对象
	 * @param omitFields
	 *            需要忽略的、位于<code>target</code>
	 *            中的字段名称。也可以用@XStreamOmitField在字段上做标注。
	 * @return 如果<code>target</code>为<code>null</code>，返回'{}'字符串，否则返回json字符串。
	 */
	protected String toJson(Object target, String... omitFields) {
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
			public HierarchicalStreamWriter createWriter(Writer writer) {
				return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
			}
		});
		if (target == null) {
			xstream.toXML(target);
		}
		xstream.autodetectAnnotations(true); // 自动处理XStream Annotations
		// 忽略的字段
		if (ArrayUtils.isNotEmpty(omitFields)) {
			for (String omitField : omitFields) {
				xstream.omitField(target.getClass(), omitField);
			}
		}
		return xstream.toXML(target);
	}

	/**
	 * 使用XStream，将一个对象转变为json字符串，例如：<br>
	 * 
	 * <pre>
	 * // 输出{&quot;loginId&quot;:&quot;sss&quot;,&quot;isOpen&quot;:1,&quot;createTime&quot;:&quot;2010-02-06 22:29:44.156 CST&quot;,&quot;photos&quot;:[&quot;&quot;]}
	 * toJson(user);
	 * </pre>
	 * 
	 * @param target
	 *            被转换的对象
	 * @param omitFields
	 *            @param omitFieldsMap 需要忽略的字段名称，Key值为字段所在的Class,
	 *            value为该Class中被忽略的字段。也可以用@XStreamOmitField在字段上做标注。
	 * @return 如果<code>target</code>为<code>null</code>，返回'{}'字符串，否则返回json字符串。
	 */
	protected String toJson(Object target, Map<Class, String[]> omitFieldsMap) {
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
			public HierarchicalStreamWriter createWriter(Writer writer) {
				return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
			}
		});
		if (target == null) {
			xstream.toXML(target);
		}
		xstream.autodetectAnnotations(true); // 自动处理XStream Annotations
		// 忽略的字段
		if (MapUtils.isNotEmpty(omitFieldsMap)) {
			Set<Class> keys = omitFieldsMap.keySet();
			for (Class clazz : keys) {
				String[] fields = omitFieldsMap.get(clazz);
				for (String field : fields) {
					xstream.omitField(clazz, field);
				}
			}
		}
		return xstream.toXML(target);
	}
}
