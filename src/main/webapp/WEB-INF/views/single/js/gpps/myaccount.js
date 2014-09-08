var myscore = function(){
	var content = $('<div></div>');
	content.append('<p>您好王冬，您的积分是<span class="orange">1000000</span>分，等级为<span class="orange">钻石会员</span></p>');
	content.append('<br><span class="orange">积分规则：</span>');
	content.append('<p>如何获取积分的说明</p>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">会员等级</td><td style="min-width:50px;">对应积分</td><td style="min-width:100px;">有效期</td><td style="min-width:50px;">最低贡献值</td><td style="min-width:50px;">说明</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td>钻石VIP会员</td><td>10000000以上</td><td>3个月</td><td>1000000</td><td>有最低消费</td></tr>';
	str += '<tr><td>白金VIP会员</td><td>1000000-10000000</td><td>3个月</td><td>300000</td><td>有最低消费</td></tr>';
	str += '<tr><td>黄金VIP会员</td><td>200000-1000000</td><td>半年</td><td>100000</td><td>有最低消费</td></tr>';
	str += '<tr><td>白银VIP会员</td><td>50000-200000</td><td>一年</td><td>10000</td><td>有最低消费</td></tr>';
	str += '<tr><td>VIP会员</td><td>10000-50000</td><td>永久</td><td>无</td><td>无最低消费</td></tr>';
	str += '<tr><td>普通会员</td><td>0-10000</td><td>永久</td><td>无</td><td>无最低消费</td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	
	
	
	content.append('<br><span class="orange">会员特权：</span>');
	
	
	var str1 = "";
	str1 += '<table class="table table-striped table-hover" style="min-width:300px;">';
	str1 += '<thead>';	
	str1 += '<tr><td style="min-width:100px;">会员等级</td><td style="min-width:50px;">特权一</td><td style="min-width:100px;">特权二</td><td style="min-width:50px;">特权三</td><td style="min-width:50px;">说明</td></tr>';
	str1 += '</thead>';
	str1 += '<tbody>';
	str1 += '<tr><td>钻石VIP会员</td><td>10000000以上</td><td>3个月</td><td>1000000</td><td>有最低消费</td></tr>';
	str1 += '<tr><td>白金VIP会员</td><td>1000000-10000000</td><td>3个月</td><td>300000</td><td>有最低消费</td></tr>';
	str1 += '<tr><td>黄金VIP会员</td><td>200000-1000000</td><td>半年</td><td>100000</td><td>有最低消费</td></tr>';
	str1 += '<tr><td>白银VIP会员</td><td>50000-200000</td><td>一年</td><td>10000</td><td>有最低消费</td></tr>';
	str1 += '<tr><td>VIP会员</td><td>10000-50000</td><td>永久</td><td>无</td><td>无最低消费</td></tr>';
	str1 += '<tr><td>普通会员</td><td>0-10000</td><td>永久</td><td>无</td><td>无最低消费</td></tr>';
	str1 += '</tbody>';
	str1 += '</table>';
	content.append(str1);
	return content;
}
var myactivity = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">活动名称</td><td style="min-width:50px;">活动日期</td><td style="min-width:100px;">活动类型</td><td style="min-width:50px;">主办方</td><td style="min-width:50px;">状态</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td><a href="../activity/index.html" target="_blank">XXXX杯跑步活动</td><td>2014-9-31</td><td>长跑</td><td>春蕾资产管理</td><td><span class="orange">待参加</span></td></tr>';
	str += '<tr><td><a href="../activity/index.html" target="_blank">XXXX杯跑步活动</td><td>2014-9-31</td><td>徒步</td><td>春蕾资产管理</td><td><span class="orange">待参加</span></td></tr>';
	str += '<tr><td><a href="../activity/index.html" target="_blank">XXXX杯跑步活动</td><td>2014-9-31</td><td>自驾游</td><td>春蕾资产管理</td><td>已参加</td></tr>';
	str += '<tr><td><a href="../activity/index.html" target="_blank">XXXX杯跑步活动</td><td>2014-9-31</td><td>自驾游</td><td>春蕾资产管理</td><td>已参加</td></tr>';
	str += '<tr><td><a href="../activity/index.html" target="_blank">XXXX杯跑步活动</td><td>2014-9-31</td><td>旅游</td><td>春蕾资产管理</td><td>已参加</td></tr>';
	str += '<tr><td><a href="../activity/index.html" target="_blank">XXXX杯跑步活动</td><td>2014-9-31</td><td>旅游</td><td>春蕾资产管理</td><td>未参加</td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}

var submitall = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">状态</td><td style="min-width:100px;">投标完成时间</td><td style="min-width:50px;">金额</td><td style="min-width:50px;">已回款</td><td style="min-width:50px;">待回款</td><td style="min-width:50px;">合同</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>回款中</td><td>2014-8-5</td><td>500</td><td>0</td><td>23</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>回款中</td><td>2014-7-31</td><td>200</td><td>0</td><td>9.87</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款3</a></td><td>回款中</td><td>2014-7-16</td><td>300</td><td>0.57</td><td>13</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款4</a></td><td>回款中</td><td>2014-7-3</td><td>1500</td><td>15</td><td>150</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款5</a></td><td>回款中</td><td>2014-6-18</td><td>1000</td><td>20</td><td>123</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款6</a></td><td>投标中</td><td>2014-8-5</td><td>500</td><td>0</td><td>23</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款7</a></td><td>回款中</td><td>2014-7-31</td><td>200</td><td>0</td><td>9.87</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款8</a></td><td>回款中</td><td>2014-7-16</td><td>300</td><td>0.57</td><td>13</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款9</a></td><td>回款中</td><td>2014-7-3</td><td>1500</td><td>15</td><td>150</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款10</a></td><td>回款中</td><td>2014-6-18</td><td>1000</td><td>20</td><td>123</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}

var submittoafford = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">状态</td><td style="min-width:100px;">投标完成时间</td><td style="min-width:50px;">金额</td><td style="min-width:50px;">最迟支付时间</td><td style="min-width:50px;">操作</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>待支付</td><td>2014-8-5</td><td>500</td><td>2014-8-7</td><td><a href="#">立即支付</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>待支付</td><td>2014-8-6</td><td>200</td><td>2014-8-7</td><td><a href="#">立即支付</a></td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}

var submittoaudit = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">状态</td><td style="min-width:100px;">投标完成时间</td><td style="min-width:50px;">金额</td><td style="min-width:50px;">最迟审核时间</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>待审核</td><td>2014-8-5</td><td>500</td><td>2014-8-7</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>待审核</td><td>2014-8-6</td><td>200</td><td>2014-8-7</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>待审核</td><td>2014-8-5</td><td>500</td><td>2014-8-7</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>待审核</td><td>2014-8-6</td><td>200</td><td>2014-8-7</td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}

var submitpayback = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">状态</td><td style="min-width:100px;">投标完成时间</td><td style="min-width:50px;">金额</td><td style="min-width:50px;">已回款</td><td style="min-width:50px;">待回款</td><td style="min-width:50px;">合同</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>回款中</td><td>2014-8-5</td><td>500</td><td>0</td><td>23</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>回款中</td><td>2014-7-31</td><td>200</td><td>0</td><td>9.87</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款3</a></td><td>回款中</td><td>2014-7-16</td><td>300</td><td>0.57</td><td>13</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款4</a></td><td>回款中</td><td>2014-7-3</td><td>1500</td><td>15</td><td>150</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款5</a></td><td>回款中</td><td>2014-6-18</td><td>1000</td><td>20</td><td>123</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款7</a></td><td>回款中</td><td>2014-7-31</td><td>200</td><td>0</td><td>9.87</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款8</a></td><td>回款中</td><td>2014-7-16</td><td>300</td><td>0.57</td><td>13</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款9</a></td><td>回款中</td><td>2014-7-3</td><td>1500</td><td>15</td><td>150</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款10</a></td><td>回款中</td><td>2014-6-18</td><td>1000</td><td>20</td><td>123</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}

var submitdone = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">状态</td><td style="min-width:100px;">投标完成时间</td><td style="min-width:50px;">金额</td><td style="min-width:50px;">总利息</td><td style="min-width:50px;">合同</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>已关闭</td><td>2014-8-5</td><td>500</td><td>100</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>已关闭</td><td>2014-7-31</td><td>200</td><td>40</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款3</a></td><td>已关闭</td><td>2014-7-16</td><td>300</td><td>60</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款4</a></td><td>已关闭</td><td>2014-7-3</td><td>1500</td><td>300</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款5</a></td><td>已关闭</td><td>2014-6-18</td><td>1000</td><td>200</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款7</a></td><td>已关闭</td><td>2014-7-31</td><td>200</td><td>40</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}

var paybackall = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">已回款统计</td><th style="min-width:50px;">最近一年</th><th style="min-width:100px;">最近半年</th><th style="min-width:50px;">最近三个月</th><th style="min-width:50px;">最近两个月</th><th style="min-width:50px;">最近一个月</th></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td>本金</td><td>30000</td><td>20000</td><td>15000</td><td>10000</td><td>5000</td></tr>';
	str += '<tr><td>利息</td><td>6000</td><td>4000</td><td>3000</td><td>2000</td><td>1000</td></tr>';
	str += '<tr><td>总计</td><td>36000</td><td>24000</td><td>18000</td><td>12000</td><td>6000</td></tr>';
	str += '</tbody>';
	str += '</table>';
	
	str += '<br>';
	str += '<br>';
	
	
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">待回款统计</td><th style="min-width:50px;">未来一年</th><th style="min-width:100px;">未来半年</th><th style="min-width:50px;">未来三个月</th><th style="min-width:50px;">未来两个月</th><th style="min-width:50px;">未来一个月</th></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td>本金</td><td>30000</td><td>20000</td><td>15000</td><td>10000</td><td>5000</td></tr>';
	str += '<tr><td>利息</td><td>6000</td><td>4000</td><td>3000</td><td>2000</td><td>1000</td></tr>';
	str += '<tr><td>总计</td><td>36000</td><td>24000</td><td>18000</td><td>12000</td><td>6000</td></tr>';
	str += '</tbody>';
	str += '</table>';
	
	content.append(str);
	return content;
}


var paybackhave = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">还款额</td><td>本金</td><td>利息</td><td style="min-width:100px;">还款时间</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>300</td><td>250</td><td>50</td><td>2014-8-5</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>500</td><td>400</td><td>100</td><td>2014-7-31</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款3</a></td><td>2000</td><td>2000</td><td>0</td><td>2014-7-16</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款4</a></td><td>12000</td><td>11900</td><td>100</td><td>2014-7-3</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款5</a></td><td>20</td><td>0</td><td>20</td><td>2014-6-18</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款7</a></td><td>17</td><td>0</td><td>17</td><td>2014-7-31</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款8</a></td><td>700</td><td>700</td><td>0</td><td>2014-7-16</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款9</a></td><td>90</td><td>0</td><td>90</td><td>2014-7-3</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款10</a></td><td>12</td><td>0</td><td>12</td><td>2014-6-18</td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}


var paybackto = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">还款额</td><td>本金</td><td>利息</td><td style="min-width:100px;">预计还款时间</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>300</td><td>250</td><td>50</td><td>2014-8-5</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>500</td><td>400</td><td>100</td><td>2014-7-31</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款3</a></td><td>2000</td><td>2000</td><td>0</td><td>2014-7-16</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款4</a></td><td>12000</td><td>11900</td><td>100</td><td>2014-7-3</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款5</a></td><td>20</td><td>0</td><td>20</td><td>2014-6-18</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款7</a></td><td>17</td><td>0</td><td>17</td><td>2014-7-31</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款8</a></td><td>700</td><td>700</td><td>0</td><td>2014-7-16</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款9</a></td><td>90</td><td>0</td><td>90</td><td>2014-7-3</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款10</a></td><td>12</td><td>0</td><td>12</td><td>2014-6-18</td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}

var cashall = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">时间</td><td style="min-width:50px;">金额</td><td>本金</td><td>利息</td><td>手续费</td><td>操作</td><td>备注</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td>2014-8-5</td><td>300</td><td>250</td><td>50</td><td>0</td><td>还款</td><td>项目名称1</td></tr>';
	str += '<tr><td>2014-7-31</td><td>500</td><td>400</td><td>100</td><td>0</td><td>还款</td><td>项目名称2</td></tr>';
	str += '<tr><td>2014-7-16</td><td>2000</td><td>2000</td><td>0</td><td>0</td><td>还款</td><td>项目名称3</td></tr>';
	str += '<tr><td>2014-7-3</td><td>12000</td><td>12000</td><td>0</td><td>0</td><td>充值</td><td></td></tr>';
	str += '<tr><td>2014-6-18</td><td>6000</td><td>6000</td><td>0</td><td>0</td><td>提现</td><td></td></tr>';
	str += '<tr><td>2014-7-31</td><td>2000</td><td>2000</td><td>0</td><td>0</td><td>投标</td><td>项目名称3</td></tr>';
	str += '<tr><td>2014-7-16</td><td>3000</td><td>3000</td><td>0</td><td>0</td><td>投标</td><td>项目名称1</td></tr>';
	str += '<tr><td>2014-7-3</td><td>1000</td><td>1000</td><td>0</td><td>0</td><td>提现</td><td></td></tr>';
	str += '<tr><td>2014-6-18</td><td>13000</td><td>13000</td><td>0</td><td>0</td><td>充值</td><td></td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}

var cashrecharge = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">时间</td><td style="min-width:50px;">金额</td><td>本金</td><td>利息</td><td>手续费</td><td>操作</td><td>备注</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td>2014-7-3</td><td>12000</td><td>12000</td><td>0</td><td>0</td><td>充值</td><td></td></tr>';
	str += '<tr><td>2014-6-18</td><td>13000</td><td>13000</td><td>0</td><td>0</td><td>充值</td><td></td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}

var cashwithdraw = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">时间</td><td style="min-width:50px;">金额</td><td>本金</td><td>利息</td><td>手续费</td><td>操作</td><td>备注</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td>2014-6-18</td><td>6000</td><td>6000</td><td>0</td><td>0</td><td>提现</td><td></td></tr>';
	str += '<tr><td>2014-7-3</td><td>1000</td><td>1000</td><td>0</td><td>0</td><td>提现</td><td></td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}


var cashinvest = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">时间</td><td style="min-width:50px;">金额</td><td>本金</td><td>利息</td><td>手续费</td><td>操作</td><td>备注</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td>2014-7-31</td><td>2000</td><td>2000</td><td>0</td><td>0</td><td>投标</td><td>项目名称3</td></tr>';
	str += '<tr><td>2014-7-16</td><td>3000</td><td>3000</td><td>0</td><td>0</td><td>投标</td><td>项目名称1</td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}

var cashreceive = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">时间</td><td style="min-width:50px;">金额</td><td>本金</td><td>利息</td><td>手续费</td><td>操作</td><td>备注</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td>2014-8-5</td><td>300</td><td>250</td><td>50</td><td>0</td><td>还款</td><td>项目名称1</td></tr>';
	str += '<tr><td>2014-7-31</td><td>500</td><td>400</td><td>100</td><td>0</td><td>还款</td><td>项目名称2</td></tr>';
	str += '<tr><td>2014-7-16</td><td>2000</td><td>2000</td><td>0</td><td>0</td><td>还款</td><td>项目名称3</td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}

var mynote = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">标题</td><td style="min-width:50px;">发布日期</td><td style="min-width:100px;">公告类型</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td><a href="#">新一期产品即将火爆抢购</td><td>2014-9-31</td><td>理财推荐</td></tr>';
	str += '<tr><td><a href="#">XXXX杯跑步活动</td><td>2014-10-5</td><td>春蕾活动</td></tr>';
	str += '<tr><td><a href="#">系统二期功能模块开放</td><td>2014-10-15</td><td>平台资讯</td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}


var nav2funtion = {
		"my-score" : myscore,
		"my-activity" : myactivity,
		"my-note" : mynote,
		"submit-all" : submitall,
		"submit-toafford" : submittoafford,
		"submit-toaudit" : submittoaudit,
		"submit-payback" : submitpayback,
		"submit-done" : submitdone,
		"payback-all" : paybackall,
		"payback-have" : paybackhave,
		"payback-to" : paybackto,
		"cash-all" : cashall,
		"cash-recharge" : cashrecharge,
		"cash-withdraw" : cashwithdraw,
		"cash-invest" : cashinvest,
		"cash-receive" : cashreceive,
		"tools-fluxility" : "tools-fluxility",
		"tools-receive-statistics" : "tools-receive-statistics"
}