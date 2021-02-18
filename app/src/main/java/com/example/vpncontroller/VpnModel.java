package com.example.vpncontroller;

/***
 *     public static final int TYPE_PPTP = 0;
 *     public static final int TYPE_L2TP_IPSEC_PSK = 1;
 *     public static final int TYPE_L2TP_IPSEC_RSA = 2;
 *     public static final int TYPE_IPSEC_XAUTH_PSK = 3;
 *     public static final int TYPE_IPSEC_XAUTH_RSA = 4;
 *     public static final int TYPE_IPSEC_HYBRID_RSA = 5;
 *     public static final int TYPE_MAX = 5;
 */

public class VpnModel {
    private int type;
    private String name;
    private String server;
    private String username;
    private String password;
    // PPTP
    private boolean mppe;               // 是否启用PPP加密(MPPE)

    // L2TP
    private String l2tpSecret;          // L2TP密钥
    private String ipsecIdentifier;     // IPSec标识符
    private String ipsecSecret;         // IPSec预共享密钥

    private boolean saveLogin;

    public VpnModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

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

    public boolean isMppe() {
        return mppe;
    }

    public void setMppe(boolean mppe) {
        this.mppe = mppe;
    }

    public String getL2tpSecret() {
        return l2tpSecret;
    }

    public void setL2tpSecret(String l2tpSecret) {
        this.l2tpSecret = l2tpSecret;
    }

    public String getIpsecIdentifier() {
        return ipsecIdentifier;
    }

    public void setIpsecIdentifier(String ipsecIdentifier) {
        this.ipsecIdentifier = ipsecIdentifier;
    }

    public String getIpsecSecret() {
        return ipsecSecret;
    }

    public void setIpsecSecret(String ipsecSecret) {
        this.ipsecSecret = ipsecSecret;
    }

    public boolean isSaveLogin() {
        return saveLogin;
    }

    public void setSaveLogin(boolean saveLogin) {
        this.saveLogin = saveLogin;
    }
}
