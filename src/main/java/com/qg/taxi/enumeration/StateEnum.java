package com.qg.taxi.enumeration;

import lombok.Getter;
import lombok.Setter;

/**
 * Create by ming on 18-8-3 下午2:33
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
public enum StateEnum {

    /**
     * OK
     */
    OK(200, "一切正常"),

    NO_SUCH_FILE(4, "暂时还没有文件，请先生成文件"),

    FILE_IS_EXIST(5, "文件已经存在");

    /**
     * 状态码
     */
    @Getter
    @Setter
    private int state;


    /**
     * 状态信息
     */
    @Getter
    @Setter
    private String info;

    StateEnum(int state, String info) {
        this.state = state;
        this.info = info;
    }
}
