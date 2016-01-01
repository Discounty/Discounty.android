package discounty.com.data.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "BarcodeTypes", id = BaseColumns._ID)
public class BarcodeType extends Model {

    @Column(name = "BarcodeType")
    public String barcodeType;

    @Column(name = "needsSync")
    public boolean needsSync;

    @Column(name = "ServerId")
    public Integer serverId;

    @Column(name = "CreatedAt")
    public Long createdAt;

    @Column(name = "UpdatedAt")
    public Long updatedAt;

    public BarcodeType() {
        super();
    }

    public BarcodeType(String barcodeType, Integer serverId, Long createdAt, Long updatedAt, boolean needsSync) {
        super();
        this.barcodeType = barcodeType;
        this.serverId = serverId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.needsSync = needsSync;
    }
}
