package mainactivity.schedule;

public class ScheduleInfo {
    private String filmName;
    private String showTime;
    private int showHall;
    private double ticketPrice;

    public ScheduleInfo() {
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public int getShowHall() {
        return showHall;
    }

    public void setShowHall(int showHall) {
        this.showHall = showHall;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

}
