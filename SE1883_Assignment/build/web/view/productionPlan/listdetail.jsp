<%-- 
    Document   : list
    Created on : Oct 31, 2024, 11:02:52 PM
    Author     : admin
--%>
<%@ taglib prefix="mytag" uri="/WEB-INF/tlds/customtags.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Plan Detail List</title>
        <link rel="stylesheet" href="styles.css">
        <style>
            /* General body styling */
            body {
                font-family: Arial, sans-serif;
                background-color: #f0f2f5;
                margin: 0;
                padding: 20px;
            }
            
            /* Title styling */
            h1 {
                text-align: center;
                color: #333;
                font-size: 24px;
                margin-bottom: 20px;
            }

            /* Table styling */
            table {
                width: 100%;
                border-collapse: collapse;
                background-color: #fff;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                margin-top: 20px;
            }

            /* Header and cell styling */
            th, td {
                padding: 12px;
                text-align: center;
                border: 1px solid #ddd;
                color: #333;
            }
            
            /* Header background color */
            th {
                background-color: #4CAF50;
                color: white;
                text-transform: uppercase;
                letter-spacing: 0.1em;
            }

            /* Hover effect for rows */
            tr:hover {
                background-color: #f1f1f1;
            }
            
            /* Styling for even rows */
            tbody tr:nth-child(even) {
                background-color: #f9f9f9;
            }
            
            /* Hidden input styling (just for clarification) */
            input[type="hidden"] {
                display: none;
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
            display: inline-block;
            margin-bottom: 20px;
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
    </head>
    <body>
        <div class="back-button">
            <a href="../productionPlan/list">&larr; Back</a>
        </div>
        <h1>${requestScope.plan.name}</h1>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Product Name</th>
                    <th>Shift</th>
                    <th>Date</th>
                    <th>Quantity</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="detail" items="${details}">
                    <input type="hidden" name="plid" value="${plan.id}" />
                    <tr>
                        <td>${detail.id}</td>
                        <td>${detail.header.product.name}</td>
                        <td>${detail.shift.sname}</td>
                        <td><mytag:ToVietnameseDate value="${detail.date}"/></td>
                        <td>${detail.quantity}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="create?plid=${plan.id}" class="create-button">Create New Detail</a>
    </body>
</html>
