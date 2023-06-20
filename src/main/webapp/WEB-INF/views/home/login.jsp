<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>HIMS SF - Login</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="Hardware Management System">
        <meta name="author" content="FTC">
        <link href="${contextPath}/resources/public/css/bootstrap.min.css" rel="stylesheet">
        <link href="${contextPath}/resources/public/css/bootstrap-responsive.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${contextPath}/resources/public/css/typica-login.css">
        <link rel="shortcut icon" href="${contextPath}/resources/img/warehouse.ico">
    </head>

    <body>
        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>
                    <center style="margin-top: 30px;"><a href="${contextPath}/" style="font-family: 'Lato'; font-size: 22px;">Hardware Inventory Management System (HIMS) - Seremban Factory</a></center>
                </div>
            </div>
        </div>

        <div class="container">
            <div id="login-wraper">
                <form action="${contextPath}/" method="post" class="form login-form">
                    <a href="${contextPath}/">
                        <img src="${contextPath}/resources/img/warehouse.png" alt="FTC" width="35%" />
                    </a>
                    <legend>Sign in to <span class="blue">HIMS SF</span></legend>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">
                            <a class="close" data-dismiss="alert" href="#" aria-hidden="true">&times;</a>
                            <strong>${error}</strong>
                        </div>
                    </c:if>
                    <div class="body">
                        <label>Username</label>
                        <input type="text" name="username">

                        <label>Password</label>
                        <input type="password" name="password">
                    </div>
                    <div class="footer">
                        <button type="submit" class="btn btn-success">Login</button>
                    </div>
                </form>
            </div>
        </div>

        <footer class="white navbar-fixed-bottom"></footer>

        <script src="${contextPath}/resources/public/js/jquery.js"></script>
        <script src="${contextPath}/resources/public/js/bootstrap.js"></script>
        <script src="${contextPath}/resources/public/js/backstretch.min.js"></script>
        <script src="${contextPath}/resources/public/js/typica-login.js"></script>
    </body>
</html>