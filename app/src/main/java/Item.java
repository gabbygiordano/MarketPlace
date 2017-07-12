import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by gabbygiordano on 7/12/17.
 */

public class Item implements Parcelable {

    public String itemName;
    public User user;
    public String price;
    public ImageView itemImage;


    protected Item(Parcel in) {
        itemName = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        price = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemName);
        parcel.writeParcelable(user, i);
        parcel.writeString(price);
    }
}
