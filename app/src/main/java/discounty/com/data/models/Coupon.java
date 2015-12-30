package discounty.com.data.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Coupons")
public class Coupon extends Model {

    @Column(name = "Description")
    public String description;

    @Column(name = "CreatedAt")
    public Long createdAt;

    @Column(name = "UpdatedAt")
    public Long updatedAt;

    @Column(name = "Barcode")
    public Barcode barcode;

    @Column(name = "Customer")
    public Customer customer;

    public Coupon() {
        super();
    }

    public Coupon(String description, Long createdAt, Long updatedAt, Barcode barcode, Customer customer) {
        super();
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.barcode = barcode;
        this.customer = customer;
    }
}
