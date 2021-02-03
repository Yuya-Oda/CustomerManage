<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean class="user.UserBean" id="user" scope="session" />
<jsp:useBean class="customer.CustomerBean" id="customer" scope="session" />
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>顧客管理</title>
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <h1>顧客管理</h1>
    <div class="main">
        <h2>修正の確認</h2>
        <table>
            <tr>
            <td class="title">氏名</td>
            <td><%=customer.getName()%></td>
            </tr>
            <tr>
            <td class="title">郵便番号</td>
            <td><%=customer.getZip()%></td>
            </tr>
            <tr>
            <td class="title">住所１</td>
            <td><%=customer.getAddress1()%></td>
            </tr>
            <tr>　
            <td class="title">住所2</td>
            <!-- 任意入力 -->
            <td><%=customer.getAddress2()%></td>
            </tr>
            <tr>
            <td class="title">TEL</td>
            <td><%=customer.getTel()%></td>
            </tr>
            <tr>
            <td class="title">FAX</td>
            <!-- 任意入力 -->
            <td><%=customer.getFax()%></td>
            </tr>
            <tr>
                <td class="title">E-mail</td>
                <td><%=customer.getEmail()%></td>
            </tr>
        </table>

        <form action="CustomerServlet" method="post">
            <p>
                <%-- --<input type="submit" value="送信"> --%>
                <button type="submit" name="state" value="update">OK</button>
                <input type="button" value="戻る" onclick="history.back()">
            </p>
        </form>
    </div>
</body>
</html>
