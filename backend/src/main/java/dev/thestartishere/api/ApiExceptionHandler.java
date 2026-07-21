package dev.thestartishere.api;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(BadCredentialsException.class) ResponseEntity<Map<String,Object>> auth(BadCredentialsException e,HttpServletRequest r){return problem(401,"Authentication failed","Email, password, or session is invalid.",r,null);}
  @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException e,HttpServletRequest r){var fields=new TreeMap<String,List<String>>();e.getBindingResult().getFieldErrors().forEach(x->fields.computeIfAbsent(x.getField(),k->new ArrayList<>()).add(x.getDefaultMessage()));return problem(400,"Validation failed","One or more fields are invalid.",r,fields);}
  private ResponseEntity<Map<String,Object>> problem(int status,String title,String detail,HttpServletRequest req,Map<String,List<String>> fields){var body=new LinkedHashMap<String,Object>();body.put("type","about:blank");body.put("title",title);body.put("status",status);body.put("detail",detail);body.put("instance",req.getRequestURI());body.put("traceId",Optional.ofNullable(MDC.get("traceId")).orElse("unavailable"));if(fields!=null)body.put("fieldErrors",fields);return ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON).body(body);}
}
