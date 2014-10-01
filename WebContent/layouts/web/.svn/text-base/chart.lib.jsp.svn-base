<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script src="<%=request.getContextPath() %>/js/chart/src/utils.js"></script>
<script src="<%=request.getContextPath() %>/js/chart/d3.v3.js"></script>
<script src="<%=request.getContextPath() %>/js/chart/nv.d3.js"></script>
<script src="<%=request.getContextPath() %>/js/chart/src/tooltip.js"></script>
<script src="<%=request.getContextPath() %>/js/chart/src/interactiveLayer.js"></script>

<script src="<%=request.getContextPath() %>/js/chart/src/models/legend.js"></script>
<script src="<%=request.getContextPath() %>/js/chart/src/models/axis.js"></script>
<script src="<%=request.getContextPath() %>/js/chart/src/models/scatter.js"></script>
<script src="<%=request.getContextPath() %>/js/chart/src/models/line.js"></script>
<script src="<%=request.getContextPath() %>/js/chart/src/models/lineChart.js"></script>

<script src="<%=request.getContextPath() %>/js/chart/src/models/pie.js"></script>
<script src="<%=request.getContextPath() %>/js/chart/src/models/pieChart.js"></script>


<link href="<%=request.getContextPath() %>/css/chart/nv.d3.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
jQuery(function($){
    $.datepicker.regional['month_of_numbers'] = {
    		closeText: "Chọn", // Display text for close link
    		prevText: "Lùi", // Display text for previous month link
    		nextText: "Tiếp", // Display text for next month link
    		currentText: "Hôm nay", // Display text for current month link
    		monthNames: ["Tháng 1","Tháng 2","Tháng 3","Tháng 4","Tháng 5","Tháng 6",
    			"Tháng 7","Tháng 8","Tháng 9","Tháng 10","Tháng 11","Tháng 12"], // Names of months for drop-down and formatting
    		monthNamesShort: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"], // For formatting
    		dayNames: ["Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"], // For formatting
    		dayNamesShort: ["CN", "T2", "T3", "T4", "T5", "T6", "T7"], // For formatting
    		dayNamesMin: ["CN","2","3","4","5","6","7"], // Column headings for days starting at Sunday
    		weekHeader: "Wk", // Column header for week of the year
    		dateFormat: "mm/dd/yy", // See format options on parseDate
    		firstDay: 0, // The first day of the week, Sun = 0, Mon = 1, ...
    		isRTL: false, // True if right-to-left language, false if left-to-right
    		showMonthAfterYear: false, // True if the year select precedes month, false for month then year
    		yearSuffix: "" // Additional text to append to the year in the month headers
    		};
    $.datepicker.setDefaults($.datepicker.regional['month_of_numbers']);
});

</script>