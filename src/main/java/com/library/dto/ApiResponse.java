package com.library.dto;

/**
 * ApiResponse - Generic wrapper for all REST API responses
 * 
 * This standardizes the API response format:
 * {
 *   "success": true/false,
 *   "message": "Description of the result",
 *   "data": { ... }  // or null on error
 * }
 * 
 * This is a best practice for REST APIs - consistent response structure
 * makes it easier for frontend developers to handle responses.
 */
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    // Default constructor
    public ApiResponse() {}

    // Constructor for success response
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // ==================== Static Factory Methods ====================
    // These make it easy to create responses without new keywords everywhere

    /**
     * Create a success response with data
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data);
    }

    /**
     * Create a success response with custom message and data
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Create an error response
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
