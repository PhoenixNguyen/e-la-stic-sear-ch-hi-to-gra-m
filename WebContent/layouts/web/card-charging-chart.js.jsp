<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
	var listAll = [], listSuccess = [], listError = [], listWrong = [];

	///////////////////////////////////////// LINE =============================================
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
	
	draw(listAll);

	function draw(json) {
		var chart;

		nv.addGraph(function() {
			chart = nv.models.lineChart().options({
				margin : {
					left : 100,
					bottom : 100
				},
				x : function(d, i) {
					return i
				},
				showXAxis : true,
				showYAxis : true,
				transitionDuration : 250
			});

			// chart sub-models (ie. xAxis, yAxis, etc) when accessed directly, return themselves, not the parent chart, so need to chain separately
			chart.xAxis.axisLabel("Thời gian").tickFormat(d3.format(',.1f'));

			chart.yAxis.axisLabel('Số lượng').tickFormat(d3.format('d')); //,.2f

			//var format = d3.time.format("%Y-%m-%d %H:%m:%s"); 
			chart.xAxis.tickFormat(function(d) {
				var date;
				if(json[d].key != '')
					date = new Date(parseInt(json[d].key));
				else
					date = new Date();
				
				return [ date.getDate(), date.getMonth() + 1 ].join('/') + " "
						+ [ date.getHours(), date.getMinutes() ].join(':');
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

		function generateLineCoordinates() {
			for (var i = 0; i < json.length; i++) {
				if ((listSuccess.length - 1) < i
						|| json[i].key != listSuccess[i].key) {
					var blank = {
						key : json[i].key,
						count : 0
					};
					listSuccess.splice(i, 0, blank);
				}
				if ((listError.length - 1) < i
						|| json[i].key != listError[i].key) {
					var blank = {
						key : json[i].key,
						count : 0
					};
					listError.splice(i, 0, blank);
				}
				if ((listWrong.length - 1) < i
						|| json[i].key != listWrong[i].key) {
					var blank = {
						key : json[i].key,
						count : 0
					};
					listWrong.splice(i, 0, blank);
				}
			}
			
			var lineTotal = [], lineSuccess = [], lineError = [], lineWrong = [];
			var countArr = [];
			for(var i = 0; i < json.length; i++){
				countArr.push(json[i].count);
			}
			
			for (var i = 0; i < json.length; i++) {
				console.log(listError[i]);
				lineTotal.push({
					x : i,
					y : json[i].count
				});

				lineSuccess.push({
					x : i,
					y : listSuccess[i].count
				});

				lineError.push({
					x : i,
					y : listError[i].count
				});
				lineWrong.push({
					x : i,
					y : listWrong[i].count
				});

			}

			return [ {

				values : lineTotal,
				key : "Tất cả",
				color : "#2222ff"
			}, {

				values : lineSuccess,
				key : "Thành công",
				color : "#2ca02c"
			}
			, {

				values : lineError,
				key : "Thẻ lỗi",
				color : "#FF0000"
			}, {

				values : lineWrong,
				key : "Thẻ sai",
				color : "#DAA520"
			} 
			];
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