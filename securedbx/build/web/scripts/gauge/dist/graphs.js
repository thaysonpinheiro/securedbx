function update(color) {
    var opts = {
        lines: 12, // The number of lines to draw
        angle: 0, // The length of each line
        lineWidth: 0.12, // The line thickness
        pointer: {
          length: 0.78, // The radius of the inner circle
          strokeWidth: 0.042, // The rotation offset
          color: '#000000' // Fill color
        },
        limitMax: 'false',   // If true, the pointer will not go past the end of the gauge
        colorStart: '#E0E0E0',   // Colors
        colorStop: color,    // just experiment with them
        strokeColor: '#E0E0E0',   // to see which ones work best for you
        generateGradient: true
    };
     
    return opts;
 }


 function colorTest(nivel){
    if(nivel<=33){
         //color red
        return '#FF0000';
    }else if(nivel > 66){
        //color green
        return '#228B22';
    }else{
        //color yellow
        return '#FFD700';
    }
 }
 
 function gauge(valor, div, grafico){
     
    var target = document.getElementById(grafico);

    var color = colorTest(valor);
    
    gauge1 = new Gauge(target).setOptions(update(color));
    gauge1.setTextField(document.getElementById(div));
    gauge1.maxValue = 100;
    
    if(valor == 0)
        gauge1.set(1);
    else
        gauge1.set(valor);
 };
 


 

