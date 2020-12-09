<%-- 
    Document   : login.jsp
    Created on : Oct 14, 2020, 9:22:49 AM
    Author     : Administrator
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <style>
            .login-page {
                width: 360px;
                padding: 8% 0 0;
                margin: auto;
            }
            .form {
                position: relative;
                z-index: 1;
                background: #FFFFFF;
                max-width: 360px;
                margin: 0 auto 100px;
                padding: 45px;
                text-align: center;
                box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);
            }
            .form input {
                font-family: "Roboto", sans-serif;
                outline: 0;
                background: #f2f2f2;
                width: 100%;
                border: 0;
                margin: 0 0 15px;
                padding: 15px;
                box-sizing: border-box;
                font-size: 14px;
            }
            .form .button {
                font-family: "Roboto", sans-serif;
                text-transform: uppercase;
                outline: 0;
                background: #4CAF50;
                width: 100%;
                border: 0;
                padding: 15px;
                color: #FFFFFF;
                font-size: 14px;
                -webkit-transition: all 0.3 ease;
                transition: all 0.3 ease;
                cursor: pointer;
            }
            .form .button:hover,.form button:active,.form button:focus {
                background: #43A047;
            }
            .form .message {
                margin: 15px 0 0;
                color: #b3b3b3;
                font-size: 12px;
            }
            .form .message a {
                color: #4CAF50;
                text-decoration: none;
            }
            .form .register-form {
                display: none;
            }
            .container {
                position: relative;
                z-index: 1;
                max-width: 300px;
                margin: 0 auto;
            }
            .container:before, .container:after {
                content: "";
                display: block;
                clear: both;
            }
            .container .info {
                margin: 50px auto;
                text-align: center;
            }
            .container .info h1 {
                margin: 0 0 15px;
                padding: 0;
                font-size: 36px;
                font-weight: 300;
                color: #1a1a1a;
            }
            .container .info span {
                color: #4d4d4d;
                font-size: 12px;
            }
            .container .info span a {
                color: #000000;
                text-decoration: none;
            }
            .container .info span .fa {
                color: #EF3B3A;
            }
            body {
                background: #76b852; /* fallback for old browsers */
                background: -webkit-linear-gradient(right, #76b852, #8DC26F);
                background: -moz-linear-gradient(right, #76b852, #8DC26F);
                background: -o-linear-gradient(right, #76b852, #8DC26F);
                background: linear-gradient(to left, #76b852, #8DC26F);
                font-family: "Roboto", sans-serif;
                -webkit-font-smoothing: antialiased;
                -moz-osx-font-smoothing: grayscale;      
            }
        </style>
        <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    </head>
    <body>
        <div class="login-page">
            <div class="form">
                <form id="yourFormId" class="login-form" action="login" method="POST">
                    <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                    <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                    <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                    <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                    <input type="hidden" name="txtAmount" value="${param.txtAmount}" />
                    <input type="text" placeholder="email" name="txtUserID" value="${param.txtUserID}"/>
                    <input type="password" placeholder="password" name="txtPassword" value=""/>
                    <div class="g-recaptcha" data-sitekey="6LevZd4ZAAAAANnLj68ltJo1DAG7aTm1GFDpeCIZ"></div>
                    <br/>
                    <input class="button" type="button" onclick="checkCaptcha()" value="Login" name="btnAction"/>
                </form>
                <c:url var="registerLink" value="registerPage">
                    <c:param name="txtNameSearch" value="${param.txtNameSearch}" />
                    <c:param name="cmbArea" value="${param.cmbArea}" />
                    <c:param name="txtCheckInDate" value="${param.txtCheckInDate}" />
                    <c:param name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                    <c:param name="txtAmount" value="${param.txtAmount}" />
                </c:url>
                <p class="message">Not registered? <a href="${registerLink}">Create an account</a></p>
            </div>

        </div>
    </body>
</html>
<script>
    function checkCaptcha() {
        if (!grecaptcha.getResponse()) {
            alert("You need to prove that you're not a robot");
        } else {
            document.getElementById('yourFormId').submit();
        }
    }
</script>
