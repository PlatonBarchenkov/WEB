<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>

<fmt:setLocale value="${empty param.lang ? (empty lang ? 'ru' : lang) : param.lang}"/>
<fmt:setBundle basename="message"/>

<!DOCTYPE html>
<html lang="${empty param.lang ? (empty lang ? 'ru' : lang) : param.lang}">
<head>
    <meta charset="UTF-8"/>
    <title>
        <fmt:message key="page.title">
            <fmt:param><fmt:message key="${periodKey}"/></fmt:param>
        </fmt:message>
    </title>
    <style>
        body{font-family:system-ui,Arial,sans-serif;margin:24px}
        a{color:#2e7d32;text-decoration:none;margin-right:12px}
        a:hover{text-decoration:underline}
        h1{font-size:28px;margin:0 0 16px}
        table{border-collapse:collapse;margin-top:16px;min-width:720px}
        th,td{border:1px solid #bbb;padding:10px 12px;text-align:left}
        th{background:#f6f6f6}
        .row{margin:8px 0}
        .lang{margin:8px 0 16px}
        .filters input{padding:6px 8px}
    </style>
</head>
<body>

<h1>
    <fmt:message key="page.title">
        <fmt:param><fmt:message key="${periodKey}"/></fmt:param>
    </fmt:message>
</h1>

<div class="lang">
    <a href="<c:url value='/progress'><c:param name='q' value='${q}'/></c:url>">RU</a> /
    <a href="<c:url value='/progress'><c:param name='q' value='${q}'/><c:param name='lang' value='en'/></c:url>">EN</a>
</div>

<div class="row">
    <strong><fmt:message key="periods"/>:</strong>
    <a href="<c:url value='/progress'><c:param name='q' value='q1'/><c:param name='lang' value='${lang}'/></c:url>"><fmt:message key="period.q1"/></a>
    <a href="<c:url value='/progress'><c:param name='q' value='q2'/><c:param name='lang' value='${lang}'/></c:url>"><fmt:message key="period.q2"/></a>
    <a href="<c:url value='/progress'><c:param name='q' value='q3'/><c:param name='lang' value='${lang}'/></c:url>"><fmt:message key="period.q3"/></a>
    <a href="<c:url value='/progress'><c:param name='q' value='q4'/><c:param name='lang' value='${lang}'/></c:url>"><fmt:message key="period.q4"/></a>
</div>

<form class="filters" method="get" action="<c:url value='/progress'/>">
    <input type="hidden" name="q" value="${q}"/>
    <input type="hidden" name="lang" value="${lang}"/>
    <label><fmt:message key="clazz"/>:
        <input type="text" name="class" value="${clazz != null ? clazz : ''}" placeholder="например 8A"/>
    </label>
    &nbsp;&nbsp;
    <label><fmt:message key="teacher"/>:
        <input type="text" name="teacher" value="${teacher != null ? teacher : ''}" placeholder="например petrov"/>
    </label>
    &nbsp;&nbsp;
    <button type="submit"><fmt:message key="apply"/></button>
    <a href="<c:url value='/progress'><c:param name='q' value='${q}'/><c:param name='lang' value='${lang}'/></c:url>">
        <fmt:message key="reset"/>
    </a>
</form>

<table>
    <thead>
    <tr>
        <th><fmt:message key="clazz"/></th>
        <th><fmt:message key="teacher"/></th>
        <th><fmt:message key="student"/></th>
        <th><fmt:message key="grade"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="r" items="${rows}">
        <tr>
            <td>${r.clazz()}</td>
            <td>${r.teacher()}</td>
            <td>${r.student()}</td>
            <td>${r.grade()}</td>
        </tr>
    </c:forEach>
    <c:if test="${empty rows}">
        <tr><td colspan="4"><em><fmt:message key="no.data"/></em></td></tr>
    </c:if>
    </tbody>
</table>

<p style="margin-top:14px">
    <a href="<c:url value='/'/>"><fmt:message key="back.home"/></a>
</p>

</body>
</html>