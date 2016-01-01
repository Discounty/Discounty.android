package discounty.com.data.models;


import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "DiscountCards", id = BaseColumns._ID)
public class DiscountCard extends Model {

    @Column(name = "Name")
    public String name;

    @Column(name = "needsSync")
    public boolean needsSync;

    @Column(name = "Description")
    public String description;

    @Column(name = "Barcode")
    public Barcode barcode;

    @Column(name = "CreatedAt")
    public Long createdAt;

    @Column(name = "UpdatedAt")
    public Long updatedAt;

    @Column(name = "ServerId")
    public Integer serverId;

    @Column(name = "Customer")
    public Customer customer;

    public DiscountCard() {
        super();
    }

    public DiscountCard(String name, String description, Barcode barcode, Long createdAt,
                        Long updatedAt, Integer serverId, Customer customer, boolean needsSync) {
        super();
        this.name = name;
        this.description = description;
        this.barcode = barcode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.serverId = serverId;
        this.customer = customer;
        this.needsSync = needsSync;
    }
}
