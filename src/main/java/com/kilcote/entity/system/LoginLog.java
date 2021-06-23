package com.kilcote.entity.system;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kilcote.entity._base.GenericEntity;
import com.kilcote.utils.DateUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Shun fu
 */

@Entity
@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name="t_login_log")
public class LoginLog extends GenericEntity {

	/**
	 * Login user
	 */
	@Column(name="email", nullable=false, length=64)
	private String email;

	/**
	 * Log in time
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern=DateUtil.FULL_TIME_SPLIT_PATTERN)
	@Column(name="login_time")
	private LocalDateTime loginTime;

	/**
	 * Login location
	 */
	@Column(name="location", nullable=true, length=256)
	private String location;
	/**
	 * Login IP
	 */
	@Column(name="ip", nullable=true, length=64)
	private String ip;
	/**
	 * operating system
	 */
	@Column(name="system", nullable=true, length=64)
	private String system;
	/**
	 * Login browser
	 */
	@Column(name="browser", nullable=true, length=64)
	private String browser;

	public void setSystemBrowserInfo(String ua) {
		try {
			StringBuilder userAgent = new StringBuilder("[");
			userAgent.append(ua);
			userAgent.append("]");
			int indexOfMac = userAgent.indexOf("Mac OS X");
			int indexOfWindows = userAgent.indexOf("Windows NT");
			int indexOfIe = userAgent.indexOf("MSIE");
			int indexOfIe11 = userAgent.indexOf("rv:");
			int indexOfFirefox = userAgent.indexOf("Firefox");
			int indexOfSogou = userAgent.indexOf("MetaSr");
			int indexOfChrome = userAgent.indexOf("Chrome");
			int indexOfSafari = userAgent.indexOf("Safari");
			boolean isMac = indexOfMac > 0;
			boolean isWindows = indexOfWindows > 0;
			boolean isLinux = userAgent.indexOf("Linux") > 0;
			boolean containIe = indexOfIe > 0 || (isWindows && (indexOfIe11 > 0));
			boolean containFireFox = indexOfFirefox > 0;
			boolean containSogou = indexOfSogou > 0;
			boolean containChrome = indexOfChrome > 0;
			boolean containSafari = indexOfSafari > 0;
			String browser = "";
			if (containSogou) {
				if (containIe) {
					browser = "Sogou" + userAgent.substring(indexOfIe, indexOfIe + "IE x.x".length());
				} else if (containChrome) {
					browser = "Sogou" + userAgent.substring(indexOfChrome, indexOfChrome + "Chrome/xx".length());
				}
			} else if (containChrome) {
				browser = userAgent.substring(indexOfChrome, indexOfChrome + "Chrome/xx".length());
			} else if (containSafari) {
				int indexOfSafariVersion = userAgent.indexOf("Version");
				browser = "Safari "
						+ userAgent.substring(indexOfSafariVersion, indexOfSafariVersion + "Version/x.x.x.x".length());
			} else if (containFireFox) {
				browser = userAgent.substring(indexOfFirefox, indexOfFirefox + "Firefox/xx".length());
			} else if (containIe) {
				if (indexOfIe11 > 0) {
					browser = "IE 11";
				} else {
					browser = userAgent.substring(indexOfIe, indexOfIe + "IE x.x".length());
				}
			}
			String os = "";
			if (isMac) {
				os = userAgent.substring(indexOfMac, indexOfMac + "MacOS X xxxxxxxx".length());
			} else if (isLinux) {
				os = "Linux";
			} else if (isWindows) {
				os = "Windows ";
				String version = userAgent.substring(indexOfWindows + "Windows NT".length(), indexOfWindows
						+ "Windows NTx.x".length());
				switch (version.trim()) {
				case "5.0":
					os += "2000";
					break;
				case "5.1":
					os += "XP";
					break;
				case "5.2":
					os += "2003";
					break;
				case "6.0":
					os += "Vista";
					break;
				case "6.1":
					os += "7";
					break;
				case "6.2":
					os += "8";
					break;
				case "6.3":
					os += "8.1";
					break;
				case "10":
					os += "10";
				default:
					break;
				}
			}
			this.system = os;
			this.browser = StringUtils.replace(browser, "/", " ");
		} catch (Exception e) {
			log.error("Failed to obtain login information：{}", e.getMessage());
			this.system = "";
			this.browser = "";
		}
	}
}
