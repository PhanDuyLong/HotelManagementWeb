/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longpd.controllers;

import java.io.IOException;
import java.sql.SQLException;
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
import longpd.daos.TblCodesDAO;
import longpd.dtos.ErrorTblCodes;
import longpd.dtos.TblCodesDTO;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "CheckDiscountCodeController", urlPatterns = {"/CheckDiscountCodeController"})
public class CheckDiscountCodeController extends HttpServlet {
    
    private static final String CHECKOUT_PAGE = "checkoutPage";
    private static final String ERROR_PAGE = "error";
    private org.apache.log4j.Logger logger = null;
    
    public void initLog() {
        logger = org.apache.log4j.Logger.getLogger(CheckDiscountCodeController.class.getName());
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
            String id = request.getParameter("txtCodeID");
            if (id != null && !id.trim().isEmpty()) {
                String totalPriceString = request.getParameter("txtTotalPrice").trim();
                float totalPrice = 0;
                try {
                    totalPrice = Float.parseFloat(totalPriceString.trim());
                } catch (Exception ex) {
                }
                if (totalPrice != 0) {
                    boolean foundError = false;
                    ErrorTblCodes error = new ErrorTblCodes();
                    HttpSession session = request.getSession();
                    TblCodesDAO codeDao = new TblCodesDAO();
                    TblCodesDTO code = codeDao.getCodeByID(id);
                    if (code == null) {
                        foundError = true;
                        error.setCodeID("Code is not found");
                    }
                    if (!foundError) {
                        session.setAttribute("CODE", code);
                    } else {
                        request.setAttribute("CODE_ERROR", error);
                    }
                }
            }
            url = siteMap.getString(CHECKOUT_PAGE);
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
