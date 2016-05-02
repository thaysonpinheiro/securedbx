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
                    alert("Comeco");
                    $.post("ServletSgbd", {base: $("#base").val(),
                                          user: $("#user").val(),
                                          password: $('#password').val()}, function( data ){
                        $('#result').html(data);
                    });
                });
            });
        </script>
    </head>
    <body>
        <section id="connection">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3">
                        <p>
                        <label for="base">Base</label>
                        <input type="text" class="form-control" id="base" placeholder="nome da base">
                        </p>
                        <p>
                        <label for="user">Usuário</label>
                        <input type="text" class="form-control" id="user" placeholder="nome da base">
                        </p>
                        <p>
                        <label for="password">Senha</label>
                        <input type="password" class="form-control" id="password" placeholder="senha da base">
                        </p>
                        <button type="button" id="btn_connection" class="btn btn-primary">conectar</button>
                        <button type="button" id="btn_cancel" class="btn btn-default">cancelar</button>
                    </div>
                </div>
                <div class="row" id="result">
                </div>
            </div>   
        </section>
    </body>
</html>
