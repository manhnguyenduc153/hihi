<%-- 
    Document   : filter
    Created on : Oct 30, 2024, 11:07:34 AM
    Author     : admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Filter Employees</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f0f2f5;
                margin: 0;
                padding: 20px;
            }
            h1 {
                text-align: center;
                color: #333;
            }
            form {
                width: 60%;
                margin: 20px auto;
                background-color: #fff;
                padding: 20px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                border-radius: 8px;
            }
            label {
                display: block;
                margin-top: 10px;
                font-weight: bold;
            }
            input[type="text"], select {
                width: 100%;
                padding: 10px;
                margin: 5px 0 15px 0;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-sizing: border-box;
            }
            input[type="submit"] {
                width: 100%;
                background-color: #4CAF50;
                color: white;
                padding: 15px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 16px;
            }
            input[type="submit"]:hover {
                background-color: #45a049;
            }
            table {
                width: 80%;
                margin: 20px auto;
                border-collapse: collapse;
                background-color: #fff;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                border-radius: 8px;
                overflow: hidden;
            }
            th, td {
                padding: 12px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }
            th {
                background-color: #4CAF50;
                color: white;
                text-transform: uppercase;
                letter-spacing: 0.1em;
            }
            tr:hover {
                background-color: #f1f1f1;
            }
            select {
                text-align: center;
                width: auto; /* Tự động vừa với nội dung */
                min-width: 100%; /* Đảm bảo ít nhất bằng chiều rộng của ô nhập liệu */
            }
            td {
                color: #333;
            }
        </style>
    </head>
    <body>
        <h1>Filter Employees</h1>
        <form action="filter" method="GET"> 
            <label for="id">Id:</label>
            <input type="text" id="id" name="id" value="${param.id}"/> <br/>

            <label for="name">Name:</label>
            <input type="text" id="name" name="name" value="${param.name}" /> <br/>

            <label for="phonenumber">Phone Number:</label>
            <input type="text" id="phonenumber" name="phonenumber" value="${param.phonenumber}" /> <br/>

            <label for="address">Address:</label>
            <input type="text" id="address" name="address" value="${param.address}" /> <br/>

            <label for="did">Department:</label>
            <select id="did" name="did">
                <option value="-1">All</option>
                <c:forEach items="${requestScope.depts}" var="d">
                    <option 
                        ${param.did ne null and param.did eq d.id ? "selected=\"selected\"" : ""}
                        value="${d.id}">${d.name}
                    </option>
                </c:forEach>
            </select> <br/>

            <input type="submit" value="Search"/>
        </form>

        <table>
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Phone Number</th>    
                    <th>Address</th>
                    <th>Department</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${requestScope.emps}" var="e">
                    <tr>
                        <td>${e.id}</td>
                        <td>${e.name}</td>
                        <td>${e.phoneNumber}</td>
                        <td>${e.address}</td>
                        <td>${e.dept.name}</td>
                    </tr>   
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
