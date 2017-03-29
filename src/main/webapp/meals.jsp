<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 3/23/2017
  Time: 8:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Meals list</title>
</head>
<body>
<table>
    <tr>
        <th>LocalDateTime</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Update</th>
        <th>Delete</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <tr class="${meal.exceed ? 'red':'green'}">
            <td align="center">

                    ${fn:replace(meal.dateTime, "T", " ")}
            </td>
            <td align="center">
                    ${meal.description}
            </td>
            <td align="center">
                    ${meal.calories}
            </td>
            <td><a href="/topjava/meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="/topjava/meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<p><a href="/topjava/editForm.jsp">Add Meal</a></p>
<style>
    .green {
        color: green
    }

    .red {
        color: red
    }
</style>

</body>
</html>
