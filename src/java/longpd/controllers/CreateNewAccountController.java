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
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import longpd.daos.TblUsersDAO;
import longpd.dtos.ErrorTblUsers;
import longpd.dtos.TblUsersDTO;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "CreateNewAccountController", urlPatterns = {"/CreateNewAccountController"})
public class CreateNewAccountController extends HttpServlet {

    private static final String REGISTER_PAGE = "registerPage";
    private static final String LOGIN_PAGE = "loginPage";
    private org.apache.log4j.Logger logger = null;

    public void initLog() {
        logger = org.apache.log4j.Logger.getLogger(CreateNewAccountController.class.getName());
        BasicConfigurator.configure();
    }

    private String encryptPass(String pass) {
        String encryptedPass = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(pass.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            encryptedPass = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex);
        }
        return encryptedPass;
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
        String url = siteMap.getString(REGISTER_PAGE);
        ErrorTblUsers errors = new ErrorTblUsers();
        boolean foundError = false;
        try {
            initLog();
            String userID = request.getParameter("txtUserID");
            String password = request.getParameter("txtPassword");
            String confirm = request.getParameter("txtConfirmPassword");
            String name = request.getParameter("txtName");
            String address = request.getParameter("txtAddress");
            String phone = request.getParameter("txtPhone");
            if (userID.trim().length() < 15 || userID.trim().length() > 30) {
                foundError = true;
                errors.setUserID("Email is required from 15 to 30 chars");
            } else {
                Pattern VALID_EMAIL_ADDRESS_REGEX
                        = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                if (!VALID_EMAIL_ADDRESS_REGEX.matcher(userID).matches()) {
                    foundError = true;
                    errors.setUserID("Wrong format of email");
                }
            }
            if (password.trim().length() < 6 || password.trim().length() > 30) {
                foundError = true;
                errors.setPassword("Password is required from 6 to 30 chars");
            } else if (!confirm.trim().equals(password.trim())) {
                foundError = true;
                errors.setConfirmPassword("Confirm must match password");
            }
            if (name.trim().length() < 2 || name.trim().length() > 50) {
                foundError = true;
                errors.setName("Fullname is required from 2 to 50 chars");
            }
            if (!phone.isEmpty() && !Pattern.compile("^\\d{10}$").matcher(phone).matches()) {
                foundError = true;
                errors.setPhone("Wrong format of phone");
            }
            if (!foundError) {
                TblUsersDAO dao = new TblUsersDAO();
                password = encryptPass(password);
                boolean result = dao.insertNewUser(new TblUsersDTO(userID, password, name, new Date(), address, phone, "MB", "Active"));
                if (result) {
                    url = siteMap.getString(LOGIN_PAGE);
                }
            } else {
                request.setAttribute("ERRORS", errors);
            }
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            logger.error(ex);
            if (msg.contains("duplicate")) {
                errors.setUserID("User name is existed!!!!");
                request.setAttribute("ERRORS", errors);
            }
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
