package com.crowd.util;


/**
 * 统一Ajax请求返回的结果，未来也可用于分布式
 * @param <T>
 */
public class ResultEntity<T> {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED ="FAILED";

    private String result;

    private String message;

    private T data;

    /**
     * 请求处理成功，且不需要返回数据
     * @param <E>
     * @return
     */
    public static <E> ResultEntity<E> successWithoutData(){
        return new ResultEntity<E>(SUCCESS,null,null);
    }

    /**
     * 请求处理成功，且需要返回数据
     * @param data
     * @param <E>
     * @return
     */
    public static <E> ResultEntity<E> successWithData(E data){
        return new ResultEntity<E>(SUCCESS,null,data);
    }

    /**
     * 请求处理失败
     * @param message 错误信息
     * @param <E>
     * @return
     */
    public static <E> ResultEntity<E> failed(String message){
        return new ResultEntity<E>(FAILED,message,null);
    }


    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public ResultEntity() {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
