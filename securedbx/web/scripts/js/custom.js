function showRow(rowId){
    $(rowId).fadeToggle();
    return false;
};

$( document ).ready(function() {
    
    
    $('#btn_connection').click(function(){
        
        $('#page_sgbd').load($('#sgbd').val()+".html");
        
        if($('#host').val()!="" && $('#port').val()!="" && $('#base').val()!="" && $('#user').val()!="" && $('#password').val()!="" && $('#sgbd').val()!=""){
            $.post("ServletSgbd", { host: $('#host').val(),
                                    port: $('#port').val(),
                                    base: $("#base").val(),
                                    user: $("#user").val(),
                                    password: $('#password').val(),
                                    sgbd: $('#sgbd').val()}, function( data ){ 
                                    
                                    $.cookie('host', $('#host').val(), {expires: 1});
                                    $.cookie('port', $('#port').val(), {expires: 1});
                                    $.cookie('base', $('#base').val(), {expires: 1});
                                    $.cookie('user', $('#user').val(), {expires: 1});
                                    $.cookie('password', $('#password').val(), {expires: 1});
                                    $.cookie('sgbd', $('#sgbd').val(), {expires: 1});

                                    if(data==0){
                                        $('#page_sgbd').text("");
                                        bootbox.alert("Invalid informations!");
                                        
                                    }else{
                                        
                                        if($('#sgbd').val() == 'sqlserver'){
                                            //sqlserver
                                            //Setando resultado para grupo 1: 

                                            //Setando resultado para grupo 2:
                                            setIcon("#traceFilesDiagSecIssues", data[0].traceFilesDiagSecIssues);
                                            setIcon("#informationViews", data[1].informationViews);
                                            
                                            //Setando resultado para grupo 3:
                                            setIcon("#defaultPort", data[2].defaultPort);
                                            setIcon("#auditLevel", data[3].auditLevel);
                                            setIcon("#loginFailures", data[4].loginFailures);
                                            setIcon("#administratorsGroup", data[5].administratorsGroup);
                                            setIcon("#localAdministratorsGroup", data[6].localAdministratorsGroup);
                                            setIcon("#sysAdminUsers", data[7].sysAdminUsers);
                                            setIcon("#dbOwnerUser", data[8].dbOwnerUser);
                                            setIcon("#passwordExpirationPolicy", data[9].passwordExpirationPolicy);
                                            setIcon("#exampleDatabases", data[10].exampleDatabases);
                                            setIcon("#saUser", data[11].saUser);
                                            setIcon("#guestUser", data[12].guestUser);
                                            setIcon("#autenticationmode", data[13].autenticationmode);
                                            setIcon("#enabledNetworkProtocols", data[14].enabledNetworkProtocols);
                                            setIcon("#loginsWithoutPermissions", data[15].loginsWithoutPermissions);
                                            setIcon("#usersWithoutLogin", data[16].usersWithoutLogin);
                                            
                                            //grupo 3
                                            setIcon("#shellFileEnable", data[17].shellFileEnable);
                                            setIcon("#filestreamUsers", data[18].filestreamUsers);
                                            
                                            //grupo 1
                                            setIcon("#dbOwnerLogins", data[19].dbOwnerLogins);
                                            
                                            //grupo 3
                                            setIcon("#lastPatch", data[20].lastPatch);
                                            
                                            //grupo 1
                                            setIcon("#validBackups", data[21].validBackups);

                                            //Setando resultado para grupo 4;
                                            setIcon("#directUpdInSystemTables", data[22].directUpdInSystemTables);
                                            setIcon("#remoteAccessToServer", data[23].remoteAccessToServer);
                                            setIcon("#remoteAdminAccess", data[24].remoteAdminAccess);
                                            setIcon("#remoteLoginTimeout", data[25].remoteLoginTimeout);
                                            setIcon("#masterKey", data[26].masterKey);
                                            
                                            //5
                                            setIcon("#certificatesOrSymmetricKeys", data[27].certificatesOrSymmetricKeys);
                                            setIcon("#encryptedDatabases", data[28].encryptedDatabases);

                                            //se tiver invisivel
                                            if($("#system").hasClass("invisible")){
                                                $("#system").toggleClass( "invisible" );
                                            }
                                            
                                            gauge1();
                                            gauge2();
                                            gauge3();
                                            gauge4();
                                            gauge5();
                                             
                                        }else if($('#sgbd').val() == "oracle"){
                                            
                                            //oracle
                                            //Setando resultado para grupo 1: 
                                            setIcon("#invisibleUsers", data[7].invisibleUsers);
                                            setIcon("#distinctUsers", data[4].distinctUsers);
                                            setIcon("#securityRoles", data[19].securityRoles);
                                            setIcon("#nonAdministrativeUsers2", data[16].nonAdministrativeUsers2);
                                            setIcon("#nonAdministrativeUsers3", data[11].nonAdministrativeUsers3);                                            
                                            setIcon("#nonDefaultPrivilege", data[19].nonDefaultPrivilege);
                                            setIcon("#privilegesConfigured", data[18].privilegesConfigured);
                                            setIcon("#administrativeRoles", data[0].administrativeRoles);
                                            
                                            //Setando resultado para grupo 2:
                                            setIcon("#auditingIsEnabled", data[1].auditingIsEnabled);
                                            setIcon("#manyNonSystemUserSessions", data[15].manyNonSystemUserSessions);
                                            setIcon("#nonAdministrativeUsers", data[12].nonAdministrativeUsers);
                                            setIcon("#enablesSystemAuditing", data[5].enablesSystemAuditing);
                                                      
                                            //Setando resultado para grupo 3:
                                            setIcon("#systemPrivileges", data[19].systemPrivileges); 
                                            setIcon("#externalLibraries", data[6].externalLibraries);
                                            setIcon("#defaultDatabasePassword", data[2].defaultDatabasePassword);
                                            setIcon("#writeFiles", data[19].writeFiles); 
                                            setIcon("#deprecatedOptimizer", data[3].deprecatedOptimizer);
                                            setIcon("#useMaximumMemorySize", data[19].useMaximumMemorySize); 
                                            
                                            //Setando resultado para grupo 4;
                                            setIcon("#failedLogin", data[14].failedLogin);
                                            setIcon("#loginAttempts", data[8].loginAttempts);
                                            setIcon("#loginAttempts2", data[10].loginAttempts2);
                                            setIcon("#serverVersionInformation", data[18].serverVersionInformation);
                                            setIcon("#nonCaseSensitivePasswords", data[18].nonCaseSensitivePasswords);
                                            
                                            //se tiver invisivel
                                            if($("#system").hasClass("invisible")){
                                                $("#system").toggleClass( "invisible" );
                                            }
                                            
                                            gauge1();
                                            gauge2();
                                            gauge3();
                                            gauge4();
                                            gauge5();
                                            
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
                                            
                                            gauge1();
                                            gauge2();
                                            gauge3();
                                            gauge4();
                                            gauge5(); 
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
        $(div).html(' <img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;">');
    }else if(data == "false"){
        $(div).html(' <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">');
    }else {
        $(div).html(' <img src="scripts/img/warning.png" style="width:30px;height:55px;padding-top: 25px;">');
    }
}