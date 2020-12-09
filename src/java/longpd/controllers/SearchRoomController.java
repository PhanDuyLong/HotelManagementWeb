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
import longpd.daos.TblAreasDAO;
import longpd.daos.TblHotelsDAO;
import longpd.daos.TblBookingDetailsDAO;
import longpd.daos.TblBookingsDAO;
import longpd.daos.TblRolesDAO;
import longpd.daos.TblRoomsDAO;
import longpd.daos.TblTypesDAO;
import longpd.dtos.ErrorTblHotels;
import longpd.dtos.TblAreasDTO;
import longpd.dtos.TblBookingDetailsDTO;
import longpd.dtos.TblHotelsDTO;
import longpd.dtos.TblRoomsDTO;
import longpd.dtos.TblUsersDTO;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "SearchRoomController", urlPatterns = {"/SearchRoomController"})
public class SearchRoomController extends HttpServlet {
    
    private static final String ERROR_PAGE = "error";
    private static final String LOAD_AREA_CONTROLLER = "loadArea";
    private org.apache.log4j.Logger logger = null;
    
    public void initLog() {
        logger = org.apache.log4j.Logger.getLogger(SearchRoomController.class.getName());
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
            String nameSearch = request.getParameter("txtNameSearch");
            String areaName = request.getParameter("cmbArea");
            if (nameSearch != null && !areaName.isEmpty()) {
                String checkInDateString = request.getParameter("txtCheckInDate");
                String checkOutDateString = request.getParameter("txtCheckOutDate");
                String amountString = request.getParameter("txtAmount");
                HttpSession session = request.getSession();
                TblUsersDTO user = (TblUsersDTO) session.getAttribute("USER");
                String role = "";
                if (user != null) {
                    TblRolesDAO roleDao = new TblRolesDAO();
                    role = roleDao.getRoleNameByID(user.getRoleID());
                }
                boolean foundError = false;
                ErrorTblHotels errors = new ErrorTblHotels();
                int amount = -1;
                try {
                    amount = Integer.parseInt(amountString.trim());
                } catch (Exception ex) {
                    if (ex != null) {
                        errors.setAmountOfRoom("Amount must be integer");
                        foundError = true;
                    }
                }
                if (amount <= 0 && !role.equals("Admin")) {
                    errors.setAmountOfRoom("Amount must be positive integer");
                    foundError = true;
                } else if (amount < 0 && role.equals("Admin")) {
                    errors.setAmountOfRoom("Amount must be non-negative integer");
                    foundError = true;
                }
                Date checkInDate = null;
                Date checkOutDate = null;
                Date present = new Date();
                if (checkInDateString.isEmpty()) {
                    errors.setCheckInDate("Check-in date mustn't be empty");
                    foundError = true;
                } else {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        format.setLenient(false);
                        checkInDate = format.parse(checkInDateString);
                    } catch (Exception ex) {
                        errors.setCheckInDate("Format of date must be yyyy-MM-dd");
                        foundError = true;
                    }
                    if (checkInDate != null && checkInDate.compareTo(present) <= 0) {
                        errors.setCheckInDate("Check-in date must be after today");
                        foundError = true;
                    }
                }
                if (checkOutDateString.isEmpty()) {
                    errors.setCheckOutDate("Check-out date mustn't be empty");
                    foundError = true;
                } else {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        format.setLenient(false);
                        checkOutDate = format.parse(checkOutDateString);
                    } catch (Exception ex) {
                        errors.setCheckOutDate("Format of date must be yyyy-MM-dd");
                        foundError = true;
                    }
                    if (checkOutDate != null && checkOutDate.compareTo(present) < 0) {
                        errors.setCheckOutDate("Check-out date must be after today");
                        foundError = true;
                    }
                }
                if (errors.getCheckInDate() == null && errors.getCheckOutDate() == null) {
                    if (checkInDate.compareTo(checkOutDate) >= 0) {
                        errors.setCheckOutDate("Check-out date must be after check-in date");
                        foundError = true;
                    }
                }
                if (!foundError) {
                    TblAreasDAO areaDao = new TblAreasDAO();
                    TblHotelsDAO hotelDao = new TblHotelsDAO();
                    List<TblAreasDTO> areaList = areaDao.getAreas();
                    if (!areaList.isEmpty()) {
                        String areaID = "";
                        for (TblAreasDTO area : areaList) {
                            if (area.getAreaName().equals(areaName)) {
                                areaID = area.getAreaID();
                            }
                        }
                        List<TblHotelsDTO> hotelList = null;
                        if (role.equals("Admin")) {
                            hotelList = hotelDao.getHotelsByNameAreaAndRoomAmountAdmin(nameSearch, areaID, amount);
                        } else {
                            hotelList = hotelDao.getHotelsByNameAreaAndRoomAmount(nameSearch, areaID, amount);
                        }
                        if (hotelList != null && !hotelList.isEmpty()) {
                            Map<TblRoomsDTO, TblHotelsDTO> roomMap = new HashMap();
                            TblRoomsDAO roomDao = new TblRoomsDAO();
                            TblBookingsDAO bookingDao = new TblBookingsDAO();
                            TblBookingDetailsDAO detailDao = new TblBookingDetailsDAO();
                            for (TblHotelsDTO hotel : hotelList) {
                                int hotelID = hotel.getHotelID();
                                List<TblRoomsDTO> roomList = null;
                                if (role.equals("Admin")) {
                                    roomList = roomDao.getRoomsWithHotelIDAndAmountAdmin(hotelID, amount);
                                } else {
                                    roomList = roomDao.getRoomsWithHotelIDAndAmount(hotelID, amount);
                                }
                                if (roomList != null && !roomList.isEmpty()) {
                                    for (TblRoomsDTO room : roomList) {
                                        int totalAmount = room.getAmount();
                                        List<TblBookingDetailsDTO> detailList = detailDao.getDetailsByRoomID(room.getRoomID());
                                        if (detailList != null && !detailList.isEmpty()) {
                                            for (TblBookingDetailsDTO detail : detailList) {
                                                boolean isBookingActive = bookingDao.checkBookingByIDAndDates(detail.getBookingID(), checkInDate, checkOutDate);
                                                if (isBookingActive) {
                                                    totalAmount = totalAmount - detail.getAmount();
                                                }
                                            }
                                            if (totalAmount >= amount) {
                                                roomMap.put(room, hotel);
                                            }
                                        } else {
                                            roomMap.put(room, hotel);
                                        }
                                    }
                                    
                                }
                            }
                            if (!roomMap.isEmpty()) {
                                Map<Integer, Integer> indexMap = new TreeMap<>();
                                int count = 0;
                                while (count != roomMap.size()) {
                                    if ((roomMap.size() - (count + 3)) > 0) {
                                        indexMap.put(count, count + 3);
                                        count += 4;
                                    } else {
                                        indexMap.put(count, count + (roomMap.size() - count - 1));
                                        count += ((roomMap.size() - count));
                                    }
                                }
                                TblTypesDAO typeDao = new TblTypesDAO();
                                Map<String, String> typeMap = typeDao.getTypes();
                                if (!typeMap.isEmpty()) {
                                    session.setAttribute("TYPE_MAP", typeMap);
                                }
                                request.setAttribute("INDEX_MAP", indexMap);
                                request.setAttribute("ROOM_MAP", roomMap);
                            }
                        }
                    }
                } else {
                    request.setAttribute("SEARCH_ERROR", errors);
                }
            }
            url = siteMap.getString(LOAD_AREA_CONTROLLER);
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
