package com.community.user.dto;

public class ActivateDto {

    private int userId;

    private String code;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ActivateDto{" +
                "userId=" + userId +
                ", code='" + code + '\'' +
                '}';
    }
}
