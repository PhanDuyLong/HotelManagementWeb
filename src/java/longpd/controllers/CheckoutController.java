/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longpd.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
import longpd.dtos.CartDTO;
import longpd.dtos.ErrorTblBookings;
import longpd.dtos.TblBookingDetailsDTO;
import longpd.dtos.TblBookingsDTO;
import longpd.dtos.TblCodesDTO;
import longpd.dtos.TblRoomsDTO;
import longpd.dtos.TblUsersDTO;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "CheckoutController", urlPatterns = {"/CheckoutController"})
public class CheckoutController extends HttpServlet {

    private static final String CHECKOUT_PAGE = "checkoutPage";
    private static final String VIEW_CART_PAGE = "viewCart";
    private static final String SEND_CONFIRM_BOOKING_LINK_CONTROLLER = "send";
    private org.apache.log4j.Logger logger = null;

    public void initLog() {
        logger = org.apache.log4j.Logger.getLogger(CheckoutController.class.getName());
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
    private String generateOrderID(String s) {
        String id = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            md.update(salt);
            byte[] bytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            id = sb.toString().substring(0, 8).toUpperCase();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CheckoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

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
            if (cart != null && !cart.getCart().isEmpty()) {
                String totalPriceString = request.getParameter("txtTotalPrice").trim();
                float totalPrice = 0;
                try {
                    totalPrice = Float.parseFloat(totalPriceString.trim());
                } catch (Exception ex) {
                }
                if (totalPrice != 0) {
                    TblUsersDTO user = (TblUsersDTO) session.getAttribute("USER");
                    TblBookingsDAO bookingDao = new TblBookingsDAO();
                    TblBookingDetailsDAO detailDao = new TblBookingDetailsDAO();
                    String email = request.getParameter("txtEmail");
                    String phone = request.getParameter("txtPhone");
                    ErrorTblBookings checkoutError = new ErrorTblBookings();
                    if (email.trim().length() < 15 || email.trim().length() > 30) {
                        foundError = true;
                        checkoutError.setEmail("Email is required from 15 to 30 chars");
                    } else {
                        Pattern VALID_EMAIL_ADDRESS_REGEX
                                = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()) {
                            foundError = true;
                            checkoutError.setEmail("Wrong format of email");
                        }
                    }
                    if (phone == null || phone.isEmpty()) {
                        foundError = true;
                        checkoutError.setPhone("Phone mustn't be empty");
                    } else if (!Pattern.compile("^\\d{10}$").matcher(phone).matches()) {
                        foundError = true;
                        checkoutError.setPhone("Wrong format of phone");
                    }
                    if (!foundError) {
                        String bookingID = "";
                        do {
                            bookingID = generateOrderID(email + phone);
                        } while (bookingID.isEmpty() || bookingDao.checkExistedBookingID(bookingID));
                        String cartCheckInDateString = (String) session.getAttribute("CART_CHECK_IN_DATE");
                        String cartCheckOutDateString = (String) session.getAttribute("CART_CHECK_OUT_DATE");
                        if (cartCheckInDateString != null && cartCheckOutDateString != null) {
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
                            TblCodesDTO code = (TblCodesDTO) session.getAttribute("CODE");
                            String codeID = null;
                            if (code != null) {
                                codeID = code.getCodeID();
                            }
                            TblBookingsDTO booking = new TblBookingsDTO(bookingID, user.getUserID(), new Date(), phone, email, totalPrice, checkInDate, checkOutDate, codeID, "Unconfirmed", "Active");
                            bookingDao.insertBooking(booking);
                            List<Integer> ids = new ArrayList<>();
                            for (int id : cart.getCart().keySet()) {
                                if (cart.getCart().get(id).getAmount() > 0) {
                                    ids.add(id);
                                }
                            }
                            for (int id : ids) {
                                TblRoomsDTO item = cart.getCart().get(id);
                                TblBookingDetailsDTO detail = new TblBookingDetailsDTO(0, bookingID, item.getRoomID(), item.getAmount(), item.getPrice(), "Active");
                                detailDao.insertBookingDetail(detail);
                                cart.delete(id);
                            }
                            session.removeAttribute("CODE");
                            request.setAttribute("BOOKING_ID", bookingID);
                            request.setAttribute("MESSAGE", "Book successfully. Your bookingID: " + bookingID + ". Link to confirm booking will be sent to your inputted email.");
                            url = siteMap.getString(SEND_CONFIRM_BOOKING_LINK_CONTROLLER);
                        }
                    } else {
                        request.setAttribute("ERRORS", checkoutError);
                        url = siteMap.getString(CHECKOUT_PAGE);
                    }
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
