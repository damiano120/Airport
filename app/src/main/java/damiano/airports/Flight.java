package damiano.airports;

public class Flight {

    private String time;
    private String direction;
    private String flightNumber;
    private String status;
    private String expectedTime;

    public Flight(String time, String direction, String flightNumber, String status, String expectedTime) {
        this.time = time;
        this.direction = direction;
        this.flightNumber = flightNumber;
        this.status = status;
        this.expectedTime = expectedTime;
    }

    public String getTime() {
        return time;
    }

    public String getDirection() {
        return direction;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    @Override
    public String toString() {
        return time + " " + direction + " " + flightNumber + " " + status + " " + expectedTime;
    }
}
