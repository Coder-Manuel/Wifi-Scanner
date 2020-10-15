package com.example.wifi_scanner;

public class items {
    String ipaddress;
    String connection;

    public items(String ipaddress, String connection, String macadress) {
        this.ipaddress = ipaddress;
        this.connection = connection;
        this.macadress = macadress;
    }

    public String getConnection() {
        return connection;
    }

    String macadress;

    public items(String ipaddress, String macadress) {
        this.ipaddress = ipaddress;
        this.macadress = macadress;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public String getMacadress() {
        return macadress;
    }

    public items(String ipaddress) {
        this.ipaddress = ipaddress;
    }
}
