$(function(){
	$(".er_box").mouseover(function(){
		$(".er_ewm").each(function(){
			$(this).hide();
		})
		$(this).children(".er_ewm").show();
	}).mouseleave(function(){
		$(this).children(".er_ewm").hide();
	})
	
})
