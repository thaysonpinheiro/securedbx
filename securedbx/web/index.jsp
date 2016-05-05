<%-- 
    Document   : oracle
    Created on : 29/04/2016, 16:43:32
    Author     : Thayson
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <title>DBS</title>
        <link href="scripts/img/min.png" rel="icon">
        
        <!-- Bootstrap Core JavaScript -->
        <script src="scripts/js/bootstrap.js"></script>

        <!-- Bootstrap Core CSS -->
        <link href="scripts/css/bootstrap.css" rel="stylesheet" type="text/css"/>
        
        <!-- Custom Core CSS -->
        <link href="scripts/css/custom.css" rel="stylesheet" type="text/css"/>
        <script src="scripts/js/jquery.js" type="text/javascript"></script>
        <!-- jQuery 
        <script src="https://code.jquery.com/jquery-1.9.1.min.js"></script>-->
        
        <script src="scripts/js/custom.js" type="text/javascript"></script>
    </head>
    <body>
        
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
        
        <div  class="container"> 
            <!-- Database Connection -->
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
                            <td class="col-md-1">
                                <input type="checkbox">
                            </td>
                            <td>
                                Login Default
                            </td>
                        </tr>
                        <tr>
                            <td class="col-md-1">
                                <input type="checkbox">
                            </td>
                            <td>
                                Login Default Login Default
                            </td>
                        </tr>
                    </table>
                    <div class="col-md-4 col-sm-offset-4">
                        <center>
                        <button type="button" id="btn_connection" class="btn btn-primary">confirm</button>
                        </center>
                    </div>
                </div>
            </div>
        </div>        
    </body>
</html>
