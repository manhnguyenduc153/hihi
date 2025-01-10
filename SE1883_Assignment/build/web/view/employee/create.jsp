<%-- 
    Document   : create
    Created on : Oct 30, 2024, 11:07:23 AM
    Author     : admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Employee</title>
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
                margin: 0 auto;
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
            .error {
                color: red;
                margin-bottom: 10px;
            }
            .success {
                color: green;
                text-align: center;
                margin-bottom: 15px;
            }
        </style>
    </head>
    <body>
        <h1>Create Employee</h1>

        <!-- Display success message -->
        <c:if test="${not empty successMessage}">
            <p class="success">${successMessage}</p>
        </c:if>

        <form action="create" method="POST">
            <label for="name">Name:</label>
            <input type="text" id="name" name="name" />
            <c:if test="${not empty errName}">
                <span class="error">${errName}</span>
            </c:if>
            <br/>

            <label for="phonenumber">Phone Number:</label>
            <input type="text" id="phonenumber" name="phonenumber" />
            <c:if test="${not empty errPhoneNumber}">
                <span class="error">${errPhoneNumber}</span>
            </c:if>
            <br/>

            <label for="address">Address:</label>
            <input type="text" id="address" name="address" />
            <c:if test="${not empty errAddress}">
                <span class="error">${errAddress}</span>
            </c:if>
            <br/>

            <label for="did">Department:</label>
            <select id="did" name="did" required>
                <c:forEach items="${requestScope.depts}" var="d">
                    <option value="${d.id}">${d.name}</option>
                </c:forEach>
            </select>
            <c:if test="${not empty errDid}">
                <span class="error">${errDid}</span>
            </c:if>
            <br/>

            <input type="submit" value="Save"/>
        </form>
    </body>
</html>
