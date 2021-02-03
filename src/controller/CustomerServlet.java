package controller;

import static constants.MessageConstants.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import customer.CustomerBean;
import customer.CustomerListBean;
import customer.CustomerListLogic;
import customer.CustomerLogic;
import user.UserBean;
import user.UserLogic;
import util.LogUtil;
import util.StringUtil;

/**
 * 顧客管理のサーブレット
 */
@WebServlet("/CustomerServlet")
public class CustomerServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ
     * @see BaseServlet#BaseServlet()
     */
    public CustomerServlet() {
        super();
    }

    /**
     * HTTPのPOSTメソッド受信時に呼び出される処理
     * <pre>
     * セッションに含まれるstate属性の値に応じて
     * 顧客管理固有の処理を行う
     * </pre>
     * @see BaseServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        LogUtil.println("**** " + this.getClass().getSimpleName() + "#doPost *****");

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        UserBean user = (UserBean) session.getAttribute("user");

        if ((user == null) || (user.getName() == null)) {
            procSessionError(request, response, session);
            return;
        }

        UserLogic userLogic = new UserLogic();
        user = userLogic.load(user.getId());
        session.setAttribute("user", user);

        String param = request.getParameter("state");
        String[] state = param.split(",");

        LogUtil.println("state: " + state[0]);

        if (state[0] == null) {
            procSessionError(request, response, session);
            return;
        }

        switch (state[0]) {
        // 検索条件
        case "search":
            procSearch(request, response);
            break;
        // 検索結果
        case "list":
            procList(request, response);
            break;
        // 詳細
        case "detail":
            procDetail(request, response, session, state[1]);
            break;
        // 更新完了/更新未完了
        case "update":
            procUpdate(request, response, session);
            break;
        // 新規登録
        case "new":
            procNew(request, response);
            break;
        // 新規登録完了/新規登録未完了
        case "add":
            procAdd(request, response, session);
            break;
        // 削除確認
        case "delete_confirm":
            procDeleteConfirm(request, response, state[1]);
            break;
        // 削除完了/削除未完了
        case "delete":
            procDelete(request, response, session);
            break;
        // 既存データ編集･削除
        case "edit":
            procEdit(request, response);
            break;
        // 編集確認
        case "edit_confirm":
            procEditConfirm(request, response);
            break;
        // 入力確認
        case "new_confirm":
            procNewConfirm(request, response);
            break;
        // セッション･エラー
        default:
            procSessionError(request, response, session);
            break;
        }
    }

    /**
     * 検索条件画面に遷移する
     * <pre>
     * state属性＝"search"時の処理
     * </pre>
     * @param request   HTTPのリクエスト
     * @param response  HTTPのレスポンス
     */
    private void procSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/customer/search.jsp").forward(request, response);
    }

    /**
     * リクエスト内のserch属性の値と、顧客情報の名前で、部分一致する全レコードをDBから取得し、検索結果画面に遷移する
     * <pre>
     * state属性＝"list"時の処理
     * </pre>
     * @param request   HTTPのリクエスト
     * @param response  HTTPのレスポンス
     */
    private void procList(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException, ServletException, IOException {
        String strSearch = StringUtil.exchangeESCEncoding(request.getParameter("search"));
        CustomerListLogic customerListLogic = new CustomerListLogic();
        ArrayList<CustomerBean> alCustomer = customerListLogic.search(strSearch);
        CustomerListBean customerList = new CustomerListBean();
        customerList.setAlCustomer(alCustomer);
        request.setAttribute("customerList", customerList);

        getServletContext().getRequestDispatcher("/WEB-INF/customer/list.jsp").forward(request, response);
    }

    /**
     * 表示対象の顧客情報をDBから取得し、詳細画面に遷移する
     * <pre>
     * state属性＝"detail"時の処理
     * </pre>
     * @param request   HTTPのリクエスト
     * @param response  HTTPのレスポンス
     * @param session   HTTPのリクエストに含まれるセッション
     * @param id        表示対象の顧客情報のID(HTTPのリクエストに含まれるセッションのstate属性の2つ目の値)
     */
    private void procDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session, String id)
            throws ServletException, IOException {
        int intId = Integer.parseInt(id);
        System.out.println(Integer.parseInt(id));

        CustomerLogic customerLogic = new CustomerLogic();
        CustomerBean customer = null;
        customer = customerLogic.load(intId);
        session.setAttribute("customer", customer);

        getServletContext().getRequestDispatcher("/WEB-INF/customer/detail.jsp").forward(request, response);
    }

    /**
     * セッションから顧客情報を取得し、DB更新処理後、更新完了(成功時)画面、または、更新未完了(失敗時)画面に遷移する
     * <pre>
     * state属性＝"update"時の処理
     * </pre>
     * @param request   HTTPのリクエスト
     * @param response  HTTPのレスポンス
     * @param session   HTTPのリクエストに含まれるセッション
     */
    private void procUpdate(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        UserBean user = (UserBean) session.getAttribute("user");
        if (user.getLvl() != 1) {
            session.setAttribute("errMessage", MESSAGE_NO_OPERATION_PRIVILEGE);
            getServletContext().getRequestDispatcher("/WEB-INF/user/error.jsp").forward(request, response);
            return;
        }

        CustomerLogic customerLogic = new CustomerLogic();
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
        session.setAttribute("customer", customer);
        String errMessage = null;
        errMessage = customerLogic.update(customer);
        if (errMessage == null) {
            session.setAttribute("customer", customer);
            getServletContext().getRequestDispatcher("/WEB-INF/customer/update_success.jsp").forward(request, response);
        } else {
            session.setAttribute("errMessage", errMessage);
            getServletContext().getRequestDispatcher("/WEB-INF/customer/update_fail.jsp").forward(request, response);
        }
    }

    /**
     * 新規登録画面に遷移する
     * <pre>
     * state属性＝"new"時の処理
     * </pre>
     * @param request   HTTPのリクエスト
     * @param response  HTTPのレスポンス
     */
    private void procNew(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        getServletContext().getRequestDispatcher("/WEB-INF/customer/new.jsp").forward(request, response);
    }

    /**
     * セッションから顧客情報を取得し、DB追加処理後、新規登録完了(成功時)画面、または、新規登録未完了(失敗時)画面に遷移する
     * <pre>
     * state属性＝"add"時の処理
     * </pre>
     * @param request   HTTPのリクエスト
     * @param response  HTTPのレスポンス
     * @param session   HTTPのリクエストに含まれるセッション
     */
    private void procAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        //
        UserBean user = (UserBean) session.getAttribute("user");
        if (user.getLvl() != 2) {
            session.setAttribute("errMessage", MESSAGE_NO_OPERATION_PRIVILEGE);
            getServletContext().getRequestDispatcher("/WEB-INF/user/error.jsp").forward(request, response);
            return;
        }

        CustomerLogic customerLogic = new CustomerLogic();
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
        session.setAttribute("customer", customer);
        String errMessage = null;
        errMessage = customerLogic.add(customer);
        if (errMessage == null) {
            session.setAttribute("customer", customer);
            getServletContext().getRequestDispatcher("/WEB-INF/customer/add_success.jsp").forward(request, response);
        } else {
            session.setAttribute("errMessage", errMessage);
            getServletContext().getRequestDispatcher("/WEB-INF/customer/add_fail.jsp").forward(request, response);
        }
    }

    /**
     * 顧客情報をDBから取得し、削除確認画面に遷移する
     * <pre>
     * state属性＝"delete_confirm"時の処理
     * </pre>
     * @param request   HTTPのリクエスト
     * @param response  HTTPのレスポンス
     * @param id        削除対象の顧客情報のID(HTTPのリクエストに含まれるセッションのstate属性の2つ目の値)
     */
    private void procDeleteConfirm(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        int intId = Integer.parseInt(id);

        CustomerLogic customerLogic = new CustomerLogic();
        CustomerBean customer = null;
        customer = customerLogic.load(intId);
        HttpSession session = request.getSession();
        session.setAttribute("customer", customer);
        getServletContext().getRequestDispatcher("/WEB-INF/customer/delete_confirm.jsp").forward(request, response);
    }

    /**
     * セッションから顧客情報を取得し、DB削除処理後、削除完了(成功時)画面、または、削除未完了(失敗時)画面に遷移する
     * <pre>
     * state属性＝"delete"時の処理
     * </pre>
     * @param request   HTTPのリクエスト
     * @param response  HTTPのレスポンス
     * @param session   HTTPのリクエストに含まれるセッション
     */
    private void procDelete(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        String errMessage = null;
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
        CustomerLogic customerLogic = new CustomerLogic();
        errMessage = customerLogic.delete(customer);

        session.removeAttribute("customer");

        if (errMessage == null) {
            getServletContext().getRequestDispatcher("/WEB-INF/customer/delete_success.jsp").forward(request, response);
        } else {
            session.setAttribute("errMessage", errMessage);

            getServletContext().getRequestDispatcher("/WEB-INF/customer/delete_fail.jsp").forward(request, response);
        }
    }

    /**
     * 既存データ編集･削除画面に遷移する
     * <pre>
     * state属性＝"edit"時の処理
     * </pre>
     * @param request   HTTPのリクエスト
     * @param response  HTTPのレスポンス
     */
    private void procEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");

        session.setAttribute("customer", customer);

        // フォワード
        getServletContext().getRequestDispatcher("/WEB-INF/customer/edit.jsp").forward(request, response);
    }

    /**
     * リクエスト内の顧客情報をセッションに設定し、編集確認画面に遷移する
     * <pre>
     * state属性＝"edit_confirm"時の処理
     * </pre>
     * @param request   HTTPのリクエスト
     * @param response  HTTPのレスポンス
     */
    private void procEditConfirm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");

        String name = request.getParameter("customer_name");
        String zip = request.getParameter("zip");
        String address1 = request.getParameter("address1");
        String address2 = request.getParameter("address2");
        String tel = request.getParameter("tel");
        String fax = request.getParameter("fax");
        String email = request.getParameter("email");

        System.out.println(name);

        // 登録するユーザーの情報を設定
        customer.setName(name);
        customer.setZip(zip);
        customer.setAddress1(address1);
        customer.setAddress2(address2);
        customer.setTel(tel);
        customer.setFax(fax);
        customer.setEmail(email);
        System.out.println(customer.getName());

        //      customer = new CustomerBean(name, zip, address1, address2, tel, fax, email);

        // セッションスコープに登録ユーザーを保存
        session.setAttribute("customer", customer);
        //
        getServletContext().getRequestDispatcher("/WEB-INF/customer/edit_confirm.jsp").forward(request, response);
    }

    /**
     * リクエスト内の顧客情報をセッションに設定し、入力確認画面に遷移する
     * <pre>
     * state属性＝"new_confirm"時の処理
     * </pre>
     * @param request   HTTPのリクエスト
     * @param response  HTTPのレスポンス
     */
    private void procNewConfirm(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException, UnsupportedEncodingException {
        //パラメータ取得

        HttpSession session = request.getSession();
        session.removeAttribute("customer");

        //入力された値の、リクエストパラメータの取得
        request.setCharacterEncoding("UTF-8");
        String name = "";
        String zip = "";
        String address1 = "";
        String address2 = "";
        String tel = "";
        String fax = "";
        String email = "";
        name = request.getParameter("customer_name");
        zip = request.getParameter("zip");
        address1 = request.getParameter("address1");
        address2 = request.getParameter("address2");
        tel = request.getParameter("tel");
        fax = request.getParameter("fax");
        email = request.getParameter("email");
        //入力されたユーザーの値を取得
        CustomerBean customer = new CustomerBean(name, zip, address1, address2, tel, fax, email);

        //セッションスコープに保存
        session.setAttribute("customer", customer);
        getServletContext().getRequestDispatcher("/WEB-INF/customer/new_confirm.jsp").forward(request, response);
    }

}