function showRow(rowId){
    $(rowId).fadeToggle();
    return false;
};

$( document ).ready(function() {
    $('#btn_connection').click(function(){
        if($('#host').val()!="" && $('#port').val()!="" && $('#base').val()!="" && $('#user').val()!="" && $('#password').val()!="" && $('#sgbd').val()!=""){
            $.post("ServletSgbd", { host: $('#host').val(),
                                    port: $('#port').val(),
                                    base: $("#base").val(),
                                    user: $("#user").val(),
                                    password: $('#password').val(),
                                    sgbd: $('#sgbd').val()}, function( data ){ 

                                    if(data==0){
                                        alert("Invalid informations!");

                                    }else{
                                        
                                        $.cookie('host', $('#host').val(), {expires: 1});
                                        $.cookie('port', $('#port').val(), {expires: 1});
                                        $.cookie('base', $('#base').val(), {expires: 1});
                                        $.cookie('user', $('#user').val(), {expires: 1});
                                        $.cookie('password', $('#password').val(), {expires: 1});
                                        $.cookie('sgbd', $('#sgbd').val(), {expires: 1});
                                        

                                        /*  window.location.assign(data);*/
                                        $("#intro-info").toggleClass( "invisible" );

                                        //Item one
                                        setTimeout(function(){
                                            $("#item-one").show("slow").toggleClass( "item-invisible" );
                                        }, 1000);

                                        setTimeout(function(){
                                            $("#item-one").show("slow").toggleClass( "item-load" );
                                            $("#item-one").show("slow").toggleClass( "list-group-item-success" );
                                            $("#item-one").show("slow").toggleClass( "text-success " );
                                            $("#item-one").show("slow").toggleClass( "item-check" );
                                        }, 3000);  

                                        //Item two
                                        setTimeout(function(){
                                            $("#item-two").show("slow").toggleClass( "item-invisible" );
                                        }, 5000);

                                        setTimeout(function(){
                                            $("#item-two").show("slow").toggleClass( "item-load" );
                                            $("#item-two").show("slow").toggleClass( "list-group-item-success" );
                                            $("#item-two").show("slow").toggleClass( "text-success " );
                                            $("#item-two").show("slow").toggleClass( "item-check" );
                                        }, 7000);         

                                        //Item three
                                        setTimeout(function(){
                                            $("#item-three").show("slow").toggleClass( "item-invisible" );
                                        }, 8000);

                                        setTimeout(function(){
                                            $("#item-three").show("slow").toggleClass( "item-load" );
                                            $("#item-three").show("slow").toggleClass( "list-group-item-success" );
                                            $("#item-three").show("slow").toggleClass( "text-success " );
                                            $("#item-three").show("slow").toggleClass( "item-check" );
                                        }, 10000);         

                                        //Item four
                                        setTimeout(function(){
                                            $("#item-four").show("slow").toggleClass( "item-invisible" );
                                        }, 11000);

                                        setTimeout(function(){
                                            $("#item-four").show("slow").toggleClass( "item-load" );
                                            $("#item-four").show("slow").toggleClass( "list-group-item-success" );
                                            $("#item-four").show("slow").toggleClass( "text-success " );
                                            $("#item-four").show("slow").toggleClass( "item-check" );
                                        }, 13000);    

                                        //Item five
                                        setTimeout(function(){
                                            $("#item-five").show("slow").toggleClass( "item-invisible" );
                                        }, 14000);

                                        setTimeout(function(){
                                            $("#item-five").show("slow").toggleClass( "item-load" );
                                            $("#item-five").show("slow").toggleClass( "list-group-item-success" );
                                            $("#item-five").show("slow").toggleClass( "text-success " );
                                            $("#item-five").show("slow").toggleClass( "item-check" );
                                        }, 16000);    

                                        setTimeout(function(){
                                                $( "#list-parametrs" ).slideUp("slow");
                                        }, 18000);           


                                        setTimeout(function(){

                                            $("#system").toggleClass( "invisible" );
                                             gauge1();
                                             gauge2();
                                             gauge3();
                                             gauge4();
                                             gauge5();


                                        }, 20000);   

                                             //chamando os gráficos do arquivo gauge/dist/graphs.js
                                    }
            });

        }else{
            alert("Enter all fields!");
        }
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
    
    function checkParameters(fields){

    }
    
    function onlyNumber(fields){
        $(fields).unbind('keyup').bind('keyup',function(e){
            var thisVal = $(this).val();
            var tempVal = "";

            for(var i = 0; i<thisVal.length; i++){
                    if(RegExp(/^[0-9]$/).test(thisVal.charAt(i))){
                            tempVal += thisVal.charAt(i); 

                            if(e.keyCode == 8){
                                    tempVal = thisVal.substr(0,i); 
                            }						
                    }
            }			
            $(this).val(tempVal);
        });
    }

    onlyNumber($('input[id="port"]'));
});