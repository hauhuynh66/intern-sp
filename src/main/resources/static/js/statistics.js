function number_format(number, decimals, dec_point, thousands_sep) {
  // *     example: number_format(1234.56, 2, ',', ' ');
  // *     return: '1 234,56'
  number = (number + '').replace(',', '').replace(' ', '');
  var n = !isFinite(+number) ? 0 : +number,
    prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
    sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
    dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
    s = '',
    toFixedFix = function(n, prec) {
      var k = Math.pow(10, prec);
      return '' + Math.round(n * k) / k;
    };
  // Fix for IE parseFloat(0.55).toFixed(0) = 0;
  s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
  if (s[0].length > 3) {
    s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
  }
  if ((s[1] || '').length < prec) {
    s[1] = s[1] || '';
    s[1] += new Array(prec - s[1].length + 1).join('0');
  }
  return s.join(dec);
}
//BarChart
var bar = document.getElementById("myBarChart");
var myBarChart = new Chart(bar, {
  type: 'bar',
  data: {
    labels: ["January", "February", "March", "April", "May", "June","July"],
    datasets: [{
      label: "Value",
      backgroundColor: "#4e73df",
      hoverBackgroundColor: "#2e59d9",
      borderColor: "#4e73df",
      data: [4215, 5312, 6251, 7841, 9821, 14984],
    }],
  },
  options: {
    maintainAspectRatio: false,
    layout: {
      padding: {
        left: 10,
        right: 25,
        top: 25,
        bottom: 0
      }
    },
    scales: {
      xAxes: [{
        time: {
          unit: 'month'
        },
        gridLines: {
          display: false,
          drawBorder: false
        },
        ticks: {
          maxTicksLimit: 15
        },
        maxBarThickness: 25,
      }],
      yAxes: [{
        ticks: {
          min: 0,
          max: 200,
          maxTicksLimit: 4,
          padding: 10,
          // Include a dollar sign in the ticks
          callback: function(value, index, values) {
            return number_format(value);
          }
        },
        gridLines: {
          color: "rgb(234, 236, 244)",
          zeroLineColor: "rgb(234, 236, 244)",
          drawBorder: false,
          borderDash: [2],
          zeroLineBorderDash: [2]
        }
      }],
    },
    legend: {
      display: false
    },
    tooltips: {
      titleMarginBottom: 10,
      titleFontColor: '#6e707e',
      titleFontSize: 14,
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      caretPadding: 10,
      callbacks: {
        label: function(tooltipItem, chart) {
          var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
          return number_format(tooltipItem.yLabel);
        }
      }
    },
  }
});
// Pie Chart Example
//var pie = document.getElementById("myPieChart").getContext('2d');
//var myPieChart = new Chart(pie, {
//  type: 'pie',
//  data: {
//    labels: ["Direct", "Referral", "Social"],
//    datasets: [{
//      data: [55, 30, 15],
//      backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc'],
//      hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],
//      hoverBorderColor: "rgba(234, 236, 244, 1)",
//    }],
//  },
//  options: {
//    maintainAspectRatio: false,
//    tooltips: {
//      enabled : true,
//      mode: 'nearest'
//    },
//    legend: {
//      display: true,
//      position : 'right',
//      labels: {
//          fontColor: 'rgb(255, 99, 132)'
//      }
//    },
//    plugins: {
//        datalabels: {
//            color: '#111',
//            textAlign: 'center',
//            font: {
//                lineHeight: 1.6
//            }
//        }
//    }
//  },
//});

$(document).ready(function() {
	$('input[type=text]').val('');
	var dataReport = ["Event", "Candidate"];
	$("#startDate").datepicker({autoclose:true});
	$("#endDate").datepicker({autoclose:true});
	for(i=0; i < dataReport.length; i++){
	    $("#dataReport").append("<option>" + dataReport[i] + "</option>");
	}
	$.ajax({
		type: "GET",
		url : "/manage/statistics/events/getData",
		 dataType: "json",
		success: function(data){
			if(data == null) {
				dataEventNull();
				return;
			};
			var txtSite = $("#siteStatistic").text();
			var txtSkill = $("#skillStatistic").text();
			var txtProgram = $("#programStatistic").text();
			var sumSite = calSum(Object.values(data[2]));
			var sumSkill = calSum(Object.values(data[1]));
			var sumProgram = calSum(Object.values(data[3]));
			$("#siteStatistic").text(txtSite + "| Sum:" + sumSite);
			$("#skillStatistic").text(txtSkill + "| Sum:"+ sumSkill);
			$("#programStatistic").text(txtProgram+"| Sum:"+ sumProgram);

			createChart(data);
		},
		error: function(data,status,er){
			console.log(status);
			console.log(er);
			console.log(data);
		}
	});
	$.ajax({
		type: "GET",
		url : "/manage/statistics/candidates/getData",
		 dataType: "json",
		success: function(data){
			if(data == null) {
				dataCandidateNull();
				removeData(myBarChart);
				return;
			}
			createBarChart(data);
		},
		error: function(data,status,er){
			console.log(status);
			console.log(er);
			console.log(data);
		}
	})
});

function clearIdData(id){
	$("#" + id).empty();
}

function filterSelected(){
	clearIdData("modal-body-container");
	$("#modal-body-container").append("<div id=\"titleFilter\" style=\"font-size: 200%; color: black\; \"></div>");
	$("#modal-body-container").append("<div id=\"tableFilter\" class=\"table-responsive\"></div>");
	var dataInput = $("#dataReport").val();
	var subDataInput = $("#subDataReport").val();
	if(dataInput != "Event" && dataInput !="Candidate"){
		dataNull();
		return;
	}
	$.ajax({
		type: "GET",
		url : "/manage/statistics/filterData",
		 dataType: "json",
		success: function(data){
			if(data == null) {
				dataAjaxNull();
				return;
			}
			//console.log("Filter Data:",data);
			let events = data[0];
			let candidates = data[1];
			//Create label(TR),data(TH)
			let uniEventLabel = Object.keys(events[0]);
			let uniEventData = Object.values(events[0]);

			let skillEventLabel = Object.keys(events[1]);
			let skillEventData = Object.values(events[1]);

			let siteLabel = Object.keys(events[2]);
			let siteData = Object.values(events[2]);

			let programLabel = Object.keys(events[3]);
			let programData = Object.values(events[3]);

			let uniCandidateLabel = Object.keys(candidates[0]);
			let uniCandidateData = Object.values(candidates[0]);

			let skillCandidateLabel = Object.keys(candidates[1]);
			let skillCandidateData = Object.values(candidates[1]);

			let genderLabel = Object.keys(candidates[2]);
			let genderData = Object.values(candidates[2]);

			let item = subDataInput;
				let label = [];
				let dataFilter = [];
				if(dataInput == "Event") {
					switch(item){
					case "University":
						label = uniEventLabel;
						dataFilter = uniEventData;
						break;
					case "Skill":
						label = skillEventLabel;
						dataFilter = skillEventData;
						break;
					case "Site":
						label = siteLabel;
						dataFilter = siteData;
						break;
					case "Program":
						label = programLabel;
						dataFilter = programData;
						break;
					}
				}
				if(dataInput == "Candidate") {
					switch(item){
					case "University":
						label = uniCandidateLabel;
						dataFilter = uniCandidateData;
						break;
					case "Skill":
						label = skillCandidateLabel;
						dataFilter = skillCandidateData;
						break;
					case "Gender":
						label = genderLabel;
						dataFilter = genderData;
						break;
					}
				}
				let sum = calSum(dataFilter);
				$("#titleFilter").append("<span class=\"m-0 font-italic text-primary pt-2\" > Total " + item +" Filter By "+dataInput+": "+sum + "</span> <br>");
				$("#titleFilter").css("text-align","center");
				$("#tableFilter").append("<table id=\"tableContainer\" class=\"table table-hover table-bordered table-striped \">");
				$("#tableContainer").append("<thead class=\"thead-light\" id=\"tableThead\"></thead>");
				$("#tableContainer").append("<tbody id=\"tableTbody\"></tbody>");
				$("#tableThead").append("<tr class=\" bg-primary \" id=\"theadTr\"></tr>");
				for(let i = 0; i < label.length;i++){
					$("#theadTr").append("<th scope=\"col\">"+ label[i]+"</th>");
				};
				$("#tableTbody").append("\"<tr id=\"tbodyTr\">");
				for(let i =0; i < dataFilter.length;i++){
					$("#tbodyTr").append("<td class=\"bg-success text-light \" >"+dataFilter[i]+"</td>");
				};

		},
		error: function(data,status,er){
			console.log(status);
			console.log(er);
			console.log(data);
		}
	})
}

function calSum(data){
	const reducer = (accumulator, currentValue) => accumulator + currentValue;
	return data.reduce(reducer);
}

function clearFilter(){
	clearIdData("tableFilter");
}

function subData(){
	var eventDataReport = ["University","Site","Skill","Program"];
	var candidateDataReport = ["University","Skill","Gender"];
	$("#subDataReport").empty();
	//$("#subDataReport").append("<option>" + "Select Data To Filter" + "</option>");
	var x = document.getElementById("dataReport").value;
	if(x == "Event") {
		for(i=0; i < eventDataReport.length; i++){
		    $("#subDataReport").append("<option>" + eventDataReport[i] + "</option>");
		}
	}else if(x == "Candidate"){
		for(i=0; i < candidateDataReport.length; i++){
		    $("#subDataReport").append("<option>" + candidateDataReport[i] + "</option>");
		}
	}
}

function createBarChart(data){
	var skills = data[1];
	let label = Object.keys(skills);
	let candidateData = Object.values(skills);
	removeData(myBarChart);
	addData(myBarChart,label,candidateData);

}

function updateChartByNewData(data){

	let skills = data[1];
	let sites = data[2];
	let programs = data[3];
	//Create Label for chart
	let skillLabel = Object.keys(skills);
	let siteLabel = Object.keys(sites);
	let programLabel = Object.keys(programs);

	//Create Data for chart
	let siteDataset = Object.values(sites);
	let skillDataset = Object.values(skills);
	let programDataset = Object.values(programs);

	//Clear Old Chart
	document.getElementById("skillChart-container").innerHTML = '&nbsp;';
	document.getElementById("skillChart-container").innerHTML = '<canvas id="skillChart" style="width: 100%; height: 400px;"></canvas>';
	var ctx = document.getElementById("skillChart").getContext("2d");
	document.getElementById("siteChart-container").innerHTML = '&nbsp;';
	document.getElementById("siteChart-container").innerHTML = '<canvas id="siteChart" style="width: 100%; height: 400px;"></canvas>';
	var ctx = document.getElementById("siteChart").getContext("2d");
	document.getElementById("programChart-container").innerHTML = '&nbsp;';
	document.getElementById("programChart-container").innerHTML = '<canvas id="programChart" style="width: 100%; height: 400px;"></canvas>';
	var ctx = document.getElementById("programChart").getContext("2d");

	//Add new Chart
	siteChart(siteLabel,siteDataset);
	skillChart(skillLabel,skillDataset);
	programChart(programLabel,programDataset);

}

function createChart(data){
	let skills = data[1];
	let sites = data[2];
	let programs = data[3];
	//Create Label for chart
	let skillLabel = Object.keys(skills);
	let siteLabel = Object.keys(sites);
	let programLabel = Object.keys(programs);

	//Create Data for chart
	let siteDataset = Object.values(sites);
	let skillDataset = Object.values(skills);
	let programDataset = Object.values(programs);

	//Create Chart
	siteChart(siteLabel,siteDataset);
	skillChart(skillLabel,skillDataset);
	programChart(programLabel,programDataset);
}

function updateChart(){
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	if(startDate == "" || endDate == "") {
		dateNull();
		return;
	}
	startDate = formatDate(startDate);
	endDate = formatDate(endDate);
	var sd = new Date(startDate);
	var ed = new Date(endDate);
	if(startDate > endDate) {
		dateInvalid();
		return;
	}
	$.ajax({
		type: "GET",
		data: {
			startDate : startDate,
			endDate : endDate
		},
		url : "/manage/statistics/getEventBetweenDate",
		success: function(data){
			updateChartByNewData(data);
		},
		error : function(e){
			console.log(e);
		}
	})
}

function formatDate(strDate){
	var m = strDate.split('/')[0];
	var d = strDate.split('/')[1];
	var y = strDate.split('/')[2];
	return d+'/'+m+'/'+y;
}

function skillChart(skillLabel,skillDataset){
			var dataPieList = skillDataset;
			var skillChart = document.getElementById("skillChart");
			var mySkillChart = new Chart(skillChart, {
				type: 'pie',
				data: {
					labels :skillLabel,
					datasets: [{
					      data: dataPieList	,
					      backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc','#FF0000','#f6e58d','#e056fd','#7bed9f','#ff6b81','#ced6e0']
					    }],
					options : {
				        legend: {
				            display: true,
				            position: 'bottom',
				            labels: {
				                boxWidth: 20,
				                fontColor: '#111',
				                padding: 15
				            }
				        },
				        tooltips: {
				        	callbacks: {
				                label: function(tooltipItem, chart) {
				                  var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
				                  return number_format(tooltipItem.yLabel);
				                }
				              }
				        },
				        plugins: {
				            datalabels: {
				                color: '#111',
				                textAlign: 'center',
				                font: {
				                    lineHeight: 1.6
				                },
				                formatter: function(value, ctx) {
				                    return '%';
				                }
				            }
				        }
					}
		},
	})
}

function siteChart(siteLabel,siteDataset){
	var dataPieList = siteDataset;
	var siteChart = document.getElementById("siteChart");
	var mySiteChart = new Chart(siteChart, {
		type: 'pie',
		data: {
			labels : siteLabel,
			datasets: [{
			      data: dataPieList	,
			      backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc','#FF0000','#f6e58d','#e056fd','#7bed9f','#ff6b81','#ced6e0'],
			      hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],
			      hoverBorderColor: "rgba(234, 236, 244, 1)",
			    }],
			options : {
		        legend: {
		            display: true,
		            position: 'bottom',
		            labels: {
		                boxWidth: 20,
		                fontColor: '#111',
		                padding: 15
		            }
		        },
		        tooltips: {
		            enabled: false
		        },
		        plugins: {
		            datalabels: {
		                color: '#111',
		                textAlign: 'center',
		                font: {
		                    lineHeight: 1.6
		                },
		                formatter: function(value, ctx) {
		                    return '%';
		                }
		            }
		        }
			}
},
})
}

function programChart(programLabel,programDataset){
	var dataPieList = programDataset;
	var programChart = document.getElementById("programChart");
	var myProgramChart = new Chart(programChart, {
		type: 'pie',
		data: {
			labels :programLabel,
			datasets: [{

			      data: dataPieList	,
			      backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc','#FF0000','#f6e58d','#e056fd','#7bed9f','#ff6b81','#ced6e0'],
			      hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],
			      hoverBorderColor: "rgba(234, 236, 244, 1)",
			    }],
			options : {
		        legend: {
		            display: true,
		            position: 'bottom',
		            labels: {
		                boxWidth: 20,
		                fontColor: '#111',
		                padding: 15
		            }
		        },
		        tooltips: {
		            enabled: false
		        },
		        plugins: {
		            datalabels: {
		                color: '#111',
		                textAlign: 'center',
		                font: {
		                    lineHeight: 1.6
		                },
		                formatter: function(value, ctx) {
		                    return '%';
		                }
		            }
		        }
			}
},
})
}



function number_format(number, decimals, dec_point, thousands_sep) {
  // *     example: number_format(1234.56, 2, ',', ' ');
  // *     return: '1 234,56'
  number = (number + '').replace(',', '').replace(' ', '');
  var n = !isFinite(+number) ? 0 : +number,
    prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
    sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
    dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
    s = '',
    toFixedFix = function(n, prec) {
      var k = Math.pow(10, prec);
      return '' + Math.round(n * k) / k;
    };
  // Fix for IE parseFloat(0.55).toFixed(0) = 0;
  s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
  if (s[0].length > 3) {
    s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
  }
  if ((s[1] || '').length < prec) {
    s[1] = s[1] || '';
    s[1] += new Array(prec - s[1].length + 1).join('0');
  }
  return s.join(dec);
}

function addData(chart, label, data) {
    chart.data.labels = label;
//    chart.data.datasets.forEach((dataset) => {
//        dataset.data.push(data);
//    });
    chart.data.datasets[0].data = data;
    chart.update();
}
function removeData(chart) {
    chart.data.datasets.forEach((dataset) => {
        dataset.data.pop();
    });
    chart.update();
}
function randomizeData(chart){

}
function clearDate(){
	$('input[type=text]').val('');
}