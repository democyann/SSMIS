package com.systop.common.modules.sector.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.common.modules.sector.SectorConstants;
import com.systop.common.modules.sector.model.Sector;
import com.systop.core.service.BaseGenericsManager;
import com.systop.core.util.StringUtil;

/**
 * 计算部门编号<br>
 * <ul>
 * <li>同级部门，从01开始，最多99</li>
 * <li>部门编号等于"上级部门编号,部门编号",例如， 行政部编号01，下属小车组编号为01,02</li>
 * </ul>
 * @author Sam Lee Create time:2008-1-24 上午09:59:00
 */
@SuppressWarnings({"unchecked","rawtypes"})
@Service
public class SectorSerialNoManager extends BaseGenericsManager<Sector> {

  /**
   * 返回给定部门的部门编号
   * @param parentSector 给定部门
   * 
   */
  public String getSerialNo(final Sector sector) {
    Assert.notNull(sector, "The given Sector must not be null.");
    String serialNo = null;
    if (sector.getParentSector() != null) { // 如果有上级部门
      List serialNos = getDao().query(
          "select d.serialNo from Sector d "
              + "where d.parentSector = ? order by d.serialNo desc",
          sector.getParentSector());
      if (serialNos == null || serialNos.isEmpty()) { // 如果同级部门
        serialNo = buildFirstSerialNo(sector.getParentSector());
      } else { // 有同级级部门
        String maxSerialNo = (String) serialNos.get(0); // 同级部门最大编号
        // 找出同级部门最大编号的最后两位
        final String[] splited = org.springframework.util.StringUtils
            .commaDelimitedListToStringArray(maxSerialNo);
        if (splited == null || splited.length == 0) { // 同级部门没有编号
          serialNo = buildFirstSerialNo(sector.getParentSector());
        } else {// 计算当前部门编号
          Integer serial = StringUtil
              .getNumFromSerial(splited[splited.length - 1]);
          serialNo = sector.getParentSector().getSerialNo() + ","
              + StringUtil.zeroPadding((serial + 1), 2);
        }
      }
    } else { // 如果没有上级部门
      serialNo = this.getTopSectorSerialNo();
      sector.setSerialNo(serialNo);
    }

    logger.debug("Create serial No." + serialNo);
    return serialNo;
  }

  /**
   * 计算顶级部门编号
   */
  public String getTopSectorSerialNo() {
    List serialNos = getDao().query(
        "select d.serialNo from Sector d "
            + "where d.parentSector is null order by d.serialNo desc");
    // 没有部门
    if (serialNos == null || serialNos.isEmpty()) {
      return buildFirstSerialNo(null);
    }
    String maxSerialNo = (String) serialNos.get(0);
    // 部门编号为null
    if (StringUtils.isBlank(maxSerialNo)) {
      return buildFirstSerialNo(null);
    } else {
      Integer serial = StringUtil.getNumFromSerial(maxSerialNo);
      return StringUtil.zeroPadding((serial + 1), 2);
    }
  }

  /**
   * 根据上级部门和当前部门的前一个编号，获得当前部门编号的数字表示 ，
   * 用于重置所有部门编号:<br>
   * 首先根据上级部门编号，计算同级部门的第一个编号，以后便再此基础上
   * +1即可。
   */
  private Integer getSerial(Sector sector, final Integer preSerial) {
    Integer serial = null;
    if (preSerial == null) {
      String serialNo = getSerialNo(sector);
      if (StringUtils.isNotBlank(serialNo)) {
        String[] splits = org.springframework.util.StringUtils
            .commaDelimitedListToStringArray(serialNo);
        if (splits != null && splits.length > 0) {
          serial = StringUtil.getNumFromSerial(splits[splits.length - 1]);
        } else {
          serial = 0;
        }
      }
    } else {
      serial = preSerial + 1;
    }

    return serial;
  }

  /**
   * 重设所有部门编号。
   */
  @Transactional
  public void updateAllSerialNo() {
    List<Sector> tops = getDao().query("from Sector d where d.parentSector is null");
    Map serialMap = new HashMap(100); // 用于存放部门ID-SerialNo
    // 计算所有部门编号，并将部门ID-部门编号的对应关系存入serialMap
    Integer serial = null;
    for (Sector top : tops) {
      serial = this.getSerial(top, serial);
      // String serialNo = getSerialNo(top);
      top.setSerialNo(buildSerialNo(null, serial));
      serialMap.put(top.getId(), buildSerialNo(null, serial));
      // getDao().saveObject(top);
      if (top.getChildSectors().size() > 0) {
        this.updateChildrenSerialNo(top, serialMap);
      }
    }
    // 批量更新部门编号
    Set<String> ids = serialMap.keySet();
    for (String id : ids) {
      Sector sector = get(id);
      getDao().evict(sector);
      sector.setSerialNo((String) serialMap.get(id));
      getDao().getHibernateTemplate().update(sector);
    }
  }

  /**
   * 更新所有子部门的SerialNo
   */
  @Transactional
  public void updateChildrenSerialNo(Sector parent, Map serialMap) {
    logger.debug("Update " + parent.getName() + "'s children serial No.");
    Set<Sector> children = parent.getChildSectors();
    Integer serial = null;
    for (Sector child : children) {
      serial = this.getSerial(child, serial);
      child.setSerialNo(buildSerialNo(parent, serial));
      serialMap.put(child.getId(), buildSerialNo(parent, serial));
      // getDao().saveObject(child);
      if (child.getChildSectors().size() > 0) {
        updateChildrenSerialNo(child, serialMap);
      }
    }
  }

  /**
   * 构建第一个部门的编号
   */
  private static String buildFirstSerialNo(Sector parent) {
    if (parent == null) {
      return SectorConstants.FIRST_SERIAL_NO;
    } else {
      return new StringBuffer(100).append(parent.getSerialNo()).append(",")
          .append(SectorConstants.FIRST_SERIAL_NO).toString();
    }
  }

  /**
   * 构建部门编号
   */
  private static String buildSerialNo(Sector parent, Integer serial) {
    if (parent == null) {
      return StringUtil.zeroPadding(serial, 2);
    } else {
      return new StringBuffer(100).append(parent.getSerialNo()).append(",")
          .append(StringUtil.zeroPadding(serial, 2)).toString();
    }
  }

}
