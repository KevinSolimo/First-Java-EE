/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author net
 */
public class JDBC_Servlet extends HttpServlet {
    
    
    InitialContext ctx = null; //Usa JND (per raggiungere il connection pool)
    DataSource ds = null; //Contiene le informazioni per raggiungere il db
    Connection conn = null; //La connessione
    PreparedStatement ps = null; //Pre-inserisco la query
    ResultSet rs = null;  //Risultati

    private void Connect(PrintWriter out) {
        try {
            //Richiamo il pool di connessione
            ctx = new InitialContext();
            //Imposto la stringa di connessione e ottengo il DataSource
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mysql_conn");
            conn = ds.getConnection(); //Ottengo l'oggetto Connection
        } catch (SQLException se) {
            out.println("SQLException: " + se.getMessage());
        } catch (NamingException ne) {
            out.println("NamingException: " + ne.getMessage());
        }
    }

    private void ConnectionTester(PrintWriter out) {
        try {
            DatabaseMetaData md = conn.getMetaData();

            ResultSet rs = md.getTables(null, null, null, new String[]{"TABLE"});
            System.out.println("List of tables: ");
            while (rs.next()) {
                System.out.println(
                        "   " + rs.getString("TABLE_CAT")
                        + ", " + rs.getString("TABLE_SCHEM")
                        + ", " + rs.getString("TABLE_NAME")
                        + ", " + rs.getString("TABLE_TYPE")
                        + ", " + rs.getString("REMARKS"));
            }

            rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
                out.println(rs.getString(3) + "<br>");
            }

        } catch (SQLException se) {
            out.println("SQLException: " + se.getMessage());
        }
    }
    
    private void aFirstQuery(PrintWriter out) {
        try {
            String sql = "SELECT * FROM sakila.actor";
            ResultSet rs;
            ps = conn.prepareStatement(sql); 
            rs =  ps.executeQuery();
            
            while (rs.next()) {
               out.println( "<br>" + rs.getString(1) + "|" + rs.getString(2) + "|" +       
               rs.getString(3) + "|..." ); 
            }
        } catch (SQLException se) {
            out.println("SQLException: " + se.getMessage());
        }
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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>IL MIO SITO</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet JDBC_Servlet at " + request.getContextPath() + "</h1>");
            
            Connect(out);
            ConnectionTester(out);
            aFirstQuery(out);
            
            out.println("</body>");
            out.println("</html>");
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
