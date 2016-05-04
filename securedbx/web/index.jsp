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
        <!-- Bootstrap Core JavaScript -->
        <script src="scripts/js/bootstrap.js"></script>

        <!-- Bootstrap Core CSS -->
        <link href="scripts/css/bootstrap.css" rel="stylesheet" type="text/css"/>

        <!-- jQuery -->
        <script src="https://code.jquery.com/jquery-1.9.1.min.js"></script>
        <script>
            $( document ).ready(function() {
                $('#btn_connection').click(function(){
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
                            $("#connection").hide("slow");
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
        </script>
    </head>
    <body>
        <div  class="container">
            <div class="col-md-12">            
                    <div id="result" class="col-md-4 col-sm-offset-4 text-center alert">
                    </div>
            </div>
            <div class="container" id="connection">
                <div class="col-md-12">
                    <center><h2>Connection</h2></center>
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
            <div class="container" id="parameters">
                
            </div>
        </div>    
    </body>
</html>
