package discounty.com.data.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Barcodes")
public class Barcode extends Model {

    @Column(name = "Barcode")
    public String barcode;

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

    @Column(name = "Customer")
    public Customer customer;

    public Barcode() {
        super();
    }

    public Barcode(String barcode, Double discountPercentage, String extraInfo, Long createdAt, Long updatedAt, BarcodeType barcodeType, Customer customer) {
        super();
        this.barcode = barcode;
        this.discountPercentage = discountPercentage;
        this.extraInfo = extraInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.barcodeType = barcodeType;
        this.customer = customer;
    }
}
