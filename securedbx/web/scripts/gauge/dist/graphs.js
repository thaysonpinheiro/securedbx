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
 
 function gauge1(){
     
    var target = document.getElementById('graph1');
    var nivel = 50;
    var color = colorTest(nivel);
    
    gauge1 = new Gauge(target).setOptions(update('#FFD700'));
    gauge1.setTextField(document.getElementById("preview1"));
    gauge1.maxValue = 100;
    
    gauge1.set(nivel);

 };

 function gauge2(){

    var target = document.getElementById('graph2');
    var nivel = 25;
    var color = colorTest(nivel);
    
    gauge1 = new Gauge(target).setOptions(update(color));
    gauge1.setTextField(document.getElementById("preview2"));
    gauge1.maxValue = 100;
    
    gauge1.set(nivel);

 };
 
function gauge3(){
    
    var target = document.getElementById('graph3');
    var nivel = 55;
    var color = colorTest(nivel);
    
    gauge1 = new Gauge(target).setOptions(update(color));
    gauge1.setTextField(document.getElementById("preview3"));
    gauge1.maxValue = 100;
    
    gauge1.set(nivel);

 };
 
 function gauge4(){
    
    var target = document.getElementById('graph4');
    var nivel = 1;
    var color = colorTest(nivel);
    
    gauge1 = new Gauge(target).setOptions(update(color));
    gauge1.setTextField(document.getElementById("preview4"));
    gauge1.maxValue = 100;
    
    gauge1.set(nivel);

 };
 
 function gauge5(){
    
    var target = document.getElementById('graph5');
    var nivel = 100;
    var color = colorTest(nivel);
    
    gauge1 = new Gauge(target).setOptions(update(color));
    gauge1.setTextField(document.getElementById("preview5"));
    gauge1.maxValue = 100;
    
    gauge1.set(nivel);

 };
 
 function gauge6(){
    
    var target = document.getElementById('graph6');
    var nivel = Math.floor( (Math.random() * 100) + 1);
    var color = colorTest(nivel);
    
    gauge1 = new Gauge(target).setOptions(update(color));
    gauge1.setTextField(document.getElementById("preview6"));
    gauge1.maxValue = 100;
    
    gauge1.set(nivel);

 };
 
 function gauge7(){
    
    var target = document.getElementById('graph7');
    var nivel = Math.floor( (Math.random() * 100) + 1);
    var color = colorTest(nivel);
    
    gauge1 = new Gauge(target).setOptions(update(color));
    gauge1.setTextField(document.getElementById("preview7"));
    gauge1.maxValue = 100;
    
    gauge1.set(nivel);

 };
 


 

