<%-- 
    Document   : shopping
    Created on : Nov 2, 2020, 8:28:22 AM
    Author     : Administrator
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <title>Main Page</title>
    </head>
    <body>
        <c:set var="roomError" value="${requestScope.ROOM_ERROR}"/>
        <c:set var="user" value="${sessionScope.USER}" />
        <c:set var="role" value="${sessionScope.ROLE}" /> 
        <c:set var="areas" value="${sessionScope.AREA_LIST}" />
        <c:set var="roomMap" value="${requestScope.ROOM_MAP}"/>
        <c:set var="types" value="${sessionScope.TYPE_MAP}" />
        <c:set var="cart" value="${sessionScope.CART}"/>
        <c:set var="searchError" value="${requestScope.SEARCH_ERROR}"/>
        <c:set var="message" value="${requestScope.MESSAGE}"/>
        <nav class="navbar navbar-expand-sm bg-dark navbar-dark fixed-top">
            <c:if test="${not empty user}">
                <span class="navbar-text mr-sm-2">
                    Welcome, ${sessionScope.USER.name}
                </span>
            </c:if>
            <ul class="navbar-nav mr-auto">
                <c:if test="${not empty user}" var="checkUser">
                    <li class="nav-item">
                        <form action="logout">
                            <button class="btn btn-secondary mr-sm-2" type="submit" value="Logout" name="btnAction">
                                Logout
                            </button>
                        </form>
                    </li>
                </c:if>
                <c:if test="${!checkUser}">
                    <li class="nav-item">
                        <form action="loginPage">
                            <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                            <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                            <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                            <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                            <input type="hidden" name="txtAmount" value="${param.txtAmount}" />
                            <button class="btn btn-info mr-sm-2" type="submit" value="Login" name="btnAction">
                                Login
                            </button>
                        </form>
                    </li>
                </c:if>
                <c:if test="${role eq 'Member'}" var="isMember">
                    <li class="nav-item">
                        <form action="loadHotel">
                            <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                            <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                            <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                            <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                            <input type="hidden" name="txtAmount" value="${param.txtAmount}" />
                            <button class="btn btn-danger mr-sm-2" type="submit" value="viewCart" name="btnAction">
                                <c:if test="${cart != null && cart.amount() > 0}" var="checkCart">
                                    Cart(${cart.amount()})
                                </c:if>
                                <c:if test="${!checkCart}">
                                    Cart
                                </c:if>
                            </button>
                        </form>
                    </li>
                    <li class="nav-item">
                        <form action="viewHistory">
                            <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                            <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                            <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                            <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                            <input type="hidden" name="txtAmount" value="${param.txtAmount}" />
                            <button class="btn btn-info mr-sm-2" type="submit" value="viewHistory" name="btnAction">
                                View history
                            </button>
                        </form>
                    </li>
                </c:if>
            </ul>
        </nav>
        <div class="container" style="margin-top:80px">
            <h1>Welcome to Hotel Booking Web</h1>
            <form class="form-horizontal" action="search">
                <div class="form-row">
                    <div class="form-group col-md-9">
                        <label>Hotel name</label>
                        <input type="text" class="form-control" name="txtNameSearch" value="${param.txtNameSearch}"/>
                    </div>
                    <div class="form-group col-md-3">
                        <label>Area</label>
                        <select name="cmbArea" class="form-control">
                            <option>----</option>
                            <c:forEach var="area" items="${areas}">
                                <option
                                    <c:if test="${area.areaName eq param.cmbArea}">
                                        selected="selected"
                                    </c:if>
                                    >${area.areaName}</option>     
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group col-md-5">
                        <label>Check-in date</label>
                        <input type="text" class="form-control" name="txtCheckInDate" value="${param.txtCheckInDate}">
                        <c:if test="${not empty searchError.checkInDate}">
                            <font color="red">
                            ${searchError.checkInDate}<br/>
                            </font>
                        </c:if>
                    </div>
                    <div class="form-group col-md-5">
                        <label>Check-out date</label>
                        <input type="text" class="form-control" name="txtCheckOutDate" value="${param.txtCheckOutDate}">
                        <c:if test="${not empty searchError.checkOutDate}">
                            <font color="red">
                            ${searchError.checkOutDate}<br/>
                            </font>
                        </c:if>
                    </div>
                    <div class="form-group col-md-2">
                        <label for="inputZip">Amount of room</label>
                        <input type="text" class="form-control" name="txtAmount" value="${param.txtAmount}">
                        <c:if test="${not empty searchError.amountOfRoom}">
                            <font color="red">
                            ${searchError.amountOfRoom}<br/>
                            </font>
                        </c:if>
                    </div>
                </div>
                <div class="form-group">        
                    <button type="submit" class="btn btn-primary">Search</button>
                </div>
            </form>
            <c:if test="${not empty roomError}">
                <c:if test="${not empty roomError.roomID}">
                    <div class="alert alert-danger alert-dismissible">
                        <a class="close" data-dismiss="alert" aria-label="close">&times;</a>
                        ${roomError.roomID}
                    </div>
                </c:if>
                <c:if test="${not empty roomError.amount}">
                    <div class="alert alert-danger alert-dismissible">
                        <a class="close" data-dismiss="alert" aria-label="close">&times;</a>
                        ${roomError.amount}
                    </div>
                </c:if>
            </c:if>
            <c:if test="${not empty message}">
                <div class="alert alert-success alert-dismissible">
                    <a class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    ${message}
                </div>
            </c:if> 
            <c:if test="${empty searchError && not empty param.cmbArea}">
                <c:if test="${roomMap != null}" var="checkMap">
                    <c:set var="indexMap" value="${requestScope.INDEX_MAP}"/>
                    <c:forEach var="ele" items="${indexMap}">
                        <div class="card-deck">
                            <c:forEach var="item" items="${roomMap}" begin="${ele.key}" end="${ele.value}">
                                <c:set var="room" value="${item.key}"/>
                                <c:set var="hotel" value="${item.value}"/>
                                <div class="card" style="width:100%">
                                    <img class="card-img-top" src="${hotel.image}" alt="No image" style="width:100%">
                                    <div class="card-body">
                                        <h4 class="card-title">${types[room.typeID]}</h4>
                                        <p class="card-text text-primary">${room.price}</p>
                                        <p class="card-text">${hotel.hotelName}</p>
                                        <c:forEach var="area" items="${areas}">
                                            <c:if test="${area.areaID eq hotel.areaID}">
                                                <p class="card-text">Area: ${area.areaName}</p>  
                                            </c:if>
                                        </c:forEach>
                                        <div class="justify-content-center">
                                            <button type="button" class="btn btn-primary float-left" data-toggle="modal" data-target="#_${room.roomID}">
                                                See details
                                            </button>
                                            <c:if test="${isMember}" var="checkRole">
                                                <form action="addToCart">
                                                    <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                                                    <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                                                    <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                                                    <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                                                    <input type="hidden" name="txtAmount" value="${param.txtAmount}"/>
                                                    <input type="hidden" name="txtRoomID" value="${room.roomID}" />
                                                    <input type="hidden" name="txtRoomType" value="${types[room.typeID]}" />
                                                    <input type="hidden" name="txtHotelName" value="${hotel.hotelName}" />
                                                    <input type="submit" class="btn btn-danger float-right" value="Add" name="btnAction"/>
                                                </form>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach> 
                        </div>
                    </c:forEach>
                    <c:forEach var="item" items="${roomMap}">
                        <c:set var="room" value="${item.key}"/>
                        <c:set var="hotel" value="${item.value}"/>
                        <div class="modal" id="_${room.roomID}">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header justify-content-center">
                                        <h4 class="modal-title">Room's Details</h4>
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    </div>
                                    <div class="media modal-body">
                                        <img src="${hotel.image}" class="align-self-center mr-3" style="width:200px">
                                        <div class="media-body">
                                            <h4>${types[room.typeID]}</h4>
                                            <h5 class="text-primary">${room.price}</h5>
                                            <p>Hotel: ${hotel.hotelName}</p>
                                            <p>description: ${hotel.description}</p>
                                            <p>Address: ${hotel.hotelAddress}</p>
                                            <p>Phone: ${hotel.hotelPhone}</p>
                                        </div>
                                    </div>
                                    <div class="modal-footer justify-content-center">
                                        <c:if test="${isMember}">
                                            <form action="addToCart">
                                                <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}" />
                                                <input type="hidden" name="cmbArea" value="${param.cmbArea}" />
                                                <input type="hidden" name="txtCheckInDate" value="${param.txtCheckInDate}" />
                                                <input type="hidden" name="txtCheckOutDate" value="${param.txtCheckOutDate}" />
                                                <input type="hidden" name="txtAmount" value="${param.txtAmount}" />
                                                <input type="hidden" name="txtRoomID" value="${room.roomID}" />
                                                <input type="hidden" name="txtRoomType" value="${types[room.typeID]}" />
                                                <input type="hidden" name="txtHotelName" value="${hotel.hotelName}" />
                                                <input type="submit" class="btn btn-primary" value="Add to cart" name="btnAction"/>
                                            </form>
                                        </c:if>
                                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>
                <c:if test="${!checkMap}">
                    <h1>NOT FOUND</h1>
                </c:if>
            </c:if>
        </div>
    </body>
</html>
