package discounty.com.data.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name = "Customers", id = BaseColumns._ID)
public class Customer extends Model implements Parcelable {

    @Column(name = "FirstName")
    public String firstName;

    @Column(name = "LastName")
    public String lastName;

    @Column(name = "needsSync")
    public boolean needsSync;

    @Column(name = "Email")
    public String email;

    @Column(name = "PhoneNumber")
    public String phoneNumber;

    @Column(name = "City")
    public String city;

    @Column(name = "Country")
    public String country;

    @Column(name = "ServerId")
    public Integer serverId;

    @Column(name = "CreatedAt")
    public Long createdAt;

    @Column(name = "UpdatedAt")
    public Long updatedAt;

    @Column(name = "SmallAvatar")
    public String avatarSmall;

    @Column(name = "BigAvatar")
    public String avatarBig;

    public Customer() {
        super();
    }

    public Customer(String firstName, String lastName, String email, String phoneNumber, String city,
                    String country, Long createdAt, Long updatedAt, Integer serverId, boolean needsSync,
                    String avatarSmall, String avatarBig) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.country = country;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.serverId = serverId;
        this.needsSync = needsSync;
        this.avatarSmall = avatarSmall;
        this.avatarBig = avatarBig;
    }

    public List<DiscountCard> discountCards() {
        return getMany(DiscountCard.class, "Customer");
    }

    public List<Feedback> feedbacks() {
        return getMany(Feedback.class, "Customer");
    }


    protected Customer(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        needsSync = in.readByte() != 0;
        email = in.readString();
        phoneNumber = in.readString();
        city = in.readString();
        country = in.readString();
        avatarSmall = in.readString();
        avatarBig = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeByte((byte) (needsSync ? 1 : 0));
        parcel.writeString(email);
        parcel.writeString(phoneNumber);
        parcel.writeString(city);
        parcel.writeString(country);
        parcel.writeString(avatarSmall);
        parcel.writeString(avatarBig);
    }
}
