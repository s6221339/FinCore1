package com.example.FinCore.vo.response;

/**
 * 回傳單一會員姓名的 Response
 */
public class MemberNameResponse extends BasicResponse {

    private MemberData memberData;

    public MemberNameResponse(int code, String message, MemberData memberData) {
        super(code, message);
        this.memberData = memberData;
    }

    public MemberData getMemberData() {
        return memberData;
    }

    public void setMemberData(MemberData memberData) {
        this.memberData = memberData;
    }

    // 內部靜態類別表示成員資料
    public static class MemberData {
        private String name;
        private String account; 

        public MemberData() {}

        public MemberData(String name, String account) {
            this.name = name;
            this.account = account;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
}