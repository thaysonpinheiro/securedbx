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
                    <p>----------</p>
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
                                #
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
                                Nível de Auditoria
                            </td>
     
                        </tr>
                        <tr id="parametro1" class="hidden-row">
                            <td></td>
                            <td>Descricao do primeiro parâmetro Descricao do primeiro parâmetro  </td>
                        </tr>
                
                    </tr>
                    <tr>
                        <tr class="assinatura success" onclick="showRow('#parametro2')">
                            <td  style="padding-bottom: 0px;">
                                <img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Número de LOGS de eventos
                            </td>
                        </tr>
                        <tr id="parametro2" class="hidden-row">
                            <td></td>
                            <td>Descricao do primeiro parâmetro Descricao do primeiro parâmetro  </td>
                        </tr>
                    </tr>
                    <tr>
                        <tr class="assinatura success" onclick="showRow('#parametro3')">
                            <td  style="padding-bottom: 0px;">
                                <img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Notificações sobre eventos
                            </td>
                        </tr>
                        <tr id="parametro3" class="hidden-row">
                            <td></td>
                            <td>Descricao do primeiro parâmetro Descricao do primeiro parâmetro  </td>
                        </tr>
                    </tr>    
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro4')">
                            <td  style="padding-bottom: 0px;">
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Eventos de falha de login
                            </td>
                        </tr>
                        <tr id="parametro4" class="hidden-row">
                            <td></td>
                            <td>Descricao do primeiro parâmetro Descricao do primeiro parâmetro  </td>
                        </tr>
                    </tr>     
                    <tr>
                        <tr class="assinatura success" onclick="showRow('#parametro5')">
                            <td  style="padding-bottom: 0px;">
                                <img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Grupo Administrators removido da role sysadmins
                            </td>
                        </tr>
                        <tr id="parametro5" class="hidden-row">
                            <td></td>
                            <td>Descricao do primeiro parâmetro Descricao do primeiro parâmetro  </td>
                        </tr>
                    </tr>                              
                    <tr>
                        <tr class="assinatura success" onclick="showRow('#parametro6')">
                            <td  style="padding-bottom: 0px;">
                                <img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Existem membros do grupo "Local Administrators" no servidor
                            </td>
                        </tr>
                        <tr id="parametro6" class="hidden-row">
                            <td></td>
                            <td>Descricao do primeiro parâmetro Descricao do primeiro parâmetro  </td>
                        </tr>
                    </tr>   
                    <tr>
                        <tr class="assinatura danger" onclick="showRow('#parametro7')">
                            <td  style="padding-bottom: 0px;" >
                                <img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;">
                            </td>  
                            <td>
                                Usuarios pertencentes a role "Sysadmins"
                            </td>
                        </tr>
                        <tr id="parametro7" class="hidden-row">
                            <td></td>
                            <td>Descricao do primeiro parâmetro Descricao do primeiro parâmetro  </td>
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
