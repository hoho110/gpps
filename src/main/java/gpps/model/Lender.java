package gpps.model;

public class Lender {
	private Integer id;//主键ID
	private String name;//名称
	private String tel;//手机
	private String email;//email
	private String loginId;//登录名
	private String password;//md5加密密码
	private String identityCard;//身份证
	private Integer accountId;//账户ID
	private long createtime=System.currentTimeMillis();//创建时间
	public static final int PRIVILEGE_UNOFFICIAL=0;//非正式用户
	public static final int PRIVILEGE_OFFICIAL=1;//正式用户
	private int privilege=PRIVILEGE_UNOFFICIAL;//用户角色
	public static final int LEVEL_COMMON=0;//普通用户
	public static final int LEVEL_VIP1=1;//VIP1
	private int level=LEVEL_COMMON;
	private int grade=0;//评分
	private int sex=0;//男：0；女：1
	private String address;//通信地址
	private String annualIncome;//年收入
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public int getPrivilege() {
		return privilege;
	}
	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
