<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 3/28/2017
  Time: 6:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Form</title>
</head>
<body>
<form action="meals" method="POST">
    Date and Time:<br/>
    <input type="text" name="datatime" value="${fn:replace(meal.dateTime, "T", " ")}"><br/>
    Description:<br/>
    <input type="text" name="description" value="${meal.description}"><br/>
    Calories:<br/>
    <input type="text" name="calories" value="${meal.calories}"><br/>
    <input type="hidden" name="id" value="${meal.id}">
    <input type="submit" value="save">

</form>

</body>
</html>
