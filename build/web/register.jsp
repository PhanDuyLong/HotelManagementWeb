<%-- 
    Document   : register
    Created on : Nov 3, 2020, 9:40:40 AM
    Author     : Administrator
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
        <title>Register Page</title>
    </head>
    <body>
        <c:set var="errors" value="${requestScope.ERRORS}"/>
        <div class="container">
            <h1>Register</h1>
            <form class="form-horizontal" action="create" method="POST">
                <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                <input type="hidden" name="txtAmount" value="${param.txtAmount}" />
                <div class="form-group">
                    <label class="control-label col-sm-2">Email:</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="txtUserID" value="${param.txtUserID}">
                        <c:if test="${not empty errors.userID}">
                            <font color="red">
                            ${errors.userID}<br/>
                            </font>
                        </c:if>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2">Password:</label>
                    <div class="col-sm-10">
                        <input type="password" class="form-control" name="txtPassword" value="">
                        <c:if test="${not empty errors.password}">
                            <font color="red">
                            ${errors.password}<br/>
                            </font>
                        </c:if>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2">Confirm:</label>
                    <div class="col-sm-10">
                        <input type="password" class="form-control" name="txtConfirmPassword" value="">
                        <c:if test="${not empty errors.confirmPassword}">
                            <font color="red">
                            ${errors.confirmPassword}<br/>
                            </font>
                        </c:if>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2">Name:</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="txtName" value="">
                        <c:if test="${not empty errors.name}">
                            <font color="red">
                            ${errors.name}<br/>
                            </font>
                        </c:if>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2">Address:</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="txtAddress" value="${param.txtAddress}">
                        <c:if test="${not empty errors.address}">
                            <font color="red">
                            ${errors.address}<br/>
                            </font>
                        </c:if>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2">Phone:
                        (XXXXXXXXXX, X for digit) </label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="txtPhone" value="${param.txtPhone}">
                        <c:if test="${not empty errors.phone}">
                            <font color="red">
                            ${errors.phone}<br/>
                            </font>
                        </c:if>
                    </div>
                </div>
                <div class="form-group">        
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-primary">Confirm</button>
                        <input type="reset" class="btn btn-secondary" value="Reset" />
                        <c:url var="loginLink" value="loginPage">
                            <c:param name="txtNameSearch" value="${param.txtNameSearch}"/>
                            <c:param name="cmbArea" value="${param.cmbArea}"/>
                            <c:param name="txtCheckInDate" value="${param.txtCheckInDate}"/>
                            <c:param name="txtCheckOutDate" value="${param.txtCheckOutDate}"/>
                            <c:param name="txtAmount" value="${param.txtAmount}"/>
                        </c:url>
                        <a href="${loginLink}" class="btn btn-default">Back to Login Page</a>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
