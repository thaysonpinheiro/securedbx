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

               -->  
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




              <div id="system" class="invisible"> 
                   <!--GROUP 1-->
                    <div class="painel">
                        <div class="titulo-table">Assessment of Users and Permissions</div>
                        <table class="table table-bordered tableGroup group">
                            <thead class="thead-inverse">
                              <tr>
                                <th>Item</th>
                                <th>Desciption</th>
                                <th>Avaliation</th>
                              </tr>
                            </thead>
                            <tbody>
                              <tr>
                                <th scope="row">1</th>
                                <td>Sysadmins Users</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">2</th>
                                <td >db_owner Users</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  

                     
                              </tr>
                              <tr>
                                <th scope="row">3</th>
                                <td >SA User</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                          
                              </tr>
                              <tr>
                                <th scope="row">4</th>
                                <td >Guest User</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">5</th>
                                <td >Logins without Permissionsr</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">6</th>
                                <td >Users without Login</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                            </tbody>
                        </table>
                        <div class="group graph">
                            <div id="preview">
                                <canvas width=220 height=70 class="canvas-preview" id="graph1"></canvas>
                                <div class="preview-textfield" id="preview1"></div>
                            </div>
                        </div>
                    </div>   
                   
                   <!--GROUP 2-->
                   <div class="painel">
                       <div class="titulo-table">Monitoring and Auditing</div>
                        <table class="table table-bordered tableGroup group">
                            <thead class="thead-inverse">
                              <tr>
                                <th>Item</th>
                                <th>Desciption</th>
                                <th>Avaliation</th>
                              </tr>
                            </thead>
                            <tbody>
                              <tr>
                                <th scope="row">1</th>
                                <td>Audit Level</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">2</th>
                                <td >Number of Event Logs</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">3</th>
                                <td >Notifications about Events</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">4</th>
                                <td >db_owner Logins</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>     
                              </tr>

                            </tbody>
                        </table>
                        <div class="group graph">
                            <div id="preview">
                                <canvas width=220 height=70 class="canvas-preview" id="graph2"></canvas>
                                <div class="preview-textfield" id="preview2"></div>
                            </div>
                        </div>
                    </div>    
                   
                   <!--GROUP 3-->
                   <div class="painel">
                       <div class="titulo-table">Vulnerability and Configuration Management</div> 
                       <table class="table table-bordered tableGroup group">
                            <thead class="thead-inverse">
                              <tr>
                                <th>Item</th>
                                <th>Desciption</th>
                                <th>Avaliation</th>
                              </tr>
                            </thead>
                            <tbody>
                              <tr>
                                <th scope="row">1</th>
                                <td>Administrators Group</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">2</th>
                                <td >Local Administrators Group</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">3</th>
                                <td >Password Expiration Policy</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">4</th>
                                <td >Example Databases</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">5</th>
                                <td >Authentication Mode</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">6</th>
                                <td >Enabled Network Protocols</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">7</th>
                                <td >Valid Backups</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">8</th>
                                <td >Current Security Patches</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">9</th>
                                <td >Number of Views</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                            </tbody>
                        </table>
                        <div class="group graph">
                            <div id="preview">
                                <canvas width=220 height=70 class="canvas-preview" id="graph3"></canvas>
                                <div class="preview-textfield" id="preview3"></div>
                            </div>
                        </div>
                    </div> 
                   
                   <!--GROUP 4-->
                    <div class="painel">
                        <div class="titulo-table">Prevention and Blocking Attacks</div>
                        <table class="table table-bordered tableGroup group">
                            <thead class="thead-inverse">
                              <tr>
                                <th>Item</th>
                                <th>Desciption</th>
                                <th>Avaliation</th>
                              </tr>
                            </thead>
                            <tbody>
                              <tr>
                                <th scope="row">1</th>
                                <td>Login Failures</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                              <tr>
                                <th scope="row">2</th>
                                <td >Default Port</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/bad.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>  
                              </tr>
                            </tbody>
                        </table>
                        <div class="group graph">
                            <div id="preview">
                                <canvas width=220 height=70 class="canvas-preview" id="graph4"></canvas>
                                <div class="preview-textfield" id="preview4"></div>
                            </div>
                        </div>
                    </div>                         
                   
                   <!--GROUP 5-->
                   <div class="painel">
                       <div class="titulo-table">Encryption, Tokenization and Data Masking</div> 
                       <table class="table table-bordered tableGroup group">
                            <thead class="thead-inverse">
                              <tr>
                                <th>Item</th>
                                <th>Desciption</th>
                                <th>Avaliation</th>
                              </tr>
                            </thead>
                            <tbody>
                              <tr>
                                <th scope="row">1</th>
                                <td>Number of Encrypted Objects</td>
                                <td  style="padding-bottom: 0px;">
                                    <center><img src="scripts/img/good.png" style="width:30px;height:55px;padding-top: 25px;"></center>
                                </td>
                              </tr>
                            </tbody>
                        </table>
                        <div class="group graph">
                            <div id="preview">
                                <canvas width=220 height=70 class="canvas-preview" id="graph5"></canvas>
                                <div class="preview-textfield" id="preview5"></div>
                            </div>
                        </div>
                    </div>
                   <div id="gresults">
                        <div id="pie-chart" style="position: relative; min-width: 310px; height: 400px; max-width: 600px;"></div>
                        <div id="bar-chart" style="position: relative; min-width: 310px; height: 400px;"></div>
                        <div id="line-chart" style="position: relative; min-width: 310px; height: 400px;"></div>
                   </div>
               <!--           
                        <div class="groups">
                            <div class="list-group group">
                                <a href="#" class="list-group-item active">Assessment of Users and Permissions</a>
                                <a href="#" class="list-group-item">Sysadmins Users</a>
                                <a href="#" class="list-group-item">db_owner Users</a>
                                <a href="#" class="list-group-item">SA User</a>
                                <a href="#" class="list-group-item">Guest User</a>
                                <a href="#" class="list-group-item">Logins without Permissions</a>
                                <a href="#" class="list-group-item">Users without Login</a>
                            </div>
                            <div class="list-group group">
                                <a href="#" class="list-group-item active">Monitoring and Auditing</a>
                                <a href="#" class="list-group-item">Number of Event Logs</a>
                                <a href="#" class="list-group-item">Notifications about Events</a>
                                <a href="#" class="list-group-item">db_owner Logins</a>
                            </div>
                        </div>
                        <div class="groups">    
                            <div class="list-group group">
                                <a href="#" class="list-group-item active">Vulnerability and Configuration Management</a>
                                <a href="#" class="list-group-item">Administrators Group</a>
                                <a href="#" class="list-group-item">Local Administrators Group</a>
                                <a href="#" class="list-group-item">Password Expiration Policy</a>
                                <a href="#" class="list-group-item">Example Databases</a>
                                <a href="#" class="list-group-item">Authentication Mode</a>
                                <a href="#" class="list-group-item">Enabled Network Protocols</a>
                                <a href="#" class="list-group-item">Valid Backups</a>
                                <a href="#" class="list-group-item">Current Security Patches</a>
                                <a href="#" class="list-group-item">Number of Views</a>
                            </div>
                        </div>
                        <div class="groups">
                            <div class="list-group group">
                                <a href="#" class="list-group-item active">Prevention and Blocking Attacks</a>
                                <a href="#" class="list-group-item">Login Failures</a>
                                <a href="#" class="list-group-item">Default Port</a>
                            </div>
                            <div class="list-group group">
                                <a href="#" class="list-group-item active">Encryption, Tokenization and Data Masking</a>
                                <a href="#" class="list-group-item">Não temos nada aqui ainda</a>
                            </div>   
                        </div>
                </section>
            </div>
          <div  class="container"> 
                 Database Connection 
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
            </div>    -->
        </div>
    </body>
    <script src="scripts/gauge/assets/fd-slider/fd-slider.js" type="text/javascript"></script>
    <script src="scripts/gauge/dist/gauge.js" type="text/javascript"></script>
    <script src="scripts/gauge/dist/graphs.js" type="text/javascript"></script>
</html>