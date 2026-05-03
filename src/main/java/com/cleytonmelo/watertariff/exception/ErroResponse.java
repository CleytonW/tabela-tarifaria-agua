package com.cleytonmelo.watertariff.exception;

public record ErroResponse(
        String erro,
        int status
        ) {

}
