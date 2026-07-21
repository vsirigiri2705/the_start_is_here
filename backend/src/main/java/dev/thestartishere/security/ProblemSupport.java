package dev.thestartishere.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
@Component
public class ProblemSupport implements AuthenticationEntryPoint {
  private final ObjectMapper json; public ProblemSupport(ObjectMapper json){this.json=json;}
  @Override public void commence(HttpServletRequest req,HttpServletResponse res,AuthenticationException ex)throws IOException{write(req,res,401,"Authentication required","Valid authentication is required.");}
  public void write(HttpServletRequest req,HttpServletResponse res,int status,String title,String detail)throws IOException{res.setStatus(status);res.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);json.writeValue(res.getOutputStream(),Map.of("type","about:blank","title",title,"status",status,"detail",detail,"instance",req.getRequestURI(),"traceId",MDC.get("traceId")==null?"unavailable":MDC.get("traceId")));}
}
