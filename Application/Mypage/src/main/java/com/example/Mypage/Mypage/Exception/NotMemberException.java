package com.example.Mypage.Mypage.Exception;

import lombok.Getter;
import org.aspectj.weaver.ast.Not;

@Getter
public class NotMemberException extends NullPointerException{

    private Long memId;

    public NotMemberException(Long memId){
        super();
        this.memId=memId;
    }
}
