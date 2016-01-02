package discounty.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * This class represents the BarcodeType model.
 * It consists of fields that are returned from
 * the correspondent API (/customers/full_info) and
 * getters and setters to them.
 */
public class CardBarcodeType {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("barcode_type")
    @Expose
    private String barcodeType;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("barcode__id")
    @Expose
    private Object barcodeId;



    public Object getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(Object barcodeId) {
        this.barcodeId = barcodeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "CardBarcodeType{" +
                "id=" + id +
                ", barcodeType='" + barcodeType + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", barcodeId='" + barcodeId + '\'' +
                '}';
    }
}
