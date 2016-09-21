package com.systop.common.modules.security.user.service.listener;

import com.systop.common.modules.security.user.model.User;
/**
 * 用户操作监听器。由于<code>User</code>经常被其他对象引用或引用其他对象，
 * 所以在操作<code>User</code>的时候，往往涉及大量的级联操作。例如，一个
 * <code>User</code>负责很多“客户”，如果<code>User</code>被删除，那么
 * “客户”应该保留，此时hibernate的cascade不应该包含delele或delele-orphan
 * 而是应该在删除用户之前，将关联关系去掉。比如下面的代码:
 * <pre>
 * Set customers = user.getCustomers();
 * for(Iterator itr = customers.iterator(); itr.next();) {
 *    Customer c = (Customer)itr.next();
 *    c.setUser(null);
 * }
 * user.setCustomers(null);
 * session.delete(user);
 * </pre>
 * 但是这样的代码会严重影响代码的整洁性，并且，如果<code>User</code>需要关联新的
 * 对象，那么必须修改<code>UserManager</code>或重新定义一个
 * <code>UserManger</code>，这，实在太麻烦了。
 * 所以我们引入<code>UserListener</code>接口，实现者自行定义接口的实现，然后
 * 将实现类注入<code>UserManager</code>，<code>UserManager</code>
 * 会自动调用<code>UserListener</code>的实现。
 * @author SAM
 *
 */
public interface UserChangeListener {
  /**
   * <code>UserManager</code>会在删除用户之前调用<code>beforeRemove</code>
   * 方法。
   */
   void onRemove(User user);
   /**
    * <code>UserManager</code>会在用户保存之前调用<code>beforeSave</code>方法
    */
   void onSave(User newUser, User oldUser);
}
