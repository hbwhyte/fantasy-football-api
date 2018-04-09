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

    /**
     * If the user tries a path or page that does not exist,
     * returns this exception message.
     *
     * @param e NoHandlerFoundException
     * @return GeneralResponse with a failed 404 message
     */
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

    /**
     * If the user tries an incompatible, unmapped, or not allowed
     * HTTP request, returns this exception message. For example,
     * trying to POST to a path that only accepts GET requests.
     *
     * @param e HttpRequestMethodNotSupportedException
     * @return GeneralResponse with a failed 405 message
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public @ResponseBody
    GeneralResponse handle405(HttpRequestMethodNotSupportedException e) {
        GeneralResponse gr = new GeneralResponse();
        gr.setStatus("Fail");
        gr.setResponse_code(HttpStatus.NOT_FOUND);

        CustomException error = new CustomException();
        error.setErrorName("Page Not Found");
        error.setReason(e.getMessage());
        gr.setError(error);
        return gr;
    }

    /**
     *  If the user exceeds their API call limit, returns this exception message.
     *
     * @param e CallsExceededException
     * @return GeneralResponse with a failed 429 message
     */
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler({CallsExceededException.class})
    public @ResponseBody GeneralResponse handle429(CallsExceededException e) {
        GeneralResponse gr = new GeneralResponse();
        gr.setStatus("Fail");
        gr.setResponse_code(HttpStatus.TOO_MANY_REQUESTS);
        gr.setError(generateCustomEx(e));
        return gr;
    }

    /**
     * If the user does not include required request parameters in their
     * HTTP request, returns this exception message.
     *
     * @param e MissingServletRequestParameterException
     * @return GeneralResponse with a failed 400 message
     */
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

    /**
     * If the user includes unreadable JSON to in their HTTP
     * request, returns this exception message. For example, one
     * sided quotes in their POST RequestBody.
     *
     * @param e HttpMessageNotReadableException
     * @return GeneralResponse with a failed 400 message
     */
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

    /**
     * If the API Key provided is not found in the database, returns this
     * exception message.
     *
     * @param e UnauthenticatedUserException
     * @return General Response with a failed 401 message
     */
    @ResponseBody
    @ExceptionHandler(UnauthenticatedUserException.class)
    public GeneralResponse badApiError(UnauthenticatedUserException e) {
        GeneralResponse gr = new GeneralResponse();
        gr.setStatus("Fail");
        gr.setResponse_code(HttpStatus.UNAUTHORIZED);
        gr.setError(generateCustomEx(e));
        return gr;
    }

    /**
     * If the user requested is not found in the database, returns this
     * exception message.
     *
     * @param e DatabaseException
     * @return General Response with a failed 404 message
     */
    @ResponseBody
    @ExceptionHandler(DatabaseException.class)
    public GeneralResponse getUserError(DatabaseException e) {
        GeneralResponse gr = new GeneralResponse();
        gr.setStatus("Fail");
        gr.setResponse_code(HttpStatus.NOT_FOUND);
        gr.setError(generateCustomEx(e));
        return gr;
    }

    /**
     * If the API key could not be generated for the user, returns
     * this exception message.
     *
     * @param e DatabaseException
     * @return General Response with a failed 503 message
     */
    @ResponseBody
    @ExceptionHandler(APIGenerationException.class)
    public GeneralResponse apiGenerationError(DatabaseException e) {
        GeneralResponse gr = new GeneralResponse();
        gr.setStatus("Fail");
        gr.setResponse_code(HttpStatus.SERVICE_UNAVAILABLE);
        gr.setError(generateCustomEx(e));
        return gr;
    }

    /**
     * Generates a nicely formatted Custom Exception response
     * for JSON output.
     *
     * @param e Any exception to be formatted
     * @return CustomException Object
     */
    public CustomException generateCustomEx(Exception e) {
        CustomException c = new CustomException();
        c.setErrorName(e.toString());
        c.setReason(e.getMessage());
        return c;
    }
}