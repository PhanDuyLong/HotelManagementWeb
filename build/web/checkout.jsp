<%-- 
    Document   : checkOut
    Created on : Oct 18, 2020, 8:49:55 AM
    Author     : Administrator
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Checkout Page</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container">
            <h2>Checkout</h2>
            <c:set var="errors" value="${requestScope.ERRORS}"/>
            <c:set var="codeError" value="${requestScope.CODE_ERROR}"/>
            <c:set var="user" value="${sessionScope.USER}"/>
            <c:set var="phone" value="${param.txtPhone}"/>
            <c:set var="code" value="${sessionScope.CODE}"/>
            <c:if test="${phone == null}">
                <c:set var="phone" value="${user.phone}"/>
            </c:if>
            <c:set var="email" value="${param.txtEmail}"/>
            <c:if test="${email == null}">
                <c:set var="email" value="${user.userID}"/>
            </c:if>
            <div class="container">
                <label class="control-label col-sm-3">Total Price:</label>
                <label class="control-label"> ${param.txtTotalPrice}</label>
            </div>
            <form class="form-horizontal" action="checkCart" method="POST">
                <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                <input type="hidden" name="txtAmount" value="${param.txtAmount}" />
                <input type="hidden" name="txtTotalPrice" value="${param.txtTotalPrice}" />
                <input type="hidden" name="txtCartCheckInDate" value="${param.txtCartCheckInDate}">
                <input type="hidden" name="txtCartCheckOutDate" value="${param.txtCartCheckOutDate}">
                <div class="form-inline">
                    <label class="control-label col-sm-2">Discount code:</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="txtCodeID" value="${param.txtCodeID}">
                        <button type="submit" class="btn btn-primary" name="btnAction" value="Check Code">Use</button>
                    </div>
                </div>
            </form>
            <c:if test="${not empty codeError}">
                <font color="red">
                ${codeError.codeID}<br/>
                </font>
            </c:if>
            <c:if test="${not empty code}">
                <div>
                    <label class="control-label col-sm-3">Using <font color="red">${code.codeName}:</font></label>
                    <label class="control-label">-${code.discountPercent}%</label>
                </div>
                <div>
                    <label class="control-label col-sm-3">After discount:</label>
                    <label class="control-label">${param.txtTotalPrice - param.txtTotalPrice * code.discountPercent / 100}</label>
                </div>
            </c:if>
            <form class="form-horizontal" action="checkCart" method="POST">
                <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                <input type="hidden" name="txtAmount" value="${param.txtAmount}" />
                <input type="hidden" name="txtTotalPrice" value="${param.txtTotalPrice}" />
                <input type="hidden" name="txtCartCheckInDate" value="${param.txtCartCheckInDate}">
                <input type="hidden" name="txtCartCheckOutDate" value="${param.txtCartCheckOutDate}">
                <div class="form-group">
                    <label class="control-label col-sm-2">Email:</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="txtEmail" value="${email}">
                        <c:if test="${not empty errors.email}">
                            <font color="red">
                            ${errors.email}<br/>
                            </font>
                        </c:if>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2">Phone:
                        (XXXXXXXXXX, X for digit) </label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="txtPhone" value="${phone}">
                        <c:if test="${not empty errors.phone}">
                            <font color="red">
                            ${errors.phone}<br/>
                            </font>
                        </c:if>
                    </div>
                </div>

                <div class="form-group">        
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-primary" name="btnAction" value="Confirm">Confirm</button>
                        <input type="reset" class="btn btn-secondary" value="Reset" />
                        <c:url var="viewCartLink" value="viewCart">
                            <c:param name="txtNameSearch" value="${param.txtNameSearch}" />
                            <c:param name="cmbArea" value="${param.cmbArea}" />
                            <c:param name="txtCheckInDate" value="${param.txtCheckInDate}" />
                            <c:param name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                            <c:param name="txtAmount" value="${param.txtAmount}" />
                        </c:url>
                        <a href="${viewCartLink}" class="btn btn-default">Back to Shopping Cart</a>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
