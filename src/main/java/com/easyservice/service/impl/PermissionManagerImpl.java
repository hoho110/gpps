package com.easyservice.service.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.easyservice.service.IPermissionManager;
@Component
public class PermissionManagerImpl implements IPermissionManager,BeanPostProcessor{
	private Resource configLocation;//权限配置文件路径
	@Override
	public boolean checkPermission(int role,boolean fetchServiceSdl, String applyService,
			String applyMethod) {
		//TODO 校验权限
		return false;
	}
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		//TODO 加载权限配置文件到内存中
		return bean;
	}

}
