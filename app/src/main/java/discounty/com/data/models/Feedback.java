package discounty.com.data.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Feedbacks")
public class Feedback extends Model {

    @Column(name = "Feedback")
    public String feedback;

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

    public Feedback(String feedback, Short rating, Long createdAt, Long updatedAt, Customer customer) {
        super();
        this.feedback = feedback;
        this.rating = rating;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.customer = customer;
    }
}
