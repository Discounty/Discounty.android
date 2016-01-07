package discounty.com.data.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Feedbacks", id = BaseColumns._ID)
public class Feedback extends Model {

    @Column(name = "Feedback")
    public String feedback;

    @Column(name = "needsSync")
    public boolean needsSync;

    @Column(name = "Rating")
    public Short rating;

    @Column(name = "CreatedAt")
    public Long createdAt;

    @Column(name = "UpdatedAt")
    public Long updatedAt;

    @Column(name = "Customer")
    public Customer customer;

    public Feedback() {
        super();
    }

    public Feedback(String feedback, Short rating, Long createdAt, Long updatedAt,
                    Customer customer, boolean needsSync) {
        super();
        this.feedback = feedback;
        this.rating = rating;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.customer = customer;
        this.needsSync = needsSync;
    }
}
