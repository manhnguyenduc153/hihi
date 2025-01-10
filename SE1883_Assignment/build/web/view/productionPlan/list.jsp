<%-- 
    Document   : list
    Created on : Oct 27, 2024, 12:11:05 AM
    Author     : admin
--%>
<%@ taglib prefix="mytag" uri="/WEB-INF/tlds/customtags.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Production Plan List</title>
        <link rel="stylesheet" href="styles.css">
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
            table {
                width: 100%;
                border-collapse: collapse;
                margin: 20px 0;
                background-color: #fff;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
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
            td {
                color: #333;
            }
            .product-list, .quantity-list {
                line-height: 1.6;
            }
            .button {
                padding: 8px 16px;
                border: none;
                color: white;
                cursor: pointer;
                text-decoration: none;
                border-radius: 4px;
                font-size: 14px;
                transition: background-color 0.3s ease;
            }
            .button-update {
                background-color: #2196F3;
            }
            .button-update:hover {
                background-color: #0b7dda;
            }
            .button-delete {
                background-color: #f44336;
                margin-left: 5px;
            }
            .button-delete:hover {
                background-color: #d32f2f;
            }
            .create-button {
                padding: 10px 20px;
                background-color: #4CAF50;
                color: white;
                border: none;
                cursor: pointer;
                border-radius: 4px;
                font-size: 16px;
                transition: background-color 0.3s ease;
                text-decoration: none;
            }
            .create-button:hover {
                background-color: #45a049;
            }
            .back-button {
                display: block;
                margin-bottom: 20px;
                text-align: left;
            }
            .back-button a {
                padding: 10px 15px;
                background-color: #4CAF50;
                color: white;
                text-decoration: none;
                border-radius: 4px;
                font-size: 14px;
            }
            .back-button a:hover {
                background-color: #45a049;
            }
        </style>
        <script>
            function redirectToCreatePlan() {
                window.location.href = 'create';
            }
            function confirmDelete(planId) {
                return confirm("Are you sure you want to delete plan: ");
            }
        </script>
    </head>
    <body>
        <div class="back-button">
            <a href="../home">&larr; Back to Home</a>
        </div>
        <h1>Production Plan List</h1>
        <table border="1" cellspacing="0" cellpadding="5">
            <thead>
                <tr>
                    <th>Plan Name</th>
                    <th>Start</th>
                    <th>End</th>
                    <th>Product</th>
                    <th>Quantity Expected</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${requestScope.plans}" var="pl">
                    <tr>
                        <td><a href="../planDetail/list?plid=${pl.id}">${pl.name}</a></td>
                        <td><mytag:ToVietnameseDate value="${pl.start}" /></td>
                        <td><mytag:ToVietnameseDate value="${pl.end}" /></td>
                        <td>
                            <c:forEach items="${pl.headers}" var="ph">
                                ${ph.product.name} <br>
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach items="${pl.headers}" var="ph">
                                ${ph.quantity} <br>
                            </c:forEach>
                        </td>
                        <td>
                            <a href="update?plid=${pl.id}" class="button button-update">Update</a>
                            <a href="delete?plid=${pl.id}" class="button button-delete" onclick=" return confirmDelete(${pl.id});">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div style="text-align: center; margin-top: 20px;">
            <button onclick="redirectToCreatePlan()" class="create-button">Create New Plan</button>
        </div>
    </body>
</html>
