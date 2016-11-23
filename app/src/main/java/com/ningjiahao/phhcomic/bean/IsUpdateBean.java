package com.ningjiahao.phhcomic.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 甯宁寧 on 2016-11-23.
 */

public class IsUpdateBean {
    /**
     * status : 0
     * error : ok
     * data : {"version":2,"vsersion_url":"http://oh0vbg8a6.bkt.clouddn.com/app-debug.apk"}
     */

    @SerializedName("status")
    private int status;
    @SerializedName("error")
    private String error;
    @SerializedName("data")
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * version : 2
         * vsersion_url : http://oh0vbg8a6.bkt.clouddn.com/app-debug.apk
         */

        @SerializedName("version")
        private int version;
        @SerializedName("vsersion_url")
        private String vsersionUrl;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getVsersionUrl() {
            return vsersionUrl;
        }

        public void setVsersionUrl(String vsersionUrl) {
            this.vsersionUrl = vsersionUrl;
        }
    }
}
