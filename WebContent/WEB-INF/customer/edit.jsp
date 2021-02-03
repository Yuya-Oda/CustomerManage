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
    <h2>既存データの編集</h2>
    <form action="CustomerServlet" method="post">
        <table>
            <tr>
            <td class="title">氏名</td>
            <td><input type="text" name="customer_name"  value=" <%= customer.getName() %>"  maxlength="20"></td>
            </tr>
            <tr>
            <td class="title">郵便番号</td>
            <td><input type="text" name="zip"  value=" <%= customer.getZip() %>"  maxlength="20"></td>
            </tr>
            <tr>
            <td class="title">住所１</td>
            <td><input type="text" name="address1"  value=" <%= customer.getAddress1() %>"  maxlength="100"></td>
            </tr>
            <tr>
            <td class="title">住所2</td>
            <!-- 任意入力 -->
            <td><input type="text" name="address2"  value=" <%= customer.getAddress2() %>"  maxlength=""></td>
            </tr>
            <tr>
            <td class="title">TEL</td>
            <td><input type="text" name="tel"  value=" <%= customer.getTel() %>"  maxlength="100"></td>
            </tr>
            <tr>
            <td class="title">FAX</td>
            <!-- 任意入力 -->
            <td><input type="text" name="fax"  value=" <%= customer.getFax() %>"  maxlength="20"></td>
            </tr>
            <tr>
            <td class="title">E-mail</td>
            <td><input type="text" name="email"  value=" <%= customer.getEmail() %>"  maxlength="20"></td>
            </tr>
        </table>


            <p>
                <%-- --<input type="submit" value="送信"> --%>
                <button type="submit" name="state" value="edit_confirm">送信</button>
                <input type="button" value="戻る" onclick="history.back()">
            </p>
        </form>
    </div>
</body>
<script type="text/javascript">
function funcConfirm() {

    //正規表現の変数宣言
    var regaxZip = /^\d{3}-\d{4}$/;//XXX‐XXXXの形式
    var regaxEmail = /^[A-Za-z0-9]{1}[A-Za-z0-9_.-]*@{1}[A-Za-z0-9_.-]{1,}\.[A-Za-z0-9]{1,}$/;
    var regaxTel = /^0\d{2,3}-\d{1,4}-\d{4}$/;

        if (document.form1.customer_name.value == "") {
            alert("氏名が入力されていません。");
            return false;
        }
        if (!document.form1.zip.value.match(regaxZip)) {
            alert("郵便番号はハイフンを入れた形式で入力してください");
            return false;
        }
        if (document.form1.address1.value == "") {
            alert("住所１が入力されていません。");
            return false;
        }
        if (document.form1.tel.value.match(regaxTel)) {
            alert("電話番号の入力が正しくありません。");
            return false;
        }
        if (!document.form1.zip.value.match(regaxEmail)) {
            alert("E-mailの入力が正しくありません");
            return false;
        }

}
</script>
</html>
