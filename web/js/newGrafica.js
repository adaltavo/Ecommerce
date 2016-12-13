$(function () {
    var json_grafica = new Array();
    $.ajax({
        url: 'GetProducts',
        type: 'get',
        dataType: 'json'
    }).done(function (json) {
        $.each($.parseJSON(json.msg),
                function (i, row) {
                    json_grafica.push([row.productname, row.stock]);
                });//ajax
        graficar(json_grafica);
    });
});//function

function graficar(json){
    Highcharts.chart('grafica', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text: 'Gráfica de producto y su stock'
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

