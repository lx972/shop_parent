package cn.lx.shop.exception;

import cn.lx.shop.entity.Result;
import cn.lx.shop.entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * cn.lx.shop.exception
 *
 * @ControllerAdvice注解，全局捕获异常类，只要作用在@RequestMapping上，所有的异常都会被捕获。
 * @Author Administrator
 * @date 17:09
 */
//@ControllerAdvice
public class BaseExceptionHandler {


    /**
     * 异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }
}
