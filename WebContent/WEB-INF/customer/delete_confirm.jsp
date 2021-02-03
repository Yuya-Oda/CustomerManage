<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean class="customer.CustomerBean" id="customer" scope="session" />
<!doctype html>
<html>
<head>
<title>顧客管理</title>
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <h1>顧客管理</h1>
<div class="main">
<table>
<tr>
<div class="title">
<h2>削除の確認</h2>
</div>
</tr>
<tr>
<td class="title">氏名</td>
<td><%=customer.getName() %></td>
</tr>
<tr>
<td class="title">郵便番号</td>
<td><%=customer.getZip() %></td>
</tr>
<tr>
<td class="title">住所１</td>
<td><%=customer.getAddress1() %></td>
</tr>
<tr>
<td class="title">住所2</td><!-- 任意入力 -->
<td><%=customer.getAddress2() %></td>
</tr>
<tr>
<td class="title">TEL</td>
<td><%=customer.getTel() %></td>
</tr>
<tr>
<td class="title">FAX</td><!-- 任意入力 -->
<td><%=customer.getFax() %></td>
</tr>
<tr>
<td class="title">E-mail</td>
<td><%=customer.getEmail() %></td>
</tr>
</table>
 <p>
 <form method = "post">
        <%-- --<input type="submit" value="送信"> --%>
        <button type="submit" name="state" value="delete"formaction="CustomerServlet">OK</button>
        <input type="button" value="戻る" onclick="history.back()">

</form>
 </p>
    </div>
</body>
</html>