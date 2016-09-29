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

            <!-- GRAPHS -->
            <section>
                <!--
                   <div id="preview">
                       <canvas width=220 height=70 class="canvas-preview" id="graph1"></canvas>
                       <div class="preview-textfield" id="preview1"></div>
                   </div>
                   <div id="preview">
                       <canvas width=220 height=70 class="canvas-preview" id="graph2"></canvas>
                       <div class="preview-textfield" id="preview2"></div>
                   </div>
                   <div id="preview">
                       <canvas width=220 height=70 class="canvas-preview" id="graph3"></canvas>
                       <div class="preview-textfield" id="preview3"></div>
                   </div>
                   <div id="preview">
                       <canvas width=220 height=70 class="canvas-preview" id="graph4"></canvas>
                       <div class="preview-textfield" id="preview4"></div>
                   </div>
                   <div id="preview">
                       <canvas width=220 height=70 class="canvas-preview" id="graph5"></canvas>
                       <div class="preview-textfield" id="preview5"></div>
                   </div>
                   <div id="preview">
                       <canvas width=220 height=70 class="canvas-preview" id="graph6"></canvas>
                       <div class="preview-textfield" id="preview6"></div>
                   </div>
                   <div id="preview">
                       <canvas width=220 height=70 class="canvas-preview" id="graph7"></canvas>
                       <div class="preview-textfield" id="preview7"></div>
                   </div>

                  
                 <div id="intro-system">
                       <div id="intro-info" class="intro-info" >
                           Enter your database credentials<br/> to proceed the analysis
                       </div>
                       <div id="list-parametrs" >
                           <ul class="list-group">
                               <li id="item-one" class="list-group-item item-invisible item-load"> Assessment of Users and Permissions </li>
                               <li id="item-two" class="list-group-item item-invisible item-load">Monitoring and Auditing  </li>
                               <li id="item-three" class="list-group-item item-invisible item-load"> Vulnerability and Configuration Management </li>
                               <li id="item-four" class="list-group-item item-invisible item-load"> Prevention and Blocking Attacks </li>
                               <li id="item-five" class="list-group-item item-invisible item-load"> Encryption, Tokenization and Data Masking </li>
                           </ul>
                       </div>      
                 </div> 
                -->  



                 <div id="system" class="invisible"> 
                     <div id="page_sgbd">
                         
                     </div>
                 </div>
<!--             <div  class="container"> 

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
               </div> -->   
           </div>
        </section>
    </body>
    <script src="scripts/gauge/assets/fd-slider/fd-slider.js" type="text/javascript"></script>
    <script src="scripts/gauge/dist/gauge.js" type="text/javascript"></script>
    <script src="scripts/gauge/dist/graphs.js" type="text/javascript"></script>
</html>