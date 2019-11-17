package mainactivity.boxoffice;

public class BoxofficeInfo {
    private String filmNo;
    private String filmName;
    private String showTime;
    private int showHall;
    private int ticketNum;
    private double boxoffice;

    public String getFilmNo() {
        return filmNo;
    }

    public void setFilmNo(String filmNo) {
        this.filmNo = filmNo;
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

    public int getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(int ticketNum) {
        this.ticketNum = ticketNum;
    }

    public double getBoxoffice() {
        return boxoffice;
    }

    public void setBoxoffice(double boxoffice) {
        this.boxoffice = boxoffice;
    }
}
