//Funcao para gerar grafico de pizza
 $(function () {
     $('#pie-chart').highcharts({
         chart: {
             plotBackgroundColor: null,
             plotBorderWidth: null,
             plotShadow: false,
             type: 'pie'
         },
         title: {
             //texto do titulo superior
             text: 'Browser market shares January, 2015 to May, 2015'
         },
         tooltip: {
             //formato da legenda
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
         //dados do grafico 
         series: [{
             name: 'Brands',
             colorByPoint: true,
             data: [{
                 name: 'Microsoft Internet Explorer',
                 y: 56.33
             }, {
                 name: 'Chrome',
                 y: 24.03,
             }, {
                 name: 'Firefox',
                 y: 10.38
             }, {
                 name: 'Safari',
                 y: 4.77
             }, {
                 name: 'Opera',
                 y: 0.91
             }, {
                 name: 'Proprietary or Undetectable',
                 y: 0.2
             }]
         }]
     });
 });


 //gerar grafico de barra
 $(function () {
     $('#bar-chart').highcharts({
         chart: {
             type: 'column'
         },
         title: {
             text: 'Monthly Average Rainfall'
         },
         subtitle: {
             text: 'Source: WorldClimate.com'
         },
         xAxis: {
             //cada categoria representa o numero de conjuntos de barras
             categories: [
                 'Jan',
                 'Feb',
                 'Mar',
                 'Apr',
                 'May',
                 'Jun',
             ],
             crosshair: true
         },
         yAxis: {
             min: 0,
             title: {
                 text: 'Rainfall (mm)'
             }
         },
         tooltip: {
             headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
             pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                 '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
             footerFormat: '</table>',
             shared: true,
             useHTML: true
         },
         plotOptions: {
             column: {
                 pointPadding: 0.2,
                 borderWidth: 0
             }
         },
         //cada serie adiciona uma barra com o valor descrito para cada conjunto de barras
         //de acordo com a ordem.
         series: [{
             name: 'Tokyo',
             data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0]

         }, {
             name: 'New York',
             data: [83.6, 78.8, 98.5, 93.4, 106.0, 84.5]

         }, {
             name: 'London',
             data: [48.9, 38.8, 39.3, 41.4, 47.0, 48.3]

         }, {
             name: 'Berlin',
             data: [42.4, 33.2, 34.5, 39.7, 52.6, 75.5]

         }]
     });
 });


 $(function () {
     $('#line-chart').highcharts({
         title: {
             text: 'Monthly Average Temperature',
             x: -20 //center
         },
         subtitle: {
             text: 'Source: WorldClimate.com',
             x: -20
         },
         xAxis: {
             //numero de pontos em cada linha
             categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
         },
         yAxis: {
             title: {
                 text: 'Temperature (°C)'
             },
             plotLines: [{
                 value: 0,
                 width: 1,
                 color: '#808080'
             }]
         },
         tooltip: {
             valueSuffix: '°C'
         },
         legend: {
             layout: 'vertical',
             align: 'right',
             verticalAlign: 'middle',
             borderWidth: 0
         },
         series: [{
             //altura de cada ponto nas categorias descritas acima
             name: 'Tokyo',
             data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
         }, {
             name: 'New York',
             data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
         }, {
             name: 'Berlin',
             data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
         }, {
             name: 'London',
             data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
         }]
     });
 });