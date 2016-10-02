function showRow(rowId){
    $(rowId).fadeToggle();
    return false;
};
jQuery.fn.toggleText = function(a,b) {
return   this.html(this.html().replace(new RegExp("("+a+"|"+b+")"),function(x){return(x==a)?b:a;}));
}
$( document ).ready(function() {
    /*
    $('#host').val($.cookie('host'));
    $('#port').val($.cookie('port'));
    $('#base').val($.cookie('base'));
    $('#user').val($.cookie('user'));
    $('#sgbd').val($.cookie('sgbd'));*/
    
    $('#btn_connection').click(function(){
        
        $('#page_sgbd').load($('#sgbd').val()+".html");
        
        if($('#host').val()!="" && $('#port').val()!="" && $('#base').val()!="" && $('#user').val()!="" && $('#password').val()!="" && $('#sgbd').val()!=""){
            $.post("ServletSgbd", { host: $('#host').val(),
                                    port: $('#port').val(),
                                    base: $("#base").val(),
                                    user: $("#user").val(),
                                    password: $('#password').val(),
                                    sgbd: $('#sgbd').val()}, function( data ){ 
                                    
                                   /* $.cookie('host', $('#host').val(), {expires: 1});
                                    $.cookie('port', $('#port').val(), {expires: 1});
                                    $.cookie('base', $('#base').val(), {expires: 1});
                                    $.cookie('user', $('#user').val(), {expires: 1});
                                    $.cookie('sgbd', $('#sgbd').val(), {expires: 1});*/

                                    if(data==0){
                                        $('#page_sgbd').text("");
                                        bootbox.alert("Invalid informations!");
                                        
                                    }else{
                                        
                                        if($('#sgbd').val() == 'sqlserver'){
                                            //sqlserver
                                            //Setando resultado para grupo 1: 
                                            setIcon("#sysAdminUsers", data[0].sysAdminUsers);
                                            setIcon("#dbOwnerUsers", data[0].dbOwnerUser);
                                            setIcon("#saUser", data[0].saUser);
                                            setIcon("#guestUser", data[0].guestUser);
                                            setIcon("#loginsWithoutPermissions", data[0].loginsWithoutPermissions);
                                            setIcon("#usersWithoutLogin", data[0].usersWithoutLogin);
                                            
                                            //Setando resultado para grupo 2:
                                            setIcon("#auditLevel", data[1].auditLevel);
                                            setIcon("#dbOwnerLogins", data[1].dbOwnerLogins);
                                            setIcon("#traceFilesDiagSecIssues", data[1].traceFilesDiagSecIssues);
                                            setIcon("#informationViews", data[1].informationViews);
                                            
                                            //Setando resultado para grupo 3:
                                            setIcon("#administratorsGroup", data[2].administratorsGroup);
                                            setIcon("#localAdministratorsGroup", data[2].localAdministratorsGroup);
                                            setIcon("#passwordExpirationPolicy", data[2].passwordExpirationPolicy);
                                            setIcon("#exampleDatabases", data[2].exampleDatabases);
                                            setIcon("#authenticationMode", data[2].authenticationMode);
                                            setIcon("#enabledNetworkProtocols", data[2].enabledNetworkProtocols);
                                            setIcon("#validBackups", data[2].validBackups);
                                            setIcon("#shellFileEnable", data[2].shellFileEnable);
                                            setIcon("#filestreamUsers", data[2].filestreamUsers);
                                            setIcon("#lastPatch", data[2].lastPatch);
                                            
                                            //Setando resultado para grupo 4:
                                            setIcon("#loginFailures", data[3].loginFailures);
                                            setIcon("#defaultPort", data[3].defaultPort);
                                            setIcon("#directUpdInSystemTables", data[3].directUpdInSystemTables);
                                            setIcon("#remoteAccessToServer", data[3].remoteAccessToServer);
                                            setIcon("#remoteAdminAccess", data[3].remoteAdminAccess);
                                            setIcon("#remoteLoginTimeout", data[3].remoteLoginTimeout);
                                            setIcon("#masterKey", data[3].masterKey);
                                           
                                            //Setando resultado para grupo 5
                                            setIcon("#certificatesOrSymmetricKeys", data[4].certificatesOrSymmetricKeys);
                                            setIcon("#encryptedDatabases", data[4].encryptedDatabases);

                                            //se tiver invisivel
                                            if($("#system").hasClass("invisible")){
                                                $("#system").toggleClass( "invisible" );
                                            }
                                            
                                            gauge(67, "preview1", "graph1");
                                            gauge(22, "preview2", "graph2");
                                            gauge(67, "preview3", "graph3");
                                            gauge(33, "preview4", "graph4");
                                            gauge(45, "preview5", "graph5");
                                             
                                        }else if($('#sgbd').val() == "oracle"){
                                            
                                            //oracle
                                            //Setando resultado para grupo 1: 
                                            setIcon("#invisibleUsers", data[0].invisibleUsers);
                                            setIcon("#distinctUsers", data[1].distinctUsers);
                                            setIcon("#securityRoles", data[2].securityRoles);
                                            setIcon("#nonAdministrativeUsers2", data[3].nonAdministrativeUsers2);
                                            setIcon("#nonAdministrativeUsers3", data[4].nonAdministrativeUsers3);                                            
                                            setIcon("#nonDefaultPrivilege", data[5].nonDefaultPrivilege);
                                            setIcon("#privilegesConfigured", data[6].privilegesConfigured);
                                            setIcon("#administrativeRoles", data[7].administrativeRoles);
                                            
                                            //Setando resultado para grupo 2:
                                            setIcon("#auditingIsEnabled", data[8].auditingIsEnabled);
                                            setIcon("#manyNonSystemUserSessions", data[9].manyNonSystemUserSessions);
                                            setIcon("#nonAdministrativeUsers", data[10].nonAdministrativeUsers);
                                            setIcon("#enablesSystemAuditing", data[11].enablesSystemAuditing);
                                                      
                                            //Setando resultado para grupo 3:
                                            setIcon("#systemPrivileges", data[12].systemPrivileges); 
                                            setIcon("#externalLibraries", data[13].externalLibraries);
                                            setIcon("#defaultDatabasePassword", data[14].defaultDatabasePassword);
                                            setIcon("#writeFiles", data[15].writeFiles); 
                                            setIcon("#deprecatedOptimizer", data[16].deprecatedOptimizer);
                                            setIcon("#useMaximumMemorySize", data[17].useMaximumMemorySize); 
                                            
                                            //Setando resultado para grupo 4;
                                            setIcon("#failedLogin", data[18].failedLogin);
                                            setIcon("#loginAttempts", data[19].loginAttempts);
                                            setIcon("#loginAttempts2", data[20].loginAttempts2);
                                            setIcon("#serverVersionInformation", data[21].serverVersionInformation);
                                            setIcon("#nonCaseSensitivePasswords", data[22].nonCaseSensitivePasswords);
                                            
                                            //se tiver invisivel
                                            if($("#system").hasClass("invisible")){
                                                $("#system").toggleClass( "invisible" );
                                            }
                                            
                                            //avaliando cada grupo e já montando o grafico
                                            gauge((getAvaliation(data, 0, 8)/8)*100, "preview1", "graph1");
                                            gauge((getAvaliation(data, 8, 12)/4)*100, "preview2", "graph2");
                                            gauge((getAvaliation(data, 12, 18)/6)*100, "preview3", "graph3");
                                            gauge((getAvaliation(data, 18, 23)/5)*100, "preview4", "graph4");
                                            
                                        }else{
                                            //postgres
                                            //Setando resultado para grupo 1: 
                                            setIcon("#superUsers", data[0].superUsers);
                                            setIcon("#usersAccessOtherUsers", data[1].usersAccessOtherUsers);
                                            setIcon("#securityPolicies", data[2].securityPolicies);
                                            setIcon("#publicObjectsPrivileges", data[3].publicObjectsPrivileges);
                                            setIcon("#usersEternalPass", data[4].usersEternalPass);
                                            setIcon("#usersNoADMCreateDB", data[5].usersNoADMCreateDB);
                                             
                                            //Setando resultado para grupo 2:
                                            setIcon("#auditingEnabled", data[6].auditingEnabled);
                                            setIcon("#tablesWithRowSecurity", data[7].tablesWithRowSecurity);
                                            
                                            //Setando resultado para grupo 3:
                                            setIcon("#objectsInPublicSchema", data[8].objectsInPublicSchema);
                                            setIcon("#publicObjectsInsDelUp", data[9].publicObjectsInsDelUp);
                                            setIcon("#defaultProceduralLang", data[10].defaultProceduralLang);
                                            setIcon("#nonTrustedProceduralLang", data[11].nonTrustedProceduralLang);
                                            setIcon("#latestVersionBin", data[12].latestVersionBin);
                                            setIcon("#noADMStreamOrOffBackup", data[13].noADMStreamOrOffBackup);
                                            setIcon("#listenAddressesDefault", data[14].listenAddressesDefault);
                                            setIcon("#perDBUserNames", data[15].perDBUserNames);
                                            
                                            //Setando resultado para grupo 4;
                                            setIcon("#serverWithDefaultEncription", data[16].serverWithDefaultEncription);
                                            setIcon("#dbServerGivesRowSecurity", data[17].dbServerGivesRowSecurity);
                                            setIcon("#shortTimeoutAut", data[18].shortTimeoutAut);
                                            setIcon("#functionsHighNumbersOfParameters", data[19].functionsHighNumbersOfParameters);
                                            setIcon("#dbHighNumberOfConnections", data[20].dbHighNumberOfConnections);
                                            
                                            //Setando resultado para grupo 5;
                                            setIcon("#dbServerUseSSL", data[21].dbServerUseSSL);
                                            
                                            //se tiver invisivel
                                            if($("#system").hasClass("invisible")){
                                                $("#system").toggleClass( "invisible" );
                                            }
                                            
                                            //avaliando cada grupo e já montando o grafico
                                            gauge((getAvaliation(data, 0, 6)/6)*100, "preview1", "graph1");
                                            gauge((getAvaliation(data, 6, 8)/2)*100, "preview2", "graph2");
                                            gauge((getAvaliation(data, 8, 16)/8)*100, "preview3", "graph3");
                                            gauge((getAvaliation(data, 16, 21)/5)*100, "preview4", "graph4");
                                            gauge((getAvaliation(data, 21, 22))*100, "preview5", "graph5");
                                            
                                            
                                        }
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
    
    $('.tgl').before('<span>Revelar conteúdo</span>');
    $('.tgl').css('display', 'none');
    $('span', '#box-toggle').click(function() {
            $(this).next().slideToggle('slow')
            .siblings('.tgl:visible').slideToggle('fast');

            $(this).toggleText('Revelar','Esconder')
            .siblings('span').next('.tgl:visible').prev()
            .toggleText('Revelar','Esconder');
    });
});

function setIcon(div, data){
    if(data == "true"){ 
        $(div).html(' <img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;">');
    }else if(data == "false"){
        $(div).html(' <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">');
    }else {
        $(div).html(' <img src="scripts/img/warning.png" style="width:30px;height:55px;padding-top: 25px;">');
    }
}

function getAvaliation(data, down, up){
    
    var cont = 0;
    for(var i=down; i < up; i++){
        var obj = data[i];
        for(var key in obj){
            var value = obj[key];
            if(value=="true"){
                ++cont;
            }
        }
    }
    
    return cont;
}