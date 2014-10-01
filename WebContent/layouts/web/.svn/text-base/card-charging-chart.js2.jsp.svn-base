<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
	var listSuccess = [], listError = [], listWrong = [];
	var time = '0.1h';//0.1h
	var urlGet = 'http://localhost:9200/cardcdrs/_search?pretty=true';
	var timeFormat = 'dd/MM/yyyy'

	var query = function(timePicked, field, facet, facet_size, merchant_permission, filter_merchant,
			filter_provider, filter_card_type, status) {
		if(timePicked == '')
			timePicked = '00/00/0000';
		
		var fromDate = Date.parseExact(timePicked, timeFormat);
		var toDate = Date.parseExact(timePicked, timeFormat);
		toDate.setDate(toDate.getDate() + 1);

		var q = {};
		q["query"] = {
			"match_all" : {}
		};
		
		if(facet == 'histogram')
			q["facets"] = {
				"published" : {
					"date_histogram" : {
						"field" : field,
						"interval" : time
					}
				}
			};
		else
			q["facets"] = {
				"tags" : {
					"terms" : {
						"field" : field,
						"size" : facet_size
					}
				}
			};
		
		//and
		var and_operator = [];
		if(facet == 'histogram')
			and_operator.push({
				"range" : {
					"timestamp" : {
						"from" : fromDate.getTime(),
						"to" : toDate.getTime()
					}
				}
			});

		//alert(status);
		if (status != null) {
			and_operator.push({
				"query" : {
					"terms" : {
						"status" : status

					}
				}
			});
		}
		and_operator.push({
			"query" : {
				"terms" : {
					"merchant" : merchant_permission

				}
			}
		});
		and_operator.push({
			"regexp" : {
				"merchant" : ".*" + filter_merchant + ".*"

			}
		});
		and_operator.push({
			"query" : {
				"terms" : {
					"paymentProvider" : filter_provider
				}
			}
		});

		and_operator.push({
			"query" : {
				"terms" : {
					"type" : filter_card_type
				}
			}
		});

		q["query"] = {
			"filtered" : {
				"filter" : {
					"and" : and_operator
				}
			}
		}
		//alert(q.query);
		return q;
	};

	///////////////////////////////////////// LINE =============================================
	$(document)
			.ready(
					function() {
						//load 1st
						if ('<c:out value="${param.filter_merchant}"/>' != '') {

							filter_merchant = '<c:out value="${param.filter_merchant}"/>';
							//alert(filter_merchant);
						} else
							filter_merchant = '';

						<c:forEach var="item" items="${model.facetAllsMap['merchant'] }">

						merchant_permission
								.push('<c:out value="${item.getTerm()}"/>');
						</c:forEach>

						if ('<c:out value="${paramValues.filter_provider}"/>' != '') {
							<c:forEach var="item" items="${paramValues.filter_provider}">
							filter_provider.push('<c:out value="${item}"/>');
							</c:forEach>
						} else {
							<c:forEach var="item" items="${model.facetFilterMap['paymentProvider'] }">
							filter_provider
									.push('<c:out value="${item.getTerm()}"/>');
							</c:forEach>
						}
						if ('<c:out value="${paramValues.filter_card_type}"/>' != '') {
							<c:forEach var="item" items="${paramValues.filter_card_type}">
							filter_card_type.push('<c:out value="${item}"/>');
							</c:forEach>
						} else {
							<c:forEach var="item" items="${model.facetFilterMap['type'] }">
							filter_card_type
									.push('<c:out value="${item.getTerm()}"/>');
							</c:forEach>
						}

						var pick_date = $('input[name=time_search]').val();
						load_time_detail(pick_date, merchant_permission,
								filter_merchant, filter_provider,
								filter_card_type, statusSuccess)
								.success(
										function(data) {
											listSuccess = data.facets.published.entries;

										});
						load_time_detail(pick_date, merchant_permission,
								filter_merchant, filter_provider,
								filter_card_type, statusError).success(
								function(data2) {
									listError = data2.facets.published.entries;
								});
						load_time_detail(pick_date, merchant_permission,
								filter_merchant, filter_provider,
								filter_card_type, statusWrong).success(
								function(data3) {
									listWrong = data3.facets.published.entries;
								});
						load_time(pick_date, merchant_permission,
								filter_merchant, filter_provider,
								filter_card_type);

					});

	var load_time = function(timePicked, merchant_permission, filter_merchant,
			filter_provider, filter_card_type) {

		//var timePicked = '18/09/2014';
		var fromDate = Date.parseExact(timePicked, timeFormat);
		var toDate = Date.parseExact(timePicked, timeFormat);
		toDate.setDate(toDate.getDate() + 1);

		//alert(new Date(fromDate));
		$.ajax({
			url : urlGet,
			type : 'POST',
			data : JSON.stringify(query(timePicked, 'timestamp', 'histogram', 0, merchant_permission,
					filter_merchant, filter_provider, filter_card_type, null)),
			dataType : 'json',
			processData : false,
			success : function(json, statusText, xhr) {
				//alert(json.facets.published.entries);
				return draw(json.facets.published.entries);
			},
			error : function(xhr, message, error) {
				console.error("Error while loading data from ElasticSearch",
						message);
				throw (error);
			}
		});
	};

	var load_time_detail = function(timePicked, merchant_permission,
			filter_merchant, filter_provider, filter_card_type, status) {
		//var timePicked = '18/09/2014';
		var fromDate = Date.parseExact(timePicked, timeFormat);
		var toDate = Date.parseExact(timePicked, timeFormat);
		toDate.setDate(toDate.getDate() + 1);

		return $.ajax({
			url : urlGet,
			type : 'POST',
			data : JSON
					.stringify(query(timePicked, 'timestamp', 'histogram', 0, merchant_permission,
							filter_merchant, filter_provider, filter_card_type,
							status))
		});

	};

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

			chart.yAxis.axisLabel('Số lượng').tickFormat(d3.format(',.2f'));

			//var format = d3.time.format("%Y-%m-%d %H:%m:%s"); 
			chart.xAxis.tickFormat(function(d) {
				var date = new Date(json[d].time);

				//return [date.getDate(), date.getMonth()+1].join('/');
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
			//alert(listSuccess[0].count);
			for (var i = 0; i < json.length; i++) {
				if ((listSuccess.length - 1) < i
						|| json[i].time != listSuccess[i].time) {
					var blank = {
						time : json[i].time,
						count : 0
					};
					listSuccess.splice(i, 0, blank);
				}
				if ((listError.length - 1) < i
						|| json[i].time != listError[i].time) {
					var blank = {
						time : json[i].time,
						count : 0
					};
					listError.splice(i, 0, blank);
				}
				if ((listWrong.length - 1) < i
						|| json[i].time != listWrong[i].time) {
					var blank = {
						time : json[i].time,
						count : 0
					};
					listWrong.splice(i, 0, blank);
				}
			}

			var lineTotal = [], lineSuccess = [], lineError = [], lineWrong = [];
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
			}
			, {

				values : lineSuccess,
				key : "Thành công",
				color : "#2ca02c"
			}, {

				values : lineError,
				key : "Thẻ lỗi",
				color : "#FF0000"
			}, {

				values : lineWrong,
				key : "Thẻ sai",
				color : "#DAA520"
			} ];
		}

	}
	//End LINE

	////////////////////////////////////// PIE ==================================================

	$(function() {
		var size_facet_all = 1000;
		var size_facet_limit = 10;
		var size_chart_w = 500;
		var size_chart_h = 500;

		load_data(filter_merchant, filter_provider, filter_card_type, 'status',
				size_chart_w, size_chart_h, '#status_chart', size_facet_all);

		load_data(filter_merchant, filter_provider, filter_card_type, 'type',
				size_chart_w, size_chart_h, '#type_chart', size_facet_all);
		load_data(filter_merchant, filter_provider, filter_card_type,
				'paymentProvider', size_chart_w, size_chart_h,
				'#provider_chart', size_facet_all);
		load_data(filter_merchant, filter_provider, filter_card_type,
				'merchant', size_chart_w, size_chart_h, '#merchant_chart',
				size_facet_limit);
	});

	var load_data = function(filter_merchant, filter_provider,
			filter_card_type, field, width, height, id, facet_size) {

		$.ajax({
			url : urlGet,
			type : 'POST',
			data : JSON
					.stringify(query('', field, 'facet', facet_size, merchant_permission,
							filter_merchant, filter_provider, filter_card_type,
							null)
							),
			dataType : 'json',
			processData : false,
			success : function(json, statusText, xhr) {
				//drawPie(json);
				return drawPie(json.facets.tags.terms, field, width,
						height, id);
			},
			error : function(xhr, message, error) {
				console.error(
						"Error while loading data from ElasticSearch",
						message);
				throw (error);
			}
		});
	};

	function drawPie(json, field, width, height, id) {
		//alert(json);
		if (field == 'status') {
			var statusArrays = [];
			var successCount = 0;
			var errorCount = 0;
			var wrongCount = 0;

			$.each(json, function(k, v) {

				$.each(statusSuccess, function(i, item) {

					if (item == v.term)
						successCount += v.count;
				});

				$.each(statusError, function(i, item) {
					if (item == v.term) {
						errorCount += v.count;
					}
				});

				$.each(statusWrong, function(i, item) {
					if (item == v.term)
						wrongCount += v.count;
				});

			});
			//alert(numeral(32.32).format('0%'));
			var totalCount = successCount + errorCount + wrongCount;
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
				/* var width = 400,
				    height = 400; */
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
				}).color(d3.scale.category10().range()) //d3.scale.category10().range()
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