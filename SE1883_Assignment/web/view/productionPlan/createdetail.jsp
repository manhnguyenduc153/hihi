<%-- 
    Document   : createdetail
    Created on : Nov 1, 2024, 12:04:41 PM
    Author     : admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Create Plan Detail</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            padding: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table, th, td {
            border: 1px solid black;
            padding: 10px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
        input[type="number"] {
            width: 100px;
        }
        input[type="submit"] {
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 4px;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <h1>Create Plan Detail for ${plan.name}</h1>

    <form action="create" method="post">
        <input type="hidden" name="plid" value="${plan.id}" />
        
        <table>
            <thead>
                <tr>
                    <th>Date</th>
                    
                    <th>Product Name</th>
                    <th>Shift</th>
                    <th>Quantity</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="date" items="${dateList}">
                    <c:forEach var="header" items="${headers}">
                        <tr>
                            <td>${date}</td>
                            
                            <td>${header.product.name}</td> <!-- Hiển thị tên sản phẩm -->
                            <td>
                                <select name="shift_${date}_${header.product.id}">
                                    <option value="1">K1</option>
                                    <option value="2">K2</option>
                                    <option value="3">K3</option>
                                </select>
                            </td>
                            <td>
                                <input type="number" name="quantity_${date}_${header.product.id}" 
                                       value="${header.quantity != null ? header.quantity : 0}" min="0" />
                            </td>
                        </tr>
                    </c:forEach>
                </c:forEach>
            </tbody>
        </table>

        <input type="submit" value="Save" />
    </form>
</body>
</html>
