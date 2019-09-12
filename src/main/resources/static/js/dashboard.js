/*
var chart = $("#bar-chart-1");
var pie = $("#pie-chart-1");
var bg = [];
var barChart = new Chart(chart,{
    type: "bar",
    data: {
        labels: [],
        datasets: [{
          label: "Candidates",
          backgroundColor: bg,
          borderColor: "#000000",
          borderWidth: 2,
          data: [],
        }],
    },
    options: {
        legend: {
            display: false,
        }
    }
});
var pieChart = new Chart(pie,{
    type: 'pie',
    data: {
        labels: [],
        datasets: [{
          label: "Candidates",
          backgroundColor: bg,
          borderColor: "#000000",
          borderWidth: 2,
          data: [],
        }],
    },
});

$(document).ready(function(){
    getData();
    bg.push("#ffssff");
    for(var i=0;i<20;i++){
        var c = generateColor();
        bg.push(c);
    }
});
function getData(){
    $.ajax({
        type: "GET",
        url: "/manage/statistics/dashboard/chart1",
        success: function(data){
            var c_data = [];
            var c_label = [];
            var res = $.map(data,function(key,item){
                c_data.push(key);
                c_label.push(item);
            });
            for(i=0;i<c_data.length;i++){
                addData(barChart,c_label[i],c_data[i]);
                addData(pieChart,c_label[i],c_data[i]);
            }
        },
        error: function(e){
            console.log(e);
        },
    });
}
function addData(chart, label, data) {
    chart.data.labels.push(label);
    chart.data.datasets.forEach((dataset) => {
        dataset.data.push(data);
    });
    chart.update();
};*/
function generateColor(){
    var c = "#";
    var str = "0123456789ABCDEF";
    for(i=0;i<6;i++){
        c += str.charAt(Math.floor(Math.random()*str.length));
    }
    return c;
};
