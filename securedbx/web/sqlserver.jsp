<%-- 
    Document   : sqlserver
    Created on : 11/05/2016, 21:21:08
    Author     : Thayson
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SQLServer</title>
        <link href="scripts/img/min.png" rel="icon">
            
        <!-- jQuery -->
        <script src="scripts/js/jquery.js" type="text/javascript"></script>
        <!-- <script src="https://code.jquery.com/jquery-1.9.1.min.js"></script> -->
        
        <!-- Bootstrap Core JavaScript -->
        <script src="scripts/js/bootstrap.js"></script>

        <!-- Custom Core JavaScript -->
        <script src="scripts/js/custom.js" type="text/javascript"></script>
        
        <!-- Bootstrap Core CSS -->
        <link href="scripts/css/bootstrap.css" rel="stylesheet" type="text/css"/>
        
        <!-- Custom Core CSS -->
        <link href="scripts/css/custom.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <%--
            Cookie[] cookies = request.getCookies();

            for(Cookie cookie: cookies){
                out.println("<b>nomeCookie:</b>" + cookie.getName());   
                out.println("<b>Valor:</b>" + cookie.getValue());
                out.println("<br>");
            }
        --%>
        
        <!-- Menu --> 
        <header class="navbar navbar-default navbar-fixed">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#"><img src="scripts/img/logo.png" alt="DBS"></a>
                </div>
                <div class="navbar-left">
                    <p></p>
                </div>
            </div>
        </header>

        <!-- Parameters Table -->
        <div class="container" id="parameters">
            <div class="col-md-6 col-sm-offset-3">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>
                                
                            </th>
                            <th>
                                Options
                            </th>
                        </tr>
                    </thead>
                    <tr>
                        <tr class="assinatura success" onclick="showRow('#parametro1')">
                            <td  style="padding-bottom: 0px;">
                                <img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Audit level
                            </td>
     
                        </tr>
                        <tr id="parametro1" class="hidden-row">
                            <td></td>
                            <td>Collects a single instance of actions at the server level and / or database and action groups to be monitored</td>
                        </tr>
                
                    </tr>
                    <tr>
                        <tr class="assinatura success" onclick="showRow('#parametro2')">
                            <td  style="padding-bottom: 0px;">
                                <img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Number of event logs
                            </td>
                        </tr>
                        <tr id="parametro2" class="hidden-row">
                            <td></td>
                            <td>This is to not lose information in a short time  </td>
                        </tr>
                    </tr>
                    <tr>
                        <tr class="assinatura success" onclick="showRow('#parametro3')">
                            <td  style="padding-bottom: 0px;">
                                <img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Notifications about security events
                            </td>
                        </tr>
                        <tr id="parametro3" class="hidden-row">
                            <td></td>
                            <td>Creating security event notification. </td>
                        </tr>
                    </tr>    
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro4')">
                            <td  style="padding-bottom: 0px;">
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Login failure events
                            </td>
                        </tr>
                        <tr id="parametro4" class="hidden-row">
                            <td></td>
                            <td>This security setting determines whether to audit each instance logon or logoff of a user on a computer should be made. </td>
                        </tr>
                    </tr>     
                    <tr>
                        <tr class="assinatura success" onclick="showRow('#parametro5')">
                            <td  style="padding-bottom: 0px;">
                                <img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Administrators group was removed from the sysadmin role
                            </td>
                        </tr>
                        <tr id="parametro5" class="hidden-row">
                            <td></td>
                            <td>****************************  </td>
                        </tr>
                    </tr>                              
                    <tr>
                        <tr class="assinatura success" onclick="showRow('#parametro6')">
                            <td  style="padding-bottom: 0px;">
                                <img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                There are members of the "Local Administrators" in the server
                            </td>
                        </tr>
                        <tr id="parametro6" class="hidden-row">
                            <td></td>
                            <td>******************************  </td>
                        </tr>
                    </tr>   
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro7')">
                            <td  style="padding-bottom: 0px;" >
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Users belonging to role "Sysadmins" **
                            </td>
                        </tr>
                        <tr id="parametro7" class="hidden-row">
                            <td></td>
                            <td>Members of the sysadmin fixed server role can perform any activity on the server. (Users list)</td>
                        </tr>
                    </tr>   
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro8')">
                            <td  style="padding-bottom: 0px;" >
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Each database members associated with role db_owner **
                            </td>
                        </tr>
                        <tr id="parametro8" class="hidden-row">
                            <td></td>
                            <td>
                                Members of the db_owner fixed database role can perform all configuration and maintenance activities in the database and discard the database. (list users of each database)
                            </td>
                        </tr>
                    </tr>   
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro9')">
                            <td  style="padding-bottom: 0px;" >
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Logins associated with the "BOD" ( db owner) in each database **
                            </td>
                        </tr>
                        <tr id="parametro9" class="hidden-row">
                            <td></td>
                            <td>Members of the db_owner fixed database role can perform all configuration and maintenance activities in the database and discard the database. (logins associated with the "DBO" ( db owner) in each database)</td>
                        </tr>
                    </tr>
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro10')">
                            <td  style="padding-bottom: 0px;" >
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Password expiration policy
                            </td>
                        </tr>
                        <tr id="parametro10" class="hidden-row">
                            <td></td>
                            <td>Password expiration policies are used to manage the lifespan of a password.</td>
                        </tr>
                    </tr>   
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro11')">
                            <td  style="padding-bottom: 0px;" >
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                The sample databases were removed from the server
                            </td>
                        </tr>
                        <tr id="parametro11" class="hidden-row">
                            <td></td>
                            <td>Verify if the databases samples (adventureworks, pubs, others) were removed from the server</td>
                        </tr>
                    </tr>                      
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro12')">
                            <td  style="padding-bottom: 0px;" >
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                The user SA was renamed or removed from the server
                            </td>
                        </tr>
                        <tr id="parametro12" class="hidden-row">
                            <td></td>
                            <td>The default user that SQL Server creates when we installed it.</td>
                        </tr>
                    </tr>               
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro13')">
                            <td  style="padding-bottom: 0px;" >
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                The permissions that were granted to the default user "guest" **
                            </td>
                        </tr>
                        <tr id="parametro13" class="hidden-row">
                            <td></td>
                            <td>Lists the permissions granted to the "guest" user</td>
                        </tr>
                    </tr>                 
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro14')">
                            <td  style="padding-bottom: 0px;" >
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Configured authentication mode for connections **
                            </td>
                        </tr>
                        <tr id="parametro14" class="hidden-row">
                            <td></td>
                            <td>Descricao do primeiro parâmetro Descricao do primeiro parâmetro  </td>
                        </tr>
                    </tr>                 
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro15')">
                            <td  style="padding-bottom: 0px;" >
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Network protocols enabled **
                            </td>
                        </tr>
                        <tr id="parametro15" class="hidden-row">
                            <td></td>
                            <td>list of Network Protocols enabled</td>
                        </tr>
                    </tr>       
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro16')">
                            <td  style="padding-bottom: 0px;" >
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Registered logins that do not have associated permissions **
                            </td>
                        </tr>
                        <tr id="parametro16" class="hidden-row">
                            <td></td>
                            <td>List of logins without permissions associated with them</td>
                        </tr>
                    </tr>   
                    </tr>       
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro17')">
                            <td  style="padding-bottom: 0px;" >
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Associate users without login **
                            </td>
                        </tr>
                        <tr id="parametro17" class="hidden-row">
                            <td></td>
                            <td>List of users without associated login  </td>
                        </tr>
                    </tr>  
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro18')">
                            <td  style="padding-bottom: 0px;" >
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                There are valid backups
                            </td>
                        </tr>
                        <tr id="parametro18" class="hidden-row">
                            <td></td>
                            <td>Check if there are valid backups.</td>
                        </tr>
                    </tr>                 
                </table>
                <div class="col-md-4 col-sm-offset-4">
                    <center>
                        <button type="button" id="btn_connection" class="btn btn-primary">confirm</button>
                    </center>
                </div>
            </div>
        </div>
    </body>
</html>
