package world.selfiewithdaughter.www.selfiewithdaughter;

public class Schemes {

    private int mId;

    private String mHeader;

    private String mDescription;

    public Schemes (int id, String header, String description) {
        mId = id;
        mHeader = header;
        mDescription = description;
    }

    public int getId() {
        return mId;
    }

    public String getHeader() {
        return mHeader;
    }

    public String getDescription() {
        return mDescription;
    }

}
