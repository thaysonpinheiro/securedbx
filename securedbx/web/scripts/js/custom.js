$( document ).ready(function() {
     $('#btn_connection').click(function(){

         //alert($('#sgbd').val()+" "+ $('#host').val()+" "+ $('#password').val()+" "+$('#port').val() +" "+ $("#base").val()+" "+$("#user").val());
         $.post("ServletSgbd", {host: $('#host').val(),
                               port: $('#port').val(),
                               base: $("#base").val(),
                               user: $("#user").val(),
                               password: $('#password').val(),
                               sgbd: $('#sgbd').val()}, function( data ){
             if(data==1){
                 $('#result').html("CONNECTED");
                 $('#result').addClass("alert-success");
                 $('#result').removeClass("alert-danger");
             }else{
                 $('#result').html("FAIL");
                 $('#result').removeClass("alert-success");
                 $('#result').addClass("alert-danger");
             }
         });
     });

     $('#btn_cancel').click(function(){
         $('#result').removeClass("alert-danger");
         $('#result').removeClass("alert-success");
         $('#result').html("");
         $('#host').val("");
         $('#port').val("");
         $("#base").val("");
         $("#user").val("");
         $('#password').val("");
         $('#sgbd').val("oracle");

     });
 });