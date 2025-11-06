<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<body>
<h1>Progress</h1>

<form method="post" action="${pageContext.request.contextPath}/progress">
    Student: <input name="student" value="${lastStudent}" />
    Subject: <input name="subject" value="${lastSubject}" />
    Grade:   <input name="grade" type="number" min="1" max="10" />
    <button type="submit">Add</button>
</form>

<p>Visits: ${visits}</p>

<table border="1" cellpadding="6">
    <tr><th>ID</th><th>Student</th><th>Subject</th><th>Grade</th></tr>
    <c:forEach items="${rows}" var="r">
        <tr>
            <td>${r.id()}</td>
            <td>${r.student()}</td>
            <td>${r.subject()}</td>
            <td>${r.grade()}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
