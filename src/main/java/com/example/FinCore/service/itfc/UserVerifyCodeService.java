package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.UpdatePwdByEmailRequest;
import com.example.FinCore.vo.response.BasicResponse;

public interface UserVerifyCodeService {

	/**
     * 忘記密碼－重設密碼
     * 會員通過驗證後可使用此方法重設密碼，未驗證或驗證過期時不可重設。
     * @param req 包含帳號（email）、新密碼
     * @return BasicResponse 執行結果（成功/失敗/錯誤訊息）
     */
	public BasicResponse updatePwdByEmail(UpdatePwdByEmailRequest req);
	
	 /**
     * 發送驗證信
     * 產生一組驗證碼並寄送到會員的 email 信箱。
     * @param account 會員帳號（Email）
     * @return BasicResponse 執行結果（驗證信發送成功/查無此帳號）
     */
	public BasicResponse sendVerifyCode(String account);

	/**
     * 認證驗證碼
     * 比對指定帳號的驗證碼是否正確且未過期，成功後將會員設為已驗證。
     * @param code 驗證碼
     * @param account 會員帳號（Email）
     * @return BasicResponse 執行結果（驗證成功/驗證失敗/驗證碼過期/查無資料）
     */
	public BasicResponse checkVerifyCode(String code, String account);
	
	/**
     * 註冊會員時發送驗證信
     * @param account 新會員帳號(Email)
     * @return 驗證碼寄送結果
     */
    public BasicResponse sendRegisterVerifyCode(String account);

    /**
     * 註冊會員時驗證驗證碼
     * @param code 驗證碼
     * @param account 新會員帳號(Email)
     * @return 驗證結果
     */
    public BasicResponse checkRegisterVerifyCode(String code, String account);
}
