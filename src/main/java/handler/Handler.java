package handler;


import forwarder.Forwarder;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ecg-tudelft")
public class Handler {
    private static final String ERROR_RESPONSE = "Error occurred when" +
        " communicating with MATLAB instance";

    @Autowired
    private Forwarder forwarder;

    public Handler(Forwarder forwarder) {
        this.forwarder = forwarder;
    }

    /***
     * This method retrieves the **next** five signals from
     * the ECG signal.
     *
     *
     * @param request The HTTP request
     * @param patientId The ID of the patient of the signal
     * @return The next five signals that be viewed.
     */
    @GetMapping("/get-signal/{patientId}")
    public ResponseEntity<?> getSignal(HttpServletRequest request,
                                       @PathVariable Integer patientId) {
        try {
            String response = forwarder.requestNextSignal(patientId);

            return ResponseEntity
                .ok()
                .body(response);
        } catch (Exception e) {
            return ResponseEntity.
                status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ERROR_RESPONSE);
        }
    }

    /**
     *  This method handles the request to see **original** signal.
     *
     *
     * @param request The HTTP request
     * @param startTime Start time of the ECG signal in milliseconds from 0
     * @param endTime End time of the ECG signal in milliseconds from 0
     * @return The real ECG signals from specified startTime and endTime
     */
    @GetMapping("/get-real-signal")
    public ResponseEntity<?> getRealSignal(HttpServletRequest request,
                                           @RequestParam(name = "startTime") String startTime,
                                           @RequestParam(name = "endTime") String endTime) {
        try {
            String response = forwarder.requestRealSignal(Double.parseDouble(startTime),
                Double.parseDouble(endTime));

            return ResponseEntity
                .ok()
                .body(response);
        } catch (Exception e) {
            return ResponseEntity.
                status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ERROR_RESPONSE);
        }
    }
}
