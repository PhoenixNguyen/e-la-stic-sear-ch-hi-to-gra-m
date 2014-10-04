<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
	var listAll = [], listSuccess = [], listError = [], listWrong = [], limitError = [];
	var countLimit = parseInt('<c:out value="${model.total/1000}"/>');
	
	var listAllLast = [], listSuccessLast = [], listErrorLast = [], listWrongLast = [], limitErrorLast = [];
	
	///////////////////////////////////////// LINE =============================================
	//curr
	<c:forEach var="item" items="${model.statusHistogramMap['successStatus'] }">
		listSuccess.push({key : '<c:out value="${item.key}"/>', count : parseInt('<c:out value="${item.count}"/>')});
	</c:forEach>
	<c:forEach var="item" items="${model.statusHistogramMap['errorStatus'] }">
		listError.push({key : '<c:out value="${item.key}"/>', count : parseInt('<c:out value="${item.count}"/>')});
	</c:forEach>
	<c:forEach var="item" items="${model.statusHistogramMap['wrongStatus'] }">
		listWrong.push({key : '<c:out value="${item.key}"/>', count : parseInt('<c:out value="${item.count}"/>')});
	</c:forEach>
	<c:forEach var="item" items="${model.statusHistogramMap['allStatus'] }">
		listAll.push({key : '<c:out value="${item.key}"/>', count : parseInt('<c:out value="${item.count}"/>')});
	</c:forEach>
	
	//last
	<c:forEach var="item" items="${model.statusHistogramMapLast['successStatus'] }">
		listSuccessLast.push({key : '<c:out value="${item.key}"/>', count : parseInt('<c:out value="${item.count}"/>')});
	</c:forEach>
	<c:forEach var="item" items="${model.statusHistogramMapLast['errorStatus'] }">
		listErrorLast.push({key : '<c:out value="${item.key}"/>', count : parseInt('<c:out value="${item.count}"/>')});
	</c:forEach>
	<c:forEach var="item" items="${model.statusHistogramMapLast['wrongStatus'] }">
		listWrongLast.push({key : '<c:out value="${item.key}"/>', count : parseInt('<c:out value="${item.count}"/>')});
	</c:forEach>
	<c:forEach var="item" items="${model.statusHistogramMapLast['allStatus'] }">
		listAllLast.push({key : '<c:out value="${item.key}"/>', count : parseInt('<c:out value="${item.count}"/>')});
	</c:forEach>
	
	/* alert(listAllLast.length);
	alert(listAll.length); */
	draw(listAll, listAllLast, true);
	
	$(document).ready(function(){
		$('#comparation').change('click', function(){
			$('#line_chart svg').empty()
			
			var val = $(this).is(':checked');
			draw(listAll, listAllLast, val);
		});	
	});
	
	function draw(listCrr, listLast, compare) {
		var json = listCrr;
		
		var chart;

		nv.addGraph(function() {
			chart = nv.models.lineChart().options({
				margin : {
					left : 100,
					bottom : 100
				},
				/* x : function(d, i) {
					
					return i;
				},  */
				showXAxis : true,
				showYAxis : true,
				transitionDuration : 250
			});

			chart.yAxis.axisLabel('Số lượng').tickFormat(d3.format('d')); //,.2f

			chart.xAxis
			.rotateLabels(-20)
			.tickFormat(function(d) {
				return new Date(d).toString("dd/MM/yyyy HH:mm");
			});
			
			d3.select('#line_chart svg').datum(generateLineCoordinates()).call(
					chart);

			//TODO: Figure out a good way to do this automatically
			nv.utils.windowResize(chart.update);
			//nv.utils.windowResize(function() { d3.select('#chart1 svg').call(chart) });

			chart.dispatch.on('stateChange', function(e) {
				nv.log('New State:', JSON.stringify(e));
			});

			return chart;
		});
		
		var time_search = '<c:out value="${param.time_search}"/>';
		//alert(time_search);
		if(time_search == ''){
			var day = new Date();
			time_search = (day.getDate() <10 ?'0'+day.getDate():day.getDate()) +'/' + (day.getMonth() +1) + '/' +day.getFullYear();
		}
		//alert(time_search);
		
		var oneDayMilis = 24*60*60*1000;
		
		var period = 0.1; //0.1h
		
		var levelTimeAll = generateTemplateTimePeriod(period, time_search);
		var levelTimeAllLast = generateTemplateTimePeriodLast(period, time_search);
		//alert(new Date(levelTimeAllLast[1]));
		
		function generateTemplateTimePeriod(period, time_search){
			//var period = 0.1;
			var day = Date.parseExact(time_search, 'dd/MM/yyyy')//'2014/09/18'
			//24*60 = 1440 minute
			var hours = 24;
			var levels = hours / period; 
			var levelTime = [];
			for(var i = 0; i < levels; i++){
				//levelTime.push(new Date(day.getTime() + i*period*60*60*1000).toString('dd/MM/yyyy HH:mm'));
				levelTime.push(day.getTime() + i*period*60*60*1000);
			}
			
			return levelTime;
		}
		function generateTemplateTimePeriodLast(period, time_search){
			//var period = 0.1;
			var day = Date.parseExact(time_search, 'dd/MM/yyyy')//'2014/09/18'
			//24*60 = 1440 minute
			var hours = 24;
			var levels = hours / period; 
			var levelTime = [];
			
			for(var i = 0; i < levels; i++){
				//levelTime.push(new Date(day.getTime() + i*period*60*60*1000).toString('dd/MM/yyyy HH:mm'));
				levelTime.push(day.getTime() - oneDayMilis  + i*period*60*60*1000);
			}
			
			return levelTime;
		}
		function generateLineCoordinates() {
			//To fill missions
			for (var i = 0; i < levelTimeAll.length; i++) {
				limitError.push({key : levelTimeAll[i], count : countLimit});
				
				if (listCrr == '' || (listCrr.length - 1) < i
						|| levelTimeAll[i] != listCrr[i].key) {
					var blank = {
						key : levelTimeAll[i],
						count : 0
					};
					
					listCrr.splice(i, 0, blank);
				}
				
				if (listSuccess == '' || (listSuccess.length - 1) < i
						|| levelTimeAll[i] != listSuccess[i].key) {
					var blank = {
						key : levelTimeAll[i],
						count : 0
					};
					listSuccess.splice(i, 0, blank);
				}
				
				if (listError == '' || (listError.length - 1) < i
						|| levelTimeAll[i] != listError[i].key) {
					var blank = {
						key : levelTimeAll[i],
						count : 0
					};
					listError.splice(i, 0, blank);
				}
				
				if (listWrong == '' || (listWrong.length - 1) < i
						|| levelTimeAll[i] != listWrong[i].key) {
					var blank = {
						key : levelTimeAll[i],
						count : 0
					};
					listWrong.splice(i, 0, blank);
				}
				
				//compare
				if(compare== true){
					if (listLast == '' || (listLast.length - 1) < i
							|| levelTimeAllLast[i] != listLast[i].key) {
						var blank = {
							key : levelTimeAllLast[i],
							count : 0
						};
						
						listLast.splice(i, 0, blank);
					}
					
					if (listSuccessLast == '' || (listSuccessLast.length - 1) < i
							|| levelTimeAllLast[i] != listSuccessLast[i].key) {
						var blank = {
							key : levelTimeAllLast[i],
							count : 0
						};
						listSuccessLast.splice(i, 0, blank);
					}
					
					if (listErrorLast == '' || (listErrorLast.length - 1) < i
							|| levelTimeAllLast[i] != listErrorLast[i].key) {
						var blank = {
							key : levelTimeAllLast[i],
							count : 0
						};
						listErrorLast.splice(i, 0, blank);
					}
					
					if (listWrongLast == '' || (listWrongLast.length - 1) < i
							|| levelTimeAllLast[i] != listWrongLast[i].key) {
						var blank = {
							key : levelTimeAllLast[i],
							count : 0
						};
						listWrongLast.splice(i, 0, blank);
					}
					
				} 
			}
			
			var lineTotal = [], lineSuccess = [], lineError = [], lineWrong = [], lineLimit = [];
			var lineTotalLast = [], lineSuccessLast = [], lineErrorLast = [], lineWrongLast = [];
			
			for (var i = 0; i < levelTimeAll.length; i++) {
				lineTotal.push({
					x : new Date(parseInt(listCrr[i].key)),
					y : listCrr[i].count
				});

				lineSuccess.push({
					x : new Date(parseInt(listSuccess[i].key)),
					y : listSuccess[i].count
				});

				lineError.push({
					x : new Date(parseInt(listError[i].key)),
					y : listError[i].count
				});
				lineWrong.push({
					x : new Date(parseInt(listWrong[i].key)),
					y : listWrong[i].count
				});
				
				lineLimit.push({
					x : new Date(parseInt(limitError[i].key)),
					y : limitError[i].count
				});
				
				//compare
				if(compare== true){
					lineTotalLast.push({
						x : new Date(parseInt(listLast[i].key) + oneDayMilis),
						y : listLast[i].count
					});

					lineSuccessLast.push({
						x : new Date(parseInt(listSuccessLast[i].key) + oneDayMilis),
						y : listSuccessLast[i].count
					});

					lineErrorLast.push({
						x : new Date(parseInt(listErrorLast[i].key) + oneDayMilis),
						y : listErrorLast[i].count
					});
					lineWrongLast.push({
						x : new Date(parseInt(listWrongLast[i].key) + oneDayMilis),
						y : listWrongLast[i].count
					});
				}

			}
			
			//compare
			if(compare == false){
				return [ {
	
					values : lineTotal,
					key : "Tất cả",
					color : "#2222ff"
				}
				 , {
	
					values : lineSuccess,
					key : "Thành công",
					color : "#2ca02c"
				}
				, {
	
					values : lineWrong,
					key : "Thẻ sai",
					color : "#DAA520"
				}  
				, {
	
					values : lineError,
					key : "Thẻ lỗi",
					color : "#FF0000"
				}
				, {
	
					values : lineLimit,
					key : "Giới hạn lỗi",
					color : "#FA8072"
				}
				];
			}
			else{
				return [ {

					values : lineTotal,
					key : "Tất cả",
					color : "#2222ff"
				}
				,{

					values : lineTotalLast,
					key : "Tất cả ngày hôm trước",
					color : "#9090ff"
				}
				 , {

					values : lineSuccess,
					key : "Thành công",
					color : "#2ca02c"
				}
				 , {

						values : lineSuccessLast,
						key : "Thành công ngày hôm trước",
						color : "#80c680"
					}
				, {

					values : lineWrong,
					key : "Thẻ sai",
					color : "#DAA520"
				}  
				, {

					values : lineWrongLast,
					key : "Thẻ sai ngày hôm trước",
					color : "#ecd28f"
				} 
				, {

					values : lineError,
					key : "Thẻ lỗi",
					color : "#FF0000"
				}
				, {

					values : lineErrorLast,
					key : "Thẻ lỗi ngày hôm trước",
					color : "#ffb2b2"
				}
				, {

					values : lineLimit,
					key : "Giới hạn lỗi",
					color : "#FA8072"
				}
				];
			}
		}
	}
	//End LINE

	////////////////////////////////////// PIE ==================================================
	var statusSuccess = [];
	var statusError = [];
	var statusWrong = [];
	<c:forEach var="item" items="${model.successStatus }">
		statusSuccess.push('<c:out value="${item}"/>');
	</c:forEach>
	<c:forEach var="item" items="${model.wrongStatus }">
		statusWrong.push('<c:out value="${item}"/>');
	</c:forEach>
	<c:forEach var="item" items="${model.errorStatus }">
		statusError.push('<c:out value="${item}"/>');
	</c:forEach>
	
	var statusFacet = [], typeFacet = [], providerFacet = [], merchantFacet = [];	
	
	<c:forEach var="item" items="${model.facetsMap['status'] }">
		statusFacet.push({term : '<c:out value="${item.term}"/>', count : '<c:out value="${item.count}"/>'});
	</c:forEach>
	<c:forEach var="item" items="${model.facetsMap['type'] }">
		typeFacet.push({term : '<c:out value="${item.term}"/>', count : '<c:out value="${item.count}"/>'});
	</c:forEach>
	<c:forEach var="item" items="${model.facetsMap['paymentProvider'] }">
		//alert('<c:out value="${item.count}"/>');
		providerFacet.push({term : '<c:out value="${item.term}"/>', count : '<c:out value="${item.count}"/>'});
	</c:forEach>
	<c:forEach var="item" items="${model.facetsMap['merchant'] }">
		merchantFacet.push({term : '<c:out value="${item.term}"/>', count : '<c:out value="${item.count}"/>'});
	</c:forEach>
	
	var size_chart_w = 500;
	var size_chart_h = 500;
	drawPie(statusFacet, 'status', size_chart_w, size_chart_h, '#status_chart');
	drawPie(typeFacet, 'type', size_chart_w, size_chart_h, '#type_chart');
	drawPie(providerFacet, 'paymentProvider', size_chart_w, size_chart_h, '#provider_chart');
	drawPie(merchantFacet, 'merchant', size_chart_w, size_chart_h, '#merchant_chart');
	
	function drawPie(json, field, width, height, id) {
		if (field == 'status') {
			var statusArrays = [];
			var successCount = 0;
			var errorCount = 0;
			var wrongCount = 0;
			
			if(json != ''){
				$.each(json, function(k, v) {
	
					$.each(statusSuccess, function(i, item) {
	
						if (item == v.term)
							successCount += parseInt(v.count);
					});
	
					$.each(statusError, function(i, item) {
						if (item == v.term) {
							errorCount += parseInt(v.count);
						}
					});
	
					$.each(statusWrong, function(i, item) {
						if (item == v.term)
							wrongCount += parseInt(v.count);
					});
	
				});
				
				statusArrays.push({
					key : 'Thành công ',
					value : successCount
				});
				statusArrays.push({
					key : 'Thẻ lỗi ',
					value : errorCount
				});
				statusArrays.push({
					key : 'Thẻ sai ',
					value : wrongCount
				});
			}
			
			nv.addGraph(function() {
				var colors = [ "#2ca02c", "#FF0000", "#DAA520" ];
				var chart = nv.models.pieChart().x(function(d) {
					return d.key;
				}).y(function(d) {
					return d.value;
				}).color(colors) //d3.scale.category10().range()
				.width(width).height(height).labelType("percent");

				d3.select(id).datum(statusArrays).transition().duration(1200)
						.attr('width', width).attr('height', height)
						.call(chart);

				chart.dispatch.on('stateChange', function(e) {
					nv.log('New State:', JSON.stringify(e));
				});

				return chart;
			});
		} else {
			//alert(json);
			nv.addGraph(function() {

				var chart = nv.models.pieChart().x(function(d) {
					return d.term;
				}).y(function(d) {
					return d.count;
				}).color(d3.scale.category20().range()) //d3.scale.category10().range()
				.width(width).height(height).labelType("percent");

				d3.select(id).datum(json).transition().duration(1200).attr(
						'width', width).attr('height', height).call(chart);

				chart.dispatch.on('stateChange', function(e) {
					nv.log('New State:', JSON.stringify(e));
				});

				return chart;
			});
		}
	}

	//End PIE
</script>