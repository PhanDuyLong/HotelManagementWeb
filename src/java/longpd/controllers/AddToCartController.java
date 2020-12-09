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
import java.util.List;
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
import longpd.dtos.ErrorTblRooms;
import longpd.dtos.TblBookingDetailsDTO;
import longpd.dtos.TblRoomsDTO;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "AddToCartController", urlPatterns = {"/AddToCartController"})
public class AddToCartController extends HttpServlet {

    private static final String ERROR_PAGE = "error";
    private static final String SEARCH_PRODUCT_CONTROLLER = "search";
    private org.apache.log4j.Logger logger = null;

    public void initLog() {
        logger = org.apache.log4j.Logger.getLogger(AddToCartController.class.getName());
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
        String url = siteMap.getString(ERROR_PAGE);
        try {
            initLog();
            HttpSession session = request.getSession();
            ErrorTblRooms error = new ErrorTblRooms();
            boolean foundError = false;
            String idString = request.getParameter("txtRoomID");
            String amountString = request.getParameter("txtAmount");
            String roomType = request.getParameter("txtRoomType");
            String hotelName = request.getParameter("txtHotelName");
            CartDTO cart = null;
            cart = (CartDTO) session.getAttribute("CART");
            if (cart == null) {
                cart = new CartDTO();
            }
            else if(cart.getCart() == null){
                session.removeAttribute("CART_CHECK_IN_DATE");
                session.removeAttribute("CART_CHECK_OUT_DATE");
            }
            String cartCheckInDateString = (String) session.getAttribute("CART_CHECK_IN_DATE");
            String cartCheckOutDateString = (String) session.getAttribute("CART_CHECK_OUT_DATE");
            if (cartCheckInDateString == null || cartCheckInDateString.isEmpty()) {
                cartCheckInDateString = request.getParameter("txtCheckInDate");
                session.setAttribute("CART_CHECK_IN_DATE", cartCheckInDateString);
            }
            if (cartCheckOutDateString == null || cartCheckOutDateString.isEmpty()) {
                cartCheckOutDateString = request.getParameter("txtCheckOutDate");
                session.setAttribute("CART_CHECK_OUT_DATE", cartCheckOutDateString);
            }
            if (!(cartCheckInDateString).equals(request.getParameter("txtCheckInDate")) || !cartCheckOutDateString.equals(request.getParameter("txtCheckOutDate"))) {
                error.setRoomID("System only support order with same check-in date and check-out date");
                request.setAttribute("ROOM_ERROR", error);
            } else {
                Date checkInDate = null;
                Date checkOutDate = null;
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    format.setLenient(false);
                    checkInDate = format.parse(cartCheckInDateString);
                } catch (Exception ex) {
                }
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    format.setLenient(false);
                    checkOutDate = format.parse(cartCheckOutDateString);
                } catch (Exception ex) {
                }
                int id = -1;
                try {
                    id = Integer.parseInt(idString.trim());
                } catch (Exception ex) {
                }
                int amount = -1;
                try {
                    amount = Integer.parseInt(amountString.trim());
                } catch (Exception ex) {
                }
                if (id != -1 && checkOutDate != null && checkInDate != null) {
                    TblRoomsDAO roomDao = new TblRoomsDAO();
                    TblBookingDetailsDAO detailDao = new TblBookingDetailsDAO();
                    TblBookingsDAO bookingDao = new TblBookingsDAO();
                    TblRoomsDTO room = roomDao.getRoomByID(id);

                    if (room != null) {
                        int availableAmount = room.getAmount();
                        int addedAmount = 0;
                        TblRoomsDTO item = null;
                        if (cart.getCart() != null) {
                            item = cart.getCart().get(id);
                            if (item != null) {
                                addedAmount = item.getAmount();
                            }
                        }
                        List<TblBookingDetailsDTO> detailList = detailDao.getDetailsByRoomID(room.getRoomID());
                        if (detailList != null && !detailList.isEmpty()) {
                            for (TblBookingDetailsDTO detail : detailList) {
                                boolean isBookingActive = bookingDao.checkBookingByIDAndDates(detail.getBookingID(), checkInDate, checkOutDate);
                                if (isBookingActive) {
                                    availableAmount = availableAmount - detail.getAmount();
                                }
                            }
                        }
                        if ((availableAmount - addedAmount) > 0) {
                            if (cart.getCart() != null) {
                                if (item != null) {
                                    if (availableAmount < addedAmount + amount) {
                                        foundError = true;
                                        error.setAmount("Room " + roomType + " of " + hotelName + " has only " + availableAmount + " room(s) left");
                                    }
                                }
                            }
                        } else {
                            foundError = true;
                            error.setAmount("Room " + roomType + " of " + hotelName + " is not available");
                        }
                    } else {
                        foundError = true;
                        error.setRoomID("Room " + roomType + " of " + hotelName + " is not found");
                    }
                    if (!foundError) {
                        room.setAmount(amount);
                        cart.add(room);
                        long bookingDay = (checkOutDate.getTime() - checkInDate.getTime()) / (24 * 60 * 60 * 1000);
                        session.setAttribute("BOOKING_DAY", bookingDay);
                        session.setAttribute("CART", cart);
                    } else {
                        request.setAttribute("ROOM_ERROR", error);
                    }
                }
            }
            url = siteMap.getString(SEARCH_PRODUCT_CONTROLLER);
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
