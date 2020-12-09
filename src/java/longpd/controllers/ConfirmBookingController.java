/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longpd.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.ResourceBundle;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import longpd.daos.TblBookingsDAO;
import longpd.dtos.TblBookingsDTO;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "ConfirmBookingController", urlPatterns = {"/ConfirmBookingController"})
public class ConfirmBookingController extends HttpServlet {

    private static final String ERROR_PAGE = "error";
    private static final String SHOW_CONFIRM_PAGE = "showConfirm";
    private org.apache.log4j.Logger logger = null;

    public void initLog() {
        logger = org.apache.log4j.Logger.getLogger(ConfirmBookingController.class.getName());
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
            String bookingID = request.getParameter("bookingID");
            if (bookingID != null && !bookingID.isEmpty()) {
                bookingID = new String(Base64.getDecoder().decode(bookingID.getBytes()));
                TblBookingsDAO bookingDao = new TblBookingsDAO();
                TblBookingsDTO booking = bookingDao.getBookingByID(bookingID);
                if (booking != null) {
                    String message = "";
                    Date present = new Date();
                    if (booking.getStatus().equals("Cancelled")) {
                        message = "Booking " + bookingID + " was cancelled!!!";
                    } else if (booking.getStatus().equals("Confirmed")) {
                        message = "Booking " + bookingID + " was confirmed!!!";
                    } else if (booking.getStatus().equals("Unconfirmed") && booking.getCheckInDate().compareTo(present) < 0) {
                        message = "Booking " + bookingID + " is too late to confirm so booking is cancelled";
                        booking.setStatus("Cancelled");
                        bookingDao.updateBooking(booking);
                    } else if (booking.getStatus().equals("Unconfirmed") && booking.getCheckInDate().compareTo(present) >= 0) {
                        message = "Booking " + bookingID + " is confirmed";
                        booking.setStatus("Confirmed");
                        bookingDao.updateBooking(booking);
                    }
                    request.setAttribute("MESSAGE", message);
                    url = siteMap.getString(SHOW_CONFIRM_PAGE);
                }
            }
        } catch (NamingException ex) {
            logger.error(ex);
        } catch (SQLException ex) {
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
