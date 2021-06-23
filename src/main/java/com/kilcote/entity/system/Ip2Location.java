package com.kilcote.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kilcote.entity._base.GenericEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "t_ip2location")
public class Ip2Location extends GenericEntity {
	
	/**
	 * ip from
	 */
	@Column(name="ip_from", nullable=false, length=20)
	@JsonProperty(value="ipFrom")
	private Long ipFrom;
	
	/**
	 * ip to
	 */
	@Column(name="ip_to", nullable=false, length=20)
	@JsonProperty(value="ipTo")
	private Long ipTo;

	/**
	 * countryCode
	 */
	@Column(name="country_code", nullable=false, length=2)
	@JsonProperty(value="countryCode")
	private String countryCode;
	
	/**
	 * countryName
	 */
	@Column(name="country_name", nullable=false, length=64)
	@JsonProperty(value="countryName")
	private String countryName;
	
	/**
	 * regionName
	 */
	@Column(name="region_name", nullable=false, length=128)
	@JsonProperty(value="regionName")
	private String regionName;
	
	/**
	 * regionName
	 */
	@Column(name="city_name", nullable=false, length=128)
	@JsonProperty(value="cityName")
	private String cityName;
	
	/**
	 * latitude
	 */
	@Column(name="latitude", nullable=false)
	@JsonProperty(value="latitude")
	private Double latitude;
	
	
	/**
	 * longitude
	 */
	@Column(name="longitude", nullable=false)
	@JsonProperty(value="longitude")
	private Double longitude;
	
	@Override
	public String toString() {
		return String.format("%s %s %s", countryName, regionName, cityName);
	}
	
}