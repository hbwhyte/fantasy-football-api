package fantasy_football.exceptions;

import fantasy_football.model.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;


@ControllerAdvice
public class ControllerAdviceClass {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoHandlerFoundException.class})
    public @ResponseBody
    GeneralResponse handle404(NoHandlerFoundException e) {
        GeneralResponse gr = new GeneralResponse();
        gr.setStatus("Fail");
        gr.setResponse_code(HttpStatus.NOT_FOUND);

        CustomException error = new CustomException();
        error.setErrorName("Page Not Found");
        error.setReason("Invalid " + e.getHttpMethod() + " request at path " + e.getRequestURL() +
                " - Please check your URL and try again.");
        gr.setError(error);
        return gr;
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public @ResponseBody
    GeneralResponse handle404(HttpRequestMethodNotSupportedException e) {
        GeneralResponse gr = new GeneralResponse();
        gr.setStatus("Fail");
        gr.setResponse_code(HttpStatus.NOT_FOUND);

        CustomException error = new CustomException();
        error.setErrorName("Page Not Found");
        error.setReason(e.getMessage());
        gr.setError(error);
        return gr;
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    GeneralResponse handle400(MissingServletRequestParameterException e) {
        GeneralResponse gr = new GeneralResponse();
        gr.setStatus("Fail");
        gr.setResponse_code(HttpStatus.BAD_REQUEST);

        CustomException error = new CustomException();
        error.setErrorName("Missing Request Parameters");
        error.setReason(e.getMessage());
        gr.setError(error);
        return gr;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    GeneralResponse handle400(HttpMessageNotReadableException e) {
        GeneralResponse gr = new GeneralResponse();
        gr.setStatus("Fail");
        gr.setResponse_code(HttpStatus.BAD_REQUEST);

        CustomException error = new CustomException();
        error.setErrorName("HTTP Message Not Readable");
        error.setReason(e.getMessage());
        gr.setError(error);
        return gr;
    }

    @ResponseBody
    @ExceptionHandler(UnauthenticatedUserException.class)
    public GeneralResponse badApiError(UnauthenticatedUserException ex) {
        GeneralResponse gr = new GeneralResponse();
        gr.setStatus("Fail");
        gr.setResponse_code(HttpStatus.UNAUTHORIZED);
        gr.setError(generateCustomEx(ex));
        return gr;
    }

    @ResponseBody
    @ExceptionHandler(DatabaseException.class)
    public GeneralResponse getUserError(DatabaseException ex) {
        GeneralResponse gr = new GeneralResponse();
        gr.setStatus("Fail");
        gr.setResponse_code(HttpStatus.NOT_FOUND);
        gr.setError(generateCustomEx(ex));
        return gr;
    }

    @ResponseBody
    @ExceptionHandler(APIGenerationException.class)
    public GeneralResponse apiGenerationError(DatabaseException ex) {
        GeneralResponse gr = new GeneralResponse();
        gr.setStatus("Fail");
        gr.setResponse_code(HttpStatus.SERVICE_UNAVAILABLE);
        gr.setError(generateCustomEx(ex));
        return gr;
    }

    /**
     * Generates a nicely formatted Custom Exception response
     * for JSON output.
     *
     * @param ex Any exception to be formatted
     * @return CustomException Object
     */
    public CustomException generateCustomEx(Exception ex) {
        CustomException c = new CustomException();
        c.setErrorName(ex.toString());
        c.setReason(ex.getMessage());
        return c;
    }
}