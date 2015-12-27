package discounty.com.models;

import com.google.gson.annotations.SerializedName;

/*
 * This class represents the Barcode model.
 * It consists of fields that are returned from
 * the correspondent API (/customers/full_info) and
 * getters and setters to them.
 */
public class CardBarcode {

    @SerializedName("id")
    private Integer id;

    @SerializedName("barcode")
    private String barcode;

    @SerializedName("discount_percentage")
    private String discountPercentage;

    @SerializedName("extra_info")
    private String extraInfo;

    @SerializedName("barcode_type_id")
    private Integer barcodeTypeId;

    @SerializedName("discount_card_id")
    private Integer discountCardId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Integer getBarcodeTypeId() {
        return barcodeTypeId;
    }

    public void setBarcodeTypeId(Integer barcodeTypeId) {
        this.barcodeTypeId = barcodeTypeId;
    }

    public Integer getDiscountCardId() {
        return discountCardId;
    }

    public void setDiscountCardId(Integer discountCardId) {
        this.discountCardId = discountCardId;
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
        return "CardBarcode{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", discountPercentage='" + discountPercentage + '\'' +
                ", extraInfo='" + extraInfo + '\'' +
                ", barcodeTypeId=" + barcodeTypeId +
                ", discountCardId=" + discountCardId +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
