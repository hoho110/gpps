var nav2funtion = {
		"my-score" : function(){
			return "您好王冬，您的积分是1000000分，等级为钻石会员";
		},
		"submit-all" : function(){
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
			return str;
		}
}