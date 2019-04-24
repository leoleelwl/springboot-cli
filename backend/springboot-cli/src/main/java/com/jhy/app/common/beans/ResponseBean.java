package com.jhy.app.common.beans;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 通用controller返回结果的bean
 * @param <T>
 * @author jihy
 */
@Data
@Accessors(chain = true)
public class ResponseBean<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int NO_LOGIN = -1;

	public static final int SUCCESS = 0;

	public static final int FAIL = 1;

	public static final int NO_PERMISSION = 2;

	private String msg = "success";

	private int code = SUCCESS;

	private T data;

	public ResponseBean() {
		super();
	}

	public ResponseBean(T data) {
		super();
		this.data = data;
	}

	public ResponseBean(Throwable e) {
		super();
		this.msg = e.toString();
		this.code = FAIL;
	}
}
