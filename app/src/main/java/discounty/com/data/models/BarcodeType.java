package discounty.com.data.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "BarcodeTypes")
public class BarcodeType extends Model {

    @Column(name = "BarcodeType")
    public String barcodeType;

    @Column(name = "CreatedAt")
    public Long createdAt;

    @Column(name = "UpdatedAt")
    public Long updatedAt;

    public BarcodeType() {
        super();
    }

    public BarcodeType(String barcodeType, Long createdAt, Long updatedAt) {
        super();
        this.barcodeType = barcodeType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
