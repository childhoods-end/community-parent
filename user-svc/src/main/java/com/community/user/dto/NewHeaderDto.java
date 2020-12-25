package com.community.user.dto;

public class NewHeaderDto {

    private int userId;

    private String headerUrl;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    @Override
    public String toString() {
        return "NewHeaderDto{" +
                "userId=" + userId +
                ", headerUrl='" + headerUrl + '\'' +
                '}';
    }
}
