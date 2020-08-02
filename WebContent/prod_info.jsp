<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
	<link href="css/prodInfo.css" rel="stylesheet" type="text/css">
</head>
<body>
	<%@ include file="/_head.jsp" %>
	<div id="warp">
		<div id="left">
			<div id="left_top">
				<!-- <img src="img/prodInfo/prod1.png"/> -->
				<img src="${pageContext.request.contextPath}/servlet/ProdImgServlet?imgurl=${prod.imgurl}"/>
			</div>
			<div id="left_bottom">
				<img id="lf_img" src="img/prodInfo/lf.jpg"/>
				<img id="mid_img" src="${pageContext.request.contextPath}/servlet/ProdImgServlet?imgurl=${prod.imgurl}" width="60px" height="60px"/>
				<img id="rt_img" src="img/prodInfo/rt.jpg"/>
			</div>
		</div>
	<form action="${pageContext.request.contextPath}/servlet/CartAddServlet"  method="post">
		<div id="right">
			<div id="right_top">
				<span id="prod_name">跳吧 <br/></span>
				<br>
				<span id="prod_desc">我可不捞你<br/></span>
			</div>
			<div id="right_middle">
				<span id="right_middle_span">
					EasyMall 价：
				<span class="price_red">￥100.0
				<br/>
			    运&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;费：满 100 免运费<br />
			    服&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;务：由EasyMall负责发货，并提供售后服务<br />
			    购买数量：
	            <a href="javascript:void(0)" id="delNum" onclick="">-</a>
	            <input type="text" id="buyNumInp" name="buyNum" value="1">
		        <a href="javascript:void(0)" id="addNum" onclick="">+</a>
			</div>
			<div id="right_bottom">
				<input type="hidden" name="pid" value="${ prod.id }"/>
				<input class="add_cart_but" type="submit" value=""/>	
			</div>
		</div>
	</form>
	</div>
	<%@ include file="/_foot.jsp"%>
</body>
</html>