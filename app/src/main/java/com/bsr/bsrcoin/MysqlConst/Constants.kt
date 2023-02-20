package com.bsr.bsrcoin.MysqlConst

class Constants {
    companion object {
        const val ROOT_URL = "https://9308-116-73-37-184.in.ngrok.io/Ewallet/"

        // User
        const val url_login = ROOT_URL + "user/login.php"
        const val url_register = ROOT_URL + "user/register.php"
        const val url_agent = ROOT_URL + "user/getagent.php"
        const val url_reference = ROOT_URL + "user/reference.php"
        const val url_read = ROOT_URL + "user/read.php?id="
        const val url_update_wallet_coin_val = ROOT_URL + "user/updatewalletcoinval.php"
        const val url_getid = ROOT_URL + "user/getid.php"
        const val url_get_userInfo = ROOT_URL + "user/read"
        const val url_upload_docs = ROOT_URL + "user/adddocuments.php"
        const val url_get_docs = ROOT_URL + "user/getdocuments.php"
        const val url_buy_coin = ROOT_URL + "user/buy.php"
        const val url_set_token = ROOT_URL + "user/settoken.php"

        // Cheque
        const val url_add_cheque = ROOT_URL + "user/addcheque.php"
        const val url_accept_cheque = ROOT_URL + "cheque/acceptcheque.php"
        const val url_get_cheque = ROOT_URL + "cheque/getcheque.php?userId="
        const val url_send_cheque = ROOT_URL + "cheque/sendcheque.php"

        // Loan
        const val url_loan_create = ROOT_URL + "loan/create.php"
        const val url_loan_update = ROOT_URL + "loan/update.php"
        const val url_loan_status = ROOT_URL + "loan/status.php"
        const val url_loan_view = ROOT_URL + "loan/view.php"
        const val url_loan_request = ROOT_URL + "loan/requestupdate.php"
        const val url_loan_request_inr = ROOT_URL + "loan/requestinr.php"
        const val url_loan_image = ROOT_URL + "loan/loan_images/"

        // Insurance
        const val url_insurance_create = ROOT_URL + "insurance/create.php"
        const val url_insurance_update = ROOT_URL + "insurance/update.php"
        const val url_insurance_status = ROOT_URL + "insurance/status.php"
        const val url_insurance_view = ROOT_URL + "insurance/view.php"
        const val url_insurance_image = ROOT_URL + "insurance/insurance_image/"
        const val url_insurance_request = ROOT_URL + "insurance/requestupdate.php"

        // Payment Interface
        const val url_razorpay = ROOT_URL + "razorpay/api.php?amount="

        // coin
        const val url_get_coin_price = ROOT_URL + "coin/read.php"

        //Transaction
        const val url_sendMoney = ROOT_URL + "user/sendmoney.php"
        const val url_take_permission = ROOT_URL + "user/askpermission.php "
        const val url_get_id = ROOT_URL + "user/getid.php"
        const val url_create_transaction = ROOT_URL + "transaction/create"
        const val url_get_walletId = ROOT_URL + "wallet/getid.php"

        //wallet
        const val url_add_wallet_coin = ROOT_URL + "wallet/addcoin.php"
        const val url_get_requests = ROOT_URL + "user/getaddrequest.php"
        const val url_set_status = ROOT_URL + "user/setaddstatus.php"
        const val url_get_requests_image = ROOT_URL + "user/payment_images/"

        //deposit
        const val url_create_deposit = ROOT_URL + "deposit/create"
        const val url_renew_deposit = ROOT_URL + "deposit/update"
        const val url_pay_deposit_due = ROOT_URL + "deposit/payDeposit.php"

        //firebase
        const val BASE_URL = "https://fcm.googleapis.com"
        const val SERVER_KEY = "AAAAQQ_32vQ:APA91bHguCpw-V8wmAXChY7N5gqp5oMGS7dahN9fg6Yxib3dgngedGcL8SB5CrPAzS3o_xqCU7lLwY6bHRJykKhUVZOHxRVg4gG72gbuf4iwZIqNMQNXn_kfSoWA0pCZimdNow4iRf01"
        const val CONTENT_TYPE = "application/json"

        //permissiondetails
        const val url_PermissionDetails = ROOT_URL + "permissiondetails/permissiondetails.php"
        const val url_PermissionRead= ROOT_URL + "permissiondetails/permissionread.php"
    }
}