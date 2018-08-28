<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Admin Dashboard</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.html" %>

</head>
<body>
<div class="container">
  <div class="card mt-4">
    <div class="card-body">
      <h1>Welcome Admin!</h1>
      <h5>Hello ${admin.firstname} ${admin.lastname}! You are logged in as Administrator</h5>
    </div>
  </div>
	<div class="card">
		<div class="card-body">
			<h3>LIST OF SUBJECTS:</h3>
			<c:choose>
				<c:when test="${subjects==null || subjects.size() < 1 }">
					<p>There are no subjects</p>
				</c:when>
				<c:otherwise>
					<ul>
						<c:forEach var="subject" items="${subjects}">
							<li>${subject.subjectId}</li>
						</c:forEach>
					</ul>
				</c:otherwise>
			</c:choose>
			<ul>
			</ul>
		</div>
	</div>
	<div class="card">
		<div class="card-body">
			<h3>LIST OF ROOMS</h3>  	
			<c:choose>
				<c:when test="${rooms == null || rooms.size() < 1 }" >
			        <p>There are no rooms.</p>
			    </c:when>
			    <c:otherwise>
			  		<table class="table table-striped table-bordered">
						<thead class="thead-dark">
							<tr>
								<th>Room Name</th>
								<th>Capacity</th>
							</tr>
			  			</thead>
						<tbody>
							<c:forEach var="room" items="${rooms}">
								<tr>
									<td>${room.name} </td>
									<td>${room.capacity} </td>
								</tr>
							</c:forEach>
						</tbody>
			  		</table>
			 		</c:otherwise>
			</c:choose>
		</div>
	</div>

  	<div class="card">
  		<div class="card-body">
			<h3>LIST OF SECTIONS: </h3>  	
			<c:choose>
				<c:when test="${sections == null || sections.size() < 1 }" >
			        <p>There are no sections.</p>
			    </c:when>
			    <c:otherwise>
			  		<table class="table table-striped table-bordered">
						<thead class="thead-dark">
							<tr>
								<th>Section Id</th>
								<th>Subject Id</th>
								<th>Schedule</th>
								<th>Room name</th>
							</tr>
			  			</thead>
						<tbody>
							<c:forEach var="section" items="${sections}">
								<tr>
									<td>${section.sectionId} </td>
									<td>${section.subject.subjectId} </td>
									<td>${section.schedule}</td>
									<td>${section.room.name}</td>
								</tr>
							</c:forEach>
							<tr>
								<td colspan="4">
									<form action="add_section" method="post">
										<button type="submit" class="btn btn-primary btn-block">ADD SECTION</button>
									</form>
								</td>
							</tr>
						</tbody>
			  		</table>
		  		</c:otherwise>
	  		</c:choose>
  		</div>
  </div>
</div>

</body>
</html>