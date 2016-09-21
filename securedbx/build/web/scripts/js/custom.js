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
                                        bootbox.alert("Invalid informations!");
                                        

                                    }else{
                                        
                                        $.cookie('host', $('#host').val(), {expires: 1});
                                        $.cookie('port', $('#port').val(), {expires: 1});
                                        $.cookie('base', $('#base').val(), {expires: 1});
                                        $.cookie('user', $('#user').val(), {expires: 1});
                                        $.cookie('password', $('#password').val(), {expires: 1});
                                        $.cookie('sgbd', $('#sgbd').val(), {expires: 1});
                                        

                                        //  window.location.assign(data);
                                        $("#intro-info").toggleClass( "invisible" );
                                        
                                        //Setando resultado para grupo 1: 
                                        setIcon("#sysAdminUsers", data[0].sysAdminUsers);
                                        setIcon("#dbOwnerUsers", data[1].dbOwnerUser);
                                        setIcon("#saUser", data[2].saUser);
                                        setIcon("#guestUser", data[3].guestUser);
                                        setIcon("#loginsWithoutPermissions", data[4].loginsWithoutPermissions);
                                        setIcon("#usersWithoutLogin", data[5].usersWithoutLogin);
                                        
                                        //Setando resultado para grupo 2:
                                        setIcon("#auditLevel", data[6].auditLevel);
                                        setIcon("#dbOwnerLogins", data[14].dbOwnerLogins);
                                        
                                        //Setando resultado para grupo 3:
                                        setIcon("#administratorsGroup", data[7].administratorsGroup);
                                        setIcon("#localAdministratorsGroup", data[8].localAdministratorsGroup);
                                        setIcon("#exampleDatabases", data[10].exampleDatabases);
                                        setIcon("#enabledNetworkProtocols", data[15].enabledNetworkProtocols);
                                        setIcon("#validBackups", data[12].validBackups);
                                        setIcon("#passwordExpirationPolicy", data[16].passwordExpirationPolicy);
                                        setIcon("#authenticationMode", data[11].authenticationMode);
                                        
                                        //Setando resultado para grupo 4;
                                        setIcon("#defaultPort", data[18].defaultPort);
                                        setIcon("#loginFailures", data[19].loginFailures);
                                        
                                        $("#system").toggleClass( "invisible" );
                                         gauge1();
                                         gauge2();
                                         gauge3();
                                         gauge4();
                                         gauge5();


                                        
                                       

                                             //chamando os gráficos do arquivo gauge/dist/graphs.js
                                    }
            });

        }else{
            bootbox.alert("Enter all fields!");
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

function setIcon(div, data){
    if(data == "true"){ 
        $(div).prepend(' <img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;">');
    }else if(data == "false"){
        $(div).prepend(' <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">');
    }else {
        $(div).prepend(' <img src="scripts/img/warning.png" style="width:30px;height:55px;padding-top: 25px;">');
    }
}