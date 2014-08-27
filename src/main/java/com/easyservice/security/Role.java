package com.easyservice.security;

import java.util.ArrayList;
import java.util.List;

import com.easyservice.xml.annotation.CollectionStyleType;
import com.easyservice.xml.annotation.XMLMapping;

public class Role{
	private int privilege;
	public static final int LIMITEDTYPE_NOTLIMITED=1;//不限
	public static final int LIMITEDTYPE_PARTPERMIT=0;//部分许可
	public static final int LIMITEDTYPE_ALLLIMITED=-1;//全部许可
	private int limitedType=LIMITEDTYPE_PARTPERMIT;//限制类型
	private List<PermissionRule> permissionRules=new ArrayList<PermissionRule>();
	@XMLMapping(collectionStyle=CollectionStyleType.FLAT,childTag="PermissionRule")
	public List<PermissionRule> getPermissionRules() {
		return permissionRules;
	}
	public void setPermissionRules(List<PermissionRule> permissionRules) {
		this.permissionRules = permissionRules;
	}
	public int getPrivilege() {
		return privilege;
	}
	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}
	public int getLimitedType() {
		return limitedType;
	}
	public void setLimitedType(int limitedType) {
		this.limitedType = limitedType;
	}
}

