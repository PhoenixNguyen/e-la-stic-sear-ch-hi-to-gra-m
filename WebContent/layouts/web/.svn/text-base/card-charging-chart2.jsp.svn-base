<%@ page language="java" trimDirectiveWhitespaces="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/el"%>
<%@ page import="vn.onepay.common.SharedConstants, vn.onepay.account.model.Account, java.lang.String"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="head.jsp"></jsp:include>
<link href="${pageContext.request.contextPath }/css/personal.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/jquery-ui.min.css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.multiselect.js"></script>

<jsp:include page="chart.lib.jsp" />
<script src="<%=request.getContextPath()%>/js/numeral.js" type="text/javascript"></script>

<style>
	.fieldset_filter {border: solid 1px #ccc;margin-left:20px; width: auto;}
</style>

<script type="text/javascript">
$(function(){

	$("#filter_merchant").multiselect({
		selectedList: 4,
		noneSelectedText: "Tất cả"
	});
	$("#filter_provider").multiselect({
		selectedList: 4,
		noneSelectedText: "Tất cả"
	});
	$("#filter_card_type").multiselect({
		selectedList: 4,
		noneSelectedText: "Tất cả"
	});
	$("#filter_card_amount").multiselect({
		selectedList: 4,
		noneSelectedText: "Tất cả"
	});
});
</script>

<style >
	.box_locketqua table a{
		font-size: 14px;
		color: #2c8f39;
		padding-top: 2px;
		
	}
	
	.box_locketqua table a.slc_link {
		color: #FFA200;
	}
	
	.select_filter{
		border: 1px solid #CCCCCC;
		padding: 6px;
		margin: 0px;
		-webkit-border-radius: 3px;
		-moz-border-radius: 3px;
		border-radius: 3px;
		width: 115px;
		margin-left: 0px;
		box-shadow: 0 0 0 #000000, 0 3px 3px #EEEEEE inset;
	}
	.text_filter{
		border: 1px solid #CCCCCC;
		padding: 6px;
		margin: 0px;
		-webkit-border-radius: 3px;
		-moz-border-radius: 3px;
		border-radius: 3px;
		width: 250px;
		margin-left: 0px;
		box-shadow: 0 0 0 #000000, 0 3px 3px #EEEEEE inset;
	}
	.chart , .chart:HOVER{
		background: url("<%=request.getContextPath()%>/images/btngreen_bg.png") repeat-x scroll center top;
		color: #FFFFFF;
		display: block;
		float: right;
		font-weight: bold;
		height: 20px;
		line-height: 20px;
		padding-left: 5px;
		padding-right: 5px;
		border: 1px solid #39B54A;
		border-radius: 10px;
		text-shadow: 0 1px #20942B;
	}
	
	.label_filter{
		margin-left: 20px;
		width: 120px;
		display: inline-block;
	}
	
	.pie{
		margin:auto; display:table; 
	}
	.pie_child{
		
		width: 500px;
	}
	
</style>
</head>
<%
	Account account = (Account) request.getSession().getAttribute(SharedConstants.ACCOUNT_LOGINED);
	boolean isAdmin = account.isAdmin();
	boolean mbiz =  SharedConstants.MBIZ && isAdmin;
	request.setAttribute("MBIZ", mbiz);
%>
<body>
	<!-- 1PAY WEB -->
	<div id="wrapper">
		<!-- Web Top -->
		<div id="w_top">
			<div class="layout_1000">
				<!-- Header -->
				<%request.setAttribute("menuTrangChu", true); %>
				<jsp:include page="header.jsp"></jsp:include>
				<!-- / Header -->

				<!-- Body -->
				<div id="w_body">
					<div class="row_sub">
						<!-- Persanal Bar -->
						<jsp:include page="include_personal_bar.jsp" />
						<!-- Left Menu -->
						<%request.setAttribute("cardReportElastic", true);%>
						<jsp:include page="card-charging-chart-search-menu2.jsp" />
						
						<div class="right_content">
							<h1 class="srv_title">Lược đồ theo thời gian</h1>
							<!-- Filter -->
							<div id="filter">
								<form action="card-dashboard.html" method="get" name="filter_histogram">
									<c:forEach items="${model.fieldMaps }" var="map">
									
										<c:if test="${param[map.key] != null && param[map.key] != ''  }">
											<input type="hidden" name="${map.key }" value="${param[map.key] }"/>
										</c:if>
									</c:forEach>	
									
									<div class="filter_row">
										<label class="label_filter">Merchant:</label>
										<input type="text" name="filter_merchant" class="text_filter" placeholder="Nhập tên merchant" value="${param.filter_merchant }"/>
										
										<label class="label_filter">Nhà cung cấp:</label>
										<c:set var="allPv" value="," />
										<c:forEach var="pv" items="${paramValues.filter_provider}">
											<c:set var="allPv" value="${allPv}${pv}," />
										</c:forEach>
										<select id="filter_provider" class="select_filter" name="filter_provider" multiple="multiple" style="width:200px; padding: 6px;">
										
											<c:forEach var="provider" items="${model.facetAllsMap['paymentProvider'] }">
												<c:set var="provider2" value=",${provider.getTerm()}," />
												<option value="${provider.getTerm() }" ${fn:contains(allPv, provider2)?'selected':'' }>
													<c:out value="${provider.getTerm()}"/>
												</option>
												<c:set var="listForSearchPv" value="${listForSearchPv}<option value='${provider.getTerm()}'>${provider.getTerm()}</option>" />
											</c:forEach>
										</select>
										
									</div>
									
									<div class="filter_row">
										<fieldset class="fieldset_filter">
											<legend>
												<input type="checkbox" name="telcoAll" value="telcoAll" id="telcoAll" class="chck_filter" ${fn:contains(param.telcoAll,'telcoAll')?'checked="checked"':'' }  /> Loại thẻ
												<script type="text/javascript">
													$(document).ready(function(){
														$('#telcoAll').click(function() {
															if(this.checked) {
																$(this).closest('.filter_row').find('input[type=checkbox]').attr('checked', 'checked');
															} else {
																$(this).closest('.filter_row').find('input[type=checkbox]').removeAttr('checked');
															}
														});
													});
												</script>
											</legend>
											
											<div style="padding: 2px;">
												<c:set var="allTc" value="," />
												<c:forEach var="tc" items="${paramValues.filter_card_type}">
													<c:set var="allTc" value="${allTc}${tc}," />
												</c:forEach>
												<c:forEach var="tc" items="${model.facetAllsMap['type']}">
													<div style="display: inline-block;">
														<c:set var="_tc" value=",${tc.getTerm()}," />
														<input type="checkbox" class="chck_filter" name="filter_card_type" title="${tc.getTerm()}" value="${tc.getTerm()}" 
															${fn:contains(allTc,_tc)?'checked="checked"':'' } />
														<label class="lbl_chcksub">${tc.getTerm()}</label>
													</div>
												</c:forEach>
											</div>
										</fieldset>
									</div>
									
									<div class="filter_row">
										<label class="label_filter">Ngày:</label>
										<input id="time_search" maxlength="100" name="time_search" value="${param.time_search }" class="select_filter" placeholder="Chọn ngày"/>
										<script type="text/javascript">
											
											$(document).ready(function(){
												//On change
												$('input[name=time_search]').change(function(){
													$('form[name=filter_histogram]').submit();
												});
												//Enter
												$('input[name=filter_merchant]').keydown(function(event) {
											        if (event.keyCode == 13) {
											            this.form.submit();
											            return false;
											         }
											    });
												var time_search = $('input[name=time_search]');
												if(time_search.val() == '')
												{
													var date = new Date();
													time_search.val(date.toString('dd/MM/yyyy'));
												}
											});
											$(function() {
											    $('#time_search').datepicker( {
											        changeMonth: true,
											        changeYear: true,
											        showButtonPanel: true,
											        dateFormat: 'dd/mm/yy'
											    });
											});
										</script>
									</div>
									
									<!-- <div class="filter_row" style="text-align: center;">
										
						               	<input  style="margin-top: 0px;"  class="btn_greensmall" type="submit" value="Lọc" />
						               	
					                </div> -->
								</form>
								
								<div class="filter_row">
									
								</div>
							</div>
							
							<script>
								//load 1st
								var filter_merchant;
								var merchant_permission = [];
								var filter_provider = [];
								var filter_card_type = [];
								var statusSuccess = ['00'];
								var statusError = ['01', '02', '03', '04', '05', '06', '16', '17', '18', '19', '20', '21', '22', '23', '99'];
								var statusWrong = ['07', '08', '09', '10', '11', '12', '13', '14', '15'];
								
							</script>
							<!-- End Filter -->
							
							<!-- LINE CHART -->
							<jsp:include page="card-charging-chart.js2.jsp" />
							<div id="line_chart" >
							  <svg style="height: 400px;"></svg>
							</div>
							
							<!-- PIE CHART -->
							<h1 class="srv_title">Biểu đồ tổng quát</h1>
							
							<fieldset class="fieldset_filter">
								<legend >
									<label style="text-align: left;">Trạng thái</label>
								</legend>
								<span class="pie">
									<svg id="status_chart" class="pie_child" ></svg>
								</span>
							</fieldset>
							<fieldset class="fieldset_filter">
								<legend>
									Loại thẻ
								</legend>
								<span class="pie">
									<svg id="type_chart" class="pie_child"></svg>
								</span>
							</fieldset>
							<fieldset class="fieldset_filter">
								<legend style="text-align: left;">Nhà cung cấp
								</legend>
								<span class="pie">
									<svg id="provider_chart" class="pie_child"></svg>
								</span>
							</fieldset>
							<fieldset class="fieldset_filter">
								<legend>Merchant
								</legend>
								<span class="pie">
									<svg id="merchant_chart" class="pie_child"></svg>
								</span>
							</fieldset>
							
						</div>
					</div>
				</div>
				<!-- / Body -->
			</div>
		</div>
		<!-- / Web Top -->

		<!-- Web Foot -->
		<jsp:include page="footer.jsp"></jsp:include>
		<!-- / Web Foot -->
	</div>
	<!-- / 1PAY WEB -->
	<script type="text/javascript">
		/*         */
		jQuery(function($) {

			$(function() {
				$('#hot_productslides').slides({
					preload : true,
					preloadImage : 'images/loading.gif',
					play : 5000,
					pause : 2500,
					hoverPause : true
				});
			});

		});
		/*   */
	</script>
	<!-- Create Menu Settings: (Menu ID, Is Vertical, Show Timer, Hide Timer, On Click ('all' or 'lev2'), Right to Left, Horizontal Subs, Flush Left, Flush Top) -->
	<script type="text/javascript">
		qm_create(0, false, 0, 250, false, false, false, false, false);
	</script>
	<!--[if IE]><iframe onload="on_script_loaded(function() { HashKeeper.reloading=false; });" style="display: none" name="hashkeeper" src="/blank" height="1" width="1" id="hashkeeper"></iframe><![endif]-->

</body>
</html>
