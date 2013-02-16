/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.util;

import iqq.service.HttpService;

/**
 *
 * @author shisoft
 */
public class HttpServiceFactory {

    StringBuilder cookie = new StringBuilder();

    public HttpService get(String url, String method, String contents) {
        HttpService hs = new HttpService(url, method, contents);
        hs.cookie = cookie;
        return hs;
    }

    public HttpService get(String url, String method) {
        HttpService hs = new HttpService(url, method);
        hs.cookie = cookie;
        return hs;
    }

    public HttpService get(String url, String method, String contents, String filename) {
        HttpService hs = new HttpService(url, method, contents, filename);
        hs.cookie = cookie;
        return hs;
    }

    public void setCookie(String cookie) {
        this.cookie = new StringBuilder(cookie);
    }

    public String getCookie() {
        return this.cookie.toString();
    }
}
