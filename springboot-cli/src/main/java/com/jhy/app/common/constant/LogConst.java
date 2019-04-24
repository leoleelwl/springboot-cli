package com.jhy.app.common.constant;

/**
 *  日志注解里面的常量，自己增加即可
 *   @author jihy
 */
public interface LogConst {
    /**
     *  操作
     *  为了节省日志文件大小，这些常量可以使用单字母代替
     */
    String ACTION_ADD = "add";

    String ACTION_DELETE = "del";

    String ACTION_UPDATE = "update";

    String ACTION_QUERY= "query";

    String ITEM_TYPE_CACHE = "cache";

}