package com.zhijia.service.data.Medol;

/**
 * 检查更新
 */
public class CheckUpdateModel {

    private Integer version;

   // private String version;
    private String url;
    private String versionname;   
  

	/*public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}*/

	public String getVersionname() {
		return versionname;
	}

	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}