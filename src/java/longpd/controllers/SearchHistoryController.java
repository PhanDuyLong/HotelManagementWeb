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
import java.util.TreeMap;
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
import longpd.daos.TblCodesDAO;
import longpd.daos.TblHotelsDAO;
import longpd.daos.TblRoomsDAO;
import longpd.daos.TblTypesDAO;
import longpd.dtos.ErrorTblHotels;
import longpd.dtos.TblBookingDetailsDTO;
import longpd.dtos.TblBookingsDTO;
import longpd.dtos.TblCodesDTO;
import longpd.dtos.TblHotelsDTO;
import longpd.dtos.TblRoomsDTO;
import longpd.dtos.TblUsersDTO;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "SearchHistoryController", urlPatterns = {"/SearchHistoryController"})
public class SearchHistoryController extends HttpServlet {

    private static final String ERROR_PAGE = "error";
    private static final String VIEW_HISTORY_PAGE = "viewHistory";
    private org.apache.log4j.Logger logger = null;

    public void initLog() {
        logger = org.apache.log4j.Logger.getLogger(SearchHistoryController.class.getName());
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
            String nameSearch = request.getParameter("txtHotelName");
            String bookingDateString = request.getParameter("txtBookingDate");
            if (nameSearch != null && bookingDateString != null) {
                HttpSession session = request.getSession();
                TblUsersDTO user = (TblUsersDTO) session.getAttribute("USER");
                boolean foundError = false;
                ErrorTblHotels errors = new ErrorTblHotels();
                Date bookingDate = null;
                if (nameSearch.isEmpty() && bookingDateString.isEmpty()) {
                    foundError = true;
                    errors.setHotelName("Must input name or booking date");
                }
                if (!bookingDateString.isEmpty()) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        format.setLenient(false);
                        bookingDate = format.parse(bookingDateString);
                    } catch (Exception ex) {
                        errors.setCheckInDate("Format of date must be yyyy-MM-dd");
                        foundError = true;
                    }
                }
                if (!foundError) {
                    Map<TblBookingsDTO, Map<TblBookingDetailsDTO, TblRoomsDTO>> bookingMap = new HashMap();
                    Map<Integer, String> hotelMap = new HashMap();
                    Map<String, TblCodesDTO> codeMap = new HashMap();
                    Map<String, Long> dayMap = new HashMap();
                    TblHotelsDAO hotelDao = new TblHotelsDAO();
                    TblBookingsDAO bookingDao = new TblBookingsDAO();
                    TblRoomsDAO roomDao = new TblRoomsDAO();
                    TblBookingDetailsDAO detailDao = new TblBookingDetailsDAO();
                    TblCodesDAO codeDao = new TblCodesDAO();
                    List<TblHotelsDTO> hotelList = hotelDao.getHotelsByNameAreaAndRoomAmountAdmin(nameSearch, "", 0);
                    if (hotelList != null && !hotelList.isEmpty()) {
                        List<TblBookingsDTO> bookingList = null;
                        if (bookingDateString.isEmpty()) {
                            bookingList = bookingDao.getBookingsByUserID(user.getUserID());
                        } else {
                            bookingList = bookingDao.getBookingsByBookingDateAndUserID(bookingDate, user.getUserID());
                        }
                        if (bookingList != null && !bookingList.isEmpty()) {
                            for (int i = 0; i < hotelList.size(); i++) {
                                TblHotelsDTO hotel = hotelList.get(i);
                                List<TblRoomsDTO> roomList = roomDao.getRoomsWithHotelIDAndAmountAdmin(hotel.getHotelID(), 0);
                                if (roomList != null && !roomList.isEmpty()) {
                                    for (TblRoomsDTO room : roomList) {
                                        TblBookingDetailsDTO detail = null;
                                        for (TblBookingsDTO booking : bookingList) {
                                            detail = detailDao.getDetailByRoomIDAndBookingID(room.getRoomID(), booking.getBookingID());
                                            if (detail != null) {
                                                Map<TblBookingDetailsDTO, TblRoomsDTO> foundDetails = bookingMap.get(booking);
                                                List<TblBookingDetailsDTO> detailList = detailDao.getDetailsByBookingID(booking.getBookingID());
                                                for (TblBookingDetailsDTO foundDetail : detailList) {
                                                    TblRoomsDTO foundRoom = roomDao.getRoomByIDAdmin(foundDetail.getRoomID());
                                                    if (foundRoom != null) {
                                                        if (foundDetails == null) {
                                                            foundDetails = new HashMap();
                                                        }
                                                        if (booking.getCodeID() != null && !booking.getCodeID().isEmpty()) {
                                                            TblCodesDTO code = codeDao.getCodeByIDAdmin(booking.getCodeID());
                                                            if (code != null) {
                                                                codeMap.put(booking.getBookingID(), code);
                                                            }
                                                        }
                                                        dayMap.put(booking.getBookingID(), (booking.getCheckOutDate().getTime() - booking.getCheckInDate().getTime()) / (24 * 60 * 60 * 1000));
                                                        bookingMap.put(booking, foundDetails);
                                                        String hotelName = hotelDao.getHotelNameByIDAdmin(foundRoom.getHotelID());
                                                        hotelMap.put(foundRoom.getHotelID(), hotelName);
                                                        foundDetails.put(foundDetail, foundRoom);
                                                        for (int j = 0; j < bookingList.size(); j++) {
                                                            if (bookingList.get(j).getBookingID().equals(booking.getBookingID())) {
                                                                bookingList.remove(bookingList.get(j));
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                        if (detail != null) {
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (!bookingMap.isEmpty()) {
                        TreeMap<TblBookingsDTO, Map<TblBookingDetailsDTO, TblRoomsDTO>> sortedBookingMap = new TreeMap<>(bookingMap);
                        request.setAttribute("BOOKING_MAP", sortedBookingMap);
                        request.setAttribute("HOTEL_MAP", hotelMap);
                        request.setAttribute("CODE_MAP", codeMap);
                        request.setAttribute("DAY_MAP", dayMap);
                        TblTypesDAO typeDao = new TblTypesDAO();
                        Map<String, String> typeMap = typeDao.getTypes();
                        if (!typeMap.isEmpty()) {
                            request.setAttribute("TYPE_MAP", typeMap);
                        }
                    }
                    System.out.println("Size: " +bookingMap.size());
                } else {
                    request.setAttribute("SEARCH_ERROR", errors);
                }
            }
            url = siteMap.getString(VIEW_HISTORY_PAGE);
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
