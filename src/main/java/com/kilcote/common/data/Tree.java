package com.kilcote.common.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shun fu
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tree<T> {

    private Long id;
    private String label;
    private List<Tree<T>> child;
    private Long parentId;
    private boolean hasParent = false;
    private boolean hasChildren = false;

    public void initChildren() {
        this.child = new ArrayList<>();
    }
}
