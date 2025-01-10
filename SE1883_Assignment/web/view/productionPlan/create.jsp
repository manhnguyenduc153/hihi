<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Production Plan</title>
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
            input[type="text"], input[type="date"], select {
                width: 100%;
                padding: 10px;
                margin: 5px 0 15px 0;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-sizing: border-box;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin: 20px 0;
                background-color: #fff;
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
        </style>
    </head>
    <body>
        <button onclick="window.location.href='list'" style="position: absolute; top: 20px; left: 20px; background-color: #4CAF50; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; font-size: 14px;">Back</button>
        <h1>Create Production Plan</h1>
        <form id="createPlanForm" action="create" method="POST">
            <c:if test="${not empty errName}">
                <div class="error">${errName}</div>
            </c:if>
            <label for="name">Plan Name:</label>
            <input type="text" id="name" name="name" value="${param.name}" required/> <br/>
            
            <c:if test="${not empty errDate}">
                <div class="error">${errDate}</div>
            </c:if>
            <label for="from">From:</label>
            <input type="date" id="from" name="from" value="${param.from}" required/>
            
            <label for="to">To:</label>
            <input type="date" id="to" name="to" value="${param.to}" required/> <br/>
            
            <c:if test="${not empty errDid}">
                <div class="error">${errDid}</div>
            </c:if>
            <label for="did">Workshop:</label>
            <select id="did" name="did" required>
                <c:forEach items="${requestScope.depts}" var="d">
                    <option value="${d.id}" ${param.did eq d.id ? "selected=\"selected\"" : ""}>${d.name}</option>
                </c:forEach>
            </select>
            
            <table border="1px">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Estimated Effort</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${requestScope.products}" var="p">
                        <tr>
                            <td>${p.name}<input type="hidden" name="pid" value="${p.id}"></td>
                            <td><input type="text" name="quantity${p.id}" /></td>
                            <td><input type="text" name="effort${p.id}" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <button type="submit" style="width: 100%; background-color: #4CAF50; color: white; padding: 15px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px;">Save</button>
        </form>
    </body>
</html>
