<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Employee List with Filter</title>
        <style>
            /* Add your existing CSS styles here */
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
            form, table {
                width: 80%;
                margin: 20px auto;
            }
            form {
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
            .action-buttons a, .action-buttons input[type="button"] {
                display: inline-block;
                padding: 10px 15px;
                background-color: #4CAF50;
                color: white;
                text-decoration: none;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 14px;
            }
            .action-buttons a:hover, .action-buttons input[type="button"]:hover {
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
            .create-button {
                display: block;
                margin: 20px auto;
                padding: 15px 30px;
                background-color: #4CAF50;
                color: white;
                border: none;
                border-radius: 4px;
                font-size: 16px;
                cursor: pointer;
                text-align: center;
                text-decoration: none;
                transition: background-color 0.3s ease;
            }
            .create-button:hover {
                background-color: #45a049;
            }
        </style>
        <script>
            function removeEmployee(id) {
                var result = confirm("Are you sure?");
                if (result) {
                    document.getElementById("formRemove" + id).submit();
                }
            }
            function redirectToCreatePlan() {
                window.location.href = 'create';
            }
        </script>
    </head>
    <body>
        <div class="back-button">
            <a href="../home">&larr; Back to Home</a>
        </div>
        <h1>Employee List</h1>

        <!-- Filter Form -->
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

        <!-- Employee List Table -->
        <table>
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Phone Number</th>
                    <th>Address</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${requestScope.emps}" var="e">
                    <tr>
                        <td>${e.id}</td>
                        <td>${e.name}</td>
                        <td>${e.phoneNumber}</td>
                        <td>${e.address}</td>
                        <td class="action-buttons">
                            <a href="update?id=${e.id}">Edit</a>
                            <input type="button" value="Remove" onclick="removeEmployee(${e.id})"/>

                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div>
            <button onclick="redirectToCreatePlan()" class="create-button">Create New Plan</button>
        </div>
    </body>
</html>
