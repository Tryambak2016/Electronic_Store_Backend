package com.electronic.store.exceptions;

import lombok.Builder;

@Builder
public class BadApiRequest extends RuntimeException{

    public BadApiRequest(String message){
        super(message);
    }
    public BadApiRequest(){
        super("bad Api Request");
    }
}
