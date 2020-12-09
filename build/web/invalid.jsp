<%-- 
    Document   : invalid.jsp
    Created on : Oct 18, 2020, 9:22:37 AM
    Author     : Administrator
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
        <title>Invalid Page</title>
    </head>
    <body>
        <h2>
            <font color="red">
            ACCOUNT IS NOT FOUND!!!
            </font>
            <c:url var="loginLink" value="loginPage">
                <c:param name="txtNameSearch" value="${param.txtNameSearch}" />
                <c:param name="cmbArea" value="${param.cmbArea}" />
                <c:param name="txtCheckInDate" value="${param.txtCheckInDate}" />
                <c:param name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                <c:param name="txtAmount" value="${param.txtAmount}" />
                <c:param name="txtUserID" value="${param.txtUserID}"/>
            </c:url>
            <a href="${loginLink}">Reset again</a>
        </h2>
</html>
