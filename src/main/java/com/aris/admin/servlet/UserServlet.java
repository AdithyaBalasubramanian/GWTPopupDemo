package com.aris.admin.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = "C:\\Users\\ADBA\\GWT-popup-Window\\src\\main\\java\\com\\aris\\admin\\shared\\person.properties";
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String output = reader.readLine();
        resp.getWriter().write(output);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = "C:\\Users\\ADBA\\GWT-popup-Window\\src\\main\\java\\com\\aris\\admin\\shared\\person.properties";
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String output = reader.readLine();
        resp.getWriter().write(output);
    }
}
