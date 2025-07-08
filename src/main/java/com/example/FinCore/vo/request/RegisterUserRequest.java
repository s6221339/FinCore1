package com.example.FinCore.vo.request;

import com.example.FinCore.constants.ConstantsMessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RegisterUserRequest {

    @NotBlank(message = ConstantsMessage.PARAM_ACCOUNT_ERROR)
    private String account;

    @NotBlank(message = ConstantsMessage.PARAM_NAME_ERROR)
    private String name;

    @NotBlank(message = ConstantsMessage.PARAM_PASSWORD_ERROR)
    @Pattern(regexp = ConstantsMessage.PASSWORD_PATTERN, message = ConstantsMessage.PARAM_PASSWORD_FORMAT_ERROR)
    private String password;

    @NotBlank(message = ConstantsMessage.PARAM_CODE_ERROR)
    private String code;

    private String phone;

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
