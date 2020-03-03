package com.paytm.wallet.exceptions;

import com.paytm.wallet.model.response.ApiResponse;
import com.paytm.wallet.model.response.Meta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
@Slf4j
public class WalletExceptionHandler extends DefaultHandlerExceptionResolver {

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ApiResponse> handleValidationException(ValidationException e) {
        log.error("[handleValidationException] Exception :: ", e);
        return new ResponseEntity<>(populateErrorResponse("002", e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(NoResourceFoundException e) {
        log.error("[handleResourceNotFoundException] Exception :: ", e);
        log.debug("[handleException] HTTPStatus :: {}", HttpStatus.NOT_FOUND.name());
        return new ResponseEntity<>(populateErrorResponse("003", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ApiResponse apiResponse = populateErrorResponse("004", details.toString());
        log.error("[handleMethodArgumentNotValidException] Exception :: ", e);
        log.debug("[handleException] HTTPStatus :: {}", HttpStatus.BAD_REQUEST.name());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public final ResponseEntity<Object> handleException(Exception e) {
        log.error("[handleException] Exception message:: ", e);
        log.debug("[handleException] HTTPStatus :: {}", HttpStatus.BAD_REQUEST.name());
        return new ResponseEntity<>(populateErrorResponse("001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private ApiResponse populateErrorResponse(String code, String message) {
        ApiResponse apiResponse = new ApiResponse();
        Meta meta = new Meta();
        meta.setCode(code);
        meta.setMessage(message);
        apiResponse.setMeta(meta);
        return apiResponse;
    }

}
