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
             text: 'Results - Summary'
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
                 name: 'Assessment of Users and Permissions',
                 y: 50
             }, {
                 name: 'Monitoring and Auditing',
                 y: 25
             }, {
                 name: 'Vulnerability and Configuration Management',
                 y: 55
             }, {
                 name: 'Prevention and Blocking Attacks',
                 y: 1
             }, {
                 name: 'Encryption, Tokenization and Data Masking',
                 y: 100
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
             text: 'Results - Summary'
         },
         subtitle: {
             text: ''
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
             max:100,
             title: {
                 text: 'Number of Occurrences'
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
             name: 'Assessment of Users and Permissions',
             data: [49.9, 71.5, 60, 80, 40.0, 50]

         }, {
             name: 'Monitoring and Auditing',
             data: [83.6, 78.8, 55, 80, 55, 25]

         }, {
             name: 'Vulnerability and Configuration Management',
             data: [48.9, 38.8, 39.3, 41.4, 47.0, 55]

         }, {
             name: 'Prevention and Blocking Attacks',
             data: [42.4, 20, 80, 25, 52.6, 1]

         }, {
             name: 'Encryption, Tokenization and Data Masking',
             data: [42.4, 33.2, 34.5, 39.7, 52.6, 100]

         }]
     });
 });


 $(function () {
     $('#line-chart').highcharts({
         title: {
             text: 'Results - Summary',
             x: -20 //center
         },
         subtitle: {
             text: '',
             x: -20
         },
         xAxis: {
             //numero de pontos em cada linha
             categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
         },
         yAxis: {
             title: {
                 text: 'Number of Occurrences'
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
             name: 'Assessment of Users and Permissions',
             data: [49.9, 71.5, 60, 80, 40.0, 50]
         }, {
             name: 'Monitoring and Auditing',
             data: [83.6, 78.8, 55, 80, 55, 25]
         }, {
             name: 'Vulnerability and Configuration Management',
             data: [48.9, 38.8, 39.3, 41.4, 47.0, 55]
         }, {
             name: 'Prevention and Blocking Attacks',
             data: [42.4, 20, 80, 25, 52.6, 1]
         }, {
             name: 'Encryption, Tokenization and Data Masking',
             data: [42.4, 33.2, 34.5, 39.7, 52.6, 100]
         }]
     });
 });