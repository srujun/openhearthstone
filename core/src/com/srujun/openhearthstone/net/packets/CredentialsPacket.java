package com.srujun.openhearthstone.net.packets;

public class CredentialsPacket {

    static public class LoginResponse {
        public enum Response {
            INVALID, ALREADY_EXISTS, NOT_FOUND, SUCCESSFUL
        }

        public Response message;
        public String suppliedUsername;

        public LoginResponse() {}
        public LoginResponse(Response message, String suppliedUsername) {
            this.message = message;
            this.suppliedUsername = suppliedUsername;
        }
    }

    public CredentialsPacket() {}
    public CredentialsPacket(boolean newUsername, String username) {
        this.isNewUsername = newUsername;
        this.username = username;
    }

    public boolean isNewUsername;
    public String username;
}
