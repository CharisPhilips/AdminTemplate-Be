package com.kilcote.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kilcote.entity._base.GenericEntity;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "t_theme_palette")
public class ThemePalette extends GenericEntity {

	@JsonProperty(value="themeKey")
	@CsvBindByName(column="themeKey")
	@CsvBindByPosition(position=0)
    @Column(name="theme_key", nullable=false, length=128)
	private String themeKey;
	
	@JsonProperty(value="themeName")
	@CsvBindByName(column="themeName")
	@CsvBindByPosition(position=1)
	@Column(name="theme_name", nullable=false, length=128)
	private String themeName;

	//dark
	@JsonProperty(value="darkPrimaryLight")
	@CsvBindByName(column="darkPrimaryLight")
	@CsvBindByPosition(position=2)
    @Column(name="dark_primary_light", nullable=false, length=32)
    private String darkPrimaryLight;
	
	@JsonProperty(value="darkPrimaryMain")
	@CsvBindByName(column="darkPrimaryMain")
	@CsvBindByPosition(position=3)
    @Column(name="dark_primary_main", nullable=false, length=32)
    private String darkPrimaryMain;
    
	@JsonProperty(value="darkPrimaryDark")
	@CsvBindByName(column="darkPrimaryDark")
	@CsvBindByPosition(position=4)
    @Column(name="dark_primary_dark", nullable=false, length=32)
    private String darkPrimaryDark;
    
	@JsonProperty(value="darkPrimaryContrast")
	@CsvBindByName(column="darkPrimaryContrast")
	@CsvBindByPosition(position=5)
    @Column(name="dark_primary_contrast", nullable=false, length=32)
    private String darkPrimaryContrast;
    
	@JsonProperty(value="darkSecondaryLight")
	@CsvBindByName(column="darkSecondaryLight")
	@CsvBindByPosition(position=6)
    @Column(name="dark_secondary_light", nullable=false, length=32)
    private String darkSecondaryLight;
    
	@JsonProperty(value="darkSecondaryMain")
	@CsvBindByName(column="darkSecondaryMain")
	@CsvBindByPosition(position=7)
    @Column(name="dark_secondary_main", nullable=false, length=32)
    private String darkSecondaryMain;
    
	@JsonProperty(value="darkSecondaryDark")
	@CsvBindByName(column="darkSecondaryDark")
	@CsvBindByPosition(position=8)
    @Column(name="dark_secondary_dark", nullable=false, length=32)
    private String darkSecondaryDark;
    
	@JsonProperty(value="darkSecondaryContrast")
	@CsvBindByName(column="darkSecondaryContrast")
	@CsvBindByPosition(position=9)
    @Column(name="dark_secondary_contrast", nullable=false, length=32)
    private String darkSecondaryContrast;
    
    //light
	@JsonProperty(value="lightPrimaryLight")
	@CsvBindByName(column="lightPrimaryLight")
	@CsvBindByPosition(position=10)
    @Column(name="light_primary_light", nullable=false, length=32)
    private String lightPrimaryLight;
    
	@JsonProperty(value="lightPrimaryMain")
	@CsvBindByName(column="lightPrimaryMain")
	@CsvBindByPosition(position=11)
    @Column(name="light_primary_main", nullable=false, length=32)
    private String lightPrimaryMain;
    
	@JsonProperty(value="lightPrimaryDark")
	@CsvBindByName(column="lightPrimaryDark")
	@CsvBindByPosition(position=12)
    @Column(name="light_primary_dark", nullable=false, length=32)
    private String lightPrimaryDark;

	@JsonProperty(value="lightPrimaryContrast")
	@CsvBindByName(column="lightPrimaryContrast")
	@CsvBindByPosition(position=13)
    @Column(name="light_primary_contrast", nullable=false, length=32)
    private String lightPrimaryContrast;
    
	@JsonProperty(value="lightSecondaryLight")
	@CsvBindByName(column="lightSecondaryLight")
	@CsvBindByPosition(position=14)
    @Column(name="light_secondary_light", nullable=false, length=32)
    private String lightSecondaryLight;
    
	@JsonProperty(value="lightSecondaryMain")
	@CsvBindByName(column="lightSecondaryMain")
	@CsvBindByPosition(position=15)
    @Column(name="light_secondary_main", nullable=false, length=32)
    private String lightSecondaryMain;
    
	@JsonProperty(value="lightSecondaryDark")
	@CsvBindByName(column="lightSecondaryDark")
	@CsvBindByPosition(position=16)
    @Column(name="light_secondary_dark", nullable=false, length=32)
    private String lightSecondaryDark;
    
	@JsonProperty(value="lightSecondaryContrast")
	@CsvBindByName(column="lightSecondaryContrast")
	@CsvBindByPosition(position=17)
    @Column(name="light_secondary_contrast", nullable=false, length=32)
    private String lightSecondaryContrast;
    
    //theme
	@JsonProperty(value="themePrimaryLight")
	@CsvBindByName(column="themePrimaryLight")
	@CsvBindByPosition(position=18)
    @Column(name="theme_primary_light", nullable=false, length=32)
    private String themePrimaryLight;
    
	@JsonProperty(value="themePrimaryMain")
	@CsvBindByName(column="themePrimaryMain")
	@CsvBindByPosition(position=19)
    @Column(name="theme_primary_main", nullable=false, length=32)
    private String themePrimaryMain;
    
	@JsonProperty(value="themePrimaryDark")
	@CsvBindByName(column="themePrimaryDark")
	@CsvBindByPosition(position=20)
    @Column(name="theme_primary_dark", nullable=false, length=32)
    private String themePrimaryDark;
    
	@JsonProperty(value="themePrimaryContrast")
	@CsvBindByName(column="themePrimaryContrast")
	@CsvBindByPosition(position=21)
    @Column(name="theme_primary_contrast", nullable=false, length=32)
    private String themePrimaryContrast;
    
	@JsonProperty(value="themeSecondaryLight")
	@CsvBindByName(column="themeSecondaryLight")
	@CsvBindByPosition(position=22)
    @Column(name="theme_secondary_light", nullable=false, length=32)
    private String themeSecondaryLight;
    
	@JsonProperty(value="themeSecondaryMain")
	@CsvBindByName(column="themeSecondaryMain")
	@CsvBindByPosition(position=23)
    @Column(name="theme_secondary_main", nullable=false, length=32)
    private String themeSecondaryMain;
    
	@JsonProperty(value="themeSecondaryDark")
	@CsvBindByName(column="themeSecondaryDark")
	@CsvBindByPosition(position=24)
    @Column(name="theme_secondary_dark", nullable=false, length=32)
    private String themeSecondaryDark;
    
	@JsonProperty(value="themeSecondaryContrast")
	@CsvBindByName(column="themeSecondaryContrast")
	@CsvBindByPosition(position=25)
    @Column(name="theme_secondary_contrast", nullable=false, length=32)
    private String themeSecondaryContrast;

}