$(function () {
    var json_grafica2 = new Array();
    $.ajax({
        url: 'GetProducts',
        type: 'get',
        dataType: 'json'
    }).done(function (json) {
        $.each($.parseJSON(json.msg),
                function (i, row) {
                    json_grafica2.push([row.productname, row.reorderpoint]);
                });//ajax
        graficar(json_grafica2);
    });
});//function

function graficar(json){
    Highcharts.chart('grafica2', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text: 'Gráfica de producto y su reorderpoint'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            }
        },
        series: [{
            name: 'Brands',
            colorByPoint: true,
            data: json
        }]
    });
}

