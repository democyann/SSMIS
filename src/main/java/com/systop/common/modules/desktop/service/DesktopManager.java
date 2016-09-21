package com.systop.common.modules.desktop.service;

import org.springframework.stereotype.Service;

import com.systop.common.modules.desktop.model.Desktop;
import com.systop.core.service.BaseGenericsManager;

/**
 * 桌面管理类
 * 
 * @author ZhouLihui
 */
@Service
public class DesktopManager extends BaseGenericsManager<Desktop>{

	public Desktop getDesktopByUserId(Integer userId){
		return findObject("from Desktop d where d.user.id = ?", userId);
	}
}
