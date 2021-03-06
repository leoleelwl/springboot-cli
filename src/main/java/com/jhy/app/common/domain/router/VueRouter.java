package com.jhy.app.common.domain.router;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jhy.app.system.domain.Menu;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 构建 Vue路由
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VueRouter<T> implements Serializable {

    private static final long serialVersionUID = -3327478146308500708L;

    @JsonIgnore
    private String id;

    @JsonIgnore
    private String parentId;

    private String path;

    private String name;

    private String component;

    private String icon;

    private String redirect;

    private RouterMeta meta;

    private List<VueRouter<T>> children;

    @JsonIgnore
    private boolean hasParent = false;

    @JsonIgnore
    private boolean hasChildren = false;

    public void initChildren(){
        this.children = new ArrayList<>();
    }

    public static RouterMeta constructMeta(@NotNull Menu menu){
        return new RouterMeta().setCloseable(false)
                               .setIsShow(true)
                               .setIcon(menu.getIcon())
                               .setPermission(Arrays.asList(menu.getPerms()));
    }
}
