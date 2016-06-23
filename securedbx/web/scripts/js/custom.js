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
                                        
                                        confirm(data[1].p4);
                                        window.location.assign(data[0].page);
                                       
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