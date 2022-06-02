package tudelft.ecgcontainer.forwarder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Component;

@Component
public class Forwarder {
    private transient HttpClient client = HttpClient.newBuilder().build();
    private static final String MATLAB_ADDRESS = "http://localhost:8043/";
    private static final String DATA_ERROR = "Error occurred when" +
        " retrieving data from MATLAB instance";

    /***
     * This method forwards the request for the next five signals
     * to the matlab instance.
     *
     *
     * @param patientId The ECG of a specific patient
     * @return The (String) body of the response, an image probably
     * @throws IOException Matlab communication error
     * @throws InterruptedException Matlab communication error
     */
    public String requestNextSignal(Integer patientId) throws IOException, InterruptedException {
        // Create Request
        HttpRequest request = HttpRequest
            .newBuilder()
            .GET()
            .uri(URI.create(MATLAB_ADDRESS + "get-next-cycles/" + patientId))
            .build();

        // Get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return DATA_ERROR;
        }

        return response.body();
    }

    /**
     * This method communicates with MATLAB in order
     * to retrieve the real signal from specific time stamps start and end time.
     *
     * @param start Indicated start time
     * @param end Indicated end time
     * @return The string response, most likely a sequence of values or image (TBD)
     * @throws IOException Matlab Communication Error
     * @throws InterruptedException Matlab Communication Error
     */
    public String requestRealSignal(Double start, Double end)
        throws IOException, InterruptedException {
        // Create Request
        HttpRequest request = HttpRequest
            .newBuilder()
            .GET()
            .uri(URI.create(MATLAB_ADDRESS + "get-real-signal/"
                + "?start=" + start + "&end=" + end))
            .build();

        // Get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return DATA_ERROR;
        }

        return response.body();
    }
}
