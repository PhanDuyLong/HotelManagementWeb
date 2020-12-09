/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longpd.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import longpd.daos.TblBookingDetailsDAO;
import longpd.daos.TblBookingsDAO;
import longpd.daos.TblRoomsDAO;
import longpd.dtos.CartDTO;
import longpd.dtos.ErrorTblBookingDetails;
import longpd.dtos.ErrorTblHotels;
import longpd.dtos.TblBookingDetailsDTO;
import longpd.dtos.TblRoomsDTO;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "CheckCartController", urlPatterns = {"/CheckCartController"})
public class CheckCartController extends HttpServlet {

    private static final String CHECKOUT_PAGE = "checkoutPage";
    private static final String VIEW_CART_PAGE = "viewCart";
    private static final String CHECKOUT_CONTROLLER = "checkout";
    private static final String CHECK_DISCOUNT_CODE_CONTROLLER = "checkCode";
    private org.apache.log4j.Logger logger = null;

    public void initLog() {
        logger = org.apache.log4j.Logger.getLogger(CheckCartController.class.getName());
        BasicConfigurator.configure();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ServletContext context = request.getServletContext();
        ResourceBundle siteMap = (ResourceBundle) context.getAttribute("SITE_MAP");
        String url = siteMap.getString(VIEW_CART_PAGE);
        boolean foundError = false;
        try {
            initLog();
            HttpSession session = request.getSession();
            CartDTO cart = (CartDTO) session.getAttribute("CART");
            if (cart != null && cart.getCart() != null && !cart.getCart().isEmpty()) {
                String checkInDateString = request.getParameter("txtCartCheckInDate");
                String checkOutDateString = request.getParameter("txtCartCheckOutDate");
                ErrorTblHotels dateErrors = new ErrorTblHotels();
                Date checkInDate = null;
                Date checkOutDate = null;
                Date present = new Date();
                if (checkInDateString == null || checkInDateString.isEmpty()) {
                    dateErrors.setCheckInDate("Check-in date mustn't be empty");
                    foundError = true;
                } else {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        format.setLenient(false);
                        checkInDate = format.parse(checkInDateString);
                    } catch (Exception ex) {
                        dateErrors.setCheckInDate("Format of date must be yyyy-MM-dd");
                        foundError = true;
                    }
                    if (checkInDate != null && checkInDate.compareTo(present) <= 0) {
                        dateErrors.setCheckInDate("Check-in date must be after today");
                        foundError = true;
                    }
                }
                if (checkOutDateString == null || checkOutDateString.isEmpty()) {
                    dateErrors.setCheckOutDate("Check-out date mustn't be empty");
                    foundError = true;
                } else {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        format.setLenient(false);
                        checkOutDate = format.parse(checkOutDateString);
                    } catch (Exception ex) {
                        dateErrors.setCheckOutDate("Format of date must be yyyy-MM-dd");
                        foundError = true;
                    }
                    if (checkOutDate != null && checkOutDate.compareTo(present) < 0) {
                        dateErrors.setCheckOutDate("Check-out date must be after today");
                        foundError = true;
                    }
                }
                if (dateErrors.getCheckInDate() == null && dateErrors.getCheckOutDate() == null) {
                    if (checkInDate.compareTo(checkOutDate) >= 0) {
                        dateErrors.setCheckOutDate("Check-out date must be after check-in date");
                        foundError = true;
                    }
                }
                if (!foundError) {
                    long bookingDay = (checkOutDate.getTime() - checkInDate.getTime()) / (24 * 60 * 60 * 1000);
                    session.setAttribute("BOOKING_DAY", bookingDay);
                    Map<TblRoomsDTO, ErrorTblBookingDetails> errorMap = new HashMap<>();
                    TblRoomsDAO roomDao = new TblRoomsDAO();
                    TblBookingDetailsDAO detailDao = new TblBookingDetailsDAO();
                    TblBookingsDAO bookingDao = new TblBookingsDAO();
                    Map<String, String> typeMap = (Map<String, String>) session.getAttribute("TYPE_MAP");
                    Map<Integer, String> hotelMap = (Map<Integer, String>) session.getAttribute("HOTEL_MAP");
                    for (int id : cart.getCart().keySet()) {
                        TblRoomsDTO item = cart.getCart().get(id);
                        foundError = false;
                        String roomType = typeMap.get(item.getTypeID());
                        String hotelName = hotelMap.get(item.getHotelID());
                        ErrorTblBookingDetails error = new ErrorTblBookingDetails();
                        TblRoomsDTO room = roomDao.getRoomByID(id);
                        if (room == null) {
                            error.setRoomID("Room " + roomType + " of " + hotelName + " is not found");
                            room = new TblRoomsDTO();
                            cart.delete(id);
                            foundError = true;
                        } else {
                            int availableAmount = room.getAmount();
                            int addedAmount = item.getAmount();
                            List<TblBookingDetailsDTO> detailList = detailDao.getDetailsByRoomID(room.getRoomID());
                            if (detailList != null && !detailList.isEmpty()) {
                                for (TblBookingDetailsDTO detail : detailList) {
                                    boolean isBookingActive = bookingDao.checkBookingByIDAndDates(detail.getBookingID(), checkInDate, checkOutDate);
                                    if (isBookingActive) {
                                        availableAmount = availableAmount - detail.getAmount();
                                    }
                                }
                            }
                            if (availableAmount <= 0) {
                                error.setAmount("Room " + roomType + " of " + hotelName + " is not available");
                                room = new TblRoomsDTO();
                                cart.delete(id);
                                foundError = true;
                            } else if (availableAmount < addedAmount) {
                                error.setAmount("Room " + roomType + " of " + hotelName + " has only " + availableAmount + " room(s) left");
                                item.setAmount(availableAmount);
                                cart.update(id, item);
                                foundError = true;
                            }
                        }
                        if (foundError) {
                            errorMap.put(room, error);
                        }
                    }
                    if (errorMap.isEmpty()) {
                        String action = request.getParameter("btnAction");
                        if (action != null && !action.isEmpty()) {
                            if (action.equals("Checkout")) {
                                String cartCheckInDateString = (String) session.getAttribute("CART_CHECK_IN_DATE");
                                String cartCheckOutDateString = (String) session.getAttribute("CART_CHECK_OUT_DATE");
                                Date cartCheckInDate = null;
                                Date cartCheckOutDate = null;
                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    format.setLenient(false);
                                    cartCheckInDate = format.parse(cartCheckInDateString);
                                } catch (Exception ex) {
                                }
                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    format.setLenient(false);
                                    cartCheckOutDate = format.parse(cartCheckOutDateString);
                                } catch (Exception ex) {
                                }
                                if (checkInDate.compareTo(cartCheckInDate) != 0 || checkOutDate.compareTo(cartCheckOutDate) != 0) {
                                    url = siteMap.getString(VIEW_CART_PAGE);
                                } else {
                                    url = siteMap.getString(CHECKOUT_PAGE);
                                }

                            } else if (action.equals("Confirm")) {
                                url = siteMap.getString(CHECKOUT_CONTROLLER);
                            } else if (action.equals("Check Code")) {
                                url = siteMap.getString(CHECK_DISCOUNT_CODE_CONTROLLER);
                            }
                            session.setAttribute("CART_CHECK_IN_DATE", checkInDateString);
                            session.setAttribute("CART_CHECK_OUT_DATE", checkOutDateString);
                        }
                    } else {
                        request.setAttribute("ERROR_MAP", errorMap);
                    }
                } else {
                    request.setAttribute("DATE_ERROR", dateErrors);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
        } catch (NamingException ex) {
            logger.error(ex);
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
