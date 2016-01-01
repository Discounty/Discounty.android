package discounty.com.data.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Barcodes", id = BaseColumns._ID)
public class Barcode extends Model {

    @Column(name = "Barcode")
    public String barcode;

    @Column(name = "needsSync")
    public boolean needsSync;

    @Column(name = "DiscountPercentage")
    public Double discountPercentage;

    @Column(name = "ExtraInfo")
    public String extraInfo;

    @Column(name = "CreatedAt")
    public Long createdAt;

    @Column(name = "UpdatedAt")
    public Long updatedAt;

    @Column(name = "BarcodeType")
    public BarcodeType barcodeType;

    @Column(name = "Customer", onDelete = Column.ForeignKeyAction.CASCADE)
    public Customer customer;

    @Column(name = "ServerId")
    public Integer serverId;

    public Barcode() {
        super();
    }

    public Barcode(String barcode, Double discountPercentage, String extraInfo, Long createdAt, Long updatedAt,
                   BarcodeType barcodeType, Customer customer, Integer serverId, boolean needsSync) {
        super();
        this.barcode = barcode;
        this.discountPercentage = discountPercentage;
        this.extraInfo = extraInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.barcodeType = barcodeType;
        this.customer = customer;
        this.serverId = serverId;
        this.needsSync = needsSync;
    }
}
