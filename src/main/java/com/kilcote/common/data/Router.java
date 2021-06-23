package com.kilcote.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Router<T> implements Serializable {

    private static final long serialVersionUID = -3327478146308500708L;

    @JsonIgnore
    private Long id;
    @JsonIgnore
    private Long parentId;

    private String name;
    @JsonProperty(value="name_en")
    private String nameEn;
    @JsonProperty(value="name_de")
    private String nameDe;
    @JsonProperty(value="name_cs")
    private String nameCs;
    @JsonProperty(value="name_pl")
    private String namePl;
    @JsonProperty(value="name_fr")
    private String nameFr;
    @JsonProperty(value="name_ru")
    private String nameRu;
    
    private String link;
    private String key;
    private String redirect;
//    private RouterMeta meta;
    private Boolean hidden = false;
    private Boolean alwaysShow = false;
    private String icon;
    private List<Router<T>> child;

    @JsonIgnore
    private Boolean hasParent = false;

    @JsonIgnore
    private Boolean hasChildren = false;

    public void initChildren() {
        this.child = new ArrayList<>();
    }

}
