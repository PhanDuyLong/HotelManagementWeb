<%-- 
    Document   : viewCart
    Created on : Aug 26, 2020, 8:52:16 AM
    Author     : HP
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>View Cart</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container">
            <c:set var="errorMap" value="${requestScope.ERROR_MAP}"/>
            <c:set var="dateErrors" value="${requestScope.DATE_ERROR}"/>
            <c:set var="bookingDay" value="${sessionScope.BOOKING_DAY}"/>
            <c:if test="${not empty errorMap}">
                <c:forEach var="error" items="${errorMap}">
                    <c:if test="${error.key.roomID == 0}">
                        <c:if test="${not empty error.value.roomID}">
                            <div class="alert alert-danger alert-dismissible">
                                <a class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                ${error.value.roomID}
                            </div>
                        </c:if>
                        <c:if test="${not empty error.value.amount}">
                            <div class="alert alert-danger alert-dismissible">
                                <a class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                ${error.value.amount}
                            </div>
                        </c:if>
                    </c:if>
                </c:forEach>
            </c:if>
            <c:set var="cart" value="${sessionScope.CART}"/>
            <c:if test="${not empty cart && cart.amount() > 0}" var="checkCart">
                <h2>Shopping Cart(${cart.amount()} items)</h2>
                <table class="table">
                    <thead>
                        <tr class="success">
                            <th>Hotel's Name</th>
                            <th>Room's Type</th>
                            <th>Price</th>
                            <th>Amount</th>
                            <th>Total</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="totalPrice" value="0"/>
                        <c:set var="errors" value="${requestScope.ERRORS}"/>
                        <c:set var="hotelMap" value="${sessionScope.HOTEL_MAP}"/>
                        <c:set var="types" value="${sessionScope.TYPE_MAP}"/>
                        <c:forEach var="item" items="${cart.cart}"> 
                        <form action="updateItem">
                            <tr>
                                <td>
                                    <input type="hidden" name="txtRoomID" value="${item.key}" />
                                    <input type="hidden" name="txtRoomType" value="${types[item.value.typeID]}" />
                                    <input type="hidden" name="txtHotelName" value="${hotelMap[item.value.hotelID]}" />
                                    <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                                    <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                                    <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                                    <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                                    <input type="hidden" name="txtAmount" value="${param.txtAmount}"/>
                                    ${hotelMap[item.value.hotelID]}
                                </td>
                                <td>
                                    ${types[item.value.typeID]}
                                </td>
                                <td>
                                    ${item.value.price}
                                </td>
                                <td>
                                    <button type="submit" class="btn btn-default btn-sm" name="btnAction" value="Minus" 
                                            <c:if test="${item.value.amount eq 1}">
                                                disabled="true"
                                            </c:if> >
                                        <span class="glyphicon glyphicon-minus"></span>
                                    </button>
                                    ${item.value.amount}
                                    <button type="submit" class="btn btn-default btn-sm" name="btnAction" value="Plus">
                                        <span class="glyphicon glyphicon-plus"></span>
                                    </button>
                                </td>
                                <td>
                                    ${item.value.price * item.value.amount}
                                    <c:set var="totalPrice" value="${totalPrice + item.value.price * item.value.amount}"/>
                                </td>
                                <td>
                                    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#_${item.key}" >
                                        Remove
                                    </button>
                                </td>
                            </tr>
                        </form>
                        <div class="modal" id="_${item.key}">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h4 class="modal-title">Confirm Dialog</h4>
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    </div>
                                    <div class="modal-body">
                                        Do you want to remove this item?
                                    </div>
                                    <div class="modal-footer justify-content-center">
                                        <form action="updateItem">
                                            <input type="hidden" name="txtRoomID" value="${item.key}"/>
                                            <input type="hidden" name="txtRoomType" value="${types[item.value.typeID]}"/>
                                            <input type="hidden" name="txtHotelName" value="${hotelMap[item.value.hotelID]}"/>
                                            <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}"/>
                                            <input type="hidden" name="cmbArea" value="${param.cmbArea}"/>
                                            <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}"/>
                                            <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}"/>
                                            <input type="hidden" name="txtAmount" value="${param.txtAmount}"/>
                                            <input type="submit" class="btn btn-primary" value="Remove item" name="btnAction"/>
                                        </form>
                                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <c:if test="${not empty errorMap}">
                            <c:forEach var="error" items="${errorMap}">
                                <c:if test="${error.key.roomID eq item.key}">
                                    <tr>
                                        <td colspan="6">
                                            <c:if test="${not empty error.value.amount}">
                                                <div class="alert alert-danger alert-dismissible">
                                                    <a class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                                    ${error.value.amount}
                                                </div>
                                            </c:if>
                                            <c:if test="${not empty error.value.price}">
                                                <div class="alert alert-danger alert-dismissible">
                                                    <a class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                                    ${error.value.price}
                                                </div>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </c:forEach>
                    <tr class="info">
                        <td></td>
                        <td></td>
                        <td>Total Price</td>
                        <td>${totalPrice} x ${bookingDay} day(s)</td>
                        <td>
                            ${totalPrice * bookingDay}
                        </td>
                        <c:set var="totalPrice" value="${totalPrice * bookingDay}"/>
                        <td>
                            <form action="search">
                                <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                                <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                                <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                                <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                                <input type="hidden" name="txtAmount" value="${param.txtAmount}"/>
                                <input type="submit" class="btn btn-default" value="Add more items" />
                            </form>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div class="container justify-content-center" style="text-align: center">
                    <form class="form-inline" action="checkCart">
                        <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                        <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                        <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                        <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                        <input type="hidden" name="txtAmount" value="${param.txtAmount}"/>
                        <input type="hidden" name="txtTotalPrice" value="${totalPrice}" />
                        <div class="form-group col-md-4">
                            <label>Check-in date</label>
                            <input type="text" class="form-control" name="txtCartCheckInDate" value="${sessionScope.CART_CHECK_IN_DATE}">
                        </div>
                        <div class="form-group col-md-4">
                            <label>Check-out date</label>
                            <input type="text" class="form-control" name="txtCartCheckOutDate" value="${sessionScope.CART_CHECK_OUT_DATE}">
                        </div>
                        <input type="submit" class="btn btn-danger" style="text-align: center" name="btnAction" value="Update" />
                        <c:if test="${not empty dateErrors.checkInDate}">
                            <div class="alert alert-danger alert-dismissible">
                                <a class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                ${dateErrors.checkInDate}
                            </div>
                        </c:if>
                        <c:if test="${not empty dateErrors.checkOutDate}">
                            <div class="alert alert-danger alert-dismissible">
                                <a class="close" data-dismiss="alert" aria-label="close">&times;</a>
                                ${dateErrors.checkOutDate}
                            </div>
                        </c:if>
                        <div class="form-inline">
                            <input type="submit" class="btn btn-primary" style="text-align: center" name="btnAction" value="Checkout"/>
                        </div>
                    </form>
                </div>
            </c:if>
            <c:if test="${!checkCart}">
                <h2>You haven't selected any thing!!!!</h2>
                <form action="search">
                    <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                    <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                    <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                    <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                    <input type="hidden" name="txtAmount" value="${param.txtAmount}"/>
                    <input type="submit" class="btn btn-default" value="Add item" />
                </form>
            </c:if>
        </div>
    </body>
</html>
