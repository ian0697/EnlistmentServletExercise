<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Section</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.html" %>
</head>
<body>

<div class="container">

	<form action="confirm_section" method="post">
	<div class="card">
		<div class="card-body">
		<h3>Add a new section:</h3>
		<table>
			<tr>
				<td>Input a section ID: </td>
				<td><input type="text" name="sectionId" required/>
			</tr>
			<tr>
				<td>Select a subject: </td>
				<td>
					<select name="subject" id="dropdown">
					    <c:forEach var="subject" items="${subjects}">
					        <option value="<c:out value='${subject}' />" <c:if test="${param.selectValue == subject})"> selected </c:if>>
					            <c:out value="${subject}" />
					        </option>
					    </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Select a room: </td>
				<td>
					<select name="room" id="dropdown">
					    <c:forEach var="room" items="${rooms}">
					        <option value="<c:out value='${room}' />" <c:if test="${param.selectValue == room})"> selected </c:if>>
					            <c:out value="${room}" />
					        </option>
					    </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Select a faculty member: </td>
				<td>
					<select name="fac" id="dropdown">
					    <c:forEach var="fac" items="${faculty}">
					        <option value="<c:out value='${fac.firstname}'/>" <c:if test="${param.selectValue == fac})"> selected </c:if>>
					            <c:out value="${fac.firstname} ${fac.lastname}" />
					        </option>
					    </c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" class="btn btn-primary" value="Add section" />
				</td>
			</tr>
			
		</table>
		</div><!-- card-body -->
	</div> <!-- card -->
	</form>
</div>

</body>
</html>