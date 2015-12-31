package discounty.com.data.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name = "Customers", id = BaseColumns._ID)
public class Customer extends Model {

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

    @Column(name = "CreatedAt")
    public Long createdAt;

    @Column(name = "UpdatedAt")
    public Long updatedAt;

    public Customer() {
        super();
    }

    public Customer(String firstName, String lastName, String email, String phoneNumber, String city,
                    String country, Long createdAt, Long updatedAt, boolean needsSync) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.country = country;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.needsSync = needsSync;
    }

    public List<DiscountCard> discountCards() {
        return getMany(DiscountCard.class, "Customer");
    }

    public List<Feedback> feedbacks() {
        return getMany(Feedback.class, "Customer");
    }
}
