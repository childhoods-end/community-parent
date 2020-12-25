package com.community.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserDto {

    private String username;

    private String password;

    private int expiredSeconds;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getExpiredSeconds() {
        return expiredSeconds;
    }

    public void setExpiredSeconds(int expiredSeconds) {
        this.expiredSeconds = expiredSeconds;
    }

    @Override
    public String toString() {
        return "LoginUserDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", expiredSeconds=" + expiredSeconds +
                '}';
    }
}
