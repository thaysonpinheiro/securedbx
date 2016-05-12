function showRow(rowId){
    $(rowId).fadeToggle();
    return false;
};

$( document ).ready(function() {
    $('#btn_connection').click(function(){
        $.post("ServletSgbd", {host: $('#host').val(),
                              port: $('#port').val(),
                              base: $("#base").val(),
                              user: $("#user").val(),
                              password: $('#password').val(),
                              sgbd: $('#sgbd').val()}, function( data ){ 
                              
                                    if(data==0){
                                        $('#result').html("FAIL");
                                        $('#result').removeClass("alert-success");
                                        $('#result').addClass("alert-danger");              
                                    }else{
                                        window.location.assign(data);  
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