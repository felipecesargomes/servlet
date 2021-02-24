<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:url value="/usuario" var="link" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Titulo Aqui!</title>
</head>
<body>
	<form method="post" action="${link}">
		<label for="nome">Nome do Usuário</label> <input type="text" id="nome"
			name="nome"></input> <input type="submit" value="Cadastrar"></input>
	</form>
</body>
</html>