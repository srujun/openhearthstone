package com.srujun.openhearthstone.net.packets;

public class CredentialsPacket {

    static public class LoginResponse {
        public enum Response {
            INVALID, ALREADY_EXISTS, NOT_FOUND, SUCCESSFUL
        }

        public LoginResponse() {}
        public LoginResponse(Response message) {
            this.message = message;
        }

        public Response message;
    }

    public CredentialsPacket() {}
    public CredentialsPacket(boolean newUsername, String username) {
        this.isNewUsername = newUsername;
        this.username = username;
    }

    public boolean isNewUsername;
    public String username;
}

