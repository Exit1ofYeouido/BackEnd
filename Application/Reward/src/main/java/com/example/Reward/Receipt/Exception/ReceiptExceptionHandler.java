package com.example.Reward.Receipt.Exception;

import com.example.Reward.Receipt.Controller.ReceiptController;
import com.example.Reward.Receipt.Exception.ReceiptExceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = ReceiptController.class)
public class ReceiptExceptionHandler {

    @ExceptionHandler(GiveStockErrorException.class)
    private ResponseEntity<ReceiptErrorResponse> handler(GiveStockErrorException e) {
        ReceiptErrorResponse response = ReceiptErrorResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(KISApiException.class)
    private ResponseEntity<ReceiptErrorResponse> handler(KISApiException e) {
        ReceiptErrorResponse response = ReceiptErrorResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(S3UploadFailedException.class)
    private ResponseEntity<ReceiptErrorResponse> handler(S3UploadFailedException e) {
        ReceiptErrorResponse response = ReceiptErrorResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NoStockException.class)
    private ResponseEntity<ReceiptErrorResponse> handler(NoStockException e) {
        ReceiptErrorResponse response = ReceiptErrorResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(StockNotFoundException.class)
    private ResponseEntity<ReceiptErrorResponse> handler(StockNotFoundException e) {
        ReceiptErrorResponse response = ReceiptErrorResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .data(new StockNotFoundExceptionDto(e.getFoundName(), e.getReceiptURL()))
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(OcrErrorException.class)
    private ResponseEntity<ReceiptErrorResponse> handler(OcrErrorException e) {
        ReceiptErrorResponse response = ReceiptErrorResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .data(e.getUrl())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InvalidFileExtensionException.class)
    private ResponseEntity<ReceiptErrorResponse> handler(InvalidFileExtensionException e) {
        ReceiptErrorResponse response = ReceiptErrorResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .data(new InvalidFileExtensionExceptionDto(e.getExtension(), e.getValidExtentions()))
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MissingOcrInfoException.class)
    private ResponseEntity<ReceiptErrorResponse> handler(MissingOcrInfoException e) {
        ReceiptErrorResponse response = ReceiptErrorResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .data(new MissingOcrInfoExceptionDto(e.getUrl(), e.getMissingMessage()))
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}
