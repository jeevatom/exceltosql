package excel.generator;

import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

       @ExceptionHandler(NoResourceFoundException.class)
    public String noResourceFound(Exception ex, Model model){
        model.addAttribute("error", "Page Not Found");
        return "redirect:/";
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handlePageException(Exception ex, Model model){
        model.addAttribute("error", "Page Not Found");
        return "redirect:/index";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        System.out.println(ex);
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("status", 500);
        return "index";
    }
}