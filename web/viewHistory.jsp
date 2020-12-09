<%-- 
    Document   : track
    Created on : Oct 18, 2020, 7:30:36 PM
    Author     : Administrator
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Booking's History Page</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </head>
    <body>
        <c:set var="bookingMap" value="${requestScope.BOOKING_MAP}"/>
        <c:set var="searchError" value="${requestScope.SEARCH_ERROR}"/>
        <div class="container">
            <h2>Booking History</h2>
            <form class="form-horizontal" action="searchHistory">
                <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                <input type="hidden" name="txtAmount" value="${param.txtAmount}" />
                <div class="form-group">
                    <label class="control-label col-sm-2">Hotel name:</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="txtHotelName" value="${param.txtHotelName}">
                    </div>
                    <c:if test="${not empty searchError.hotelName}">
                        <font color="red">
                        ${searchError.hotelName}<br/>
                        </font>
                    </c:if>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2">Booking date:</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="txtBookingDate" value="${param.txtBookingDate}">
                    </div>
                    <c:if test="${not empty searchError.checkInDate}">
                        <font color="red">
                        ${searchError.checkInDate}<br/>
                        </font>
                    </c:if>
                </div>
                <div class="form-group">        
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-primary">Search</button>
                        <c:url var="searchLink" value="search">
                            <c:param name="txtNameSearch" value="${param.txtNameSearch}" />
                            <c:param name="cmbArea" value="${param.cmbArea}" />
                            <c:param name="txtCheckInDate" value="${param.txtCheckInDate}" />
                            <c:param name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                            <c:param name="txtAmount" value="${param.txtAmount}"/>
                        </c:url>
                        <a href="${searchLink}" class="btn btn-default">Back to Main Page</a>
                    </div>
                </div>
            </form>
            <c:if test="${empty searchError && param.txtHotelName != null}">
                <c:if test="${not empty bookingMap}" var="checkOrder">
                    <c:forEach var="ele" items="${bookingMap}">
                        <div class="container mt-3">
                            <div class="media border-bottom">
                                <div class="media-body">
                                    <h3>Booking's ID: ${ele.key.bookingID}</h3>
                                    <p class="text-muted">${ele.key.bookingDate} - ${ele.key.phone}</p>
                                    <p class="text-primary">From ${ele.key.checkInDate} to ${ele.key.checkOutDate}</p>
                                    <p class="text-danger">${ele.key.status}</p>
                                    <table class="table">
                                        <thead>
                                            <tr class="success">
                                                <th>Hotel's Name</th>
                                                <th>Room's Type</th>
                                                <th>Price</th>
                                                <th>Amount</th>
                                                <th>Total</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:set var="hotelMap" value="${requestScope.HOTEL_MAP}"/>
                                            <c:set var="types" value="${requestScope.TYPE_MAP}"/>
                                            <c:set var="codeMap" value="${requestScope.CODE_MAP}"/>
                                            <c:set var="dayMap" value="${requestScope.DAY_MAP}"/>
                                            <c:forEach var="detail" items="${ele.value}"> 
                                                <tr>
                                                    <td>
                                                        ${hotelMap[detail.value.hotelID]}
                                                    </td>
                                                    <td>
                                                        ${types[detail.value.typeID]}
                                                    </td>
                                                    <td>
                                                        ${detail.key.price}
                                                    </td>
                                                    <td>
                                                        ${detail.key.amount}
                                                    </td>
                                                    <td>
                                                        ${detail.key.price * detail.key.amount}
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <tr class="info">
                                                <td></td>
                                                <td></td>
                                                <td>Total Price</td>
                                                <td>${ele.key.totalPrice} x ${dayMap[ele.key.bookingID]} day(s)</td>
                                                <td>
                                                    ${ele.key.totalPrice * dayMap[ele.key.bookingID]}
                                                    <c:set var="totalPrice" value="${ele.key.totalPrice * dayMap[ele.key.bookingID]}"/>
                                                    <c:if test="${not empty codeMap && not empty codeMap[ele.key.bookingID]}">
                                                        <br/>
                                                        Using <font color="red">${codeMap[ele.key.bookingID].codeName}: -${codeMap[ele.key.bookingID].discountPercent}%<br/>
                                                        After discount: ${ele.key.totalPrice * dayMap[ele.key.bookingID] - ele.key.totalPrice * dayMap[ele.key.bookingID] * codeMap[ele.key.bookingID].discountPercent / 100}
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#_${ele.key.bookingID}">
                                        Delete
                                    </button>
                                    <div class="modal" id="_${ele.key.bookingID}">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h4 class="modal-title">Confirm Dialog</h4>
                                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                </div>
                                                <div class="modal-body">
                                                    Do you want to delete history of this booking?
                                                </div>
                                                <div class="modal-footer">
                                                    <form action="deleteHistory">
                                                        <input type="hidden" name="txtBookingID" value="${ele.key.bookingID}"/>
                                                        <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}"/>
                                                        <input type="hidden" name="cmbArea" value="${param.cmbArea}"/>
                                                        <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}"/>
                                                        <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}"/>
                                                        <input type="hidden" name="txtAmount" value="${param.txtAmount}"/>
                                                         <input type="hidden" name="txtHotelName" value="${param.txtHotelName}"/>
                                                        <input type="hidden" name="txtBookingDate" value="${param.txtBookingDate}"/>
                                                        <input type="submit" class="btn btn-primary" value="Delete" name="btnAction"/>
                                                    </form>
                                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>
                <c:if test="${!checkOrder}">
                    <h1>NOT FOUND</h1>
                </c:if>
            </c:if>
        </div>
    </body>
</html>
