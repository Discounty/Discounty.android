package discounty.com.data.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Coupons", id = BaseColumns._ID)
public class Coupon extends Model {

    @Column(name = "Name")
    public String name;

    @Column(name = "Description")
    public String description;

    @Column(name = "ServerId")
    public Integer serverId;

    @Column(name = "needsSync")
    public boolean needsSync;

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

    public Coupon(String name, String description, Integer serverId, Long createdAt,
                  Long updatedAt, Barcode barcode, Customer customer, boolean needsSync) {
        super();
        this.name = name;
        this.description = description;
        this.serverId = serverId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.barcode = barcode;
        this.customer = customer;
        this.needsSync = needsSync;
    }
}
