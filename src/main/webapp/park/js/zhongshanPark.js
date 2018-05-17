//记录空气质量圆环的上一个位置,初始都是-230
var lastCo2Deg = -230;
var lastOxyDeg = -230;
// 记录上一次的数字,初始都为0
var lastDbNum = 0;
var lastCo2Num = 0;
var lastOxyNum = 0;
var lastPeopleNum = 0;

var lastDoorNum1 = 0;
var lastDoorNum2 = 0;
var lastDoorNum3 = 0;

var lastTemp = 0;
var lastHumi = 0;

 var numRunCo2;
 var numRunOxy;
 var numRunDb;
 var numRunPeople;
 var numRundoor1;
 var numRundoor2;
 var numRundoor3;
 
 // 入园人数，第一次判断
 var canJudge = true;


/**
 * 画面启动函数
 */
$(function(){
	//初始化高度
	initHeight();
	// 设置星星
	showStar();
	//画面自适应
	bodyReset();
	//时间动画
	initClockAni();
	//设置日期
	getDateTime();
	// 监听窗口变化
	$(window).resize(function() {
		//初始化高度
		initHeight();
	  //画面自适应
		bodyReset();
	});
	
	numRunCo2 = $(".numberRunCo2").numberAnimate({ num: 1, speed: 2000 });
	numRunOxy = $(".numberRunOxy").numberAnimate({ num: 1, speed: 2000 });
	numRunDb = $(".envirNum").numberAnimate({ num: 1, speed: 2000 });
	numRunPeople = $(".peopleNum").numberAnimate({ num: 1, speed: 2000 });
	numRundoor1 = $(".door1Num").numberAnimate({ num: 1, speed: 2000 });
	numRundoor2 = $(".door2Num").numberAnimate({ num: 1, speed: 2000 });
	numRundoor3 = $(".door3Num").numberAnimate({ num: 1, speed: 2000 });
	
	//webapp项目名
   	var strProjectName = "/intelligence_park";
	
   	//请求地址
   	var strUrl = "http://" + window.location.host + strProjectName + "/ext/park/collectionenv.do";
   	//请求地址（人流）
   	var strUrlPerson = "http://" + window.location.host + strProjectName + "/ext/park/collectionfootfall.do";
   	
	//第一次请求 
   	intervalDo(strUrl);
	//第一次请求 (人流)
   	intervalDoByPerson(strUrlPerson);
   	
	//设置定时器 环境数据5秒一次刷新
	var  intervalTimer = setInterval(function(){
		//运行数据
		intervalDo(strUrl);
	},5000);
	
	//设置定时器 人流数据15分钟一次刷新
	var  intervalTimer = setInterval(function(){
		//运行数据 (人流)
	   	intervalDoByPerson(strUrlPerson);
	}, 900000);
	
//	//运行虚拟数据
//	virtualData();
   
	
});

// 虚拟数据
function virtualData(){
	// 温度
	changeTmpCol(10.5);
	//湿度
	changeHumCol(20);
	lastTemp = 10.5;
	lastHumi = 20;
	
	//空气质量圆环
	//画 二氧化碳圆环,起始角度是-230
	airQualityCo2RingSet("#canvasCo2","#f15524",290);
	airQualityOxyRingSet("#canvasOxy","#fbc03b",343);
	
	lastCo2Num = 123;
	lastOxyNum = 100;
	changeNum(numRunCo2,0,294,1);
	changeNum(numRunOxy,0,343,2);
	lastPeopleNum = 34245;
   	changeNum(numRunPeople,20,12310,3);
   	
   	var timeout = setTimeout(function(){
   		var temp = 10.5;
   		var humi = 30;
   		//温度
		if(lastTemp != temp){
			changeTmpCol(temp);
		}
		//湿度
		if(lastHumi != humi){
			changeHumCol(humi);
		}
		lastTemp = temp;
		lastHumi = humi;
   		airQualityCo2RingSet("#canvasCo2","#f15524",400);
   		airQualityOxyRingSet("#canvasOxy","#fbc03b",20);
   		changeNum(numRunCo2,290,400,1);
   		changeNum(numRunOxy,343,20,2);
	 	initEnvirLine(53);
	 	changeNum(numRunDb,lastDbNum,34,7);
	 	changeNum(numRunPeople,20,20,3);
	 	changeNum(numRundoor1,546,78,4);
	 	loopPeosonBg(78,1);
		clearTimeout(timeout)
   	},4000)
//   	
	

   	//环境噪音的折线图
   	initEnvirLine(23);
   	//赋值
   	lastDbNum = 10;
//	changeNum(".envirNum",lastDbNum,23,7);
     	
   	lastDoorNum1 = 200;
   	lastDoorNum2 = 34;
   	lastDoorNum3 = 98;
   	
	changeNum(numRundoor1,lastDoorNum1,456,4);
	changeNum(numRundoor2,lastDoorNum2,90,5);
   	changeNum(numRundoor3,lastDoorNum3,7890,6);
//   	
// 	//根据入园量调整小人个数
   	loopPeosonBg(456,1);
   	loopPeosonBg(1190,2);
   	loopPeosonBg(2890,3);
	
}


/**
 * 二氧化碳圆环
 * 
 * @param ele
 * @param strokeSty
 * @param num
 */
function airQualityCo2RingSet(ele,strokeStyle,num){
	setTimeout(function(){
		airQualityCo2RingSetTimeOut(ele,strokeStyle,num);
	},1000)
	
}
function airQualityCo2RingSetTimeOut(ele,strokeStyle,num){
	// 等分850份
	num = num > 850? 845 : num;
	var nowDeg = 280/850*num-230;
	if(num <=350){
		strokeStyle = "#54c7a7";
	}else if(num >350 && num <=800 ){
		strokeStyle = "#fbbc3b";
	}else if(num >800 ){
		strokeStyle = "#f15a24";
	}
	
	if(lastCo2Deg >= nowDeg){
		//如果上一次的数值大
		var drawCo2Timer1 = setInterval(function(){
			lastCo2Deg -= 1; 
			drawText(ele,strokeStyle,-230,lastCo2Deg);
			if(lastCo2Deg <= nowDeg){
				clearInterval(drawCo2Timer1);
				lastCo2Deg = nowDeg;
			}
		},17)
	}else{
		//如果上一次的数值小
		var drawCo2Timer2 = setInterval(function(){
			lastCo2Deg += 1; 
			drawText(ele,strokeStyle,-230,lastCo2Deg);
			if(lastCo2Deg >= nowDeg){
				clearInterval(drawCo2Timer2);
				lastCo2Deg = nowDeg;
			}
		},17)
	}
	
}

/**
 * 负氧离子圆环
 * 
 * @param ele
 * @param strokeSty
 * @param num
 */
function airQualityOxyRingSet(ele,strokeSty,num){
	setTimeout(function(){
		airQualityOxyRingSetTimeOut(ele,strokeSty,num);
	},1000)
}
function airQualityOxyRingSetTimeOut(ele,strokeSty,num){
	// 等分650份
	num = num > 650? 645 : num;
	var nowDeg = 280/650*num-230;
	var strokeStyle = strokeSty;
	if(num <=50){
		strokeStyle = "#f15a24";
	}else if(num >50 && num <=200 ){
		strokeStyle = "#fbbc3b";
	}else if(num >200 ){
		strokeStyle = "#54c7a7";
	}
	
	if(lastOxyDeg >= nowDeg){
		//如果上一次的数值大
		var drawOxyTimer = setInterval(function(){
			lastOxyDeg = lastOxyDeg - 1; 
			drawText(ele,strokeStyle,-230,lastOxyDeg);
			if(lastOxyDeg <= nowDeg){
				clearInterval(drawOxyTimer);
				lastOxyDeg = nowDeg;
			}
		},17)
		
	}else{
		//如果上一次的数值小
		var drawOxyTimer = setInterval(function(){
			lastOxyDeg += 1; 
			drawText(ele,strokeStyle,-230,lastOxyDeg);
			if(lastOxyDeg >= nowDeg){
				clearInterval(drawOxyTimer);
				lastOxyDeg = nowDeg;
			}
		},17)
	}
	
}
	
/**
 * 定时请求数据
 * 
 * @param strUrl
 */
function intervalDo(strUrl){
	$.ajax({
		type:"post",
		url:strUrl,
		async:true,
		data:"",
		dataType:"json",
		success:function(data){
			//设定返回值
			var reValue = data;
			
			//温度
			if(reValue.temp != lastTemp ){
				changeTmpCol(reValue.temp);
			}
			//湿度
			if(reValue.humi != lastHumi){
				changeHumCol(reValue.humi);
			}
			lastTemp = reValue.temp;
			lastHumi = reValue.humi;
			
			//环境噪音的折线图
		   	initEnvirLine(reValue.nois);
		   	//环境噪音的赋值
		   	reValue.nois = Math.round(reValue.nois);
			changeNum(numRunDb, lastDbNum, Number(reValue.nois), 7);
			
			//画 二氧化碳圆环
			airQualityCo2RingSet("#canvasCo2","#f15524", Number(reValue.carb));
			//画 oxy圆环
			airQualityOxyRingSet("#canvasOxy","#fbc03b", Number(reValue.noxy));
			// co2，oxy赋值
			changeNum(numRunCo2, lastCo2Num, Number(reValue.carb), 1);
			changeNum(numRunOxy, lastOxyNum, Number(reValue.noxy), 2);
			
			// co2,oxy的数字
			reValue.carb = Math.round(reValue.carb);
			changeNum(numRunCo2, Number(lastCo2Num), Number(reValue.carb), 1);
			
			reValue.noxy = Math.round(reValue.noxy);
			changeNum(numRunOxy, Number(lastOxyNum), Number(reValue.noxy), 2);

			changeAir(Number(reValue.noxy));
		}
	});
}

/**
 * 定时请求数据
 * 
 * @param strUrl
 */
function intervalDoByPerson(strUrl){
	$.ajax({
		type:"post",
		url:strUrl,
		async:true,
		data:"",
		dataType:"json",
		success:function(data){
			//设定返回值
			var reValue = data;
			//在园人数
			var allinPeopleNum = Number(reValue.passengers) < 20 ? 20 : Number(reValue.passengers) ;
			
			//一号门
			var door1 = 0;
			//二号门
			var door2 = 0;
			//三号门
			var door3 = 0;
			
			//循环
			for(var i=0; i < reValue.data.length ;i++) {
				//取得数组对象
				var obj = reValue.data[i];
				
				//一号门
				if (obj.cameraUuid == "701e5626966c4b67bc1170de2fe7e86b" ||
						obj.cameraUuid == "6f31c46f87a5454199c972857c6f5e81"){
					door1 += obj.passengers_in;
				}
				
				//二号门
				if (obj.cameraUuid == "09bf11713c4c49079c679ecf942c62af" ||
						obj.cameraUuid == "44c98f39c4374b288a946b40d999d81c"){
					door2 += obj.passengers_in;
				}
				
				//三号门
				if (obj.cameraUuid == "1822c13497854a20a82dde46dc038e7a"){
					door3 += obj.passengers_in;
				}
				
			}

			// 园内人数
		   	changeNum(numRunPeople, Number(lastPeopleNum), Number(allinPeopleNum), 3);
		   	
		   	// 三个门对应的人数信息
			changeNum(numRundoor1,Number(lastDoorNum1),Number(door1),4);
			changeNum(numRundoor2,Number(lastDoorNum2),Number(door2),5);
		   	changeNum(numRundoor3,Number(lastDoorNum3),Number(door3),6);
		   	
		   	//根据入园量调整小人个数
		   	loopPeosonBg(Number(door1),1);
		   	loopPeosonBg(Number(door2),2);
		   	loopPeosonBg(Number(door3),3);	
			
		}
	});
}
			
			

var dateTimer =  setInterval(function(){
	getDateTime();
},1000)

//获取时间与日期
function getDateTime(){
	
	var today = new Date();
	var nowMonth = today.getMonth() + 1;
	var nowDay = today.getDate();
	var nowWeek = today.getDay();
	var nowHour = today.getHours();
	var nowMinute = today.getMinutes();
	// 转化农历
	var lunar = calendar.solar2lunar(today.getFullYear(),nowMonth,nowDay);
	
	// 补0
	if(nowMonth<10){
		nowMonth = "0" + nowMonth;
	}
	if(nowDay<10){
		nowDay = "0" + nowDay;
	}
	if(nowHour<10){
		nowHour = "0" + nowHour;
	}
	if(nowMinute<10){
		nowMinute = "0" + nowMinute;
	}
	var nowDate = today.getFullYear()+"."+ nowMonth + "."+ nowDay;
	$(".date").text(nowDate);
	$(".week").text(weekToChinese(nowWeek));
	$(".lunarYear").text(lunar.gzYear + "年");
	$(".lunarDate").text(lunar.IMonthCn + lunar.IDayCn);
//	console.log(today,nowDate,nowHour,nowMinute,nowWeek)
	
}

// 时间动画
function initClockAni(){
	var clock = new FlipClock($('.clock'), {
		clockFace : 'TwentyFourHourClock'   
	}); 

	$(".flip-clock-wrapper .flip-clock-divider:eq(1)").hide();
	var isShow =true;
	setInterval(function(){
		if(isShow){
			$(".flip-clock-wrapper .flip-clock-divider:eq(0)").animate({
				opacity:0
			});
		}else{
			$(".flip-clock-wrapper .flip-clock-divider:eq(0)").animate({
				opacity:1
			});
		}
		
		isShow = !isShow;
		
	},1000)
}

// 噪音折线
function initEnvirLine(envirDbNum){
	var envirNum = envirDbNum;
	var data1 = [];
	var data2 = [];
	var color1 = [];
	var color2 = [];
	
	   if(envirNum <= 50){
	   		data1 = [93,131,168,176,155,124,102,86,50];
	   		data2 = [102,126,102,84,87,115,124,113,68];
	   		color1 = ["#289fe7","#5cb89f"];
	   		color2 = ["#54c7fc","#54c7bf"];
	   }else if(envirNum > 50 && envirNum <= 55){
	   		data1 = [140,186,205,188,137,111,133,150,111];
	   		data2 = [129,170,153,114,105,139,148,121,69];
	   		color1 = ["#bd9fe7","#97b89f"];
	   		color2 = ["#54c7b4","#54c766"];
	   }else if(envirNum > 55 && envirNum <= 60){
	   		data1 = [208,244,250,179,97,83,114,124,87];
	   		data2 = [167,200,204,192,133,100,112,115,95];
	   		color1 = ["#eb93a6","#A49072"];
	   		color2 = ["#AEC740","#54C7BF"];
	   }else if(envirNum > 60 && envirNum <= 65){
	   		data1 = [245,277,270,213,138,125,166,171,131];
	   		data2 = [144,161,131,125,155,172,170,148,104];
	   		color1 = ["#E57866","#F17C9F"];
	   		color2 = ["#DEC740","#83C74D"];
	   }else if(envirNum > 65 && envirNum <= 70){
	   		data1 = [265,307,291,234,157,128,153,164,131];
	   		data2 = [125,132,111,129,168,180,165,127,66];
	   		color1 = ["#E54566","#F17C9F"];
	   		color2 = ["#E3C840","#FF874D"];
	   }else if(envirNum > 70 ){
	   		data1 = [306,338,329,272,138,92,120,123,86];
	   		data2 = [117,133,110,142,195,204,175,129,67];
	   		color1 = ["#E52AA1","#F17C43"];
	   		color2 = ["#D68302","#FF2F00"];
	   }
	var myChart = echarts.init(document.getElementById('envirLine'));
 
	var option = {
	    title : {
	    	show:false
	    },
	    legend: {
//		        data:['1','2']
	    },
	    grid:{
	    	show:false,
	    	top:0,
	    	left:0,
	    	bottom:0,
	    	right:0
	    },
	    xAxis : [
	        {
	        	show:false,
	            type : 'category',
	            boundaryGap : false,
	            data : ["1","2","3","4","5","6","7","8","9"]	
	        }
	    ],
	    yAxis : [
	        {
	        	show:false,
	            type : 'value',
	            min:0,
	            max:340
	        }
	    ],
	    series : [
	        {
	            type:'line',
	            smooth:true,
	            symbol:"none",
	            animationDuration:2000,
		        animationDurationUpdate:4000,
	            itemStyle: 
		            {
		            	normal: 
		            	{
		            		areaStyle:
		            		{
		            			type: 'default',
		            			color: new echarts.graphic.LinearGradient(0,0,1,0,
		            				[{
		            					offset:0.2,color:color1[0]
		            				},
		            				{
		            					offset:0.6,color:color1[1]
		            				}],false),
		            			opacity:.5
		            		},
		            		lineStyle:
		            		{
		            			width:0
		            		}
		            	}
	            	},
	            data:data1
	        },
	        {
	            type:'line',
	            smooth:true,
	            symbol:"none",
	            animationDuration:2000,
		        animationDurationUpdate:4000,
	           	itemStyle: 
		            {
		            	normal: 
		            	{
		            		areaStyle:
		            		{
		            			type: 'default', 
		            			color: new echarts.graphic.LinearGradient(0,0,1,0,
		            				[{
		            					offset:0.2,color:color2[0]
		            				},
		            				{
		            					offset:0.6,color:color2[1]
		            				}],false),
		            			opacity:.5
		            		},
		            		lineStyle:{
		            			width:0
		            		}
		            		
		            	}
		            	
	            	},
	            data:data2
	        }
	    ]
	};
   // 使用刚指定的配置项和数据显示图表。
   
     myChart.setOption(option);
	
}

// 根据人数变化入园量小人个数及颜色
function loopPeosonBg(peopleNum,type){
	
	var imgStr = "";
	if( peopleNum > 20000){
		num = 10;
	}else if( peopleNum > 10000 && peopleNum <= 20000){
		num = 9;
	}else if( peopleNum >=5000 && peopleNum < 10000){
		num = 8;
	}else if(peopleNum >=2000 && peopleNum < 5000){
		num = 7;
	}else if(peopleNum >=1000 && peopleNum < 2000){
		num = 6;
	}else if(peopleNum >=500 && peopleNum < 1000){
		num = 5;
	}else if(peopleNum >=200 && peopleNum < 500){
		num = 4;
	}else if(peopleNum >=100 && peopleNum < 200){
		num = 3;
	}else if(peopleNum >=50 && peopleNum < 100){
		num = 2;
	}else if(peopleNum < 50){
		num = 1;
	}
	if(type == 1){
		for(var i=0;i < num;i++){
			imgStr += '<img src="img/PeopleNum-07.png" alt="" />';
		}
		$(".personImg1").html(imgStr);
	}else if( type == 2){
		for(var i=0;i < num;i++){
			imgStr += '<img src="img/PeopleNum-08.png" alt="" />';
		}
		$(".personImg2").html(imgStr);
	}else if(type == 3){
		for(var i=0;i < num;i++){
			imgStr += '<img src="img/PeopleNum-06.png" alt="" />';
		}
		setTimeout(function(){
			$(".personImg3").html(imgStr);
		},3000)
		
	}
	
}


// 星期转化为中文
function weekToChinese(num){
	
	var week;
	switch (num){
		case 0:
		week = "日";
			break;
		case 1:
		week = "一";
			break;
		case 2:
		week = "二";
			break;
		case 3:
		week = "三";
			break;
		case 4:
		week = "四";
			break;
		case 5:
		week = "五";
			break;
		case 6:
		week = "六";
			break;
			default:
			week = "";
	}
	week = "星期" + week;
	return week;
	
}

// 变换温度
function changeTmpCol(num){
	// 圆环最大显示40℃
	var tmpNum;
	tmpNum = num > 40? 40:num;
	var wid = tmpNum/40*500 + "px";
	$(".tmpCol").animate({
			opacity:.2
		},500,function(){
			if(num<=0){
				$(".tmpCol").addClass("percentTem1");
			}else if(num>0 && num <= 5){
				$(".tmpCol").addClass("percentTem2");
			}else if(num>5 && num < 10){
				$(".tmpCol").addClass("percentTem3");
			}else if(num>= 10 && num <= 15){
				$(".tmpCol").addClass("percentTem4");
			}else if(num>15 && num <= 20){
				$(".tmpCol").addClass("percentTem5");
			}else if(num>20 && num <= 25){
				$(".tmpCol").addClass("percentTem6");
			}else if(num>25 && num <= 30){
				$(".tmpCol").addClass("percentTem7");
			}else if(num>30 && num <= 35){
				$(".tmpCol").addClass("percentTem8");
			}else if(num>35){
				$(".tmpCol").addClass("percentTem9");
			};
			
			if(num>15){
				$(".tmpCol").animate({
					width:wid,
					opacity:1
				},num*160)
			}else{
				$(".tmpCol").animate({
					opacity:1
				},num*160)
			}
			var small = "<small>℃</small>"
			$(".tmpCol").html(num + small)
			
	})
}
//变换湿度
function changeHumCol(num){
		// 湿度最大值为 100%
	var humNum;
	humNum = num > 100? 100:num;
	var wid = humNum/100*500 + "px";
	$(".humCol").animate({
			opacity:.2
		},500,function(){
			if(num>0 && num <= 10){
				$(".humCol").addClass("percentHum1");
			}else if(num>10 && num <= 20){
				$(".humCol").addClass("percentHum2");
			}else if(num>20 && num <= 30){
				$(".humCol").addClass("percentHum3");
			}else if(num>30 && num <= 40){
				$(".humCol").addClass("percentHum4");
			}else if(num>40 && num <= 50){
				$(".humCol").addClass("percentHum5");
			}else if(num>50 && num <= 60){
				$(".humCol").addClass("percentHum6");
			}else if(num>60 && num <= 70){
				$(".humCol").addClass("percentHum7");
			}else if(num>70 && num <= 80){
				$(".humCol").addClass("percentHum8");
			}else if(num>80 && num <= 90){
				$(".humCol").addClass("percentHum9");
			}else if(num>90 && num <= 100){
				$(".humCol").addClass("percentHum10");
			}
			if(num>35){
				var wid = num/100*500 + "px";
				$(".humCol").animate({
					width:wid,
					opacity:1
				},num*160)
			}else{
				$(".humCol").animate({
					opacity:1
				},num*160)
			}
			$(".humCol").text(num+"%")
		})
	
}

function changeAir(num){
	//初始化字样
	var txt = "";
	
	if(num <= 900){
		txt = "空气质量正常";
	}else if(num > 900 && num <= 1500){
		txt = "空气较清新，有助于改善身体健康";
	}else if(num > 1500 && num <= 2000){
		txt = "空气清新，有利健康，增强免疫力";
	}else if(num > 2000 && num <= 5000){
		txt = "空气非常清新，对健康相当有利";
	}else if(num > 2000 && num <= 5000){
		txt = "空气质量优，有效减少疾病传染";
	}

	$("#spanAir").text(txt);
}
	
// 空气质量进度条，html中已删去
function changeAirCol(num){
	
	var wid;
	var txt;
	$(".AirCol").animate({
		opacity:.2
	},500,function(){
		if(num <= 50){
			wid = num/50*500/3 + "px";
			txt = "优";
			$(".AirCol").addClass("percentAir1");
			
		}else if(num > 50 && num <= 100){
			wid = num/50*500/3 + "px";
			txt = "良";
			$(".AirCol").addClass("percentAir2");
			
		}else if(num > 100 && num <=200){
			wid = ((num-100)/100*125 + 300) + "px";
			txt = "轻度污染";
			$(".AirCol").addClass("percentAir3");
		}else if(num > 200){
			wid = ((num-200)/300*125 + 400) + "px";
			txt = "重度污染";
			$(".AirCol").addClass("percentAir4");	
		}
		if(num>27){
			$(".AirCol").animate({
				width:wid,
				opacity:1
			},2000)
		}else{
			$(".AirCol").animate({
				opacity:1
			},2000)
		}
		$(".AirCol").text(num);
		$(".textAir").text(txt)
		
	})
	
}


// 数字跳动效果
 function changeNum(ele,lastNumber,newNumber,type){
 	if(type==3){
 		if(canJudge){
 			var newNum = newNumber;
			var len = newNum.toString().split("").length;
			if(len==1){
				$(".peopleZero").text("0000")
			}else if(len==2){
				$(".peopleZero").text("000")
			}else if(len==3){
				$(".peopleZero").text("00")
			}else if(len==4){
				$(".peopleZero").text("0")
			}else if(len==5){
				$(".peopleZero").text("")
			}
			canJudge = false;
 		}
 		
 	}
 	var numRun = ele;
 	//lastNumber == 0时，会弹框，处理 == 0 的情况
// 	lastNumber = lastNumber == "0"? 1 :lastNumber;
	var timer = setInterval(function() {
		if(type == 1){
			var lastNum = lastNumber;
			var newNum = newNumber;
			var differ = newNum-lastNum;
			lastNum += differ;
			lastCo2Num = lastNum;
			numRun.resetData(lastNum);
			clearInterval(timer);
			if(newNum <=350){
				$("#airNumBox1").find(".mt-number-animate").css({
					"color":"#54c7a7",
					'-ms-transition':0,
	                '-moz-transition':0,
	                '-webkit-transition':0,
	                '-o-transition':0,
	                'transition':0
				})
			}else if(newNum >350 && newNum <=800 ){
				$("#airNumBox1").find(".mt-number-animate").css({
					"color":"#fbbc3b",
					'-ms-transition':0,
	                '-moz-transition':0,
	                '-webkit-transition':0,
	                '-o-transition':0,
	                'transition':0
				})
			}else if(newNum >800 ){
				$("#airNumBox1").find(".mt-number-animate").css({
					"color":"#f15a24",
					'-ms-transition':0,
	                '-moz-transition':0,
	                '-webkit-transition':0,
	                '-o-transition':0,
	                'transition':0
				})
			}
			
		}else if(type == 2){
			var lastNum = lastNumber;
			var newNum = newNumber;
			var differ = newNum-lastNum;
			lastNum += differ;
			lastOxyNum = lastNum;
			numRun.resetData(lastNum);
			clearInterval(timer);
			if(newNum <= 50){
				$("#airNumBox2").find(".mt-number-animate").css({
					"color":"#f15a24",
					'-ms-transition':0,
	                '-moz-transition':0,
	                '-webkit-transition':0,
	                '-o-transition':0,
	                'transition':0
				})
			}else if(newNum > 50 && newNum <= 200){
				$("#airNumBox2").find(".mt-number-animate").css({
					"color":"#FBBC3B",
					'-ms-transition':0,
	                '-moz-transition':0,
	                '-webkit-transition':0,
	                '-o-transition':0,
	                'transition':0
				})
			}else if(newNum > 200){
				$("#airNumBox2").find(".mt-number-animate").css({
					"color":"#54C7A7",
					'-ms-transition':0,
	                '-moz-transition':0,
	                '-webkit-transition':0,
	                '-o-transition':0,
	                'transition':0
				})
			}
		}else if(type == 3){
			var lastNum = lastNumber;
			var newNum = newNumber;
			var differ = newNum-lastNum;
			var len = newNum.toString().split("").length;
			if(len==1){
				$(".peopleZero").text("0000")
			}else if(len==2){
				$(".peopleZero").text("000")
			}else if(len==3){
				$(".peopleZero").text("00")
			}else if(len==4){
				$(".peopleZero").text("0")
			}else if(len==5){
				$(".peopleZero").text("")
			}
			lastNum += differ;
			lastPeopleNum = lastNum;
			numRun.resetData(lastNum);
			clearInterval(timer);
			
		
		}else if(type == 4){
			var lastNum = lastNumber;
			var newNum = newNumber;
			var differ = newNum-lastNum;
			lastNum += differ;
			lastDoorNum1 = lastNum;
			numRun.resetData(lastNum);
			clearInterval(timer);
		
		}else if(type == 5){
			var lastNum = lastNumber;
			var newNum = newNumber;
			var differ = newNum-lastNum;
			lastNum += differ;
			lastDoorNum2 = lastNum;
			numRun.resetData(lastNum);
			clearInterval(timer);
		
		}else if(type == 6){
			var lastNum = lastNumber;
			var newNum = newNumber;
			var differ = newNum-lastNum;
			lastNum += differ;
			lastDoorNum3 = lastNum;
			numRun.resetData(lastNum);
			clearInterval(timer);
		
		}else if(type == 7){
			var lastNum = lastNumber;
			var newNum = newNumber;
			var differ = newNum-lastNum;
			lastNum += differ;
			lastDbNum = lastNum;
			numRun.resetData(lastNum);
			clearInterval(timer);
		
		}
		
		
	}, 1000)
};
	
//画星星
function showStar(){
	
	$(".bgColor>span").remove();
	var width  = window.screen.availWidth;
	var height  = window.screen.availHeight/3*2;
	var starNum = 15;
	for(var i=0;i<starNum;i++){
		var size = randNum(6,3);
		var span = document.createElement("span");
		span.style.position = "absolute";
		span.style.left = randNum(width,20)+"px";
		span.style.top = randNum(height,20)+"px";
		span.style.width = size +"px";
		span.style.height = size +"px";
		span.className = "star";
		$(".bgColor").append(span);
	}
	
}

// 随机数
function  randNum(max,min){
	
	return  Math.ceil(Math.random() * (max-min)+min);
	
}

// 按照 1080*1920初始化高度
function initHeight(){
	
	 var height = 1349/1080*1920 + "px";
	  console.log(height);
	 $("#bgSize").height(height);
	 
}

// 画灰色背景圆环，暂不用
function drawBgRing(ele,strokeStyle,degStart,degEnd){
	
	var ctx = $(ele)[0].getContext("2d");
		//起始一条路径
	ctx.beginPath();
	//设置当前线条的宽度
	ctx.lineWidth = 20;
	//设置笔触的颜色
	ctx.strokeStyle = strokeStyle;
	//arc() 方法创建弧/曲线（用于创建圆或部分圆） 
	ctx.arc(110, 110, 100, degStart * Math.PI / 180, degEnd * Math.PI / 180);
	ctx.lineCap="round";
	//绘制已定义的路径
	ctx.stroke();
	ctx.closePath();
	
}
//画前景圆环以及文字
function drawText(ele,strokeStyle,degStart,degEnd){
	
	var ctx = $(ele)[0].getContext("2d");
	ctx.beginPath();
	//清除画布
	ctx.clearRect(0,0,220,200)
	ctx.lineWidth = 20;
	ctx.strokeStyle = strokeStyle;
	//设置开始处为0点钟方向(-90 * Math.PI / 180)
	//x为百分比值(0-100)
	ctx.arc(110, 110, 100, degStart * Math.PI / 180, degEnd* Math.PI / 180);  
	ctx.lineCap = "round";
	ctx.stroke();     
 	 //在中间写字 
//  ctx.font = "bold 60px Calibri"; 
//  ctx.fillStyle = strokeStyle; 
//  ctx.textAlign = 'center'; 
//  ctx.textBaseline = 'middle'; 
//  ctx.fillText(text, 110, 110); 
    
}

//画面宽度自适应
function bodyReset(){
	
	var width  = document.body.clientWidth||document.documentElement.clientWidth;
	var scaleX = width/1349;
	$("#bgSize").addClass("transformOrigin");
	$("#bgSize").css({
		"transform":"scale("+scaleX+","+scaleX+")",
		"-webkit-transform":"scale("+scaleX+","+scaleX+")",
		"-moz-transform":"scale("+scaleX+","+scaleX+")",
		"-o-transform":"scale("+scaleX+","+scaleX+")",
	})
	
}

 $(function() {
    var lunarInfo=new Array(0x04bd8,0x04ae0,0x0a570,0x054d5,0x0d260,0x0d950,0x16554,0x056a0,0x09ad0,0x055d2,0x04ae0,0x0a5b6,0x0a4d0,0x0d250,0x1d255,0x0b540,0x0d6a0,0x0ada2,0x095b0,0x14977,0x04970,0x0a4b0,0x0b4b5,0x06a50,0x06d40,0x1ab54,0x02b60,0x09570,0x052f2,0x04970,0x06566,0x0d4a0,0x0ea50,0x06e95,0x05ad0,0x02b60,0x186e3,0x092e0,0x1c8d7,0x0c950,0x0d4a0,0x1d8a6,0x0b550,0x056a0,0x1a5b4,0x025d0,0x092d0,0x0d2b2,0x0a950,0x0b557,0x06ca0,0x0b550,0x15355,0x04da0,0x0a5d0,0x14573,0x052d0,0x0a9a8,0x0e950,0x06aa0,0x0aea6,0x0ab50,0x04b60,0x0aae4,0x0a570,0x05260,0x0f263,0x0d950,0x05b57,0x056a0,0x096d0,0x04dd5,0x04ad0,0x0a4d0,0x0d4d4,0x0d250,0x0d558,0x0b540,0x0b5a0,0x195a6,0x095b0,0x049b0,0x0a974,0x0a4b0,0x0b27a,0x06a50,0x06d40,0x0af46,0x0ab60,0x09570,0x04af5,0x04970,0x064b0,0x074a3,0x0ea50,0x06b58,0x055c0,0x0ab60,0x096d5,0x092e0,0x0c960,0x0d954,0x0d4a0,0x0da50,0x07552,0x056a0,0x0abb7,0x025d0,0x092d0,0x0cab5,0x0a950,0x0b4a0,0x0baa4,0x0ad50,0x055d9,0x04ba0,0x0a5b0,0x15176,0x052b0,0x0a930,0x07954,0x06aa0,0x0ad50,0x05b52,0x04b60,0x0a6e6,0x0a4e0,0x0d260,0x0ea65,0x0d530,0x05aa0,0x076a3,0x096d0,0x04bd7,0x04ad0,0x0a4d0,0x1d0b6,0x0d250,0x0d520,0x0dd45,0x0b5a0,0x056d0,0x055b2,0x049b0,0x0a577,0x0a4b0,0x0aa50,0x1b255,0x06d20,0x0ada0);
    var str="日一二三四五六七八九十";
    var now=new Date(),SY=now.getFullYear(),SM=now.getMonth(),SD=now.getDate();
    var SW=now.getDay();
    var lDObj=new Lunar(now);
    var LM=lDObj.month;
    var LD=lDObj.day;
    function cyclical(num){
        var Gan="甲乙丙丁戊己庚辛壬癸";
        var Zhi="子丑寅卯辰巳午未申酉戌亥";
        return(Gan.charAt(num%10)+Zhi.charAt(num%12));
    }
    function lYearDays(y){
       var i,sum=348;
       for(i=0x8000;i>0x8;i>>=1)sum+=(lunarInfo[y-1900]&i)?1:0;
       return sum+leapDays(y);
    }
    function leapDays(y){if(leapMonth(y))return (lunarInfo[y-1900]&0x10000)?30:29;else return(0);}
    function leapMonth(y){return lunarInfo[y-1900]&0xf;}
    function monthDays(y,m){return (lunarInfo[y-1900]&(0x10000>>m))?30:29;}
    function Lunar(objDate){
        var i,leap=0,temp=0;
        var baseDate=new Date(1900,0,31);
        var offset=(objDate-baseDate)/86400000;
        this.dayCyl=offset+40;
        this.monCyl=14;
        for(i=1900;i<2050&&offset>0;i++){
            temp=lYearDays(i);
            offset-=temp;
            this.monCyl+=12;
        }
        if(offset<0){
            offset+=temp;
            i--;
            this.monCyl-=12;
        }
        this.year=i;
        this.yearCyl=i-1864;
        leap=leapMonth(i);
        this.isLeap=false
        for(i=1;i<13&&offset>0;i++){
            if(leap>0&&i==(leap+1)&&this.isLeap==false){
                --i;
                this.isLeap=true;
                temp=leapDays(this.year);
            }else{
                temp=monthDays(this.year,i);
            }
            if(this.isLeap==true&&i==(leap+1))this.isLeap=false;
            offset-=temp;
            if(this.isLeap==false)this.monCyl++;
        }
        if(offset==0&&leap>0&&i==leap+1)if(this.isLeap){
            this.isLeap=false;
        }else{
            --i;
            this.isLeap=true;
            --this.monCyl;
        }
        if(offset<0){offset+=temp;--i;--this.monCyl;}
        this.month=i;
        this.day=offset+1;
    }
    function YYMMDD(){ 
        var cl = '<font color="#ffffff">'; 
        if (SW == 0) cl = '<font color="#ffffff">'; 
        if (SW == 6) cl = '<font color="#ffffff">';
        return(cl+SY+'年'+(SM+1)+'月'+SD+'日</font>'); 
    }
    function weekday(){ 
        var cl='<font color="#ffffff">'; 
        if (SW==0||SW==6)cl='<font color="#ffffff">';
        return cl+"星期"+str.charAt(SW)+'</font>';
    }
    function cDay(m,d){
        var nStr="初十廿卅　",s;
        if(m>10)s='十'+str.charAt(m-10);else s=str.charAt(m);
        s+='月';
        switch(d){
            case 10:s+='初十';break;
            case 20:s+='二十';break;
            case 30:s+='三十';break;
            default:s+=nStr.charAt(Math.floor(d/10));s+=str.charAt(d%10);
        }
        if(lDObj.isLeap)s="闰"+s;
        return(s);
    }
    function lunarTime(){
        return('<font color="#ffffff">'+cyclical(SY-4)+'年 '+cDay(LM,LD)+'</font>');
    }
    function specialDate(){
        var sTermInfo=new Array(0,21208,42467,63836,85337,107014,128867,150921,173149,195551,218072,240693,263343,285989,308563,331033,353350,375494,397447,419210,440795,462224,483532,504758);
        var solarTerm=new Array("小寒","大寒","立春","雨水","惊蛰","春分","清明","谷雨","立夏","小满","芒种","夏至","小暑","大暑","立秋","处暑","白露","秋分","寒露","霜降","立冬","小雪","大雪","冬至");
        var lFtv="0101春节0115元宵节0505端午节0707七夕情人节0715中元节0815中秋节0909重阳节1208腊八节1224小年0100除夕";
        var sFtv="0101元旦0214情人节0308妇女节0312植树节0315消费者权益日0401愚人节0501劳动节0504青年节0512护士节0601儿童节0701建党节香港回归纪念0801建军节0808父亲节0909毛主席逝世纪念0910教师节0928孔子诞辰1001国庆节1006老人节1024联合国日1112孙中山诞辰1220澳门回归纪念1225圣诞节1226毛主席诞辰";
        var tmp1,tmp2,festival='';
        tmp1=new Date((31556925974.7*(SY-1900)+sTermInfo[SM*2+1]*60000)+Date.UTC(1900,0,6,2,5));
        tmp2=tmp1.getUTCDate();
        if(tmp2==SD)festival+=' <font color="#ffffff">今日节气：'+solarTerm[SM*2+1]+'</font>';
        tmp1=new Date((31556925974.7*(SY-1900)+sTermInfo[SM*2]*60000)+Date.UTC(1900,0,6,2,5));
        tmp2=tmp1.getUTCDate();
        if(tmp2==SD)festival+=' <font color="#ffffff">今日节气：'+solarTerm[SM*2]+'</font>';
//        var reg=new RegExp((LM<10&&"0"||"")+LM+(LD<10&&"0"||"")+LD+'([^\\d]+)','');
//        if(lFtv.match(reg)!=null)festival+=' <font color="#ff0000">'+RegExp.$1+'</font>';
//        reg=new RegExp((SM<9&&"0"||"")+(SM+1)+(SD<10&&"0"||"")+SD+'([^\\d]+)','');
//        if(sFtv.match(reg)!=null)festival+=' <font color="#ff0000">'+RegExp.$1+'</font>';
        return(festival);
    }
	 YYMMDD();
	 weekday();
	 lunarTime();
 
	$(".specialDate").html(specialDate());
//    return YYMMDD()+' '+weekday()+' '+lunarTime()+specialDate();
})