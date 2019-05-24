package com.jhy.app.common.domain.router;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * Vue路由 Meta
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class RouterMeta implements Serializable {

    private static final long serialVersionUID = 5499925008927195914L;

    private Boolean closeable;

    private Boolean isShow;

    //路由标题, 用于显示面包屑, 页面标题 *推荐设置	string	-
    private String title;

    //路由在 menu 上显示的图标	[string,svg]	-
    private String icon;

    //缓存该路由	boolean	false
    private Boolean keepAlive;

    //配合alwaysShow使用，用于隐藏菜单时，提供递归到父菜单显示 选中菜单项_（可参考 个人页 配置方式）_	boolean	false
    private Boolean hidden;

    //*特殊 隐藏 PageHeader 组件中的页面带的 面包屑和页面标题栏	boolean	false
    private Boolean hiddenHeaderContent;
    //与项目提供的权限拦截匹配的权限，如果不匹配，则会被禁止访问该路由页面

    private List<String> permission = Lists.newArrayList();

}
