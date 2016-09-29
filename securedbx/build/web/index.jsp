<!DOCTYPE html>
<html lang="pt-br">
    
    <head>
        <title>ANITA</title>
        <link href="scripts/img/min.png" rel="icon">
            
        <!-- jQuery -->
        
        <script src="scripts/js/jquery.js" type="text/javascript"></script>
        <script src="scripts/js/jquery.cookie.js" type="text/javascript"></script>
        <!--<script src="https://code.jquery.com/jquery-1.9.1.min.js"></script>-->
        
        <!-- Code Result Graph -->
        <script src="https://code.highcharts.com/highcharts.js"></script>
        <script src="https://code.highcharts.com/modules/exporting.js"></script>
        <script src="scripts/js/graphs-results.js" type="text/javascript"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="scripts/js/bootstrap.js"></script>

        <!-- Custom Core JavaScript -->
        <script src="scripts/js/custom.js" type="text/javascript"></script>
        
        <!-- Bootstrap Core CSS -->
        <link href="scripts/css/bootstrap.css" rel="stylesheet" type="text/css"/>
        
        <!-- Custom Core CSS -->
        <link href="scripts/css/custom.css" rel="stylesheet" type="text/css"/>

        <!-- Code Gauge-->
        <link href="scripts/gauge/assets/main.css?v=5" rel="stylesheet" type="text/css"/>
        <link href="scripts/gauge/assets/prettify.css" rel="stylesheet" type="text/css"/>
        
        <script src="scripts/gauge/assets/prettify.js" type="text/javascript"></script>
        <script src="scripts/gauge/assets/jscolor.js" type="text/javascript"></script>
        <script src="scripts/js/bootbox.min.js" type="text/javascript"></script>
        <script src="https://github.com/makeusabrew/bootbox/releases/download/v4.4.0/bootbox.min.js"></script>

        <!--[if lt IE 9]><script type="text/javascript" src="assets/excanvas.compiled.js"></script><![endif]-->

    </head>
    <body>
        
        <!-- Menu -->
        <header class="navbar navbar-default navbar-fixed">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#"><img src="scripts/img/logo.png" alt=""/></a>
                    
                </div>
                <div class="navbar-left">
                    <p>ANITA - A Non Intrusive Tool Analyzer to Hardening Database Security</p>
                </div>
            </div>
        </header>
        
        <!-- FORM -->
        <div id="limit">
            <aside class="form-data">
                <label for="connection">Connection</label><br />

                <label for="base">Host</label>
                <input type="text" class="form-control input-sm form-connection" id="host" placeholder="host">

                <label for="user">Port</label>
                <input type="text" class="form-control input-sm form-connection" id="port" placeholder="port">

                <label for="base">Database</label>
                <input type="text" class="form-control input-sm form-connection" id="base" placeholder="base name">

                <label for="user">User</label>
                <input type="text" class="form-control input-sm form-connection" id="user" placeholder="username">

                <label for="password">Password</label>
                <input type="password" class="form-control input-sm form-connection" id="password" placeholder="password">

                <label for="sgbd">DBMS</label>
                <select class="form-control input-sm form-connection" id="sgbd">
                    <option value="oracle">Oracle</option>
                    <option value="postgresql">PostgreSQL</option>
                    <option value="sqlserver">SQLServer</option>
                </select> <br />
                <button type="button" id="btn_connection" class="btn btn-primary">Security Check</button>
            </aside>
            <section>
                <div id="system" class="invisible"> 
                    <div id="page_sgbd">

                    </div>
                </div>    
            </section>
       </div>
    </body>
    <script src="scripts/gauge/assets/fd-slider/fd-slider.js" type="text/javascript"></script>
    <script src="scripts/gauge/dist/gauge.js" type="text/javascript"></script>
    <script src="scripts/gauge/dist/graphs.js" type="text/javascript"></script>
</html>