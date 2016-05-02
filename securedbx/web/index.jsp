<%-- 
    Document   : oracle
    Created on : 29/04/2016, 16:43:32
    Author     : Thayson
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <title>SecureDB</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.js"></script>

        <!-- Bootstrap Core CSS -->
        <link rel="stylesheet" href="css/bootstrap.css" type="text/css">
        
        <!-- jQuery -->
        <script src="https://code.jquery.com/jquery-1.9.1.min.js"></script>
        <script>
            $( document ).ready(function() {
                $('#btn_connection').click(function(){
                     $('#result').html("");
                    //alert($('#sgbd').val()+" "+ $('#host').val()+" "+ $('#password').val()+" "+$('#port').val() +" "+ $("#base").val()+" "+$("#user").val());
                    $.post("ServletSgbd", {host: $('#host').val(),
                                          port: $('#port').val(),
                                          base: $("#base").val(),
                                          user: $("#user").val(),
                                          password: $('#password').val(),
                                          sgbd: $('#sgbd').val()}, function( data ){
                        $('#result').html(data);
                    });
                });
            });
        </script>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <center>
                    <div id="result" class=" col-md-4 col-sm-offset-4 alert alert-success">
                    </div>
                </center>
            </div>
            <div class="row">
                <div class="col-md-12 col-sm-offset-3">
                    <div class="col-md-3">
                        <p>
                        <label for="base">Host</label>
                        <input type="text" class="form-control" id="host" placeholder="host">
                        </p>
                        <p>
                        <label for="user">Port</label>
                        <input type="text" class="form-control" id="port" placeholder="port">
                        </p>
                        <p>
                        <label for="base">Base</label>
                        <input type="text" class="form-control" id="base" placeholder="base name">
                        </p>

                    </div>
                    <div class="col-md-3">
                        <p>
                        <label for="user">User</label>
                        <input type="text" class="form-control" id="user" placeholder="username">
                        </p>
                        <p>
                        <label for="password">Password</label>
                        <input type="password" class="form-control" id="password" placeholder="password">
                        </p>
                        <p>
                        <label for="sgbd">Sgbd</label>
                        <select class="form-control" id="sgbd">
                            <option value="oracle">Oracle</option>
                            <option value="postgresql">PostgreSQL</option>
                            <option value="sqlserver">SQLServer</option>
                        </select> 
                        </p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 col-sm-offset-4">
                    <center>
                    <button type="button" id="btn_connection" class="btn btn-primary">conectar</button>
                    <button type="button" id="btn_cancel" class="btn btn-default">cancelar</button>
                    </center>
                </div>
            </div>
        </div>    
    </body>
</html>
