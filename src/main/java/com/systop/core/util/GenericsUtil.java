package com.systop.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Provides a helper that locates the declarated generics type of a class.
 * 
 * @author sshwsfc@gmail.com
 */
@SuppressWarnings("rawtypes")
public final class GenericsUtil {
	/**
	 * prevent from initializing
	 */
	private GenericsUtil() {
	}

	/**
	 * Locates the first generic declaration on a class.
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or <code>null</code> if cannot be
	 *         determined
	 */
	public static Class getGenericClass(Class clazz) {
		return getGenericClass(clazz, 0);
	}

	/**
	 * Locates generic declaration by index on a class.
	 * 
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic ddeclaration,start from 0.
	 */
	public static Class getGenericClass(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (genType instanceof ParameterizedType) {
			Type[] params = ((ParameterizedType) genType)
					.getActualTypeArguments();

			if ((params != null) && (params.length >= (index - 1))) {
				return (Class) params[index];
			}
		}
		return null;
	}
}
