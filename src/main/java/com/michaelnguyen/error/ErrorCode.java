package com.michaelnguyen.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {

	USER_NOT_EXIST(1000, "Email or Password is incorrect", HttpStatus.NOT_FOUND),
 	USER_NOT_ACTIVATED(1001, "User not activated", HttpStatus.BAD_REQUEST),
	EMAIL_EXIST(1002, "Email is existed", HttpStatus.BAD_REQUEST),
	EMAIL_NOT_EXIST(1003, "Email does not exist", HttpStatus.BAD_REQUEST),
 
	UNAUTHENTICATED(1004, "Email not found or Password is invalid", HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED(1005, "You do not have permission", HttpStatus.FORBIDDEN),
//
	PRODUCT_NOT_EXIST(1004, "Product not found", HttpStatus.NOT_FOUND),
	CART_NOT_EXIST(1100, "Cart not found", HttpStatus.NOT_FOUND),
	CART_ITEM_NOT_EXIST(1101, "Cart item not found", HttpStatus.NOT_FOUND), 
	
	MY_FILE_NOT_FOUND(2001,
			"Could not create the directory where the uploaded files will be stored.", HttpStatus.NOT_FOUND);

	private int code;
	private String message;
	private HttpStatusCode statuscode;

	private ErrorCode(int code, String message, HttpStatusCode statuscode) {
		this.code = code;
		this.message = message;
		this.statuscode = statuscode;
	}

}
